package com.kdcloud.lib.rest.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Put;

public interface WorkflowNodeResource {
	
	public static final String URI = "/engine/node/{id}";
	
	@Put
	public void addPlugin(Representation rep);

}
