package com.bodycloud.server.persistence.gae;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import com.bodycloud.server.persistence.DataMapperFactory;
import com.bodycloud.server.persistence.EntityMapper;
import com.bodycloud.server.persistence.InstancesMapper;

public class DataMapperFactoryImpl implements DataMapperFactory {
	
	private static final PersistenceManagerFactory pmfInstance =
	        JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@Override
	public EntityMapper getEntityMapper() {
		return new EntityMapperImpl(pmfInstance.getPersistenceManager());
	}

	@Override
	public InstancesMapper getInstancesMapper() {
		return new InstancesMapperImpl();
	}

}
