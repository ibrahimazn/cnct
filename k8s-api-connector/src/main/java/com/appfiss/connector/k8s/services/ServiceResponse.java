package com.appfiss.connector.k8s.services;

import com.appfiss.connector.k8s.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Service Response object to check whether service created or not.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponse extends Response {

	@JsonProperty("result")
	private NodeService result;

	public NodeService getResult() {
		return result;
	}

	public void setResult(NodeService result) {
		this.result = result;
	}
}
