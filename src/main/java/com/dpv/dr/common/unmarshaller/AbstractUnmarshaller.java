package com.dpv.dr.common.unmarshaller;

import com.dpv.dr.common.EnvVarMap;
import com.nhb.common.Loggable;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractUnmarshaller implements Unmarshaller, Loggable {
	@Setter
	@Getter
	private EnvVarMap envVarMap;
}
