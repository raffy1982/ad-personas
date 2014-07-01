package com.bodycloud.ext.rehab.relative;

import java.util.Date;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;


import com.bodycloud.ext.rehab.db.Administrator;
import com.bodycloud.ext.rehab.db.Relative;
import com.bodycloud.ext.rehab.db.Patient;
import com.bodycloud.server.entity.User;
import com.bodycloud.server.rest.resource.KDServerResource;

public abstract class RehabDoctorServerResource extends KDServerResource {


	@Override
	public void beforeHandle() {
		super.beforeHandle();
		
//		/*
//		//Esempio di registrazione di nuove entity nel DB su cloud
//		//ENtity Relative
//		 Relative rd = new Relative("luigisalvatoregalluzzi@gmail.com","luigisg", "Luigi Salvatore", "Galluzzi");
//		 ObjectifyService.register(Relative.class);
//		 Objectify ofy = ObjectifyService.begin();
//		 ofy.put(rd); 
		}

}
