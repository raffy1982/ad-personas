package com.bodycloud.server.persistence.gae;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import com.bodycloud.server.entity.DataTable;
import com.bodycloud.server.persistence.InstancesMapper;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class InstancesMapperImpl implements InstancesMapper {
	
	private static double counter = 0;
	
	@Override
	public void save(Instances instances, DataTable table) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key tableKey = KeyFactory.stringToKey(table.getUUID());
		final List<Entity> entities = new ArrayList<Entity>(instances.size());
		for (int j=0; j<instances.size(); j++) {
			Instance inst = instances.get(j);
			Entity e = new Entity(instances.relationName(), tableKey);
			e.setProperty("ID", ++counter);
			
			for (int i = 0; i < inst.numAttributes(); i++)    
				e.setProperty(inst.attribute(i).name(), inst.value(i));
						
			entities.add(e);
		}
		
		DatastoreWriter.write(new Runnable() {
			
			@Override
			public void run() {
				datastore.put(entities);
			}
		});
	}
	
	public Iterator<Entity> query(DataTable table) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key tableKey = KeyFactory.stringToKey(table.getUUID());
		PreparedQuery q = datastore.prepare(new Query(tableKey));
		Iterator<Entity> it = q.asIterator();
		it.next(); // table
		return it;
	}

	@Override
	public Instances load(DataTable table) {
		Iterator<Entity> it = query(table);
		if (!it.hasNext())
			return null;
		Entity e = it.next();
		Instances instances = new Instances(e.getKind(), loadAttributes(e), 1000);
		do {
			loadInstance(instances, e);
			e = (it.hasNext() ? it.next() : null);
		} while (e != null);
		return instances;
	}
	
	public ArrayList<Attribute> loadAttributes(Entity e) {
		ArrayList<Attribute> attrs = new ArrayList<Attribute>(e.getProperties().size());
		for (String property : e.getProperties().keySet()) {
			if (e.getProperty(property) instanceof Double) {
				attrs.add(new Attribute(property));
			}
		}
		return attrs;
	}
	
	public void loadInstance(Instances instances, Entity e) {
		double[] row = new double[instances.numAttributes()];
		for (int i = 0; i < row.length; i++) {
			String property = instances.attribute(i).name();
			row[i] = (Double) e.getProperty(property);
		}
		instances.add(new DenseInstance(1, row));
	}
	
	@Override
	public void clear(DataTable table) {
		counter = 0;
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Iterator<Entity> it = query(table);
		DatastoreWriter.write(new Runnable() {
			
			@Override
			public void run() {
				while (it.hasNext()) {
					datastore.delete(it.next().getKey());
				}
			}
		});
	}

}
