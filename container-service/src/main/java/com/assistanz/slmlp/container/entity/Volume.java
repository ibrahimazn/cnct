/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.google.gson.annotations.SerializedName;

/**
 * The Class Volume.
 *
 * @author ibrahim
 */
@Entity
@Table(name = "volumes")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Volume {

  /** The id. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** The uid. */
  @Column(name = "uid")
  private String uid;

  /** The name. */
  @Column(name = "name")
  private String name;

  /** The user. */
  @Column(name = "user")
  private String user;

  /** The namespace. */
  @SerializedName("namespace")
  private String namespace;

  /** The pvc. */
  @Column(name = "pvc")
  private String pvc;

  /** The created by. */
  @Column(name = "created_user_id")
  private Long createdBy;

  /** The status. */
  @Column(name = "status")
  private Status status;

  /**
   * The Enum Status.
   */
  public enum Status {

    /** The provisioning. */
    PROVISIONING,

    /** The externalprovisioning. */
    EXTERNALPROVISIONING,

    /** The provisioningsucceeded. */
    PROVISIONINGSUCCEEDED,
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
   *          the new id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the uid.
   *
   * @return the uid
   */
  public String getUid() {
    return uid;
  }

  /**
   * Sets the uid.
   *
   * @param uid
   *          the new uid
   */
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the namespace.
   *
   * @return the namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * Sets the namespace.
   *
   * @param namespace
   *          the new namespace
   */
  public void setNamespace(String namespace) {
    this.namespace = namespace;
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
   * @param pvc the new pvc
   */
  public void setPvc(String pvc) {
    this.pvc = pvc;
  }

  /**
   * Gets the user.
   *
   * @return the user
   */
  public String getUser() {
    return user;
  }

  /**
   * Sets the user.
   *
   * @param user the new user
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * Gets the created by.
   *
   * @return the created by
   */
  public Long getCreatedBy() {
    return createdBy;
  }

  /**
   * Sets the created by.
   *
   * @param createdBy the new created by
   */
  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Gets the status.
   *
   * @return the status
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status the new status
   */
  public void setStatus(Status status) {
    this.status = status;
  }

}
