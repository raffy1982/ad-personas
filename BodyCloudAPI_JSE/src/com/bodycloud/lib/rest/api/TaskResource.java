package com.bodycloud.lib.rest.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

/**
 * URI /report/{id}
 * @author spax
 *
 */
public interface TaskResource {
	
	
	
	public static final String URI = "/task/{id}";

	
	/**
	 * retrives the report of the task with the given id
	 * @return
	 */
	@Get
	public Representation retriveOutput();

}
