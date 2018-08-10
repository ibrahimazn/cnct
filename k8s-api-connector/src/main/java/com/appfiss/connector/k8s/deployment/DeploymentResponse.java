package com.appfiss.connector.k8s.deployment;

import com.appfiss.connector.k8s.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Deployment Response for deployed container.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentResponse extends Response {

	@JsonProperty("result")
	private Deployment result;

	public Deployment getResult() {
		return result;
	}

	public void setResult(Deployment result) {
		this.result = result;
	}

}
