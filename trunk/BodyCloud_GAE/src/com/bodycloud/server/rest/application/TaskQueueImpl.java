package com.bodycloud.server.rest.application;

import org.restlet.Request;
import org.restlet.data.Form;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class TaskQueueImpl implements TaskQueue {

	@Override
	public void push(Request request) {
		String url = request.getResourceRef().toString();
		TaskOptions opt = TaskOptions.Builder.withUrl(url);
		Form form = new Form(request.getEntity());
		for (String name : form.getNames())
			opt.param(name, form.getFirstValue(name));
		
		QueueFactory.getDefaultQueue().add(opt);
	}

}
