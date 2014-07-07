package com.bodycloud.ext.actrec.patient;



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
import com.bodycloud.ext.actrec.relative.RehabDoctorServerResource;
import com.bodycloud.ext.actrec.user.XMLUtils;

public class GetPatientInfoRestlet extends RehabDoctorServerResource {

	public static final String URI = "/activityrecognition/getpatientinfo";

	@Post("xml")
	public Representation acceptItem(Representation entity) {

		DomRepresentation result = null;
		Document d = null;
		try {
			
			DomRepresentation input = new DomRepresentation(entity);
			Document doc = input.getDocument();
			// handle input document
			Element rootEl = doc.getDocumentElement();
			String usernamePatient = XMLUtils.getTextValue(rootEl, "username");
			// output
			result = new DomRepresentation(MediaType.TEXT_XML);
			d = result.getDocument();
			
			
			try {
				ObjectifyService.register(Patient.class);
			} catch (Exception e) {}
			
			
			Objectify ofy = ObjectifyService.begin();
			
			List<Patient> ruList = ofy.query(Patient.class).list();
            Patient ru = null;
            
            
            for(Patient user: ruList)
            {
            	if(usernamePatient.equals(""+user.getUsername()))
            	  ru=user;
            }
			
	
			Element root = d.createElement("getuserinfoOutput");
			d.appendChild(root);
            		root.setAttribute("firstname", "" + ru.getFirstName());
					root.setAttribute("lastname", "" + ru.getLastName());
					root.setAttribute("username", "" + ru.getUsername());
					root.setAttribute("password", "" + ru.getPassword());
					root.setAttribute("dodctor", "" + ru.getDoctor().getName());
					d.normalizeDocument();
			
		} catch (Exception e) {
			result = XMLUtils.createXMLError("get patient info error", "" + e.getMessage());
		}

		return result;
	}

}
