package com.bodycloud.server.entity;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.bodycloud.lib.domain.Metadata;
import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Describable extends Entity {
	
	@Unowned
	@Persistent
	private User owner;
	
	@Persistent(serialized="true")
	private Metadata metadata = new Metadata();

	public Describable() {
		this(null, null);
	}

	public Describable(String name) {
		this(name, null);
	}
	
	public Describable(String name, User owner) {
		super(name);
		this.owner = owner;
		this.metadata.setName(name);
		if (owner != null)
			this.metadata.setOwner(owner.getName());
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	

}
