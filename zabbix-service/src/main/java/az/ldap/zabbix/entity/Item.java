package az.ldap.zabbix.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "items")
@SuppressWarnings("serial")
public class Item implements Serializable {

	@Id
	private String id;
	
	@Field("itemid")
	private String itemId;

	@Field("name")
	private String name;

	@Field("key_")
	private String key_;

	@Field("hostid")
	private String hostId;

	@Field("type")
	private Integer type;

	@Field("value_type")
	private Integer valueType;
	
	@Field("data_type")
	private Integer dataType;

	@Field("interfaceid")
	private String interfaceId;

	@Field("delay")
	private Integer delay;
	
	@Field("delay_units")
	private String delayUnits;
	
	@Field("units")
	private String units;
	
	@Field("graph")
	private Boolean graph;
	
	@Field("status")
	private Integer status;
	
	@Field("category")
	private String category;
	
	@Field("os_type")
	private String osType;
	
	@Field("usage")
	private Integer usage;
	
	@Field("userid_")
	private Integer userid_;
	
	@Field("domainid_")
	private Integer domainid_;
	
	@Field("departmentid_")
	private Integer departmentid_;
	
	@Field("trigger_status")
	private Boolean triggerStatus;

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
	 * @return the key_
	 */
	public String getKey_() {
		return key_;
	}

	/**
	 * @param key_ the key_ to set
	 */
	public void setKey_(String key_) {
		this.key_ = key_;
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
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the valueType
	 */
	public Integer getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the interfaceId
	 */
	public String getInterfaceId() {
		return interfaceId;
	}

	/**
	 * @param interfaceId the interfaceId to set
	 */
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * @return the delay
	 */
	public Integer getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	public String getDelayUnits() {
		return delayUnits;
	}

	public void setDelayUnits(String delayUnits) {
		this.delayUnits = delayUnits;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Boolean getGraph() {
		return graph;
	}

	public void setGraph(Boolean graph) {
		this.graph = graph;
	}
	
	public Boolean getTriggerStatus() {
		return triggerStatus;
	}

	public void setTriggerStatus(Boolean triggerStatus) {
		this.triggerStatus = triggerStatus;
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

	public Integer getUsage() {
		return usage;
	}

	public void setUsage(Integer usage) {
		this.usage = usage;
	}
}
