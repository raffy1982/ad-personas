package com.bodycloud.lib.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="metadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	String name;
	String owner;
	String company;
	String description;
	
	
	public Metadata() {
	}
	
	public Metadata(String name, String owner, String company,
			String description) {
		super();
		this.name = name;
		this.owner = owner;
		this.company = company;
		this.description = description;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public void update(Metadata newMetadata) {
		if (newMetadata.name != null)
			this.name = newMetadata.name;
		
		if (newMetadata.owner != null)
			this.owner = newMetadata.owner;
		
		if (newMetadata.company != null)
			this.company = newMetadata.company;
		
		if (newMetadata.description != null)
			this.description = newMetadata.description;
	}

}
