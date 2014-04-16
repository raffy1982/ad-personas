package com.bodycloud.server.rest.resource;

import org.restlet.representation.Representation;

import com.bodycloud.lib.domain.ModalitySpecification;
import com.bodycloud.lib.rest.api.ModalityResource;
import com.bodycloud.server.entity.Modality;

public class ModalityServerResource extends BasicServerResource<Modality> implements
		ModalityResource {

	@Override
	public ModalitySpecification getModality() {
		return read().getSpecification();
	}

	@Override
	public void saveModality(Representation rep) {
		createOrUpdate(rep);
	}

	@Override
	public Modality find() {
		return getEntityMapper().findByName( Modality.class, getResourceIdentifier());
	}

	@Override
	public void save(Modality e) {
		getEntityMapper().save(e);
	}

	@Override
	public void delete(Modality e) {
		getEntityMapper().delete(e);
	}

	@Override
	public Modality create() {
		Modality stored = new Modality();
		stored.setName(getResourceIdentifier());
		stored.setOwner(user);
		return stored;
	}

	@Override
	public void update(Modality entity, Representation representation) {
		ModalitySpecification m = unmarshal(ModalitySpecification.class, representation);
		entity.setSpecification(m);
	}

	@Override
	public void deleteModality() {
		super.remove();
	}

}
