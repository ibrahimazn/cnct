/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class ReplicaSet.
 */
@Entity
@Table(name = "replica_set")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class ReplicaSet implements Serializable {

  /** Id of the ReplicaSet. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** The name. */
  @Column(name = "name")
  private String name;

  /** The uid. */
  @Column(name = "uid")
  private String uid;

  /** The deployment id. */
  @Column(name = "deployment_id")
  private Long deploymentId;

  /** The user id. */
  @Column(name = "user_id")
  private Long userId;

  /** The is active. */
  @Column(name = "is_active")
  private Boolean isActive;

  /** The message. */
  @Column(name = "message")
  private String message;

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
   * Gets the deployment id.
   *
   * @return the deployment id
   */
  public Long getDeploymentId() {
    return deploymentId;
  }

  /**
   * Sets the deployment id.
   *
   * @param deploymentId
   *          the new deployment id
   */
  public void setDeploymentId(Long deploymentId) {
    this.deploymentId = deploymentId;
  }

  /**
   * Gets the user id.
   *
   * @return the user id
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * Sets the user id.
   *
   * @param userId
   *          the new user id
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /**
   * Gets the checks if is active.
   *
   * @return the checks if is active
   */
  public Boolean getIsActive() {
    return isActive;
  }

  /**
   * Sets the checks if is active.
   *
   * @param isActive
   *          the new checks if is active
   */
  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message.
   *
   * @param message
   *          the new message
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
