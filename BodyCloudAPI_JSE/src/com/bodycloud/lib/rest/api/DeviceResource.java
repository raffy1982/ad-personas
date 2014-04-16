package com.bodycloud.lib.rest.api;

import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface DeviceResource {
	
	public static final String URI = "/device";
	
	@Put
	public void register(String regId);
	
	@Post
	public void unregister(String regId);

}
