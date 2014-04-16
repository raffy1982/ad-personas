package com.bodycloud.engine.embedded.node;

import java.io.IOException;
import java.io.InputStream;

import weka.core.converters.CSVLoader;
import weka.core.converters.Loader;

import com.bodycloud.engine.embedded.BufferedInstances;
import com.bodycloud.engine.embedded.NodeAdapter;
import com.bodycloud.engine.embedded.WorkerConfiguration;
import com.bodycloud.engine.embedded.WrongConfigurationException;


public class FileDataReader extends NodeAdapter {
	
	String filename;
	Loader loader = new CSVLoader();
	BufferedInstances output = new BufferedInstances();

	@Override
	public void configure(WorkerConfiguration config) throws WrongConfigurationException {
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream(filename);
			loader.setSource(in);
			output.setInstances(loader.getStructure());
		} catch (IOException e) {
			throw new WrongConfigurationException("input file not valid");
		}
	}
	
	@Override
	public BufferedInstances getOutput() {
		return output;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void run() throws Exception {
		output.setInstances(loader.getDataSet());
	}
	
}
