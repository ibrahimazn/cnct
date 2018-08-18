package com.assistanz.slmlp.container.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author ibrahim
 *
 */
@Entity
@Table(name = "model_versions")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class ModelVersion implements Serializable {

  /** Id of the Model. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** UUID of the Model versions. */
  @Column(name = "uuid", unique = true)
  private String uuid;
  
  /** The version. */
  @Column(name = "version")
  private String version;

  @JoinColumn(name = "model_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Model model;

  /** Id of the model. */
  @Column(name = "model_id")
  private Long modelId;

  /** Id of the user. */
  @Column(name = "user_id")
  private Long userId;

  /** name of the user. */
  @Column(name = "user_name")
  private String createdBy;

  /** name of the user. */
  @Column(name = "creation_date")
  private Date createdAt;
  
  @Column(name = "model_file")
  private String modelFile;

  /** Check whether dataset is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Model getModel() {
    return model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public Long getModelId() {
    return modelId;
  }

  public void setModelId(Long modelId) {
    this.modelId = modelId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getModelFile() {
    return modelFile;
  }

  public void setModelFile(String modelFile) {
    this.modelFile = modelFile;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}
