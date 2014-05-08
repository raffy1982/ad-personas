package com.bodycloud.server.rest.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;

import weka.core.Instances;

import com.bodycloud.engine.KDEngine;
import com.bodycloud.engine.Worker;
import com.bodycloud.engine.embedded.node.UserDataReader;
import com.bodycloud.lib.rest.api.TaskResource;
import com.bodycloud.lib.rest.api.WorkflowResource;
import com.bodycloud.lib.rest.ext.InstancesRepresentation;
import com.bodycloud.lib.rest.ext.LinkRepresentation;
import com.bodycloud.server.entity.EngineWorkflow;
import com.bodycloud.server.entity.Task;
import com.bodycloud.server.entity.User;
import com.bodycloud.server.persistence.EntityMapper;
import com.bodycloud.server.persistence.InstancesMapper;
import com.bodycloud.server.rest.application.ConvertHelper;
import com.bodycloud.server.rest.application.MainApplication;
import com.bodycloud.server.rest.application.TaskQueue;
import com.bodycloud.server.rest.application.UrlHelper;
import com.bodycloud.server.rest.application.UserNotifier;

public class WorkflowServerResource extends BasicServerResource<EngineWorkflow> implements WorkflowResource  {
	
	private static final String QUERY_QUEUE = "queue";
	private static final String PARAMETER_TASK = "task";
	
	TaskQueue taskQueue;
	KDEngine engine;
	UserNotifier notifier;

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		engine = inject(KDEngine.class);
		taskQueue = inject(TaskQueue.class);
		notifier = inject(UserNotifier.class);
	}

	@Override
	public void putWorkflow(Representation representation) {
		super.createOrUpdate(representation);
	}
	
	@Override
	public void deleteWorkflow() {
		super.remove();
	}

	@Override
	public Document getWorkflow() {
		InputStream is = read().readWorkflow();
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "error reading workflow", e);
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}


	@Override
	public EngineWorkflow find() {
		return getEntityMapper().findByName(EngineWorkflow.class, getResourceIdentifier());
	}

	@Override
	public void save(EngineWorkflow e) {
		getEntityMapper().save(e);
	}

	@Override
	public void delete(EngineWorkflow e) {
		getEntityMapper().delete(e);
	}

	@Override
	public EngineWorkflow create() {
		EngineWorkflow stored = new EngineWorkflow();
		stored.setName(getResourceIdentifier());
		stored.setOwner(user);
		return stored;
	}

	@Override
	public void update(EngineWorkflow entity, Representation representation) {
		byte[] workflow = ConvertHelper.toByteArray(representation);
		entity.setContent(workflow);
		try {
			engine.getWorker(entity.readWorkflow()); // validate workflow
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		} 
	}
	
	public Instances execute(InputStream input, Form parameters, User applicant) throws IOException {
		Worker worker = engine.getWorker(input);
		worker.setParameter(EntityMapper.class.getName(), getEntityMapper());
		worker.setParameter(InstancesMapper.class.getName(), getInstancesMapper());
		worker.setParameter(UserDataReader.APPLICANT, applicant);
		for (String param : worker.getParameters()) {
			/* **WORKAROUND**
			String value = parameters.getFirstValue(param);
			*/
			if(param.equals("sourceUser"))
				{
					getLogger().info("setting parameter: " + param + "=" + applicant.getName());
					worker.setParameter(param, applicant.getName());
				}
			else
			{
				String value = parameters.getFirstValue(param);
				worker.setParameter(param, value);
			}
				
			}
		if (worker.configure())
			worker.run();
		if (worker.getStatus() == Worker.STATUS_JOB_COMPLETED) {
			return worker.getOutput();
		} else {
			throw new ResourceException(Status.CLIENT_ERROR_PRECONDITION_FAILED);
		}
	}

	public Instances execute(Form form, User applicant) {
		try {
			InputStream workflow = doGet().getStream();
			return execute(workflow, form, applicant);
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, e.getMessage(), e);
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}
	
	public Representation createTask(Form form) {
		if (doGet().isEmpty())
			throw new ResourceException(404);
		Task t = new Task(user);
		t.setName(Long.toString(new Date().getTime()));
		getEntityMapper().save(t);
		String url = UrlHelper.replaceId(MainApplication.WORKER_URI, getResourceIdentifier());
		Request req = new Request(Method.POST, url);
		form.add(PARAMETER_TASK, t.getUUID());
		req.setEntity(form.getWebRepresentation());
		taskQueue.push(req);
		setStatus(Status.SUCCESS_ACCEPTED);
		return new LinkRepresentation(PARAMETER_TASK, UrlHelper.replaceId(TaskResource.URI, t.getUUID()));
	}
	
	public void consumeTask(Form form) {
		String uuid = form.getFirstValue(PARAMETER_TASK);
		Logger l = Logger.getLogger(getClass().getSimpleName());
		l.severe("UUID: "+uuid);
		Task t = (Task) getEntityMapper().findByUUID(uuid);
		Instances output = execute(form, t.getApplicant());
		if (output != null && !output.isEmpty()) {
			getInstancesMapper().save(output, t.getResult());
		}
		t.setCompleted(true);
		getEntityMapper().save(t);
		notifier.notify(t.getApplicant());
	}
	
	public Representation execute(Form form) {
		Instances output = execute(form, user);
		if (output == null || output.isEmpty()) {
			setStatus(Status.SUCCESS_NO_CONTENT);
			return null;
		}
		return new InstancesRepresentation(MediaType.TEXT_CSV, output);
	}

	@Override
	@Post
	public Representation handleTask(Form form) {
		String queue = getQuery().getFirstValue(QUERY_QUEUE);
		if (queue != null && queue.equals("yes")) {
			return createTask(form);
			
		} else if (user == null) {
			consumeTask(form);
			return null;
			
		} else {
			return execute(form);
		}
	}
	

}
