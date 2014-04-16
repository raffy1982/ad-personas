package com.bodycloud.lib.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="")
@XmlRootElement(name="modality")
@XmlAccessorType(XmlAccessType.NONE)
public class ModalitySpecification implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(required=false)
	DataSpecification inputSpecification;
	
	@XmlElement(name="init-action", required=false)
	ServerAction initAction;

	@XmlElement(name="action")
	ServerAction action;
	
	@XmlElement(required=false)
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

}
