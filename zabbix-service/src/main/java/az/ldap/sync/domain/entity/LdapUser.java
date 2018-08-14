package az.ldap.sync.domain.entity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Name;
import org.json.JSONObject;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import az.ldap.sync.constants.CloudStackConstants;
import az.ldap.sync.util.JsonUtil;
import az.ldap.sync.util.RandomPasswordGenerator;

@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LdapUser implements Serializable {	

	private static final long serialVersionUID = 2192790852221028191L;
	
	@Id
	private Name dn;

	@Attribute(name = "uid")
	private String email;

	@Attribute(name = "sn")
	private String lastName;
	
	@Attribute(name = "cn")
	private String userName;

	@Attribute(name = "description")
	private String description;

	@Attribute(name = "ou")
	private String account;
	
	@Attribute(name = "givenName")
	private String firstName;
	
	@Attribute(name = "mail")
	private String mail;
	
	@Attribute(name = "l")
	private String language;
	
	@Attribute(name = "userPassword", type = Type.BINARY)
	private byte[] userPassword;
	
	@Attribute(name = "memberOf")
	private String memberOf;
	
	@Transient
    private UserType type;
	
	 /** User status. */
	@Transient
    private Status status;
	
	/** Define user type. */
    public enum UserType {
        /** Define type constant. */
        DOMAIN_ADMIN, ROOT_ADMIN, USER;
    }

    /** Define status. */
    public enum Status {
        /** Define status constant. */
        ACTIVE, BLOCKED, DELETED, DISABLED, ENABLED, SUSPENDED;
    }

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {		
		this.dn = dn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the type
	 */
	public UserType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(UserType type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
     * Convert JSONObject into user object.
     *
     * @param jsonObject JSON object.
     * @return user object.
     * @throws Exception error occurs.
     */
    public static LdapUser convert(JSONObject jsonObject) throws Exception {
    	LdapUser user = new LdapUser();
        user.setEmail(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_EMAIL));
        user.setMail(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_EMAIL));
        user.setLastName(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_LAST_NAME));
        user.setFirstName(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_FIRST_NAME));
        user.setUserName(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_USER_NAME));
        if (JsonUtil.getIntegerValue(jsonObject, CloudStackConstants.CS_ACCOUNT_TYPE) == 2) {
            user.setType(UserType.DOMAIN_ADMIN);
        } else if (JsonUtil.getIntegerValue(jsonObject, CloudStackConstants.CS_ACCOUNT_TYPE) == 1) {
            user.setType(UserType.ROOT_ADMIN);
        } else {
            user.setType(UserType.USER);
        }
        user.setAccount(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_DOMAIN));
        user.setDescription(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_ACCOUNT)+"-"+ JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_DOMAIN) +","+user.getType());
        user.setStatus(Status.valueOf(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_STATE).toUpperCase()));
        user.setUserPassword(new String(RandomPasswordGenerator.generatePswd(8, 16, 4, 2, 2)));
        return user;
    }
    

    /**
     * Mapping entity object into list.
     *
     * @param userList list of users.
     * @return user map
     */
    public static Map<Name, LdapUser> convert(List<LdapUser> userList) {
        Map<Name, LdapUser> userMap = new HashMap<Name, LdapUser>();
        for (LdapUser user : userList) {
            userMap.put(user.getDn(), user);
        }
        return userMap;
    }

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param givenName the givenName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the userPassword
	 */
	public byte[] getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword.getBytes(Charset.forName("UTF-8"));
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMemberOf() {
		return memberOf;
	}

	public void setMemberOf(String memberOf) {
		this.memberOf = memberOf;
	}

	public void setUserPassword(byte[] userPassword) {
		this.userPassword = userPassword;
	}	

}
