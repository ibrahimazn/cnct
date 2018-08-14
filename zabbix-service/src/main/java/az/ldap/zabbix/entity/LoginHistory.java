package az.ldap.zabbix.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Login history tracking entity.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "login_history")
@SuppressWarnings("serial")
public class LoginHistory implements Serializable {

    /** Unique ID of the login user. */
	@Id
	private String id;

    /** User id of the login. */
	@Field("user_id")
    private String userId;
	
	@Field("domain_id")
    private String domainId;
	
	@Field("department_id")
    private String departmentId;

    /** Already Login of the user. */
	@Field("is_already_login")
    private Boolean isAlreadyLogin;
    
    /** Session id for websocket. */
	@Field("session_id")
    private String sessionId;

    /** Token details of the login user. */
	@Field("login_token")
    private String loginToken;
	
	@Field("host_id")
	private String hostId;
	
	@Field("user_type")
	private String userType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getIsAlreadyLogin() {
		return isAlreadyLogin;
	}

	public void setIsAlreadyLogin(Boolean isAlreadyLogin) {
		this.isAlreadyLogin = isAlreadyLogin;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDomainId() {
		return domainId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
}
