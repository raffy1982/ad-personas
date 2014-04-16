package com.bodycloud.server.rest.application;

import org.restlet.Request;

import com.bodycloud.server.entity.User;
import com.bodycloud.server.persistence.EntityMapper;

public class UserProviderImpl implements UserProvider {

	public String getUserId(Request request) {
		if (request.getClientInfo().getUser() == null)
			return null;
		return request.getClientInfo().getUser().getIdentifier();
	}

	@Override
	public User getUser(Request request, EntityMapper entityMapper) {
		String id = getUserId(request);
		if (id != null) {
			User user = (User) entityMapper.findByName(User.class, id);
			if (user == null) {
				user = new User(id);
				entityMapper.save(user);
			}
			return user;
		}
		return null;
	}


}
