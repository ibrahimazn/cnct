package com.appfiss.connector.k8s;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * K8s response object format. each response object from kubectl api will have
 * following json properties, status, message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

	/** The status of request. */
	@JsonProperty("status")
	private String status;

	/** The message from k8s. */
	@JsonProperty("message")
	private String message;

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
