package com.bodycloud.ext.rehab.patient;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.bodycloud.ext.rehab.db.Patient;
import com.bodycloud.ext.rehab.relative.RehabDoctorServerResource;
import com.bodycloud.ext.rehab.user.XMLUtils;


public class LoginRehabPatientRestlet extends RehabDoctorServerResource {

	public static final String URI = "/rehabdoctor/loginrehabpatient";

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
				ObjectifyService.register(Patient.class);
				
			} catch (Exception e) {}
			Objectify ofy = ObjectifyService.begin();
			
			
			Patient ru  = ofy.query(Patient.class).filter("username", username.toLowerCase()).get();
			if(ru == null)
			{
				result = XMLUtils.createXMLError("Login Error: ", "Username Errato!");
			}
		else{
			if(ru.getPassword().equals(""+password.hashCode()))
			 {
				Element root = d.createElement("loginrehabpatientOutput");
				d.appendChild(root);
				root.setAttribute("username", ru.getUsername());
				root.setAttribute("firstname", ru.getFirstName());
				root.setAttribute("lastname", ru.getLastName());
				d.normalizeDocument();
			 
				
			 } else{result = XMLUtils.createXMLError("Login Error: ", "Incorrect Password.");}
		}     
		} catch (Exception e) {
			
			result = XMLUtils
					.createXMLError("patient login error", "" + e.getMessage());
		
		}
		
		return result;
	}
	
	
}

