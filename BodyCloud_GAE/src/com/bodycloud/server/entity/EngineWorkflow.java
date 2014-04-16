package com.bodycloud.server.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable
public class EngineWorkflow extends Describable {
	
	Blob content;

	public void setContent(Blob content) {
		this.content = content;
	}
	
	public void setContent(byte[] content) {
		this.content = new Blob(content);
	}
	
	public InputStream readWorkflow() {
		return new ByteArrayInputStream(content.getBytes());
	}

}
