package com.bodycloud.ext.actrec.administrator;

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
import com.bodycloud.ext.actrec.db.Administrator;
import com.bodycloud.ext.actrec.db.Relative;
import com.bodycloud.ext.actrec.relative.RehabDoctorServerResource;
import com.bodycloud.ext.actrec.user.XMLUtils;


public class LoginAdministratorRestlet extends RehabDoctorServerResource {

	static {
        ObjectifyService.register(Administrator.class);
    }
	
	public static final String URI = "/activityrecognition/loginAdministrator";

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
				ObjectifyService.register(Administrator.class);
			} catch (Exception e) {
			}
			Objectify ofy = ObjectifyService.begin();
			
			Administrator ad  = ofy.query(Administrator.class).filter("username", username.toLowerCase()).get();
			if(ad == null)
			{
				result = XMLUtils.createXMLError("Login Error: ","Username Errato!");
			}
		else{
			if(ad.getPassword().equals(""+password.hashCode()))
			 {
				Element root = d.createElement("loginrehabAdministratorOutput");
				d.appendChild(root);
				root.setAttribute("username", ad.getUsername());
				root.setAttribute("firstname", ad.getFirstName());
				root.setAttribute("lastname", ad.getLastName());

				List<Relative> l = new ArrayList<Relative>();
				l = ofy.query(Relative.class).list();

				Element eltName3 = d.createElement("rehab_doctor_list");
				for (Relative rd : l) {
					Element userElement = d.createElement("doctor");
					userElement.setAttribute("username", "" + rd.getUsername());
					userElement.setAttribute("firstname", "" + rd.getFirstName());
					userElement.setAttribute("lastname", "" + rd.getLastName());
					eltName3.appendChild(userElement);
				}
				root.appendChild(eltName3);

				d.normalizeDocument();
			
			 } else{result = XMLUtils.createXMLError("Login Error: ", "Incorrect Password.");}
		}     
		} catch (Exception e) {
			
			result = XMLUtils
					.createXMLError("Administrator login error", "" + e.getMessage());
		
		}
		
		return result;
	}
	
	
}

