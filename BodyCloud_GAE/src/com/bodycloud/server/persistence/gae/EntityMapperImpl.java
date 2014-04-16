package com.bodycloud.server.persistence.gae;

import java.util.Collection;

import javax.jdo.PersistenceManager;

import com.bodycloud.server.entity.Entity;
import com.bodycloud.server.persistence.EntityMapper;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class EntityMapperImpl implements EntityMapper {

	private PersistenceManager pm;

	public EntityMapperImpl(PersistenceManager pm) {
		super();
		this.pm = pm;
	}

	@Override
	public void save(final Entity e) {
		DatastoreWriter.write(new Runnable() {
			
			@Override
			public void run() {
				pm.makePersistent(e);
			}
		});
	}

	@Override
	public void delete(Entity e) {
		pm.deletePersistent(e);
	}


	@Override
	public void close() {
		pm.close();
	}

	@Override
	public Entity findByUUID(String uuid) {
		try {
			Key key = KeyFactory.stringToKey(uuid);
			String entityKind = Entity.class.getSimpleName();
			String className = Entity.class.getName().replace(entityKind,
					key.getKind());
			return (Entity) pm.getObjectById(Class.forName(className), key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> T findByName(Class<T> clazz, String name) {
		Key k = KeyFactory.createKey(clazz.getSimpleName(), name);
		try {
			return pm.getObjectById(clazz, k);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public <T> T findChildByName(Entity father, Class<T> child, String childName) {
		try {
			Key key = new KeyFactory.Builder(father.getClass().getSimpleName(),
					father.getName())
					.addChild(child.getSimpleName(), childName).getKey();
			return pm.getObjectById(child, key);
		} catch (Exception tr) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getAll(Class<T> clazz) {
		return (Collection<T>) pm.newQuery(clazz).execute();
	}

}