package az.ldap.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public class NotificationAction {

	/** Unique ID of the Type. */
	private String id;
	
	/** Notification id. */
	private Long notifyId;

	/** Object type. */
	private String objectId;
	
	/** User group id. */
	private Long serviceId;

	/** User group id. */
	private Long groupId;
	
	/** Thresold value. */
	private String thresold;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Long notifyId) {
		this.notifyId = notifyId;
	}

	public String getObjectId() {
		return objectId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getThresold() {
		return thresold;
	}

	public void setThresold(String thresold) {
		this.thresold = thresold;
	}

}
