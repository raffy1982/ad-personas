package com.bodycloud.server.entity;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class User extends Entity {
	
	
	public User() {
		super();
	}

	public User(String name) {
		super(name);
	}

	@Persistent(serialized = "true")
	private LinkedList<String> devices = new LinkedList<String>();
	
	@Persistent(mappedBy="applicant")
	private List<Task> submittedTasks = new LinkedList<Task>();
	
	public LinkedList<String> getDevices() {
		return devices;
	}

	public void setDevices(LinkedList<String> devices) {
		this.devices = devices;
	}
	
	public boolean isOwner(Describable entity) {
		return equals(entity.getOwner());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (obj instanceof User)
			return ((User) obj).getName().equals(this.getName());
		return false;
	}

	public List<Task> getSubmittedTasks() {
		return submittedTasks;
	}

	public void setSubmittedTasks(List<Task> submittedTasks) {
		this.submittedTasks = submittedTasks;
	}
	

}
