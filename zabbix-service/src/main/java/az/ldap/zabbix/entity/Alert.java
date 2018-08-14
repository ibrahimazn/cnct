package az.ldap.zabbix.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "alerts")
public class Alert implements Serializable {

	@Id
	private String id;
	
	/** id of the trigger. */
	@Field("alertid")
	private String alertid;
	
	@Field("hostid")
	private String hostId;
	
	/** description of the trigger. */
	@Field("eventid")
	private String eventid;
	
	/** expression for tigger of the host. */
	@Field("actionid")
	private String actionid;
	
	/** poriority of the trigger. */
	@Field("clock")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date clock;
	
	/** status of the trigger. */
	@Field("userid")
	private String userid;
	
	@Field("mediatypeid")
	private String mediatypeid;
	
	@Field("sendto")
	private String sendto;
	
	@Field("subject")
	private String subject;
	
	@Field("message")
	private String message;
	
	@Field("status")
	private String status;
	
	@Field("alerttype")
	private String alerttype;

	@Field("userid_")
	private Integer userid_;
	
	@Field("domainid_")
	private Integer domainid_;
	
	@Field("departmentid_")
	private Integer departmentid_;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlertid() {
		return alertid;
	}

	public void setAlertid(String alertid) {
		this.alertid = alertid;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
	}

	public String getActionid() {
		return actionid;
	}

	public void setActionid(String actionid) {
		this.actionid = actionid;
	}

	public Date getClock() {
		return clock;
	}

	public void setClock(Date clock) {
		this.clock = clock;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMediatypeid() {
		return mediatypeid;
	}

	public void setMediatypeid(String mediatypeid) {
		this.mediatypeid = mediatypeid;
	}

	public String getSendto() {
		return sendto;
	}

	public void setSendto(String sendto) {
		this.sendto = sendto;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAlerttype() {
		return alerttype;
	}

	public void setAlerttype(String alerttype) {
		this.alerttype = alerttype;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public Integer getUserid_() {
		return userid_;
	}

	public void setUserid_(Integer userid_) {
		this.userid_ = userid_;
	}

	public Integer getDomainid_() {
		return domainid_;
	}

	public void setDomainid_(Integer domainid_) {
		this.domainid_ = domainid_;
	}

	public Integer getDepartmentid_() {
		return departmentid_;
	}

	public void setDepartmentid_(Integer departmentid_) {
		this.departmentid_ = departmentid_;
	}
}
