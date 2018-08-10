package com.appfiss.connector.k8s.kafka;

import com.google.gson.annotations.SerializedName;

public class KafkaKubeEvent {

	@SerializedName("metadata")
	private KafkaKubeMetaData metadata;
	
	@SerializedName("involvedObject")
	private KafkaKubeInvolvedObject involvedObject;
	
	@SerializedName("source")
	private KafkaKubeSource source;
	
	@SerializedName("reason")
	private String reason;
	
	@SerializedName("message")
	private String message;
	
	@SerializedName("firstTimestamp")
	private String firstTimestamp;

	@SerializedName("lastTimestamp")
	private String lastTimestamp;
	
	@SerializedName("count")
	private Integer count;

	@SerializedName("type")
	private String type;

	public KafkaKubeMetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(KafkaKubeMetaData metadata) {
		this.metadata = metadata;
	}

	public KafkaKubeInvolvedObject getInvolvedObject() {
		return involvedObject;
	}

	public void setInvolvedObject(KafkaKubeInvolvedObject involvedObject) {
		this.involvedObject = involvedObject;
	}

	public KafkaKubeSource getSource() {
		return source;
	}

	public void setSource(KafkaKubeSource source) {
		this.source = source;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFirstTimestamp() {
		return firstTimestamp;
	}

	public void setFirstTimestamp(String firstTimestamp) {
		this.firstTimestamp = firstTimestamp;
	}

	public String getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(String lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
