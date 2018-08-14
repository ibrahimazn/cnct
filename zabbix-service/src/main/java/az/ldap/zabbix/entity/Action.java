package az.ldap.zabbix.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "actions")
public class Action {
	@Id
	private String id;
	
	@Field("actionid")
	private String actionid;

	@Field("trigger_id")
	private String triggerid;

	@Field("name")
	private String name;

	@Field("eventsource")
	private Integer eventsource;

	@Field("def_longdata")
	private String def_longdata;

	@Field("def_shortdata")
	private String def_shortdata;

	@Field("r_longdata")
	private String r_longdata;

	@Field("r_shortdata")
	private String r_shortdata;

	@Field("maintenance_mode")
	private Integer maintenance_mode;
	
	@Field("status")
	private String status;

	@Field("recovery_msg")
	private String recovery_msg;

	@Field("esc_period")
	private String esc_period;

	@Transient
	private FilterResponse filter;

	@Transient
	private List<Operations> operations;

	@Transient
	private List<RecoveryOperations> recovery_operations;

	@Field("userid_")
	private Integer userid_;
	
	@Field("domainid_")
	private Integer domainid_;
	
	@Field("departmentid_")
	private Integer departmentid_;

	public String getActionid() {
		return actionid;
	}

	public void setActionid(String actionid) {
		this.actionid = actionid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEventsource() {
		return eventsource;
	}

	public void setEventsource(Integer eventsource) {
		this.eventsource = eventsource;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecovery_msg() {
		return recovery_msg;
	}

	public void setRecovery_msg(String recovery_msg) {
		this.recovery_msg = recovery_msg;
	}

	public String getEsc_period() {
		return esc_period;
	}

	public void setEsc_period(String esc_period) {
		this.esc_period = esc_period;
	}

	public String getDef_longdata() {
		return def_longdata;
	}

	public void setDef_longdata(String def_longdata) {
		this.def_longdata = def_longdata;
	}

	public String getDef_shortdata() {
		return def_shortdata;
	}

	public void setDef_shortdata(String def_shortdata) {
		this.def_shortdata = def_shortdata;
	}

	public String getR_longdata() {
		return r_longdata;
	}

	public void setR_longdata(String r_longdata) {
		this.r_longdata = r_longdata;
	}

	public String getR_shortdata() {
		return r_shortdata;
	}

	public void setR_shortdata(String r_shortdata) {
		this.r_shortdata = r_shortdata;
	}

	public FilterResponse getFilter() {
		return filter;
	}

	public void setFilter(FilterResponse filter) {
		this.filter = filter;
	}

	public List<Operations> getOperations() {
		return operations;
	}

	public void setOperations(List<Operations> operations) {
		this.operations = operations;
	}
	public List<RecoveryOperations> getRecovery_operations() {
		return recovery_operations;
	}

	public void setRecovery_operations(List<RecoveryOperations> recovery_operations) {
		this.recovery_operations = recovery_operations;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getMaintenance_mode() {
		return maintenance_mode;
	}

	public void setMaintenance_mode(Integer maintenance_mode) {
		this.maintenance_mode = maintenance_mode;
	}

	public String getTriggerid() {
		return triggerid;
	}

	public void setTriggerid(String triggerid) {
		this.triggerid = triggerid;
	}
}
