package com.dpv.dr.common.eventhandler;

import com.dpv.dr.common.EnvVarMap;
import com.dpv.dr.common.Event;
import com.lmax.disruptor.EventHandler;
import com.nhb.common.Loggable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public abstract class AbstractEventHandler<E extends Event> implements EventHandler<E>, Loggable {
	private EnvVarMap envVarMap;

	public <T> T getEnvVariable(String key) {
		return envVarMap.get(key);
	}

	@Override
	public void onEvent(E event, long sequence, boolean endOfBatch) throws Exception {
		getLogger().debug("process event: {}", event.getType());
		onEvent(event);

	}

	protected abstract void onEvent(E event) throws Exception;

}
