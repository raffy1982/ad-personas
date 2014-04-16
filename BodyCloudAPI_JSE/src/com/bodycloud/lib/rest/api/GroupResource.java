package com.bodycloud.lib.rest.api;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.bodycloud.lib.domain.GroupSpecification;

public interface GroupResource {
	
	public static final String URI = "/group/{id}";
	
	@Put
	public void editGroup(Representation rep);
	
	@Post
	public void setProperties(Form form);
	
	@Get
	public GroupSpecification getSpecification();
	
	@Delete
	public void deleteGroup();

}
