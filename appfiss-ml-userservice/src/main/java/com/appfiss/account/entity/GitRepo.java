/*
 *
 */
package com.appfiss.account.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** Entity reference for the user's git repo. */
@Entity
@Table(name = "git_repo")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class GitRepo implements Serializable {

	/** Id of the User. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "git_repo_id")
	private Long id;

	/** Password of the Gitlab user's. */
	@Column(name = "password")
	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@Transient
	private String password;

	/** Git Repo URL. */
	@Column(name = "git_repo_url")
	private String gitRepoUrl;

	/** User Name of the Gitlab user's. */
	@Column(name = "user_name", nullable = false)
	private String userName;

	/** Active Status of the User. */
	@Column(name = "active")
	private boolean active;


	/**
	 * Get the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * Gets the git repo url.
	 *
	 * @return the git repo url
	 */
	public String getGitRepoUrl() {
		return gitRepoUrl;
	}

	/**
	 * Sets the git repo url.
	 *
	 * @param gitRepoUrl the new git repo url
	 */
	public void setGitRepoUrl(String gitRepoUrl) {
		this.gitRepoUrl = gitRepoUrl;
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
	 * @param active the new active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
