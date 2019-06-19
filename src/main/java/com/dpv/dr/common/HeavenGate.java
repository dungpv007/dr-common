package com.dpv.dr.common;

import com.nhb.common.data.PuObject;

public interface HeavenGate<E extends Event> extends Wrapper {
	void init(PuObject initParams) throws Exception;

	void handleEvent(E event);
	
	void marshal(E event);

	void start();

	void shutdown();
}
