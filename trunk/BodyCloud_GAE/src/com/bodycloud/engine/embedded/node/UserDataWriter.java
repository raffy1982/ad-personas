package com.bodycloud.engine.embedded.node;

import java.util.HashSet;
import java.util.Set;

import com.bodycloud.engine.embedded.BufferedInstances;
import com.bodycloud.engine.embedded.NodeAdapter;
import com.bodycloud.engine.embedded.WorkerConfiguration;
import com.bodycloud.engine.embedded.WrongConfigurationException;
import com.bodycloud.engine.embedded.WrongInputException;
import com.bodycloud.server.entity.DataTable;
import com.bodycloud.server.entity.Group;
import com.bodycloud.server.entity.User;
import com.bodycloud.server.persistence.EntityMapper;
import com.bodycloud.server.persistence.InstancesMapper;

public class UserDataWriter extends NodeAdapter {
	
	public static final String DEST_USER_PARAMETER = "destinationUser";
	public static final String DEST_GROUP_PARAMETER = "destinationGroup";

	BufferedInstances mState;
	Group group;
	User user;
	EntityMapper entityMapper;
	InstancesMapper instancesMapper;
	
	public UserDataWriter() {
	}
	

	public UserDataWriter(User user) {
		super();
		this.user = user;
	}

	@Override
	public void configure(WorkerConfiguration config) throws WrongConfigurationException  {
		String msg = null;
		String userId = (String) config.get(DEST_USER_PARAMETER);
		String groupId = (String) config.get(DEST_GROUP_PARAMETER);
		entityMapper = (EntityMapper) config.get(EntityMapper.class.getName());
		instancesMapper = (InstancesMapper) config.get(InstancesMapper.class.getName());
		if (entityMapper == null)
			msg = "no persistence context in configuration";
		if (userId != null)
			user = (User) entityMapper.findByName(User.class, userId);
		if (user == null)
			msg = "not a valid user in configuration";
		if (groupId != null)
			group = (Group) entityMapper.findByName(Group.class, groupId);
		if (group == null)
			msg = "not a valid group in configuration";
		if (msg != null)
			throw new WrongConfigurationException(msg);
	}

	@Override
	public Set<String> getParameters() {
		Set<String> params = new HashSet<String>();
		if (user == null)
			params.add(DEST_USER_PARAMETER);
		if (group == null)
			params.add(DEST_GROUP_PARAMETER);
		return params;
	}

	@Override
	public void setInput(BufferedInstances input) throws WrongInputException {
		if (input instanceof BufferedInstances) {
			mState = (BufferedInstances) input;
		} else {
			throw new WrongInputException();
		}
	}

	@Override
	public void run() {
		DataTable t = new DataTable(user);
		group.getData().add(t);
		entityMapper.save(group);
		instancesMapper.save(mState.getInstances(), t);
	}

}
