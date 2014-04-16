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

import org.restlet.data.Parameter;

public class ServerParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String REFERENCE_PREFIX = "xpath:";
	
	
	public static String newReference(String reference) {
		if (reference.contains(REFERENCE_PREFIX))
			return reference;
		return REFERENCE_PREFIX + reference;
	}
	
	private final String name;
	private final String value;
	private final Reference reference;
	
	public static class Reference implements Serializable {
		private static final long serialVersionUID = 1L;
		
		String xpath;
		
		ReferenceType type = ReferenceType.CHOICE;
		
		public String toString() {
			return "REFERENCETYPE -> " + "xpath: " + xpath + " + type: " + type;
		}
		
		public void setXPath(String xp) {
			xpath = xp;
		}
		
		public void setReferenceType(ReferenceType rt) {
			type = rt;
		}
		
	}
	
	public enum ReferenceType {
		CHOICE,
		MAP;
		
		@Override
		public String toString() {
			switch (this) {
			case CHOICE: return "CHOICE";
			case MAP: return "MAP";
			default: return super.toString();
			}
		}
		
		public static ReferenceType getByString(String refT){
			if (refT != null) {
				if (refT.equalsIgnoreCase("CHOICE")) return CHOICE;
				if (refT.equalsIgnoreCase("MAP")) return MAP;
			}
			return null;
		}
	}

	public ServerParameter() {
		this(null, null, null);
	}

	public ServerParameter(String representation) {
		this.value = null;
		if (representation.contains(REFERENCE_PREFIX)) {
			this.name = null;
			this.reference = new Reference();
			this.reference.xpath = representation.replace(REFERENCE_PREFIX, "");
		}
		else {
			this.name = representation;
			this.reference = null;
		}
	}
	
	public ServerParameter(String name, String value, String reference) {
		super();
		this.name = name;
		this.value = value;
		this.reference = new Reference();
		this.reference.xpath = reference;
	}
	
	public ServerParameter(String name, String value, String referenceXpath, String referenceType) {
		super();
		this.name = name;
		this.value = value;
		this.reference = new Reference();
		this.reference.xpath = referenceXpath;
		this.reference.type = ReferenceType.getByString(referenceType);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getReferenceExpression() {
		return reference.xpath;
	}
	
	public ReferenceType getReferenceType() {
		return reference.type;
	}

	public boolean hasReference() {
		return reference != null;
	}
	
	public boolean hasValue() {
		return value != null;
	}
	
	public ServerParameter toReference(String reference) {
		return new ServerParameter(name, null, reference);
	}
	
	public ServerParameter toValue(String value) {
		return new ServerParameter(name, value, null);
	}
	
	public Parameter toRestletParameter() {
		return new Parameter(name, value);
	}

	@Override
	public String toString() {
		/*if (value != null)
			return value;
		String repr = (hasReference() ? REFERENCE_PREFIX + reference.xpath : name);
		return "{" + repr + "}";*/
		
		String returnString = " SERVERPARAMETER -> ";
		if (name != null) returnString += " - name: " + name;
		if (value != null) returnString += " - value: " + value;
		if (reference != null) returnString += " - reference: " + reference;
		return returnString;
		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ServerParameter) {
			ServerParameter other = (ServerParameter) obj;
			if (this.name == null) {
				if (other.name == null)
					return this.reference.equals(other.reference);
				return false;
			}
			return this.name.equals(other.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (name != null)
			return name.hashCode();
		return reference.hashCode();
	};

}
