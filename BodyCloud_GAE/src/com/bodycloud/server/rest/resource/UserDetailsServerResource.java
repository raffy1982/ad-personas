package com.bodycloud.server.rest.resource;

import com.bodycloud.lib.rest.api.UserDetailsResource;

public class UserDetailsServerResource extends KDServerResource implements
		UserDetailsResource {
	

	@Override
	public String getUserId() {
		return user.getName();
	}

}
