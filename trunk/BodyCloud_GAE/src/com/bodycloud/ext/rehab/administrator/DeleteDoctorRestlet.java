package com.bodycloud.ext.rehab.administrator;

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
import com.bodycloud.ext.rehab.db.Relative;
import com.bodycloud.ext.rehab.user.XMLUtils;
import com.bodycloud.server.rest.resource.KDServerResource;

public class DeleteDoctorRestlet extends KDServerResource {

	public static final String URI = "/rehabdoctor/deleteDoctor";

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
				ObjectifyService.register(Relative.class);
			} catch (Exception e) {
			}

			Objectify ofy = ObjectifyService.begin();
			Relative dct = ofy.query(Relative.class)
					.filter("username", username).get();
			if (dct == null) {
				res = "doctor not found.";
			} else {
				
				ofy.delete(dct);
				res = "OK, " + dct.getUsername()+ " deleted.";
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("result", res);
			Document d = representation.getDocument();
			d = XMLUtils.createXMLResult("rehabDeleteDoctorOutput", map, d);

			
		} catch (IOException e) {
			representation = XMLUtils.createXMLError("doctor delete error",
					"" + e.getMessage());
		}

		// Returns the XML representation of this document.
		return representation;
	}





}
