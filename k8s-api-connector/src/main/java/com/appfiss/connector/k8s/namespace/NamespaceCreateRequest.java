package com.appfiss.connector.k8s.namespace;

import com.appfiss.connector.k8s.Request;

/**
 * Create Namespace request and its required parameters.
 * 
 * eg:- API : /create-namespace Method : POST Headers : {Content-Type :
 * application/x-www-form-urlencoded}
 * 
 * Body : { name: string} *required
 * 
 */
public class NamespaceCreateRequest extends Request {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
