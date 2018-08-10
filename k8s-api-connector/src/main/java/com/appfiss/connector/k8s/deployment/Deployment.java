package com.appfiss.connector.k8s.deployment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Deployment model of deployed container.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deployment {

	/** The name of the container. */
	@JsonProperty("name")
	private String name;

	/** Currently logged in user. */
	@JsonProperty("user")
	private String user;

	/** The namespace of container. */
	@JsonProperty("namespace")
	private String namespace;

	/** Docker image name. */
	@JsonProperty("imageName")
	private String imageName;

	/** Docker image url or Docker Hub repository name. */
	@JsonProperty("image")
	private String image;

	/** For external access of service with specific port. */
	@JsonProperty("containerPort")
	private String containerPort;

	/** PVC volume id to attach external storage to container. */
	@JsonProperty("volume")
	private String volume;

	/** Type will classified containers depending on usage. */
	@JsonProperty("type")
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
