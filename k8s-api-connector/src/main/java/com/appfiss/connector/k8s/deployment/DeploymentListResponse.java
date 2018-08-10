package com.appfiss.connector.k8s.deployment;

import java.util.List;

import com.appfiss.connector.k8s.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * List of Deployment of containers in k8s.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentListResponse extends Response {

	@JsonProperty("result")
	private List<DeploymentList> result;

	public List<DeploymentList> getResult() {
		return result;
	}

	public void setResult(List<DeploymentList> result) {
		this.result = result;
	}
}
