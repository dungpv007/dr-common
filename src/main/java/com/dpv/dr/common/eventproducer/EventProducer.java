package com.dpv.dr.common.eventproducer;

import com.dpv.dr.common.Event;
import com.lmax.disruptor.RingBuffer;

public interface EventProducer<E extends Event> {

	void setRingBuffer(RingBuffer<E> ringBuffer);

	void publish(E event);

}
