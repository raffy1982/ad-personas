package com.bodycloud.ext.rehab.user;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.bodycloud.ext.rehab.db.Patient;
import com.bodycloud.server.entity.User;
import com.bodycloud.server.rest.resource.KDServerResource;

public abstract class RehabServerResource extends KDServerResource {

	// il paziente in "sessione"
	Patient rehabUser;

	@Override
	public void beforeHandle() {
		super.beforeHandle();
		// importante
		// paziente = <cerca paziente>
		// if (paziente == null)
		User u = getUser();
		String username = u.getName();
		
		try {
			ObjectifyService.register(Patient.class);
		} catch (Exception e) {
		}

		Objectify ofy = ObjectifyService.begin();
		rehabUser = ofy.query(Patient.class).filter("username", username)
				.get();
		if (rehabUser == null)
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
	}

}
