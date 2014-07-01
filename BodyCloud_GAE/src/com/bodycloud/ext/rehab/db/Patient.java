package com.bodycloud.ext.rehab.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class Patient implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id private String username; //email di google
	private String firstname;
	private String lastname;
	private String password;
	
	
	private int[] F_MIN  = {2115,	2180,	2062};
	private int[] F_MAX  = {2720,	2805,	2675};
	private int[] F_ZERO = {2410,	2485,	2380};
	private int[] B_MIN  = {2155,	2180,	2095};
	private int[] B_MAX  = {2770,	2820,	2660};
	private int[] B_ZERO = {2470,	2504,	2385};
	
	private int registeredexercises = 0;
	
	private Key<Relative> doctor;
	private Date registrationDate;
	
	public Patient() {}
	
	public Patient (String user,  String psw, String firstName, String lastName ) {
		username = user;
		password = psw;
		firstname = firstName;
		lastname = lastName;
	}
	


	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstname;
	}
	public String getLastName() {
		return lastname;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}
	public void setLastName(String lastname) {
		this.lastname = lastname;
	}
		
	public int getRegisteredExercises() {
		return registeredexercises;
	}
	
	public void setRegisteredExercises(int registeredExercises) {
		this.registeredexercises = registeredExercises;
	}
	
	public Key<Relative> getDoctor() {
		return doctor;
	}
	
	public void setDoctor(Key<Relative> doctor) {
		this.doctor = doctor;
	}
	
	
	
	
	

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public int[] getF_MIN() {
		return F_MIN;
	}

	public void setF_MIN(int[] f_MIN) {
		F_MIN = f_MIN;
	}

	public int[] getF_MAX() {
		return F_MAX;
	}

	public void setF_MAX(int[] f_MAX) {
		F_MAX = f_MAX;
	}

	public int[] getF_ZERO() {
		return F_ZERO;
	}

	public void setF_ZERO(int[] f_ZERO) {
		F_ZERO = f_ZERO;
	}

	public int[] getB_MIN() {
		return B_MIN;
	}

	public void setB_MIN(int[] b_MIN) {
		B_MIN = b_MIN;
	}

	public int[] getB_MAX() {
		return B_MAX;
	}

	public void setB_MAX(int[] b_MAX) {
		B_MAX = b_MAX;
		
	}

	public int[] getB_ZERO() {
		return B_ZERO;
	}

	public void setB_ZERO(int[] b_ZERO) {
		B_ZERO = b_ZERO;
		
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Patient)) return false;
		Patient p = (Patient) obj;
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
