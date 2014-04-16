package com.bodycloud.engine;

import java.util.Set;

import weka.core.Instances;

public interface Worker extends Runnable {
	
	public static final int STATUS_JOB_COMPLETED = 0;
	public static final int STATUS_WAITING_CONFIGURATION = 1;
	public static final int STATUS_ERROR_WRONG_CONFIG = 2;
	public static final int STATUS_ERROR_WRONG_INPUT = 3;
	public static final int STATUS_ERROR_RUNTIME = 4;
	public static final int STATUS_READY = 5;
	
	public int getStatus();

	public void setParameter(String param, Object value);

	public Set<String> getParameters();
	
	public boolean configure();

	public Instances getOutput();

}
