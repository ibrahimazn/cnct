/*
 *
 */
package com.appfiss.account.entity;

import java.io.Serializable;
import javax.naming.Name;
import javax.persistence.Id;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.Entry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Entity reference for the user's ldap repo. */
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LdapRepo implements Serializable {

private static final long serialVersionUID = 2192790852221028191L;
	
	@Id
	private Name dn;

	@Attribute(name = "uid")
	private String userid;

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

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public byte[] getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(byte[] userPassword) {
		this.userPassword = userPassword;
	}

	public String getMemberOf() {
		return memberOf;
	}

	public void setMemberOf(String memberOf) {
		this.memberOf = memberOf;
	}
}
