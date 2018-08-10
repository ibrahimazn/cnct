package com.appfiss.connector.k8s.kafka;

import com.google.gson.annotations.SerializedName;


public class KafkaKubeSource  {

	@SerializedName("component")
	private String component;
	
	@SerializedName("host")
	private String host;

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
