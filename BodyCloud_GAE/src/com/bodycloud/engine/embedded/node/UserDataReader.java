package com.bodycloud.engine.embedded.node;

import java.util.HashSet;
import java.util.Set;

import weka.core.Instances;

import com.bodycloud.engine.embedded.BufferedInstances;
import com.bodycloud.engine.embedded.NodeAdapter;
import com.bodycloud.engine.embedded.WorkerConfiguration;
import com.bodycloud.engine.embedded.WrongConfigurationException;
import com.bodycloud.server.entity.DataTable;
import com.bodycloud.server.entity.Group;
import com.bodycloud.server.entity.User;
import com.bodycloud.server.persistence.EntityMapper;
import com.bodycloud.server.persistence.InstancesMapper;

public class UserDataReader extends NodeAdapter {
	
	public static final String SOURCE_USER_PARAMETER = "sourceUser";
	public static final String SOURCE_GROUP_PARAMETER = "sourceGroup";
	public static final String APPLICANT = "analyst_id";
	
	User user;
	Group group;
	DataTable table;
	EntityMapper entityMapper;
	InstancesMapper instancesMapper;
	
	public UserDataReader() {
	}
	
	public UserDataReader(User user, Group group) {
		super();
		this.user = user;
		this.group = group;
	}

	@Override
	public void configure(WorkerConfiguration config) throws WrongConfigurationException {
		String msg = null;
		String userId = (String) config.get(SOURCE_USER_PARAMETER);
		String groupId = (String) config.get(SOURCE_GROUP_PARAMETER);
		User applicant = (User) config.get(APPLICANT);
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
		
		if (group == null) {
			msg = "not a valid group in configuration";
			
		} else if (!group.analysisAllowed(applicant)) {
				msg = "user has no permission for this request";
				
		} else if (group != null && user != null) {
			table = (DataTable) entityMapper.findChildByName(group, DataTable.class, user.getName());
			if (table == null)
				msg = "user has no data";
		}
		
		if (msg != null)
			throw new WrongConfigurationException(msg);
	}

	@Override
	public Set<String> getParameters() {
		Set<String> params = new HashSet<String>();
		if (user == null)
			params.add(SOURCE_USER_PARAMETER);
		if (group == null)
			params.add(SOURCE_GROUP_PARAMETER);
		return params;
	}

	@Override
	public BufferedInstances getOutput() {
		Instances instances = instancesMapper.load(table);
		return new BufferedInstances(instances);
	}

}
