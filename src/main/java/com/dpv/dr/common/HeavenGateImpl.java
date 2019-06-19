package com.dpv.dr.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.dpv.dr.common.config.Config;
import com.dpv.dr.common.eventhandler.AbstractEventHandler;
import com.dpv.dr.common.eventhandler.EventHandlerFactory;
import com.dpv.dr.common.eventproducer.BaseEventProducer;
import com.dpv.dr.common.eventproducer.EventProducer;
import com.dpv.dr.common.eventproducer.EventProducerFactory;
import com.dpv.dr.common.exception.BaseExceptionHandler;
import com.dpv.dr.common.marshaller.Marshaller;
import com.dpv.dr.common.marshaller.MarshallerFactory;
import com.dpv.dr.common.unmarshaller.Unmarshaller;
import com.dpv.dr.common.unmarshaller.UnmarshallerFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.nhb.common.Loggable;
import com.nhb.common.async.RPCFuture;
import com.nhb.common.data.PuObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@SuppressWarnings("rawtypes")
public class HeavenGateImpl<E extends Event> implements HeavenGate<E>, Loggable {
	private UnmarshallerFactory unmarshallerFactory;
	private MarshallerFactory marshallerFactory;
	private EventHandlerFactory eventHandlerFactory;
	private EventProducerFactory eventProducerFactory;
	private Config config;
	private List<Disruptor<E>> disruptors;
	private EnvVarMap envVarMap;
	private ExceptionHandler<E> exceptionHandler;
	private Executor executor;
	@Setter
	private EventFactory<E> eventFactory;

	public HeavenGateImpl(EventFactory<E> eventFactory) {
		this.eventFactory = eventFactory;
	}

	@Override
	public void handleEvent(E event) {
		RPCFuture<Event> unmarshallFuture = unmarshall(event);
		if (unmarshallFuture != null) {
			unmarshallFuture.setCallback((e) -> {
				publishEvent(event);
			});
			unmarshallFuture.setTimeout(config.getFutureTimeout(), TimeUnit.MILLISECONDS);
		} else {
			publishEvent(event);
		}
	}

	@SuppressWarnings("unchecked")
	private void publishEvent(E event) {
		EventProducer producer = eventProducerFactory.getProducer(event.getRefId());
		if (producer != null) {
			producer.publish(event);
		}
	}

	private RPCFuture<Event> unmarshall(Event event) {
		Unmarshaller unmarshaller = getUnmarshallerFactory().getUnmarshaller(event.getType());
		if (unmarshaller != null) {
			return unmarshaller.unmarshall(event);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(PuObject initParams) throws Exception {
		envVarMap = new EnvVarMap(20);
		executor = Executors.newFixedThreadPool(8, new ThreadFactory() {
			int count = 0;

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, String.format("Heaven Thread #%d", count++));
			}
		});
		config = Config.fromPuObject(initParams);
		if (exceptionHandler == null) {
			exceptionHandler = new BaseExceptionHandler<E>();
		}
		eventHandlerFactory = new EventHandlerFactory();
		marshallerFactory = new MarshallerFactory();
		unmarshallerFactory = new UnmarshallerFactory();
		eventProducerFactory = new EventProducerFactory(config.getDisruptorSize());
		disruptors = new ArrayList<>(config.getDisruptorSize());
		for (int i = 0; i < config.getDisruptorSize(); i++) {
			int id = i;
			Disruptor<E> disruptor = new Disruptor<E>(eventFactory, config.getRingBufferSize(), new ThreadFactory() {
				int count = 0;

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, String.format("Disruptor #%d", id, count++));
				}
			}, ProducerType.SINGLE, new BlockingWaitStrategy());

			disruptor.handleEventsWith(this::handleEvent).then(this::marshal);
			disruptor.setDefaultExceptionHandler(getExceptionHandler());
			disruptors.add(disruptor);
			BaseEventProducer p = new BaseEventProducer();
			p.setId(i);
			p.setRingBuffer(disruptor.getRingBuffer());
			eventProducerFactory.add(p);
		}

	}

	public void setExceptionHandler(ExceptionHandler<E> exh) {
		if (disruptors == null) {
			return;
		}
		for (Disruptor<E> dr : disruptors) {
			dr.setDefaultExceptionHandler(exh);
		}
	}

	@SuppressWarnings("unchecked")
	public void handleEvent(E event, long sequence, boolean endOfBatch) throws Exception {
		AbstractEventHandler handler = eventHandlerFactory.getHandler(event.getType());
		if (handler == null) {
			getLogger().warn("Not found handler for event with type: {}", event.getType());
			return;
		}
		getLogger().debug("Handle event with type: {}", event.getType());
		handler.onEvent(event, sequence, endOfBatch);
	}

	@SuppressWarnings("unchecked")
	public void marshal(E event, long sequence, boolean endOfBatch) throws Exception {
		Marshaller marshaller = marshallerFactory.getMarshaller(event.getType());
		if (marshaller != null) {
			getLogger().debug("Do marshal for event type: {}", event.getType());
			final E clone = eventFactory.newInstance();
			clone.clone(event);
			getExecutor().execute(() -> {
				marshaller.marshal(clone);
			});
		} else {
			getLogger().debug("Marshaller not found for event type: {}", event.getType());
		}
	}

	@SuppressWarnings("unchecked")
	public void marshal(E event) {
		Marshaller marshaller = marshallerFactory.getMarshaller(event.getType());
		if (marshaller != null) {
			getLogger().debug("Do marshal for event type: {}", event.getType());
			marshaller.marshal(event);
		} else {
			getLogger().debug("Marshaller not found for event type: {}", event.getType());
		}
	}

	public void syncEnv() {
		eventHandlerFactory.setEnvVarMap(envVarMap);
		marshallerFactory.setEnvVarMap(envVarMap);
	}

	@Override
	public void start() {
		for (Disruptor dr : disruptors) {
			dr.start();
		}

	}

	@Override
	public void shutdown() {
		for (Disruptor dr : disruptors) {
			try {
				dr.shutdown(10, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				getLogger().error("error when shutdown disruptor", e);
			}
		}

	}

}
