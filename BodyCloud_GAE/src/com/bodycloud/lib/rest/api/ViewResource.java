package com.bodycloud.lib.rest.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.w3c.dom.Document;

public interface ViewResource {
	
	public static final String URI = "/view/{id}";
	
	@Get
	public Document getView();
	
	@Put
	public void saveView(Representation representation); 
	
	@Delete
	public void deleteView();

}
