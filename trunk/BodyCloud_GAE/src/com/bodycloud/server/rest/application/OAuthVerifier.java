package com.bodycloud.server.rest.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.security.User;
import org.restlet.security.Verifier;

public class OAuthVerifier implements Verifier {
	
	private static final String VALIDATION_URI = "https://www.googleapis.com/oauth2/v1/tokeninfo";
	private static final String ACCESS_TOKEN_QUERY = "access_token";
	private static final String JSON_ATTR_EMAIL = "email";
	private static final String ADMIN_FILE = "admin.properties";
	
	public static final ChallengeScheme HTTP_BEARER_SCHEME = new ChallengeScheme("HTTP_BEARER", "Bearer");
	public static final ChallengeScheme HTTP_BASIC_SCHEME = ChallengeScheme.HTTP_BASIC;
	public static final ChallengeScheme HTTP_OAUTH_SCHEME = ChallengeScheme.HTTP_OAUTH;
	
	private Logger logger;
	private boolean allowAdministrators;
	private Properties administrators;
	
	public OAuthVerifier(Logger logger, boolean allowAdministrators) {
		super();
		this.logger = logger;
		this.allowAdministrators = allowAdministrators;
		try {
			this.administrators = getAdministratorDetails();
		} catch (IOException e) {
			this.allowAdministrators = false;
		}
	}

	@Override
	public int verify(Request request, Response response) {
		if (request.getChallengeResponse() == null)
			return Verifier.RESULT_MISSING;
		ChallengeScheme scheme = request.getChallengeResponse().getScheme();
		if (scheme.equals(HTTP_BASIC_SCHEME) && allowAdministrators) {
			//administrators can login with their credentials
			return verifyCredentials(request);

		} else if (scheme.equals(HTTP_BASIC_SCHEME) && !allowAdministrators) {
			logger.log(Level.INFO, "CHALLENGE SCHEME IS: HTTP_BASIC (EMULATING OAUTH)");
			return verifyHttpBasic(request);
		}
		else if (scheme.equals(HTTP_BEARER_SCHEME) || scheme.equals(HTTP_OAUTH_SCHEME)) {
			//use oauth authentication
			return verifyOauthToken(request);
			
		} else {
			return Verifier.RESULT_INVALID;
		}
	}
	
	private int verifyHttpBasic(Request request) {
		
		String username = request.getChallengeResponse().getIdentifier();
		String secret = new String(request.getChallengeResponse().getSecret());
		logger.log(Level.INFO, "VERYFYING user: " + username + " with secret: " + secret);
		
		Reference reference = new Reference(VALIDATION_URI);
	    reference.addQueryParameter(ACCESS_TOKEN_QUERY, secret);
	    ClientResource clientResource = new ClientResource(reference);
	    try {
	    	Representation representation = clientResource.get();
	    	
	    	JsonRepresentation json = new JsonRepresentation(representation);
			JSONObject object = json.getJsonObject();
			String user = (String) object.get(JSON_ATTR_EMAIL);
			if (user != null) {
				request.getClientInfo().setUser(new User(user, secret));
				return Verifier.RESULT_VALID;
			}
	    } catch (Exception e) {
	    	logger.log(Level.SEVERE, "error getting authentication server response", e);
	    }
	    return Verifier.RESULT_INVALID;
		
	}

	private int verifyCredentials(Request request) {
		String userId = request.getChallengeResponse().getIdentifier();
		String secret = new String(request.getChallengeResponse().getSecret());
		String passwd = (String) administrators.get(userId);
		if (passwd != null && passwd.equals(secret)) {
			request.getClientInfo().setUser(new User(userId));
			return Verifier.RESULT_VALID;
		}
		return Verifier.RESULT_INVALID;
	}
	
	private Properties getAdministratorDetails() throws IOException {
		Properties prop = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream(ADMIN_FILE);
		prop.load(in);
		return prop;
	}

	public int verifyOauthToken(Request request) {
		//validate token
		String token = request.getChallengeResponse().getRawValue();
		Reference reference = new Reference(VALIDATION_URI);
	    reference.addQueryParameter(ACCESS_TOKEN_QUERY, token);
	    ClientResource clientResource = new ClientResource(reference);
	    try {
	    	Representation representation = clientResource.get();
	    	
	    	logger.log(Level.INFO, "SONO NEL verifyOauthToken E getIdentifier vale: "+clientResource.getChallengeResponse().getIdentifier());
	    	
	    	JsonRepresentation json = new JsonRepresentation(representation);
			JSONObject object = json.getJsonObject();
			String user = (String) object.get(JSON_ATTR_EMAIL);
			if (user != null) {
				//valid token
				//add user to client info
				request.getClientInfo().setUser(new User(user, token));
				return Verifier.RESULT_VALID;
			}
	    } catch (Exception e) {
	    	logger.log(Level.INFO, "error getting authentication server response");
	    }
	    return Verifier.RESULT_INVALID;
	}

}
