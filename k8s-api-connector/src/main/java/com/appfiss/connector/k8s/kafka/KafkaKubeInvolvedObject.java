package com.appfiss.connector.k8s.kafka;

import com.google.gson.annotations.SerializedName;

public class KafkaKubeInvolvedObject {

	@SerializedName("kind")
	private String kind;
	
	@SerializedName("namespace")
	private String namespace;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("uid")
	private String uid;
	
	@SerializedName("apiVersion")
	private String apiVersion;
	
	@SerializedName("resourceVersion")
	private String resourceVersion;
	
	@SerializedName("fieldPath")
	private String fieldPath;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public void setFieldPath(String fieldPath) {
		this.fieldPath = fieldPath;
	}

}
