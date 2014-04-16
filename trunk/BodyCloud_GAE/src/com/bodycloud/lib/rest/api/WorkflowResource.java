package com.bodycloud.lib.rest.api;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.w3c.dom.Document;

public interface WorkflowResource {
	
	public static final String URI = "/engine/workflow/{id}";
	
	@Post
	public Representation handleTask(Form form);
	
	@Put
	public void putWorkflow(Representation representation);
	
	@Get
	public Document getWorkflow();
	
	@Delete
	public void deleteWorkflow();

}
