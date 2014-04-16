package com.bodycloud.server.entity;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class DataTable extends Entity {

	public DataTable() {
		super();
	}

	public DataTable(User owner) {
		super(owner.getName());
	}

	public boolean isOwner(User applicant) {
		return getName().equals(applicant.getName());
	}

	public String getOwnerName() {
		return getName();
	}

}
