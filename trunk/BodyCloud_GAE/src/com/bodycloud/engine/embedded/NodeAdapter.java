package com.bodycloud.engine.embedded;

import java.util.HashSet;
import java.util.Set;

public abstract class NodeAdapter implements Node {

	@Override
	public void setInput(BufferedInstances input) throws WrongInputException {
		
	}

	@Override
	public BufferedInstances getOutput() {
		return null;
	}

	@Override
	public Set<String> getParameters() {
		return new HashSet<String>();
	}

	@Override
	public void configure(WorkerConfiguration config)
			throws WrongConfigurationException {
		
	}

	@Override
	public void run() throws Exception {
		
	}
	
	

}
