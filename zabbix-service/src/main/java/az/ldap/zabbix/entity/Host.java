package az.ldap.zabbix.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A host is a single computer.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "hosts")
@SuppressWarnings("serial")
public class Host implements Serializable {

    /** Unique ID of the Host. */
    @Id
    private String id;

    /** Name of the host. */
	@Field("host")
	private String host;
	
	@Field("items")
	private Integer items;
	
	@Field("graphs")
	private Integer graphs;
	
	/** User id of the host. */
	@Field("userid")
	private String userId;
	
	/** Group id of the host. */
	@Field("groupId")
	private String groupId;
	
	/** Description of the host. */
	@Field("description")
	private String description;
	
	@Field("triggers")
	private Integer triggers;
	
	@Field("critical_alerts")
	private Integer criticalAlerts;
	
	@Field("warn_alerts")
	private Integer warnAlerts;
	
	@Field("info_alerts")
	private Integer infoAlerts;
	
	/** Visible Name of the host. */
	@Field("visible_name")
	private String name;
	
	@Field("uuid")
	private String uuid;
	
	@Field("state")
	private String state;
	
	@Field("status")
	private Integer status;
	
	@Field("createdTime")
	private String createdTime;
	
	@Field("proxy_id")
	private String proxyId;
	
	@Field("osType")
	private String osType;	
	
	/** Name of the host id. */
	@Field("hostid")
	private String hostId;
	
	/** availability of the host. */
	@Field("available")
	private Integer available;
	
	@Field("userid_")
	private Integer userid_;
	
	@Field("domainid_")
	private Integer domainid_;
	
	@Field("departmentid_")
	private Integer departmentid_;
	
	@Field("service_id")
	private Integer servicesId;

	/** Host interface for the host. */
	@DBRef
    private List<HostInterface> hostInterfaceList;
	
	/** Host Group for the host. */
	@DBRef
	private List<HostGroup> hostGroupList;

	/** Host template list for the host. */
	@Transient
	private List<HostTemplate> hostTemplateList;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the hostInterfaceList
	 */
	public List<HostInterface> getHostInterfaceList() {
		return hostInterfaceList;
	}

	/**
	 * @param hostInterfaceList the hostInterfaceList to set
	 */
	public void setHostInterfaceList(List<HostInterface> hostInterfaceList) {
		this.hostInterfaceList = hostInterfaceList;
	}

	/**
	 * @return the hostGroupList
	 */
	public List<HostGroup> getHostGroupList() {
		return hostGroupList;
	}

	public void setHostGroupList(List<HostGroup> hostGroupList) {
		this.hostGroupList = hostGroupList;
	}

	/**
	 * @return the hostTemplateList
	 */
	public List<HostTemplate> getHostTemplateList() {
		return hostTemplateList;
	}

	/**
	 * @param hostTemplateList the hostTemplateList to set
	 */
	public void setHostTemplateList(List<HostTemplate> hostTemplateList) {
		this.hostTemplateList = hostTemplateList;
	}

	/**
	 * @return the hostId
	 */
	public String getHostId() {
		return hostId;
	}

	/**
	 * @param hostId the hostId to set
	 */
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	/**
	 * @return the available
	 */
	public Integer getAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(Integer available) {
		this.available = available;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProxyId() {
		return proxyId;
	}

	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getTriggers() {
		return triggers;
	}

	public void setTriggers(Integer triggers) {
		this.triggers = triggers;
	}

	public Integer getItems() {
		return items;
	}

	public void setItems(Integer items) {
		this.items = items;
	}

	public Integer getGraphs() {
		return graphs;
	}

	public void setGraphs(Integer graphs) {
		this.graphs = graphs;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getCriticalAlerts() {
		return criticalAlerts;
	}

	public void setCriticalAlerts(Integer criticalAlerts) {
		this.criticalAlerts = criticalAlerts;
	}

	public Integer getWarnAlerts() {
		return warnAlerts;
	}

	public void setWarnAlerts(Integer warnAlerts) {
		this.warnAlerts = warnAlerts;
	}

	public Integer getInfoAlerts() {
		return infoAlerts;
	}

	public void setInfoAlerts(Integer infoAlerts) {
		this.infoAlerts = infoAlerts;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getServicesId() {
		return servicesId;
	}

	public void setServicesId(Integer servicesId) {
		this.servicesId = servicesId;
	} 
}
