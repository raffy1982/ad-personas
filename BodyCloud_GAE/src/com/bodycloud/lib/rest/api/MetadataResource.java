package com.bodycloud.lib.rest.api;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.bodycloud.lib.domain.Metadata;

public interface MetadataResource {
	
	public static final String URI = "/metadata/{id}";
	
	@Get
	public Metadata getMetadata();
	
	@Put
	public void editMetadata(Representation representation);
	
	@Post
	public void editMetadata(Form form);

}
