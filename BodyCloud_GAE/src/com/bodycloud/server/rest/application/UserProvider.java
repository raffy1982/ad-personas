package com.bodycloud.server.rest.application;

import org.restlet.Request;

import com.bodycloud.server.entity.User;
import com.bodycloud.server.persistence.EntityMapper;

public interface UserProvider {
	
	public User getUser(Request request, EntityMapper entityMapper);

}
