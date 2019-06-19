//package com.dpv.dr.common;
//
//import java.util.concurrent.ThreadFactory;
//
//import com.lmax.disruptor.BusySpinWaitStrategy;
//import com.lmax.disruptor.dsl.Disruptor;
//import com.lmax.disruptor.dsl.ProducerType;
//
//public class DisruptorBuilder<E extends Event> {
//	private String threadNamePattern = "Disruptor #%d";
//	private int ringBufferSize;
//	private int id;
//
//	public static DisruptorBuilder create() {
//		return new DisruptorBuilder();
//	}
//
//	public DisruptorBuilder id(int id) {
//		this.id = id;
//		return this;
//	}
//
//	public DisruptorBuilder ringBufferSize(int size) {
//		this.ringBufferSize = size;
//		return this;
//	}
//
//	public DisruptorBuilder threadNamePattern(String s) {
//		this.threadNamePattern = s;
//		return this;
//	}
//
//	public Disruptor<E> build() {
//		Disruptor<E> disruptor = new Disruptor<E>(Event::new, ringBufferSize, new ThreadFactory() {
//			int count = 0;
//
//			@Override
//			public Thread newThread(Runnable r) {
//				return new Thread(r, String.format(threadNamePattern, id, count++));
//			}
//		}, ProducerType.SINGLE, new BusySpinWaitStrategy());
//		return disruptor;
//
//	}
//}
