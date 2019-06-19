package com.dpv.dr.common.eventhandler;

import java.util.HashMap;
import java.util.Map;

import com.dpv.dr.common.EnvVarMap;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public class EventHandlerFactory {
	private final Map<String, AbstractEventHandler> eventHandlerMap = new HashMap<>(10);
	@Getter
	@Setter
	private EnvVarMap envVarMap;

	public void addHandler(String eventType, AbstractEventHandler h) {
		if (envVarMap != null) {
			h.setEnvVarMap(envVarMap);
		}
		eventHandlerMap.put(eventType, h);
	}

	public AbstractEventHandler getHandler(String name) {
		return eventHandlerMap.get(name);
	}
}
