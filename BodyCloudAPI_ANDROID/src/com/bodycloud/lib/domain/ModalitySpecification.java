package com.bodycloud.lib.domain;

import java.io.Serializable;

public class ModalitySpecification implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	DataSpecification inputSpecification;
	
	ServerAction initAction;

	ServerAction action;
	
	DataSpecification outputSpecification;
	
	

	public DataSpecification getInputSpecification() {
		return inputSpecification;
	}

	public void setInputSpecification(DataSpecification inputSpecification) {
		this.inputSpecification = inputSpecification;
	}

	public ServerAction getInitAction() {
		return initAction;
	}

	public void setInitAction(ServerAction initAction) {
		this.initAction = initAction;
	}

	public ServerAction getAction() {
		return action;
	}

	public void setAction(ServerAction action) {
		this.action = action;
	}

	public DataSpecification getOutputSpecification() {
		return outputSpecification;
	}

	public void setOutputSpecification(DataSpecification outputSpecification) {
		this.outputSpecification = outputSpecification;
	}
	
	public String toString() {
		String returnString = "MODALITYSPECIFICATION -> " + "modalityName: " + name;
		if (inputSpecification != null) returnString += " - inputSpecification: " + inputSpecification.toString();
		if (initAction != null) returnString += " - initAction: " + initAction.toString();
		if (action != null) returnString += " - action: " + action.toString();
		if (outputSpecification != null) returnString += " - outputSpecification: " + outputSpecification.toString();
		
		return  returnString;
	}

}
