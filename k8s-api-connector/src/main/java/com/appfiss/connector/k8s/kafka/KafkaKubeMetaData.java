package com.appfiss.connector.k8s.kafka;

import com.google.gson.annotations.SerializedName;

public class KafkaKubeMetaData {


	@SerializedName("name")
	private String name;
	
	@SerializedName("namespace")
	private String namespace;
	
	@SerializedName("selfLink")
	private String selfLink;
	

	@SerializedName("uid")
	private String uid;
	
	@SerializedName("resourceVersion")
	private String resourceVersion;
	
	@SerializedName("creationTimestamp")
	private String creationTimestamp;

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

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(String creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

}
