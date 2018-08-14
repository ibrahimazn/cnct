package az.ldap.zabbix.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "default_items")
@SuppressWarnings("serial")
public class DefaultItems implements Serializable {

	@Id
	private String id;

	@Field("name")
	private String name;

	@Field("key_")
	private String key_;

	@Field("type")
	private Integer type;

	@Field("value_type")
	private Integer valueType;
	
	@Field("data_type")
	private Integer dataType;

	@Field("delay")
	private Integer delay;
	
	@Field("graph")
	private Boolean graph;
	
	@Field("default")
	private Boolean isDefault;
	
	@Field("units")
	private String units;
	
	@Field("category")
	private String category;
	
	@Field("os_type")
	private String osType;
	
	@Field("usage")
	private Integer usage;
	
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Boolean getGraph() {
		return graph;
	}

	public void setGraph(Boolean graph) {
		this.graph = graph;
	}

	public Boolean getDefault_() {
		return isDefault;
	}

	public void setDefault_(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public Boolean getTriggerStatus() {
		return triggerStatus;
	}

	public void setTriggerStatus(Boolean triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	public Integer getUsage() {
		return usage;
	}

	public void setUsage(Integer usage) {
		this.usage = usage;
	}
}
