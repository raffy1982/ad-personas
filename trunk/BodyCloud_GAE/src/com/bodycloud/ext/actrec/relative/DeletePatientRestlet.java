package com.bodycloud.ext.actrec.relative;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.bodycloud.ext.actrec.db.Relative;
import com.bodycloud.ext.actrec.db.Patient;
import com.bodycloud.ext.actrec.user.XMLUtils;
import com.bodycloud.server.rest.resource.KDServerResource;

public class DeletePatientRestlet extends KDServerResource {

	public static final String URI = "/activityrecognition/deletepatient";

	@Post("xml")
	public Representation acceptItem(Representation entity) {

		DomRepresentation representation = null;
		try {
			DomRepresentation input = new DomRepresentation(entity);
			// input
			Document doc = input.getDocument();
			Element rootEl = doc.getDocumentElement();
			String username = XMLUtils.getTextValue(rootEl, "username");
			

			// output
			representation = new DomRepresentation(
					MediaType.TEXT_XML);

			// Generate a DOM document representing the list of
			// items.

			String res = "";
			try {
				ObjectifyService.register(Patient.class);
			} catch (Exception e) {
			}

			Objectify ofy = ObjectifyService.begin();
			Patient us = ofy.query(Patient.class).filter("username", username).get();
			if (us == null) {
				res = "user not found.";
			} else {
				
				ofy.delete(us);
				res = "OK, " + us.getUsername()+ " deleted.";
				
			}
			getLogger().info("res: "+res);
			Map<String, String> map = new HashMap<String, String>();
			map.put("result", res);
			Document d = representation.getDocument();
			d = XMLUtils.createXMLResult("rehabDeleteUserOutput", map, d);

			
		} catch (IOException e) {
			representation = XMLUtils.createXMLError("user delete error",
					"" + e.getMessage());
		}

		// Returns the XML representation of this document.
		return representation;
	}





}
