package com.bodycloud.lib.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="users")
public class UserIndex implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@XmlElement(name="user")
	Collection<String> users;
	
	public UserIndex() {
	}

	public UserIndex(Collection<String> users) {
		super();
		this.users = users;
	}
	
	public Collection<String> asCollection() {
		return users;
	}

}
