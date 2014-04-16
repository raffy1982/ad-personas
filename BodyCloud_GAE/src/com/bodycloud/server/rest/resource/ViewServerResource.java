package com.bodycloud.server.rest.resource;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;

import com.bodycloud.lib.rest.api.ViewResource;
import com.bodycloud.server.entity.View;

public class ViewServerResource extends BasicServerResource<View> implements ViewResource {
	
	
	@Override
	public View find() {
		return getEntityMapper().findByName(View.class, getResourceIdentifier());
	}

	
	@Override
	public void save(View e) {
		getEntityMapper().save(e);
	}

	@Override
	public void delete(View e) {
		getEntityMapper().delete(e);
	}

	@Override
	@Get
	public Document getView() {
		try {
			return read().getSpecification();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "error reading document", e);
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}

	@Override
	@Put
	public void saveView(Representation representation) {
		createOrUpdate(representation);
	}

	@Override
	public View create() {
		View v = new View();
		v.setName(getResourceIdentifier());
		v.setOwner(user);
		return v;
	}

	@Override
	public void update(View entity, Representation representation) {
		DomRepresentation dom = new DomRepresentation(representation);
		try {
			entity.setSpecification(dom.getDocument());
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "error saving document", e);
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}

	@Override
	public void deleteView() {
		super.remove();
	}

	

}
