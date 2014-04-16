package com.bodycloud.server.rest.resource;

import java.util.Collection;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import com.bodycloud.lib.domain.UserIndex;
import com.bodycloud.server.entity.Group;

public class UserIndexServerResource extends KDServerResource {
	


	@Get
	public UserIndex getIndex() {
		Group group = getEntityMapper().findByName(Group.class, getResourceIdentifier());
		if (group == null)
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		String property = getResourceUri().replaceAll(".*/", "");
		Collection<String> names = group.getProperty(property, user);
		if (names == null)
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		return new UserIndex(names);
	}

}
