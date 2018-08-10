package com.appfiss.connector.k8s.volume;

import com.appfiss.connector.k8s.Request;

/**
 * Create volume claim for user to access external storage inside container
 * 
 * eg:- API : /create-volume Method : POST Headers : {Content-Type :
 * application/x-www-form-urlencoded}
 * 
 * Body : { namespace: string, name: string}
 * 
 */
public class VolumeCreateRequest extends Request {

	private String name;

	private String namespace;

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
}
