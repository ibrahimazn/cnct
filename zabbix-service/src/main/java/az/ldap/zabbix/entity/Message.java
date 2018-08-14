package az.ldap.zabbix.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

	private String operationid;
	
	private Integer default_msg;
	
	private String subject;
	
	private String message;
	
	private String mediatypeid;

	public String getOperationid() {
		return operationid;
	}

	public void setOperationid(String operationid) {
		this.operationid = operationid;
	}

	public Integer getDefault_msg() {
		return default_msg;
	}

	public void setDefault_msg(Integer default_msg) {
		this.default_msg = default_msg;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMediatypeid() {
		return mediatypeid;
	}

	public void setMediatypeid(String mediatypeid) {
		this.mediatypeid = mediatypeid;
	}
}
