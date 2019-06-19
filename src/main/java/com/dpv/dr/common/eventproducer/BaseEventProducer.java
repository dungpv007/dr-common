package com.dpv.dr.common.eventproducer;

import com.dpv.dr.common.Event;
import com.lmax.disruptor.RingBuffer;
import com.nhb.common.Loggable;

import lombok.Getter;
import lombok.Setter;

public class BaseEventProducer<E extends Event> implements EventProducer<E>, Loggable {
	@Setter
	protected RingBuffer<E> ringBuffer;
	@Getter
	@Setter
	private int id;

	@Override
	public void publish(E event) {
		getLogger().debug("[{}]publish event type: {}, remain in ringbuffer: {}", id, event.getType(),
				ringBuffer.remainingCapacity());
		long sequence = ringBuffer.next(); // Grab the next sequence
		try {
			Event e = ringBuffer.get(sequence); // Get the entry in the Disruptor
			e.clone(event);
		} finally {
			ringBuffer.publish(sequence);
		}

	}
}
