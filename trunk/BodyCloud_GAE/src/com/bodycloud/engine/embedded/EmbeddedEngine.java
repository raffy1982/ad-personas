package com.bodycloud.engine.embedded;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;

import com.bodycloud.engine.KDEngine;
import com.bodycloud.engine.Worker;
import com.bodycloud.engine.embedded.model.WorkflowDescription;

public class EmbeddedEngine implements KDEngine {

	Logger logger;
	
	NodeLoader nodeLoader = new NodeLoader() {
		
		@Override
		public Class<? extends Node> loadNode(String className) throws ClassNotFoundException {
			return Class.forName(className).asSubclass(Node.class);
		}
	};

	public EmbeddedEngine() {
		logger = Logger.getAnonymousLogger();
	}
	

	EmbeddedEngine(Logger logger) {
		super();
		this.logger = logger;
	}


	public EmbeddedEngine(Logger logger, NodeLoader nodeLoader) {
		super();
		this.logger = logger;
		this.nodeLoader = nodeLoader;
	}

	@Override
	public Worker getWorker(InputStream input) throws IOException {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("schema.xsd");
			SAXSource source = new SAXSource(new InputSource(is));
			Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(source);
			JAXBContext context = JAXBContext.newInstance(WorkflowDescription.class);
			Unmarshaller u = context.createUnmarshaller();
			u.setSchema(schema);
			WorkflowDescription d = (WorkflowDescription) u.unmarshal(input);
			return getWorker(d);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public Worker getWorker(WorkflowDescription d) throws IOException {
		EmbeddedEngineWorker worker = new EmbeddedEngineWorker(logger, d.getInstance(nodeLoader));
		return worker;
	}
	
}
