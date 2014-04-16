package com.bodycloud.lib.rest.ext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.JSONLoader;
import weka.core.converters.JSONSaver;
import weka.core.converters.Loader;
import weka.core.converters.Saver;

public class InstancesRepresentation extends AbstractInstancesRepresentation {


	public InstancesRepresentation(MediaType mediaType) {
		super(mediaType);
	}

	public InstancesRepresentation(Instances instances) {
		super(MediaType.TEXT_PLAIN, instances);
	}
	
	public InstancesRepresentation(MediaType mediaType, Instances instances) {
		super(mediaType, instances);
	}

	public InstancesRepresentation(Representation representation) {
		super(representation);
	}

	private void raiseNotSupportedMediaType() throws IOException {
		throw new IOException("media type not supported: " + getMediaType());
	}

	public byte[] getBuffer() throws IOException {
		Saver saver = getSaver();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		saver.setDestination(out);
		saver.setInstances(getInstances());
		saver.writeBatch();
		return out.toByteArray();
	}

	protected Saver getSaver() throws IOException {
		if (getMediaType().equals(MediaType.TEXT_PLAIN))
			return new ArffSaver();
		else if (getMediaType().equals(MediaType.TEXT_CSV))
			return new CSVSaver();
		else if (getMediaType().equals(MediaType.APPLICATION_JSON))
			return new JSONSaver();
		else
			raiseNotSupportedMediaType();
		return null;
	}

	@Override
	protected Loader getLoader() throws IOException {
		if (getMediaType().equals(MediaType.TEXT_PLAIN))
			return new ArffLoader();
		else if (getMediaType().equals(MediaType.TEXT_CSV))
			return new CSVLoader();
		else if (getMediaType().equals(MediaType.APPLICATION_JSON))
			return new JSONLoader();
		else
			raiseNotSupportedMediaType();
		return null;
	}

	@Override
	public Reader getReader() throws IOException {
		return new StringReader(new String(getBuffer()));
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(new String(getBuffer()));
	}

}
