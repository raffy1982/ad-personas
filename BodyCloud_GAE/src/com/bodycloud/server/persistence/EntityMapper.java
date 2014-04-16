package com.bodycloud.server.persistence;

import java.util.Collection;

import com.bodycloud.server.entity.Entity;


public interface EntityMapper {
	
	public Entity findByUUID(String uuid);
	public <T> T findByName(Class<T> clazz, String name);
	public <T> T findChildByName(Entity father, Class<T> child, String name);
	public <T> Collection<T> getAll(Class<T> clazz);
	public void save(Entity e);
	public void delete(Entity e);
	public void close();
	
}
