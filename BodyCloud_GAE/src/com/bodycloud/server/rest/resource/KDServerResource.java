package com.bodycloud.server.rest.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.restlet.data.Method;
import org.restlet.ext.xml.XmlRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.xml.sax.SAXException;

import com.bodycloud.server.entity.User;
import com.bodycloud.server.persistence.DataMapperFactory;
import com.bodycloud.server.persistence.EntityMapper;
import com.bodycloud.server.persistence.InstancesMapper;
import com.bodycloud.server.rest.application.ConvertHelper;
import com.bodycloud.server.rest.application.KDApplication;
import com.bodycloud.server.rest.application.ResourcesFinder;
import com.bodycloud.server.rest.application.UserProvider;

public abstract class KDServerResource extends ServerResource {

	private EntityMapper entityMapper;
	private InstancesMapper instancesMapper;
	private UserProvider userProvider;
	private ResourcesFinder resourcesFinder;

	User user;

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		userProvider = inject(UserProvider.class);
		resourcesFinder = inject(ResourcesFinder.class);

		DataMapperFactory factory = inject(DataMapperFactory.class);
		if (factory != null) {
			entityMapper = factory.getEntityMapper();
			instancesMapper = factory.getInstancesMapper();
		}
	}

	protected String getResourceIdentifier() {
		return (String) getRequestAttributes().get("id");
	}
	
	protected String getResourceUri() {
		return getResourceReference().replace(getHostRef().toString(), "");
	}
	
	protected String getResourceReference() {
		return getReference().toString().replaceAll("\\?.*", "");
	}
	
	public Representation doGet() {
		ClientResource cr = new ClientResource(getRequest().getResourceRef());
		if (getChallengeResponse() != null) {
			cr.setChallengeResponse(getChallengeResponse());
			if(getClientInfo().getUser() != null) {
				getLogger().log(Level.INFO, "USER ID: "+getClientInfo().getUser().getIdentifier());
				getLogger().log(Level.INFO, "USER NAME: "+getClientInfo().getUser().getName());
			}
		}
		else
			cr.setChallengeResponse(KDApplication.defaultChallenge);
		return cr.get();
	}
	
	public void beforeHandle() {
		user = userProvider.getUser(getRequest(), entityMapper);
	}
	
	@Override
	public Representation handle() {
		try {
			beforeHandle();
			Representation local = fetchLocaly();
			if (local != null)
				return local;
			return super.handle();
		} catch (Throwable t) {
			doCatch(t);
		}
		return null;
	}
	
	public Representation fetchLocaly() {
		if (resourcesFinder != null && getMethod().equals(Method.GET))
			try {
				Representation local = resourcesFinder.find(getResourceReference());
				getResponse().setEntity(local);
				return local;
			} catch (ResourceException e) {}
		return null;
	}


	@SuppressWarnings("unchecked")
	protected <T> T inject(Class<T> baseClass) {
		return (T) getApplication().getContext().getAttributes()
				.get(baseClass.getName());
	}
	
	public <T> T unmarshal(Class<T> clazz, Representation rep) {
		Representation schemaRep;
		if (resourcesFinder != null) {
			schemaRep = resourcesFinder.find(getHostRef() + ResourcesFinder.XML_SCHEMA_URI);
		} else {
			String url = getHostRef() + ResourcesFinder.XML_SCHEMA_URI;
			schemaRep = new ClientResource(url).get();
		}
		try {
			SAXSource source = XmlRepresentation.getSaxSource(schemaRep);
			Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(source);
			return ConvertHelper.toObject(clazz, rep, schema);
		} catch (SAXException e) {
			getLogger().log(Level.SEVERE, "error during schema generation", e);
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "error during schema generation", e);
		}
		throw new ResourceException(500);
	}

	public EntityMapper getEntityMapper() {
		return entityMapper;
	}
	
	public InstancesMapper getInstancesMapper() {
		return instancesMapper;
	}

	public User getUser() {
		return user;
	}	
	
}
