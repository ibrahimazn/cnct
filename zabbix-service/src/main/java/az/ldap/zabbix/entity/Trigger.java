package az.ldap.zabbix.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "triggers")
public class Trigger {

	@Id
	private String id;
	
	/** id of the trigger. */
	@Field("triggerid")
	private String triggerid;
	
	/** notification plan id of the trigger. */
	@Field("notification_plan_id")
	private String notificationPlanId;
	
	/** description of the trigger. */
	@Field("description")
	private String description;
	
	/** Name of the trigger. */
	@Field("name")
	private String name;
	
	/** expression for tigger of the host. */
	@Field("expression")
	private String expression;
	
	/** poriority of the trigger. */
	@Field("priority")
	private Integer priority;
	
	/** status of the trigger. */
	@Field("status")
	private Integer status;
	
	@Field("comments")
	private String comments;
	
	@Field("hostid")
	private String hostId;
	
	@Field("osType")
	private String osType;
	
	@Field("category")
	private String category;

	@Field("userid_")
	private Integer userid_;
	
	@Field("domainid_")
	private Integer domainid_;
	
	@Field("departmentid_")
	private Integer departmentid_;
	
	@Transient
	private Action action;
	
	@Field("metric_name")
	private String metricName;
	
	@Field("metric_key")
	private String metricKey;
	
	@Field("item_id")
	private String metricId;
	
	/** description of the trigger. */
	@Field("thresold")
	private String thresold;
	
	@Field("interval")
	private String interval;
	
	/** Message of the trigger. */
	@Field("message")
	private String message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTriggerid() {
		return triggerid;
	}

	public void setTriggerid(String triggerid) {
		this.triggerid = triggerid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	
	public String getNotificationPlanId() {
		return notificationPlanId;
	}

	public void setNotificationPlanId(String notificationPlanId) {
		this.notificationPlanId = notificationPlanId;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getMetricKey() {
		return metricKey;
	}

	public void setMetricKey(String metricKey) {
		this.metricKey = metricKey;
	}

	public String getMetricId() {
		return metricId;
	}

	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}

	public String getThresold() {
		return thresold;
	}

	public void setThresold(String thresold) {
		this.thresold = thresold;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
