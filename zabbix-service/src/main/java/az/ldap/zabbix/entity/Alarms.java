package az.ldap.zabbix.entity;

import java.util.List;

public class Alarms {
	
	private String id;
	
	private String actionid;

	private String itemName;
	
	private String triggerName;
	
	private String itemId;
	
	private String hostId;

	private String thresold;
	
	private String interval;
	
	private Integer priority;

	private String message;
	
	private String units;

	private String triggerId;

	private String status;

	private List<Users> users;
	
	private List<Groups> groups;

	private Integer userid_;
	
	private Integer domainid_;
	
	private Integer departmentid_;
	
	private String notificationPlanId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActionid() {
		return actionid;
	}

	public void setActionid(String actionid) {
		this.actionid = actionid;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getThresold() {
		return thresold;
	}

	public void setThresold(String thresold) {
		this.thresold = thresold;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
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

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	public String getNotificationPlanId() {
		return notificationPlanId;
	}

	public void setNotificationPlanId(String notificationPlanId) {
		this.notificationPlanId = notificationPlanId;
	}
	
	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
}
