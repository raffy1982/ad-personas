package com.bodycloud.server.rest.resource;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import weka.core.Instances;

import com.bodycloud.lib.domain.DataSpecification;
import com.bodycloud.lib.domain.GroupSpecification;
import com.bodycloud.lib.rest.api.DatasetResource;
import com.bodycloud.lib.rest.ext.InstancesRepresentation;
import com.bodycloud.server.entity.DataTable;
import com.bodycloud.server.entity.Group;

public class DatasetServerResource extends BasicServerResource<DataTable> implements DatasetResource  {
	
	private Group mGroup;
//	private DataTable mTable;
	private Instances mData;

	@Override
	public Representation getData() {
		DataTable entity = read();
		Instances instances = getInstancesMapper().load(entity);
		return new InstancesRepresentation(MediaType.TEXT_CSV, instances);
	}

	@Override
	public void deleteData() {
		remove();
	}
	
	public void createGroupEntity() {
		ClientResource cr = new ClientResource(getResourceReference().replace("/data", ""));
		cr.setChallengeResponse(getChallengeResponse());
		Representation rep = cr.get();
		GroupSpecification spec = unmarshal(GroupSpecification.class, rep);
		mGroup = new Group(getResourceIdentifier());
		mGroup.setInputSpecification(spec.getDataSpecification());
		mGroup.setMetadata(spec.getMetadata());
		mGroup.setInvitationMessage(spec.getInvitationMessage());
	}

	@Override
	public DataTable find() {
		mGroup = findGroup();
		if (mGroup == null)
			createGroupEntity();
		
		if(user != null) getLogger().info("logged user: " + user.getName());
		
		return getEntityMapper().findChildByName(mGroup, DataTable.class, user.getName());
	}

	@Override
	public void save(DataTable dt) {
		if (!mGroup.getData().contains(dt)) {
			mGroup.getData().add(dt);
			getEntityMapper().save(mGroup);
		}
//		getInstancesMapper().save(mData, mTable);
		getInstancesMapper().save(mData, dt);
	}

	@Override
	public void delete(DataTable e) {
		getLogger().info("deleting instances");
		getInstancesMapper().clear(e);
		getLogger().info("done. deleting table");
		mGroup.getData().remove(e);
		getLogger().info("done. committing transaction");
		getEntityMapper().save(mGroup);
	}

	@Override
	public DataTable create() {
		if (!mGroup.insertAllowed(user))
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
		DataTable table = new DataTable(user);
		return table;
	}

	@Override
	public void update(DataTable entity, Representation representation) {
		InstancesRepresentation instancesRepresentation = new InstancesRepresentation(representation);
		try {
			mData = instancesRepresentation.getInstances();
//			mTable = entity;
			DataSpecification inputSpec = mGroup.getInputSpecification();
			if (inputSpec != null && !inputSpec.matchingSpecification(mData))
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
		} catch (IOException e) {
			getLogger().log(Level.INFO, "got an invalid request", e);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}


	@Override
	public void uploadData(Representation representation) {
		createOrUpdate(representation);
	}

	public Group findGroup() {
		return getEntityMapper().findByName(Group.class, getResourceIdentifier());
	}

}
