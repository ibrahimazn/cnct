package com.appfiss.connector.k8s.namespace;

import com.appfiss.connector.k8s.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * NameSpace response object after creating namespace in k8s.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NamespaceResponse extends Response {

	@JsonProperty("result")
	private NameSpace result;

	public NameSpace getResult() {
		return result;
	}

	public void setResult(NameSpace result) {
		this.result = result;
	}

}
