package com.appfiss.connector.k8s.namespace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Namespace object definition.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NameSpace {

	@JsonProperty("name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
