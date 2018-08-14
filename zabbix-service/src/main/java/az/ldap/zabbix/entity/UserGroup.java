package az.ldap.zabbix.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "user_groups")
@SuppressWarnings("serial")
public class UserGroup implements Serializable {

	@Id
	private String id;
	
	@Field("name")
	private String name;

	@Field("usrgrpid")
	private String usrgrpId;
	
	@Field("host_group")
	private String hostId;
	
	@Field("type")
	private String type;
	
	@Transient
	private List<String> userIds;


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
	 * @return the usrgrpId
	 */
	public String getUsrgrpId() {
		return usrgrpId;
	}

	/**
	 * @param usrgrpId the usrgrpId to set
	 */
	public void setUsrgrpId(String usrgrpId) {
		this.usrgrpId = usrgrpId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
