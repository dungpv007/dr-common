package com.dpv.dr.common.utils;

public class ReflectionUtil {
	@SuppressWarnings("unchecked")
	public static <T> T newObject(String className) throws Exception {
		@SuppressWarnings("rawtypes")
		Class cls = Class.forName(className);
		return (T) cls.newInstance();
	}
}
