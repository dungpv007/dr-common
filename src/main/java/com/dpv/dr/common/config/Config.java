package com.dpv.dr.common.config;

import com.dpv.dr.common.statics.F;
import com.nhb.common.data.PuObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {
	private int ringBufferSize;
	private int disruptorSize;
	private long futureTimeout = 30000;

	public static Config fromPuObject(PuObject data) {
		Config config = new Config();
		config.setRingBufferSize(data.getInteger(F.RING_BUFFER_SIZE));
		config.setDisruptorSize(data.getInteger(F.DISRUPTOR_SIZE));
		return config;
	}

}
