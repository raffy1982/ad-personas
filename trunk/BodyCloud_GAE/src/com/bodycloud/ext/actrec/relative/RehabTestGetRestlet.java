package com.bodycloud.ext.actrec.relative;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.bodycloud.ext.actrec.db.Patient;
import com.bodycloud.ext.actrec.user.XMLUtils;
import com.bodycloud.server.rest.resource.KDServerResource;

public class RehabTestGetRestlet extends KDServerResource {

	public static final String URI = "/rehabdoctor/provaget";

//	@Get
//	public String acceptItem() {
//
//		return "<ok>ciao</ok>";
//	}
	
	@Post("xml")
	public Representation testDoctor(Representation entity) {

		DomRepresentation representation = null;
		try {
			DomRepresentation input = new DomRepresentation(entity);
			// input
			Document doc = input.getDocument();
			Element rootEl = doc.getDocumentElement();
			String username = XMLUtils.getTextValue(rootEl, "username");
			String firstName = XMLUtils.getTextValue(rootEl, "firstname");
			String lastName = XMLUtils.getTextValue(rootEl, "lastname");

			// output
			representation = new DomRepresentation(
					MediaType.TEXT_XML);

			String res = "OK "+username;

			Map<String, String> map = new HashMap<String, String>();
			map.put("result", res);
			Document d = representation.getDocument();
			d = XMLUtils.createXMLResult("doctorTestOutput", map, d);

			
		} catch (IOException e) {
			representation = XMLUtils.createXMLError("login error",
					"" + e.getMessage());
		}

		// Returns the XML representation of this document.
		return representation;
	}

}
