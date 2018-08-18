/*
 * 
 */
package com.ancode.service.account.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class DataSetStorageType.
 */
@Entity
@Table(name = "dataset_storage_type")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class DataSetStorageType implements Serializable {

  /** Id of the DataSetStorageType. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Name of the DataSetStorageType. */
  @Column(name = "name")
  private String name;

  /** Check whether DataSetStorageType is in active state or in active state. */
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

}
