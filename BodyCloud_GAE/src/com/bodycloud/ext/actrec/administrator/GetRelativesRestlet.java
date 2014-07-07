package com.bodycloud.ext.actrec.administrator;

import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.bodycloud.ext.actrec.db.Relative;
import com.bodycloud.ext.actrec.relative.RehabDoctorServerResource;
import com.bodycloud.ext.actrec.user.XMLUtils;

public class GetRelativesRestlet extends RehabDoctorServerResource {

	public static final String URI = "/activityrecognition/selectRelatives";

	@Post("xml")
	public Representation acceptItem(Representation entity) {

		DomRepresentation result = null;
		Document d = null;
		try {
			// output
			result = new DomRepresentation(MediaType.TEXT_XML);
			d = result.getDocument();

			try {
				ObjectifyService.register(Relative.class);
			} catch (Exception e) {
			}
			Objectify ofy = ObjectifyService.begin();
			
			List<Relative> listRD = ofy.query(Relative.class).list();

			Element root = d.createElement("getDoctors");
			d.appendChild(root);
			
			Element eltName4 = d.createElement("getDoctorsList");
			
			for (Relative rd : listRD){

				
					Element doct = d.createElement("doctor");
					
					doct.setAttribute("firstname", "" + rd.getFirstName());
					doct.setAttribute("lastname", "" + rd.getLastName());
					doct.setAttribute("username", "" + rd.getUsername());
					doct.setAttribute("password", "" + rd.getPassword());
					doct.appendChild(d.createTextNode(rd.getUsername()));
					eltName4.appendChild(doct);
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
