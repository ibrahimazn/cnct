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

/**
 * The Class Namespace.
 *
 * @author ibrahim
 */
@Entity
@Table(name = "namespaces")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Namespace {

  /** The id. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** The name. */
  @Column(name = "name")
  private String name;

  /** The is active. */
  @Column(name = "is_active")
  private Boolean isActive;

  /** The created by. */
  @Column(name = "created_user_id")
  private Long createdBy;

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
}
