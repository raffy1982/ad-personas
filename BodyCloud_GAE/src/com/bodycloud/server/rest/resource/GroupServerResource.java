package com.bodycloud.server.rest.resource;

import java.util.Arrays;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import com.bodycloud.lib.domain.GroupSpecification;
import com.bodycloud.lib.rest.api.GroupResource;
import com.bodycloud.server.entity.Group;

public class GroupServerResource extends BasicServerResource<Group> implements
		GroupResource {

	@Override
	public void editGroup(Representation rep) {
		createOrUpdate(rep);
	}

	@Override
	public GroupSpecification getSpecification() {
		Group group = read();
		GroupSpecification spec = new GroupSpecification(group.getMetadata(), group.getInputSpecification());
		if (group.getOwner().equals(user))
			spec.setInvitationMessage(group.getInvitationMessage());
		return spec;
	}

	@Override
	public Group find() {
		return getEntityMapper().findByName(Group.class, getResourceIdentifier());
	}

	@Override
	public void save(Group e) {
		getEntityMapper().save(e);
	}

	@Override
	public void delete(Group e) {
		e.getData().clear();
		getEntityMapper().delete(e);
	}

	@Override
	public Group create() {
		return new Group(getResourceIdentifier(), user);
	}

	@Override
	public void update(Group group, Representation rep) {
		if (!user.isOwner(group))
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
		if (rep != null && !rep.isEmpty()) {
			group.setOwner(user);
			GroupSpecification spec = unmarshal(GroupSpecification.class, rep);
			group.setInputSpecification(spec.getDataSpecification());
			group.getMetadata().update(spec.getMetadata());
			group.setInvitationMessage(spec.getInvitationMessage());
		}

	}

	@Override
	public void deleteGroup() {
		super.remove();
	}

	@Override
	public void setProperties(Form form) {
		Group group = read();
		if (!user.isOwner(group))
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);

		String values = form.getValues(Group.PROPERTY_ENROLLED);
		if (values != null) {
			String[] enrolled = values.split(",");
			group.getEnrolled(user).addAll(Arrays.asList(enrolled));
		}

		values = form.getValues(Group.PROPERTY_MEMBERS);
		if (values != null) {
			String[] members = values.split(",");
			group.getMembers(user).addAll(Arrays.asList(members));
		}

		save(group);
	}

}
