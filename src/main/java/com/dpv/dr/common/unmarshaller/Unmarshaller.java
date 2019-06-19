package com.dpv.dr.common.unmarshaller;

import com.dpv.dr.common.Event;
import com.nhb.common.async.RPCFuture;

public interface Unmarshaller {

	RPCFuture<Event> unmarshall(Event event);

}
