/**
 * Copyright (C) 2012 Vincenzo Pirrone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.kdcloud.lib.rest.ext;

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
