package com.bodycloud.engine.embedded.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bodycloud.engine.embedded.Node;
import com.bodycloud.engine.embedded.NodeLoader;

@XmlRootElement(name="workflow")
@XmlAccessorType(XmlAccessType.NONE)
public class WorkflowDescription {
	
	@XmlElement(name="node")
	List<NodeDescription> nodes = new LinkedList<NodeDescription>();
	
	public Node[] getInstance(NodeLoader nodeLoader) throws IOException {
		Node[] workflow = new Node[nodes.size()];
		int i = 0;
		for (NodeDescription factory : nodes) {
			workflow[i] = factory.create(nodeLoader);
			i++;
		}
		return workflow;
	}
	

}
