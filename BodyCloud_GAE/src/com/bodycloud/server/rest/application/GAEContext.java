package com.bodycloud.server.rest.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.restlet.Context;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.bodycloud.engine.KDEngine;
import com.bodycloud.engine.embedded.EmbeddedEngine;
import com.bodycloud.engine.embedded.Node;
import com.bodycloud.engine.embedded.NodeLoader;
import com.bodycloud.server.entity.EnginePlugin;
import com.bodycloud.server.entity.User;
import com.bodycloud.server.persistence.DataMapperFactory;
import com.bodycloud.server.persistence.EntityMapper;
import com.bodycloud.server.persistence.gae.DataMapperFactoryImpl;

public class GAEContext extends Context {
	
	
	
	public GAEContext(final Logger logger) {
		super(logger);
		
		HashMap<String, Object> attrs = new HashMap<String, Object>();
		
		final DataMapperFactory factory = new DataMapperFactoryImpl();
		
		attrs.put(DataMapperFactory.class.getName(), factory);
		
		NodeLoader loader = new NodeLoader() {
			
			@Override
			public Class<? extends Node> loadNode(String className)
					throws ClassNotFoundException {
				try {
					return Class.forName(className).asSubclass(Node.class);
				} catch (ClassNotFoundException e1) {
					String jarName = className.replaceAll(".*\\.", "");
					EntityMapper entityMapper = factory.getEntityMapper();
					EnginePlugin stored = (EnginePlugin) entityMapper.findByName(EnginePlugin.class, jarName);
					if (stored == null)
						throw new ClassNotFoundException();
					InputStream stream = stored.readPlugin();
					entityMapper.close();
					try {
						return new StreamClassLoader(stream).loadClass(className).asSubclass(Node.class);
					} catch (IOException e2) {
						throw new ClassNotFoundException();
					}
				}
			}
		};
		
		attrs.put(KDEngine.class.getName(), new EmbeddedEngine(logger, loader));
		
		attrs.put(UserProvider.class.getName(), new UserProviderImpl());
		
		attrs.put(ResourcesFinder.class.getName(), new ResourcesFinder() {
			
			@Override
			public Representation find(String path) throws ResourceException {
				if (!UrlHelper.hasExtension(path))
					path = path + ".xml";
				logger.info("fetching " + path);
				return new ClientResource(path).get();
			}
		});
		
		attrs.put(TaskQueue.class.getName(), new TaskQueueImpl());
		
		attrs.put(UserNotifier.class.getName(), new UserNotifier() {
			
			@Override
			public void notify(User user) {
				//TODO implement notifications
				logger.info("STUB: notify user " + user.getName());
			}
		});
		
		this.setAttributes(attrs);
		
	}

}
