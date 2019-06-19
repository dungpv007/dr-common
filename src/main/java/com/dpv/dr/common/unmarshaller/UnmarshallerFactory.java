package com.dpv.dr.common.unmarshaller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nhb.common.Loggable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnmarshallerFactory implements Loggable {

	private Map<String, Unmarshaller> unmarshallerMap = new ConcurrentHashMap<>(20);

	public void init() {
	}

	public Unmarshaller getUnmarshaller(String eventType) {
		if (eventType == null) {
			return null;
		}
		return unmarshallerMap.get(eventType);
	}
}
