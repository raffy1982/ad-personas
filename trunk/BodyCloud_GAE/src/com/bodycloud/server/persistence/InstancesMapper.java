package com.bodycloud.server.persistence;

import weka.core.Instances;

import com.bodycloud.server.entity.DataTable;

public interface InstancesMapper {

	public void save(Instances instances, DataTable table);
	
	public Instances load(DataTable table);

	public void clear(DataTable table);

}
