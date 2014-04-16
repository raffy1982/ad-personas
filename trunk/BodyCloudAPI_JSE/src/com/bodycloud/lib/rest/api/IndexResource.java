package com.bodycloud.lib.rest.api;

import org.restlet.resource.Get;

import com.bodycloud.lib.domain.Index;

public interface IndexResource {

	@Get
	public Index buildIndex();

}
