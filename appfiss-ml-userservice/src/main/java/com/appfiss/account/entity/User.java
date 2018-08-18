/*
 *
 */
package com.appfiss.account.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// TODO: Auto-generated Javadoc
/** Entity reference for the User. */
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class User {

	/** Id of the User. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	/** The uuid. */
	@Column(name = "uuid", columnDefinition = "VARCHAR(255)")
	private String uuid;

	/** Email id of the User. */
	@Column(name = "email")
	@Email(message = "*Please provide a valid Email")
	private String email;

	/** Password of the User. */
	@Column(name = "password", columnDefinition = "VARCHAR(255)")
	private String password;

	/** First name of the User. */
	@Column(name = "first_name")
	private String firstName;

	/** Last Name of the User. */
	@Column(name = "last_name")
	private String lastName;

	/** User Name of the User. */
	@Column(name = "user_name", nullable = false)
	private String userName;

	/** Active Status of the User. */
	@Column(name = "active")
	private boolean active;

	/** The status. */
	@Column(name = "status")
	private STATUS status;

	/** Primary phone of the User. */
	@Column(name = "primary_phone")
	private String phone;

	/** Type of the user. */
	@Column(name = "user_type")
	private UserType userType;

	/** File path for profile image. */
	@Column(name = "profile_img_path")
	private String profileImgPath;

	/** File name for profile image. */
	@Column(name = "profile_img_file")
	private String profileImgFile;

	/** The created at. */
	@Basic
	@CreatedDate
	@Column(name = "CREATED_AT", nullable = false, insertable = true, updatable = false, columnDefinition = "TIMESTAMP NOT NULL")
	private Date createdAt;

	/**
	 * Set the firstName.
	 *
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the email.
	 *
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password.
	 *
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the first name.
	 *
	 * @return the firstname
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the first name.
	 *
	 * @param firstName
	 *            the name to set
	 */
	public void setName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get the lastName.
	 *
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the lastName.
	 *
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get the userName.
	 *
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set the userName.
	 *
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Get the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set the phone.
	 *
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the profile img path.
	 *
	 * @return the profile img path
	 */
	public String getProfileImgPath() {
		return profileImgPath;
	}

	/**
	 * Sets the profile img path.
	 *
	 * @param profileImgPath
	 *            the new profile img path
	 */
	public void setProfileImgPath(String profileImgPath) {
		this.profileImgPath = profileImgPath;
	}

	/**
	 * Gets the profile img file.
	 *
	 * @return the profile img file
	 */
	public String getProfileImgFile() {
		return profileImgFile;
	}

	/**
	 * Sets the profile img file.
	 *
	 * @param profileImgFile
	 *            the new profile img file
	 */
	public void setProfileImgFile(String profileImgFile) {
		this.profileImgFile = profileImgFile;
	}

	/**
	 * Get the userType.
	 *
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}

	/**
	 * Set the userType.
	 *
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Enumeration user type for admin, user.
	 */
	public enum UserType {

		/** The admin. */
		ADMIN,

		/** The user. */
		USER
	}

	/**
	 * Enumeration user type for admin, user.
	 */
	public enum STATUS {

		/** The enabled. */
		ENABLED,

		/** The disabled. */
		DISABLED,

		/** The deleted. */
		DELETED
	}

	/**
	 * Pre insert.
	 */
	@PrePersist
	void preInsert() {
		this.uuid = UUID.randomUUID().toString();
		System.out.println(this.uuid);
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid
	 *            the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the created at.
	 *
	 * @return the created at
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the created at.
	 *
	 * @param createdAt
	 *            the new created at
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

}
