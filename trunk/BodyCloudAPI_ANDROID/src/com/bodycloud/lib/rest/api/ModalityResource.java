package com.bodycloud.lib.rest.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import com.bodycloud.lib.domain.ModalitySpecification;

public interface ModalityResource {
	
	public static final String URI = "/modality/{id}";
	
	@Get
	public ModalitySpecification getModality();
	
	@Put
	public void saveModality(Representation rep); 
	
	@Delete
	public void deleteModality();

}
