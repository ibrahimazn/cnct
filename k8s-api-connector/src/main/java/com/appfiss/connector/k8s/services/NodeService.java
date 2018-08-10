package com.appfiss.connector.k8s.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * NodePort Service to create node port to access this application from outside
 * using node ip.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeService {

	@JsonProperty("name")
	private String name;

	@JsonProperty("namespace")
	private String namespace;

	@JsonProperty("label")
	private String label;

	@JsonProperty("port")
	private String port;

	@JsonProperty("nodePort")
	private String nodePort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getNodePort() {
		return nodePort;
	}

	public void setNodePort(String nodePort) {
		this.nodePort = nodePort;
	}

}
