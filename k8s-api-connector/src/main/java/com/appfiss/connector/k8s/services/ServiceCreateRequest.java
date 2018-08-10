package com.appfiss.connector.k8s.services;

import com.appfiss.connector.k8s.Request;

/**
 * Create service request for NodePort service to access application by using
 * NodeIP.
 * 
 * eg:- API : /create-service Method : POST Headers : {Content-Type :
 * application/x-www-form-urlencoded, Kubernetes-Api-Key : e3wfdsffd=}
 * 
 * Body : { name: string, namespace: string, label: string, port: string}
 * 
 */
public class ServiceCreateRequest extends Request {

	private String name;

	private String namespace;

	private String label;

	private String port;

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
}
