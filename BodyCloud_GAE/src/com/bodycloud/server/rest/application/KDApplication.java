package com.bodycloud.server.rest.application;

import java.util.Set;

import org.reflections.Reflections;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;

import com.bodycloud.ext.actrec.administrator.DeleteDoctorRestlet;
import com.bodycloud.ext.actrec.administrator.GetRelativesRestlet;
import com.bodycloud.ext.actrec.administrator.LoginAdministratorRestlet;
import com.bodycloud.ext.actrec.administrator.RelativeRegistrationRestlet;
import com.bodycloud.ext.actrec.patient.GetPatientInfoRestlet;
import com.bodycloud.ext.actrec.patient.LoginPatientRestlet;
import com.bodycloud.ext.actrec.relative.DeletePatientRestlet;
import com.bodycloud.ext.actrec.relative.GetPatientsRestlet;
import com.bodycloud.ext.actrec.relative.LoginRelativeRestlet;
import com.bodycloud.ext.actrec.relative.PatientRegistrationRestlet;
import com.bodycloud.lib.rest.api.GroupResource;
import com.bodycloud.server.entity.Group;
import com.bodycloud.server.rest.resource.IndexServerResource;
import com.bodycloud.server.rest.resource.KDServerResource;
import com.bodycloud.server.rest.resource.UserIndexServerResource;

public class KDApplication extends Application {
	
	public static final ChallengeResponse defaultChallenge = 
			new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");
	
	private Restlet outboundRoot;
	
	
	public KDApplication(Context context, Restlet outboundRoot) {
		super(context);
		this.outboundRoot = outboundRoot;
	}

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public Restlet createInboundRoot() {

		Router router = new Router(getContext());
		
		//automatically map resources with uri
		Reflections reflections = new Reflections("com.bodycloud.server.rest.resource");

		Set<Class<? extends KDServerResource>> allClasses = reflections
				.getSubTypesOf(KDServerResource.class);

		for (Class<? extends KDServerResource> clazz : allClasses) {
			try {
				getLogger().fine("found resource " + clazz.getSimpleName());
				String uri = clazz.getField("URI").get(null).toString();
				router.attach(uri, clazz);
				getLogger().fine("mapped uri " + uri);
			} catch (Exception e) {
				getLogger().fine("could not map any uri to the class");
			}
		}
		
		//manually map indexes
		router.attach("/engine/workflow", IndexServerResource.class);
		router.attach("/modality", IndexServerResource.class);
		router.attach("/engine/plugin", IndexServerResource.class);
		router.attach("/view", IndexServerResource.class);
		router.attach("/group", IndexServerResource.class);
		
		router.attach(GroupResource.URI + "/" + Group.PROPERTY_CONTRIBUTORS, UserIndexServerResource.class);
		router.attach(GroupResource.URI + "/" + Group.PROPERTY_ENROLLED, UserIndexServerResource.class);
		router.attach(GroupResource.URI + "/" + Group.PROPERTY_MEMBERS, UserIndexServerResource.class);
		
		//redirects
//		for (final Entry<String, String> e : Redirects.getRedirects().entrySet()) {
//			router.attach(e.getKey(), new Restlet() {
//				@Override
//				public void handle(Request request, Response response) {
//					String target = e.getValue();
//					String query = request.getResourceRef().getQuery();
//					if (query != null)
//						target = target + "?" + query;
//					response.redirectPermanent(target);
//					response.commit();
//				}
//			});
//		}

		

		//activity recognition administrator restlet
		router.attach(RelativeRegistrationRestlet.URI, RelativeRegistrationRestlet.class);
		router.attach(LoginAdministratorRestlet.URI, LoginAdministratorRestlet.class);
		router.attach(GetRelativesRestlet.URI, GetRelativesRestlet.class);
		router.attach(DeleteDoctorRestlet.URI, DeleteDoctorRestlet.class);
		
		
		//activity recognition relative restlet
		router.attach(PatientRegistrationRestlet.URI, PatientRegistrationRestlet.class);
		router.attach(LoginRelativeRestlet.URI, LoginRelativeRestlet.class);
		router.attach(DeletePatientRestlet.URI, DeletePatientRestlet.class);
		router.attach(GetPatientsRestlet.URI, GetPatientsRestlet.class);
		
		//activity recognition patient restlet
		router.attach(LoginPatientRestlet.URI, LoginPatientRestlet.class);
		router.attach(GetPatientInfoRestlet.URI, GetPatientInfoRestlet.class);
		
		return router;
	}
	
	@Override
	public Restlet createOutboundRoot() {
		return outboundRoot;
	}

}
