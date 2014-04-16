package com.bodycloud.server.rest.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import com.bodycloud.engine.embedded.Node;
import com.bodycloud.engine.embedded.model.NodeDescription;
import com.bodycloud.lib.rest.api.WorkflowNodeResource;
import com.bodycloud.server.entity.EnginePlugin;
import com.bodycloud.server.rest.application.ConvertHelper;
import com.bodycloud.server.rest.application.StreamClassLoader;

public class EnginePluginServerResource extends
		BasicServerResource<EnginePlugin> implements WorkflowNodeResource {

	@Override
	public void addPlugin(Representation rep) {
		createOrUpdate(rep);
	}

	private void checkPlugin(InputStream stream, String nodeName) {
		try {
			ClassLoader loader = new StreamClassLoader(stream);
			String className = NodeDescription.NODE_PACKAGE + "." + nodeName;
			loader.loadClass(className).asSubclass(Node.class).newInstance();

		} catch (IOException e1) {
			getLogger().log(Level.INFO, "could not understand stream", e1);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);

		} catch (Exception e2) {
			getLogger().log(Level.INFO, "supplied plugin is not valid", e2);
			throw new ResourceException(
					Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public EnginePlugin find() {
		return getEntityMapper().findByName(EnginePlugin.class, getResourceIdentifier());
	}

	@Override
	public EnginePlugin create() {
		EnginePlugin stored = new EnginePlugin();
		stored.setName(getResourceIdentifier());
		stored.setOwner(user);
		return stored;
	}

	@Override
	public void save(EnginePlugin e) {
		getEntityMapper().save(e);
	}

	@Override
	public void delete(EnginePlugin e) {
		getEntityMapper().delete(e);
	}

	@Override
	public void update(EnginePlugin entity, Representation representation) {
		entity.setContent(ConvertHelper.toByteArray(representation));
		checkPlugin(entity.readPlugin(), getResourceIdentifier());
	}

}
