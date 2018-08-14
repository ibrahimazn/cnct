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

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** Entity reference for the user's ceph. */
@Entity
@Table(name = "ceph_storage")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class CephStorage implements Serializable {

	/** Id of the User. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	/** Name of the user volume. */
	@Column(name = "volumeName")
	private String volumeName;

	/** PVC Id for Volume. */
	@Column(name = "pvc")
	private String pvc;

	/** Namespace for partitioning. */
	@Column(name = "nameSpace", nullable = false)
	private String nameSpace;

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
	 * Gets the volume name.
	 *
	 * @return the volume name
	 */
	public String getVolumeName() {
		return volumeName;
	}

	/**
	 * Sets the volume name.
	 *
	 * @param volumeName
	 *            the new volume name
	 */
	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	/**
	 * Gets the pvc.
	 *
	 * @return the pvc
	 */
	public String getPvc() {
		return pvc;
	}

	/**
	 * Sets the pvc.
	 *
	 * @param pvc
	 *            the new pvc
	 */
	public void setPvc(String pvc) {
		this.pvc = pvc;
	}

	/**
	 * Gets the name space.
	 *
	 * @return the name space
	 */
	public String getNameSpace() {
		return nameSpace;
	}

	/**
	 * Sets the name space.
	 *
	 * @param nameSpace
	 *            the new name space
	 */
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
}
