package com.bodycloud.lib.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.restlet.data.Parameter;

@XmlAccessorType(XmlAccessType.FIELD)
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
	
	static class Reference implements Serializable {
		private static final long serialVersionUID = 1L;
		
		@XmlAttribute
		String xpath;
		
		@XmlAttribute
		ReferenceType type = ReferenceType.CHOICE;
		
	}
	
	public enum ReferenceType {
		CHOICE,
		MAP
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
		if (value != null)
			return value;
		String repr = (hasReference() ? REFERENCE_PREFIX + reference.xpath : name);
		return "{" + repr + "}";
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
