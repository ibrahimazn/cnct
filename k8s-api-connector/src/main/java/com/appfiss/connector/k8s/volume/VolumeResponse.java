package com.appfiss.connector.k8s.volume;

import com.appfiss.connector.k8s.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Volume PVC response from K8s.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeResponse extends Response {

	@JsonProperty("result")
	private Volume result;

	public Volume getResult() {
		return result;
	}

	public void setResult(Volume result) {
		this.result = result;
	}
}
