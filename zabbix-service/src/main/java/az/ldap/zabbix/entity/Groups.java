package az.ldap.zabbix.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "trigger_groups")
public class Groups {
	@Id
	private String id;
	
	@Field("usrgrpid")
	private String usrgrpid;
	
	@Field("triggerId")
	private String triggerId;

	public String getUsrgrpid() {
		return usrgrpid;
	}

	public void setUsrgrpid(String usrgrpid) {
		this.usrgrpid = usrgrpid;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
