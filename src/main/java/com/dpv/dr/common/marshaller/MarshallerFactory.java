package com.dpv.dr.common.marshaller;

import java.util.HashMap;
import java.util.Map;

import com.dpv.dr.common.EnvVarMap;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public class MarshallerFactory {
	private final Map<String, AbstractMarshaller> marshallerMap = new HashMap<>(10);
	@Getter
	@Setter
	private EnvVarMap envVarMap;

	public void addMarshaller(String eventType, AbstractMarshaller m) {
		m.setEnvVarMap(envVarMap);
		marshallerMap.put(eventType, m);
	}

	public AbstractMarshaller getMarshaller(String eventType) {
		if (marshallerMap.containsKey(eventType)) {
			return marshallerMap.get(eventType);
		}
		return null;
	}
}
