package com.appfiss.connector.k8s.deployment;

import com.appfiss.connector.k8s.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Delete deployment of  container response.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentDeleteResponse extends Response {

	@JsonProperty("result")
	private String result;

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

}
