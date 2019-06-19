package com.dpv.dr.common.exception;

import com.dpv.dr.common.Event;
import com.lmax.disruptor.ExceptionHandler;
import com.nhb.common.Loggable;

public class BaseExceptionHandler<E extends Event> implements ExceptionHandler<E>, Loggable {

	@Override
	public void handleEventException(Throwable ex, long sequence, E event) {
		getLogger().error("[DR] Error when process event: " + event.getType(), ex);

	}

	@Override
	public void handleOnStartException(Throwable ex) {
		getLogger().error("[DR] Error on start", ex);

	}

	@Override
	public void handleOnShutdownException(Throwable ex) {
		getLogger().error("[DR] Error on shutdown", ex);

	}

}
