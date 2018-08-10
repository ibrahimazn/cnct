package com.appfiss.connector.k8s.deployment;

import com.appfiss.connector.k8s.Request;

/**
 * Deploy the container in k8s with deployment
 * 
 * eg:- API : /create-deployment Method : POST Headers : {Content-Type :
 * application/x-www-form-urlencoded}
 * 
 * Body : { name: string, user: string, namespace: string, imageName: string,
 * image: string, containerPort: string, volume: string, type : string }
 * 
 */

public class DeploymentCreateRequest extends Request {

	private String name;

	private String user;

	private String namespace;

	private String imageName;

	private String image;

	private String containerPort;

	private String volume;

	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContainerPort() {
		return containerPort;
	}

	public void setContainerPort(String containerPort) {
		this.containerPort = containerPort;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
