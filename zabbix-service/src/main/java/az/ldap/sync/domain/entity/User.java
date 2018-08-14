package az.ldap.sync.domain.entity;

import org.springframework.ldap.odm.annotations.Attribute;
import com.google.gson.annotations.SerializedName;

public class User {
	
	@SerializedName("email")
	private String email;

	@SerializedName("lastName")
	private String lastName;
	
	@SerializedName("userName")
	private String userName;

	@SerializedName("description")
	private String description;

	@SerializedName("account")
	private String account;
	
	@SerializedName("firstName")
	private String firstName;
	
	@SerializedName("mail")
	private String mail;
	
	@SerializedName("language")
	private String language;
	
	@SerializedName("userPassword")
	private byte[] userPassword;
	
	@SerializedName("memberOf")
	private String memberOf;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}
