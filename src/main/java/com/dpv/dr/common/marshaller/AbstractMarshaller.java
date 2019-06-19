package com.dpv.dr.common.marshaller;

import com.dpv.dr.common.EnvVarMap;
import com.dpv.dr.common.Event;
import com.nhb.common.Loggable;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractMarshaller<E extends Event> implements Marshaller<E>, Loggable {
	@Setter
	@Getter
	private EnvVarMap envVarMap;
}
