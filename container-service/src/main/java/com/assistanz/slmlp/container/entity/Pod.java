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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class Pod.
 */
@Entity
@Table(name = "pod")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Pod implements Serializable {

  /** Id of the Pod. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** The uid. */
  @Column(name = "uid")
  private String uid;

  /** Name of the Pod. */
  @Column(name = "name")
  private String name;

  /** Host of the host. */
  @Column(name = "host")
  private String host;

  /** The replica set. */
  @JoinColumn(name = "replicaset_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private ReplicaSet replicaSet;

  /** Id of the Replica set. */
  @Column(name = "replicaset_id")
  private Long replicaSetId;

  /** The message. */
  @Column(name = "message")
  private String message;

  /** Check whether department is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

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
   * @param id the new id
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
   * @param uid the new uid
   */
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * Gets the replica set.
   *
   * @return the replica set
   */
  public ReplicaSet getReplicaSet() {
    return replicaSet;
  }

  /**
   * Sets the replica set.
   *
   * @param replicaSet the new replica set
   */
  public void setReplicaSet(ReplicaSet replicaSet) {
    this.replicaSet = replicaSet;
  }

  /**
   * Gets the replica set id.
   *
   * @return the replica set id
   */
  public Long getReplicaSetId() {
    return replicaSetId;
  }

  /**
   * Sets the replica set id.
   *
   * @param replicaSetId the new replica set id
   */
  public void setReplicaSetId(Long replicaSetId) {
    this.replicaSetId = replicaSetId;
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
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the host.
   *
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * Sets the host.
   *
   * @param host the new host
   */
  public void setHost(String host) {
    this.host = host;
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
   * @param message the new message
   */
  public void setMessage(String message) {
    this.message = message;
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
   * @param isActive the new checks if is active
   */
  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

}
