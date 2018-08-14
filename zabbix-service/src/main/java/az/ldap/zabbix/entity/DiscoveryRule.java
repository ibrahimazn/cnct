package az.ldap.zabbix.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "discovery_rule")
public class DiscoveryRule implements Serializable {

	@Id
	private String id;
	
	/** id of the trigger. */
	@Field("hostid")
	private String hostId;
	
	/** description of the trigger. */
	@Field("interfaceid")
	private String interfaceId;
	
	/** expression for trigger of the host. */
	@Field("ruleid")
	private String ruleId;
	
	/** priority of the trigger. */
	@Field("type")
	private Integer type;
	
	/** status of the trigger. */
	@Field("status")
	private Integer status;
	
	@Field("key_")
	private String key_;
	
	@Field("name")
	private String name;
	
	@Field("delay")
	private Integer delay;
	
	@Field("category")
	private String category;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKey_() {
		return key_;
	}

	public void setKey_(String key_) {
		this.key_ = key_;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}
