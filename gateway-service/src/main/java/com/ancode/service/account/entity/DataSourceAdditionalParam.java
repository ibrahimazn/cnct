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
 * The Class DataSourceAdditionalParam.
 */
@Entity
@Table(name = "data_source_additional_param")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class DataSourceAdditionalParam implements Serializable {

  /** Id of the Additional Param. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Key of the Additional Param. */
  @Column(name = "key_param")
  private String keyParam;

  /** Value of the Additional Param. */
  @Column(name = "value_param")
  private String valueParam;

  /**
   * Check whether dataset additional param is in active state or in active
   * state.
   */
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
   * Gets the key param.
   *
   * @return the key param
   */
  public String getKeyParam() {
    return keyParam;
  }

  /**
   * Sets the key param.
   *
   * @param keyParam
   *          the new key param
   */
  public void setKeyParam(String keyParam) {
    this.keyParam = keyParam;
  }

  /**
   * Gets the value param.
   *
   * @return the value param
   */
  public String getValueParam() {
    return valueParam;
  }

  /**
   * Sets the value param.
   *
   * @param valueParam
   *          the new value param
   */
  public void setValueParam(String valueParam) {
    this.valueParam = valueParam;
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
