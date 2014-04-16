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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.CharacterRepresentation;
import org.restlet.representation.Representation;

import weka.core.Instances;
import weka.core.converters.Loader;

public abstract class AbstractInstancesRepresentation extends CharacterRepresentation {
	
	private Instances instances;
	private Representation representation;
	
	private String response = null;
	
	public AbstractInstancesRepresentation(MediaType mediaType) {
		super(mediaType);
	}
	
	public AbstractInstancesRepresentation(MediaType mediaType,
			Instances instances) {
		super(mediaType);
		this.instances = instances;
	}

	public AbstractInstancesRepresentation(Representation representation) {
		super(representation.getMediaType());
		this.representation = representation;
	}

	protected abstract Loader getLoader() throws IOException;
	
	public void setInstances(Instances instances) {
		this.instances = instances;
	}

	public Instances getInstances() throws IOException {
		if (instances == null) {
			if (representation == null)
				throw new IOException("instances is null");
			if (representation instanceof AbstractInstancesRepresentation)
				instances = ((AbstractInstancesRepresentation) representation)
						.getInstances();
			else {
				Loader loader = getLoader();
				loader.setSource(new ByteArrayInputStream(response.getBytes()));
				instances = loader.getDataSet();
			}
		}
		return instances;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
