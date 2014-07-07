package com.bodycloud.ext.actrec.db;

import java.io.Serializable;

import javax.persistence.Id;

public class Relative implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id private String username; //email di google
	private String password;
	private String firstname;
	private String lastname;

	
	public Relative() {}
	
	public Relative (String user, String pwd, String firstName, String lastName) {
		username = user;
		password = pwd;
		firstname = firstName;
		lastname = lastName;
	}
	

	
	
	
	public String getPassword() {
		return password;
		}
	
	public String getUsername() {
		return username;
	}
	public String getFirstName() {
		return firstname;
	}
	public String getLastName() {
		return lastname;
	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}
	public void setLastName(String lastname) {
		this.lastname = lastname;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Relative)) return false;
		Relative p = (Relative) obj;
		return username.equals(p.getUsername());
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return username + " " + firstname + " " +lastname; 
	}
    

}
