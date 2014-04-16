package com.bodycloud.server.rest.resource;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import com.bodycloud.lib.rest.api.MetadataResource;
import com.bodycloud.lib.rest.ext.LinkRepresentation;
import com.bodycloud.server.entity.Describable;
import com.bodycloud.server.entity.Entity;
import com.bodycloud.server.rest.application.UrlHelper;


public abstract class BasicServerResource<T extends Entity> extends KDServerResource {
	

	/**
	 * returns the stored resource identified, or null if it does not exist
	 */
	public abstract T find();
	
	/**
	 * create a new resource instance to be stored
	 * @return the instance created
	 */
	public abstract T create();
	
	
	/**
	 * persists the resource
	 * @param e the resource to be persistet
	 */
	public abstract void save(T e);
	
	
	/**
	 * deletes a persistent resource
	 */
	public abstract void delete(T e);
	
	public abstract void update(T entity, Representation representation);
	
	
	public void createOrUpdate(Representation representation) {
		T resource = find();
		if (resource == null) {
			resource = create();
			setStatus(Status.SUCCESS_CREATED);
		} else {
			setStatus(Status.SUCCESS_NO_CONTENT);
		}
		update(resource, representation);
		save(resource);
		if (getStatus().equals(Status.SUCCESS_CREATED) && resource instanceof Describable) {
			String metadataUrl = getHostRef() + UrlHelper.replaceId(MetadataResource.URI, resource.getUUID());
			getResponse().setEntity(new LinkRepresentation("metadata", metadataUrl));
		}
	}
	
	public T read() {
		T resource = find();
		if (resource == null) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
		setStatus(Status.SUCCESS_OK);
		return resource;
	}
	
	public void remove() {
		T resource = find();
		if (resource == null) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		} else {
			delete(resource);
			setStatus(Status.SUCCESS_NO_CONTENT);
		}
	}

}
