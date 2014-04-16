/**
 * Copyright (C) 2012 Vincenzo Pirrone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.kdcloud.lib.domain;

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
