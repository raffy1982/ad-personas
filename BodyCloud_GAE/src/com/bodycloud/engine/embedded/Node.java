package com.bodycloud.engine.embedded;

import java.util.Set;

public interface Node {
	
	public void setInput(BufferedInstances input) throws WrongInputException;
	
	public BufferedInstances getOutput();
	
	public Set<String> getParameters();
	
	public void configure(WorkerConfiguration config) throws WrongConfigurationException;
	
	public void run() throws Exception;

}
