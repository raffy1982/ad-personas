package com.bodycloud.ext.actrec.relative;


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

public class GetPatientsRestlet extends RehabDoctorServerResource {

	public static final String URI = "/activityrecognition/selectUser";

	@Post("xml")
	public Representation acceptItem(Representation entity) {

		DomRepresentation result = null;
		Document d = null;
		try {
			
			DomRepresentation input = new DomRepresentation(entity);
			Document doc = input.getDocument();
			// handle input document
			Element rootEl = doc.getDocumentElement();
			String usernameDoctor = XMLUtils.getTextValue(rootEl, "username");
			// output
			result = new DomRepresentation(MediaType.TEXT_XML);
			d = result.getDocument();
			
			
			try {
				ObjectifyService.register(Patient.class);
				ObjectifyService.register(Relative.class);
			} catch (Exception e) {}
			
			Key<Relative> doctor = new Key<Relative>(Relative.class,usernameDoctor);
			
			Objectify ofy = ObjectifyService.begin();
			
			
			
			List<Patient> listRU = ofy.query(Patient.class).filter("doctor", doctor).list();

			Element root = d.createElement("getUsers");
			d.appendChild(root);
			
			Element eltName4 = d.createElement("getUsersList");
			
			for (Patient us : listRU){

				
					Element user = d.createElement("user");
					
					user.setAttribute("firstname", "" + us.getFirstName());
					user.setAttribute("lastname", "" + us.getLastName());
					user.setAttribute("username", "" + us.getUsername());
					user.setAttribute("password", "" + us.getPassword());
					user.appendChild(d.createTextNode(us.getUsername()));
					eltName4.appendChild(user);
				}
				root.appendChild(eltName4);

				d.normalizeDocument();
			
		} catch (Exception e) {
			result = XMLUtils
					.createXMLError("get doctors list error", "" + e.getMessage());
		}

		return result;
	}

}
