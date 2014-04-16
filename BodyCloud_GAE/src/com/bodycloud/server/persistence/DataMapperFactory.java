package com.bodycloud.server.persistence;

public interface DataMapperFactory {
	
	public EntityMapper getEntityMapper();
	public InstancesMapper getInstancesMapper();

}
