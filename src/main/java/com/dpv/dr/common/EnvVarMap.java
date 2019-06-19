package com.dpv.dr.common;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class EnvVarMap {
	public static final int DEFAULT_SIZE = 10;
	private Map<String, Object> envVarMap;

	public EnvVarMap() {
		this.envVarMap = new ConcurrentHashMap<>(DEFAULT_SIZE);
	}

	public EnvVarMap(int size) {
		this.envVarMap = new ConcurrentHashMap<>(size);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		if (key == null || !envVarMap.containsKey(key)) {
			return null;
		}
		return (T) envVarMap.get(key);
	}

	public void set(String key, Object obj) {
		envVarMap.put(key, obj);
	}

	public void copyTo(EnvVarMap env) {
		for (Entry<String, Object> entry : envVarMap.entrySet()) {
			env.set(entry.getKey(), entry.getValue());
		}
	}

}
