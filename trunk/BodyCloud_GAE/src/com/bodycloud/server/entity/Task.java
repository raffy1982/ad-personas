package com.bodycloud.server.entity;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Task extends Entity {
	
	@Persistent
	User applicant;
	
	@Persistent
	DataTable result;
	
	boolean completed = false;
	
	public Task() {
	}
	

	public Task(User applicant) {
		super();
		this.applicant = applicant;
		this.result = new DataTable(applicant);
	}

	public User getApplicant() {
		return applicant;
	}

	public void setApplicant(User applicant) {
		this.applicant = applicant;
	}

	public DataTable getResult() {
		return result;
	}

	public void setResult(DataTable result) {
		this.result = result;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}


}
