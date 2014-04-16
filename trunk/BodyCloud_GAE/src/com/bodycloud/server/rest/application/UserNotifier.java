package com.bodycloud.server.rest.application;

import com.bodycloud.server.entity.User;

public interface UserNotifier {
	
	public void notify(User user);

}
