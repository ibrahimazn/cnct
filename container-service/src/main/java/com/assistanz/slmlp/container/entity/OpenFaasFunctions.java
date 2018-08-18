/**
 *
 */
package com.assistanz.slmlp.container.entity;

import java.util.Date;
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
 * @author testuser
 *
 */
@Entity
@Table(name = "openfaas_functions")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class OpenFaasFunctions {

  /** Id of the OfflineDeployment. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @JoinColumn(name = "project_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Project project;

  /** Id of the project. */
  @Column(name = "project_id")
  private Long projectId;

  /** Id of the user. */
  @Column(name = "user_id")
  private Long userId;

  /** name of the user. */
  @Column(name = "user_name")
  private String createdBy;

  /** name of the functions. */
  @Column(name = "function_name")
  private String functionName;

  @Column(name = "image")
  private String image;

  @Column(name = "env_process")
  private String envProcess;

  @Column(name = "function_type")
  private String functionType;
  
  @Column(name = "engine_type")
  private String engineType;

  /** name of the user. */
  @Column(name = "creation_date")
  private Date createdAt;

  /** Model Configuration. */
  @JoinColumn(name = "model_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Model model;

  /** Model Configuration id. */
  @Column(name = "model_id")
  private Long modelId;

  /** Check whether dataset is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Long getProjectId() {
    return projectId;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
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

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
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

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public String getFunctionType() {
    return functionType;
  }

  public void setFunctionType(String functionType) {
    this.functionType = functionType;
  }

  public String getEnvProcess() {
    return envProcess;
  }

  public void setEnvProcess(String envProcess) {
    this.envProcess = envProcess;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
  
  public String getEngineType() {
    return engineType;
  }

  public void setEngineType(String engineType) {
    this.engineType = engineType;
  }

  @Override
  public String toString() {
    return "OpenFaasFunctions [id=" + id + ", project=" + project + ", projectId=" + projectId + ", userId=" + userId
        + ", createdBy=" + createdBy + ", functionName=" + functionName + ", image=" + image + ", envProcess="
        + envProcess + ", functionType=" + functionType + ", engineType=" + engineType + ", createdAt=" + createdAt
        + ", model=" + model + ", modelId=" + modelId + ", isActive=" + isActive + "]";
  }
}
