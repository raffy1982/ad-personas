package com.kdcloud.lib.rest.api;

import org.restlet.resource.Get;

import com.kdcloud.lib.domain.Index;

public interface IndexResource {

	@Get
	public Index buildIndex();

}
