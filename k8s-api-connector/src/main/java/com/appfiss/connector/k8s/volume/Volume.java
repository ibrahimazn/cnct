package com.appfiss.connector.k8s.volume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Create PersistentVolumeClaim to attach external volume.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Volume {

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Namespace")
	private String namespace;

	@JsonProperty("Pvc")
	private String pvc;

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

	public String getPvc() {
		return pvc;
	}

	public void setPvc(String pvc) {
		this.pvc = pvc;
	}
}
