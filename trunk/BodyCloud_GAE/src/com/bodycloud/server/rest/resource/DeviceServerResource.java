package com.bodycloud.server.rest.resource;

import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.bodycloud.lib.rest.api.DeviceResource;

public class DeviceServerResource extends KDServerResource implements DeviceResource {
	

	@Override
	@Put
	public void register(String regId) {
		user.getDevices().add(regId);
		getEntityMapper().save(user);
	}

	@Override
	@Post
	public void unregister(String regId) {
		user.getDevices().remove(regId);
		getEntityMapper().save(user);
	}

}
