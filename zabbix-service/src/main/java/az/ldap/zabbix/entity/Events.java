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
@Document(collection = "events")
public class Events implements Serializable {

	@Id
	private String id;
	
	@Field("hostid")
	private String hostId;
	
	/** description of the trigger. */
	@Field("triggerId")
	private String triggerId;
	
	@Field("itemId")
	private String itemId;
	
	/** expression for tigger of the host. */
	@Field("eventid")
	private String eventid;
	
	@Field("name")
	private String name;
	
	@Field("host")
	private String host;
	
	@Field("severity")
	private String severity;
	
	@Field("clock")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date clock;
	
	/** status of the trigger. */
	@Field("userid")
	private String userid;
	
	@Field("value")
	private Integer value;
	
	@Field("acknowledged")
	private Integer acknowledged;

	@Field("userid_")
	private Integer userid_;
	
	@Field("domainid_")
	private Integer domainid_;
	
	@Field("departmentid_")
	private Integer departmentid_;
	
	/** Message of the trigger. */
	@Field("event_message")
	private String eventMessage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
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

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(Integer acknowledged) {
		this.acknowledged = acknowledged;
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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}
}
