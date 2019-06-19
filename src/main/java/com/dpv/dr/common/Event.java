package com.dpv.dr.common;

import com.dpv.dr.common.statics.F;
import com.nhb.common.data.PuObject;

import lombok.Data;

@Data
public class Event {
	protected String type;
	protected int refId;
	protected PuObject data;

	public Event() {
	}

	public Event(String type, int refId, PuObject data) {
		this.type = type;
		this.refId = refId;
		this.data = data;
	}

	public void parsePuObject(PuObject data) {
		this.type = data.getString(F.TYPE);
		this.data = data;
	}

	public void clone(Event event) {
		this.type = event.getType();
		this.data = event.getData();
		this.refId = event.getRefId();
	}
}
