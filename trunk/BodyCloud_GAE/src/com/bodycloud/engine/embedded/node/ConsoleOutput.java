package com.bodycloud.engine.embedded.node;

import weka.core.converters.CSVSaver;

import com.bodycloud.engine.embedded.BufferedInstances;
import com.bodycloud.engine.embedded.NodeAdapter;
import com.bodycloud.engine.embedded.WrongInputException;

public class ConsoleOutput extends NodeAdapter {
	
	BufferedInstances input;
	
	@Override
	public void setInput(BufferedInstances input) throws WrongInputException {
		this.input = input;
	}
	
	@Override
	public void run() throws Exception {
		CSVSaver saver = new CSVSaver();
		saver.setInstances(input.getInstances());
		saver.setDestination(System.out);
		saver.writeBatch();
	}

}
