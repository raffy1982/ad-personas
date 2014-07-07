package com.bodycloud.ext.actrec.relative;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.bodycloud.ext.actrec.db.Patient;
import com.bodycloud.ext.actrec.db.Relative;
import com.bodycloud.ext.actrec.user.XMLUtils;


public class LoginRelativeRestlet extends RehabDoctorServerResource {

	public static final String URI = "/activityrecognition/loginrelative";

	@Post("xml")
	public Representation acceptItem(Representation entity) {

		DomRepresentation result = null;
		Document d = null;
		try {
			DomRepresentation input = new DomRepresentation(entity);
			Document doc = input.getDocument();
			// handle input document
			Element rootEl = doc.getDocumentElement();
			String username = XMLUtils.getTextValue(rootEl, "username");
            String password = XMLUtils.getTextValue(rootEl, "password");
			
            // output
			result = new DomRepresentation(MediaType.TEXT_XML);
			d = result.getDocument();

			try {
				ObjectifyService.register(Relative.class);
				ObjectifyService.register(Patient.class);
				
			} catch (Exception e) {}
			Objectify ofy = ObjectifyService.begin();
			Key<Relative> doctor = new Key<Relative>(Relative.class,username);
			
			Relative rd  = ofy.query(Relative.class).filter("username", username.toLowerCase()).get();
			if(rd == null)
			{
				result = XMLUtils.createXMLError("Login Error: ", "Username Errato!");
			}
		else{
			if(rd.getPassword().equals(""+password))
			 {
				Element root = d.createElement("loginrehabdoctorOutput");
				d.appendChild(root);
				root.setAttribute("username", rd.getUsername());
				root.setAttribute("firstname", rd.getFirstName());
				root.setAttribute("lastname", rd.getLastName());
				d.normalizeDocument();
			 
				
			 } else{result = XMLUtils.createXMLError("Login Error: ", "Incorrect Password.");}
		}     
		} catch (Exception e) {
			
			result = XMLUtils
					.createXMLError("doctor login error", "" + e.getMessage());
		
		}
		
		return result;
	}
	
	
}

