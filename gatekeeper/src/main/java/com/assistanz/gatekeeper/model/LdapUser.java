package com.assistanz.gatekeeper.model;

import java.io.Serializable;
import java.nio.charset.Charset;
import javax.naming.Name;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LdapUser implements Serializable {

	private static final long serialVersionUID = 2192790852221028191L;

	@Id
	private Name dn;

	@Attribute(name = "mail")
	private String email;

	@Attribute(name = "cn")
    private String fullName;

	@Attribute(name = "sn")
	private String lastName;

	@Attribute(name = "uid")
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
		ADMIN,

		ANALYZER,

		USER,

    	DATA_SCIENTIST
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
   * @return the fullName
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * @param fullName the fullName to set
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
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
   * @return the serialversionuid
   */
  public static long getSerialversionuid() {
    return serialVersionUID;
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
