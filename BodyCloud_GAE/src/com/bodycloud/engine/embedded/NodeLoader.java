package com.bodycloud.engine.embedded;

public interface NodeLoader {
	
	public Class<? extends Node> loadNode(String className) throws ClassNotFoundException;

}
