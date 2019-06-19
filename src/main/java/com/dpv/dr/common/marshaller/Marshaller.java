package com.dpv.dr.common.marshaller;

import com.dpv.dr.common.Event;

public interface Marshaller<E extends Event> {
	void marshal(E event);
}
