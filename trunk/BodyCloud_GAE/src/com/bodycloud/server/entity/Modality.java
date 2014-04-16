package com.bodycloud.server.entity;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.bodycloud.lib.domain.ModalitySpecification;

@PersistenceCapable
public class Modality extends Describable {
	
	@Persistent(serialized="true")
	ModalitySpecification specification;
	
	public ModalitySpecification getSpecification() {
		return specification;
	}

	public void setSpecification(ModalitySpecification modality) {
		this.specification = modality;
	}
	

}
