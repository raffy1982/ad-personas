package com.bodycloud.server.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.bodycloud.lib.domain.DataSpecification;

@PersistenceCapable
public class Group extends Describable {
	
	public static final String PROPERTY_ENROLLED = "enrolled"; 
	public static final String PROPERTY_CONTRIBUTORS = "contributors"; 
	public static final String PROPERTY_MEMBERS = "members"; 
	
	public Group(String name) {
		super(name);
	}
	
	public Group(String name, User owner) {
		super(name, owner);
	}
	
	@Persistent
	private Collection<DataTable> data = new HashSet<DataTable>();
	
	@Persistent(serialized="true")
	private DataSpecification inputSpecification;
	
	
	@Persistent(serialized="true")
	private Collection<String> enrolled = new LinkedList<String>();
	
	@Persistent(serialized="true")
	private Collection<String> members = new LinkedList<String>();
	
	String invitationMessage;

	public Group() {
	}
	
	public Collection<DataTable> getData() {
		return data;
	}

	public void setData(Collection<DataTable> data) {
		this.data = data;
	}

	public DataSpecification getInputSpecification() {
		return inputSpecification;
	}

	public void setInputSpecification(DataSpecification inputSpecification) {
		this.inputSpecification = inputSpecification;
	}

	public Collection<String> getEnrolled(User applicant) {
		if (analysisAllowed(applicant))
			return enrolled;
		return null;
	}

	public void setEnrolled(Collection<String> enrolled) {
		this.enrolled = enrolled;
	}

	public Collection<String> getMembers(User applicant) {
		if (analysisAllowed(applicant))
			return members;
		return null;
	}

	public void setMembers(Collection<String> members) {
		this.members = members;
	}

	public String getInvitationMessage() {
		return invitationMessage;
	}

	public void setInvitationMessage(String invitationMessage) {
		this.invitationMessage = invitationMessage;
	}
	
	public boolean insertAllowed(User committer) {
		return enrolled == null || enrolled.isEmpty() || enrolled.contains(committer.getName());
	}
	
	public boolean analysisAllowed(User applicant) {
		return getOwner() == null || applicant.isOwner(this) || members.contains(applicant.getName());
	}
	
	public Collection<String> getContributors(User applicant) {
		Collection<String> names = new LinkedList<String>();
		for (DataTable t : data) {
			if (analysisAllowed(applicant) || t.isOwner(applicant))
				names.add(t.getOwnerName());
		}
		return names;
	}
	
	public Collection<String> getProperty(String property, User applicant) {
		if (property.equals(PROPERTY_CONTRIBUTORS))
			return getContributors(applicant);
		if (property.equals(PROPERTY_ENROLLED))
			return getEnrolled(applicant);
		if (property.equals(PROPERTY_MEMBERS))
			return getMembers(applicant);
		return null;
	}
	

}
