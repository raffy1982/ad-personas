package com.bodycloud.engine.embedded;

import java.util.HashMap;

public class WorkerConfiguration extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getString(String key) {
		return (String) get(key);
	}
	
	public int getInteger(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}

}
