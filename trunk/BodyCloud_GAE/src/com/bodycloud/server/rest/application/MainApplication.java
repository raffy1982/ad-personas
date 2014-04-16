package com.bodycloud.server.rest.application;

import java.util.logging.Level;

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;

import com.bodycloud.server.rest.resource.WorkflowServerResource;

public class MainApplication extends Application {
	
	public static final String WORKER_URI = "/_exec/{id}";
	
	@Override
	public Restlet createInboundRoot() {
		getLogger().setLevel(Level.INFO);
		
		Context applicationContext = new GAEContext(getLogger());
		
		setContext(applicationContext);

		Router router = new Router(getContext());
		
		router.attach(WORKER_URI, WorkflowServerResource.class);
		
		Application kdApplication = new KDApplication(applicationContext, createOutboundRoot());
		
		// COMMENT THE FOLLOWING TWO LINES IF YOU WANT TO REMOVE AUTH-CHECK
		ChallengeAuthenticator guard = new ChallengeAuthenticator(null, ChallengeScheme.HTTP_BASIC, "testRealm");
		//guard.setVerifier(new OAuthVerifier(getLogger(), true));
		guard.setVerifier(new OAuthVerifier(getLogger(), false));
		
		Filter core = new Filter() {
			@Override
			protected int beforeHandle(Request request, Response response) {
				/*if (!request.getProtocol().equals(Protocol.HTTPS)) {
					String target = "https://" + request.getHostRef().getHostDomain() + request.getResourceRef().getPath();
	                Redirector redirector = new Redirector(getContext(), target, Redirector.MODE_CLIENT_SEE_OTHER);
	                redirector.handle(request, response);
	                return STOP;
				}*/
				return CONTINUE;
			}
		};

		//core.setNext(kdApplication); // DE-COMMENT THIS LINE IF YOU WANT TO REMOVE AUTH-CHECK (AND COMMENT THE FOLLOWING TWO LINES)
		core.setNext(guard);
		guard.setNext(kdApplication);
		
		router.attachDefault(core);

		return router;

	}
	
	@Override
	public Restlet createOutboundRoot() {
		return new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				new Client(request.getProtocol()).handle(request, response);
			}
		};
	}

}
