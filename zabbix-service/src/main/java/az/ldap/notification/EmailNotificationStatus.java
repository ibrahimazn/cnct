package az.ldap.notification;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public class EmailNotificationStatus implements Serializable {

    private String id;
	
    private Long emailNotificationStatusId;
	
    private String triggerId;

    private String emailFrom;

    private String emailTo;

    private String subject;

    private Integer failedAttempt;

    private Boolean mailStatus;
    
    private String errorMsg;
    
    private String description;
    
    private String eventId;
    
    private String itemValue;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the emailNotificationStatusId
	 */
	public Long getEmailNotificationStatusId() {
		return emailNotificationStatusId;
	}

	/**
	 * @param emailNotificationStatusId the emailNotificationStatusId to set
	 */
	public void setEmailNotificationStatusId(Long emailNotificationStatusId) {
		this.emailNotificationStatusId = emailNotificationStatusId;
	}

	/**
	 * @return the triggerId
	 */
	public String getTriggerId() {
		return triggerId;
	}

	/**
	 * @param triggerId the triggerId to set
	 */
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	/**
	 * @return the emailFrom
	 */
	public String getEmailFrom() {
		return emailFrom;
	}

	/**
	 * @param emailFrom the emailFrom to set
	 */
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	/**
	 * @return the emailTo
	 */
	public String getEmailTo() {
		return emailTo;
	}

	/**
	 * @param emailTo the emailTo to set
	 */
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the failedAttempt
	 */
	public Integer getFailedAttempt() {
		return failedAttempt;
	}

	/**
	 * @param failedAttempt the failedAttempt to set
	 */
	public void setFailedAttempt(Integer failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	/**
	 * @return the mailStatus
	 */
	public Boolean getMailStatus() {
		return mailStatus;
	}

	/**
	 * @param mailStatus the mailStatus to set
	 */
	public void setMailStatus(Boolean mailStatus) {
		this.mailStatus = mailStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

 }
