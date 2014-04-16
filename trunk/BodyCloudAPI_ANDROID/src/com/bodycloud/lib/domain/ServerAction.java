package com.bodycloud.lib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;

import weka.core.Instances;

import com.bodycloud.lib.rest.ext.InstancesRepresentation;

public class ServerAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String uri;
	
	ServerMethod method;
	
	Set<ServerParameter> postParams;
	
	boolean repeat = false;

	Trigger trigger = new Trigger();
	
	ArrayList<Parameter> postForm;
	
	static class Trigger implements Serializable {
		private static final long serialVersionUID = 1L;
		
		int after;
	}
	
	public ServerAction() {
		this.postParams = new HashSet<ServerParameter>();
		this.postForm = new ArrayList<Parameter>();
	}


	public ServerAction(ServerAction serverAction) {
		this.method = serverAction.method;
		this.repeat = serverAction.repeat;
		this.trigger = serverAction.trigger;
		this.uri = serverAction.uri;
		this.postParams = serverAction.postParams;
		this.postForm = serverAction.postForm;
	}


	public ServerAction(String uri, ServerMethod method, boolean repeat, int sleepTime) {
		this.method = method;
		this.repeat = repeat;
		this.trigger = new Trigger();
		this.trigger.after = sleepTime;
		this.uri = uri;
		this.postParams = new HashSet<ServerParameter>();
		this.postForm = new ArrayList<Parameter>();
	}


	public ServerMethod getMethod() {
		return method;
	}

	public void setMethod(ServerMethod method) {
		this.method = method;
	}


	public String getUri() {
		return uri;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public void setTrigger(int after) {
		trigger.after = after;
	}
	
	public Set<ServerParameter> getPostParams(){
		return postParams;
	}
	
	public void waitTriggers() throws InterruptedException {
		if (trigger != null)
			Thread.sleep(trigger.after * 1000);
	}
	
	public List<ServerParameter> getParams() {
		List<ServerParameter> params = 
				new LinkedList<ServerParameter>();
		for (ServerParameter p : postParams) {
			if (!p.hasValue())
				params.add(p);
		}
		return params;
	}
	
	public boolean hasParameters() {
		return !getParams().isEmpty();
	}
	
	public boolean addParameter(String name) {
		return postParams.add(new ServerParameter(name));
	}
	
	public void setResourceIdentifier(String id) {
		if (uri == null) uri = id;
		else uri = uri.replace("{id}", id);
	}

	public void setParameter(ServerParameter param, String value) {
		if (postParams.remove(param)) {
			postForm.add(new Parameter(param.getName(), value));
		}
	}
	
	public Representation getPostRepresentation() {
		for (ServerParameter p : postParams) {
			Parameter restletParameter = p.toRestletParameter();
			if (!postForm.contains(restletParameter) && p.hasValue())
				postForm.add(restletParameter);
		}
		return new Form(postForm).getWebRepresentation();
	}
	
	public Representation getPutRepresentation(Instances instances) {
		return new InstancesRepresentation(instances);
	}
	
	@Override
	public String toString() {
		String returnString = " SERVERACTION -> ";
		if (uri != null) returnString += " uri: " + uri;
		if(method != null) returnString +=  " - method: " + method.toString();
		
		String postP = " - postParams: ( ";
		for (ServerParameter p : postParams) {
			postP += p + "; ";
		}
		postP = postP + ") ";
		
		returnString += postP;
		
		if (repeat) returnString += " - repeat: " + repeat + " - trigger after: " + trigger.after;
		if(postForm != null) returnString +=  " - postForm: " + postForm;
		
		return returnString;
	}
	
}

