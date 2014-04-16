package com.bodycloud.engine.embedded.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.bodycloud.engine.embedded.Node;
import com.bodycloud.engine.embedded.NodeLoader;

public class NodeDescription {
	
	public static final String NODE_PACKAGE = "com.bodycloud.engine.embedded.node";

	public NodeDescription() {
	}

	public NodeDescription(Class<? extends Node> type) {
		super();
		this.type = type.getSimpleName();
	}
	
	@XmlElement(required=true)
	String type;

	@XmlElement(name = "parameter")
	List<InitParam> parameters = new LinkedList<NodeDescription.InitParam>();

	static class InitParam {

		public String name;
		public String value;
	}

	public Node create(NodeLoader nodeLoader) throws IOException {
		try {
			String className = NODE_PACKAGE + "." + type;
			Class<? extends Node> clazz = nodeLoader.loadNode(className);
			Node node = clazz.newInstance();
			for (InitParam p : parameters) {
				String setter = "set" + Character.toUpperCase(p.name.charAt(0))
						+ p.name.substring(1);
				clazz.getMethod(setter, String.class).invoke(node, p.value);
			}
			return node;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("error creating node " + type);
		}
	}

}
