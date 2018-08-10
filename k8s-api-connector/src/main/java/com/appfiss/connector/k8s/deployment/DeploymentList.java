package com.appfiss.connector.k8s.deployment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Deployment response object from k8s api.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentList {
	
	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("Desired")
	private String Desired;
	
	@JsonProperty("Current")
	private String Current;
	
	@JsonProperty("UpToDate")
	private String UpToDate;
	
	@JsonProperty("Available")
	private String Available;
	
	@JsonProperty("Age")
	private String Age;

	public String getName() {
		return Name;
	}

	public String getDesired() {
		return Desired;
	}

	public void setDesired(String desired) {
		Desired = desired;
	}

	public String getCurrent() {
		return Current;
	}

	public void setCurrent(String current) {
		Current = current;
	}

	public String getUpToDate() {
		return UpToDate;
	}

	public void setUpToDate(String upToDate) {
		UpToDate = upToDate;
	}

	public String getAvailable() {
		return Available;
	}

	public void setAvailable(String available) {
		Available = available;
	}

	public String getAge() {
		return Age;
	}

	public void setAge(String age) {
		Age = age;
	}
}
