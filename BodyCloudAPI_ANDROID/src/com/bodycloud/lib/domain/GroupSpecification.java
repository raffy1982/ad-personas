package com.bodycloud.lib.domain;


public class GroupSpecification {
	
	Metadata metadata;
	
	DataSpecification dataSpecification;
	
	String invitationMessage;
	
	public GroupSpecification() {
	}

	public GroupSpecification(Metadata metadata,
			DataSpecification dataSpecification) {
		super();
		this.metadata = metadata;
		this.dataSpecification = dataSpecification;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public DataSpecification getDataSpecification() {
		return dataSpecification;
	}

	public void setDataSpecification(DataSpecification dataSpecification) {
		this.dataSpecification = dataSpecification;
	}

	public String getInvitationMessage() {
		return invitationMessage;
	}

	public void setInvitationMessage(String invitationMessage) {
		this.invitationMessage = invitationMessage;
	}
	

}
