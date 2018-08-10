package com.appfiss.connector.k8s.deployment;

import com.appfiss.connector.k8s.Request;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Delete deployed container in k8s.
 * 
 * eg:- API : /list-deployments Method : GET Headers : {Content-Type : application/x-www-form-urlencoded}
 * 
 * Query parameters : namespace, type
 * 
 */
@JsonIgnoreProperties 
public class DeploymentDeleteRequest extends Request {

	private String name;
	
	private String namespace;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	
	
}
