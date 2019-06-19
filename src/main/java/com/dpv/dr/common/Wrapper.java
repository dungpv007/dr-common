package com.dpv.dr.common;

import com.dpv.dr.common.config.Config;
import com.dpv.dr.common.eventhandler.EventHandlerFactory;
import com.dpv.dr.common.eventproducer.EventProducerFactory;
import com.dpv.dr.common.marshaller.MarshallerFactory;
import com.dpv.dr.common.unmarshaller.UnmarshallerFactory;

public interface Wrapper {
	UnmarshallerFactory getUnmarshallerFactory();

	MarshallerFactory getMarshallerFactory();

	EventHandlerFactory getEventHandlerFactory();

	EventProducerFactory getEventProducerFactory();

	Config getConfig();

	EnvVarMap getEnvVarMap();
}
