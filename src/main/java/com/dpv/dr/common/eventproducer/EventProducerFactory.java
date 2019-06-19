package com.dpv.dr.common.eventproducer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class EventProducerFactory {
	private List<EventProducer> producers;

	public EventProducerFactory(int size) {
		producers = new ArrayList<>(size);
	}

	public void add(EventProducer producer) {
		producers.add(producer);
	}

	public EventProducer getProducer(int index) {
		return producers.get(index % producers.size());
	}

}
