/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import az.ancode.filemanager.connector.fileset.CsvFileResponse;

/**
 * The Class ModelDeployment.
 */
@Entity
@Table(name = "model_deployment")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class ModelDeployment implements Serializable {

  /** Id of the Dataset. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Name of the Dataset. */
  @Column(name = "name")
  private String name;


  /** OpenFaas Deployment . */
  @JoinColumn(name = "deployment_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Deployment deployment;

  /** OpenFaas Deployment id. */
  @Column(name = "deployment_id")
  private Long deploymentId;

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

  @JoinColumn(name = "model_version_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private ModelVersion modelVersion;

  /** Model Configuration id. */
  @Column(name = "model_version_id")
  private Long modelVersionId;

  /** to be predict dataset file. */
  @Column(name = "dataset_file")
  private String datasetFile;

  /** Source file. */
  @Column(name = "source_file")
  private String sourceFile;

  @Column(name = "target_column")
  private String targetColumn;

  /** prediction output folder. */
  @Column(name = "prediction_output_url")
  private String predictionOutputUrl;

  /** Set the deployment is online or offline. */
  @Column(name = "is_online")
  private Boolean isOnline;

  /** Check whether dataset is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  @Transient
  private Launcher launcher;

  @Transient
  private CsvFileResponse csvResponse;

  @Transient
  private String scoringResult;

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
   * @return the deployment
   */
  public Deployment getDeployment() {
    return deployment;
  }

  /**
   * @param deployment the deployment to set
   */
  public void setDeployment(Deployment deployment) {
    this.deployment = deployment;
  }

  /**
   * @return the deploymentId
   */
  public Long getDeploymentId() {
    return deploymentId;
  }

  /**
   * @param deploymentId the deploymentId to set
   */
  public void setDeploymentId(Long deploymentId) {
    this.deploymentId = deploymentId;
  }

  /**
   * Get the project.
   *
   * @return the project
   */
  public Project getProject() {
    return project;
  }

  /**
   * Set the project.
   *
   * @param project the project to set
   */
  public void setProject(Project project) {
    this.project = project;
  }

  /**
   * @return the projectId
   */
  public Long getProjectId() {
    return projectId;
  }

  /**
   * @param projectId the projectId to set
   */
  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  /**
   * @return the userId
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /**
   * @return the createdBy
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * @return the createdAt
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * @param createdAt the createdAt to set
   */
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * @return the model
   */
  public Model getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(Model model) {
    this.model = model;
  }

  /**
   * @return the modelId
   */
  public Long getModelId() {
    return modelId;
  }

  /**
   * @param modelId the modelId to set
   */
  public void setModelId(Long modelId) {
    this.modelId = modelId;
  }

  /**
   * @return the modelVersion
   */
  public ModelVersion getModelVersion() {
    return modelVersion;
  }

  /**
   * @param modelVersion the modelVersion to set
   */
  public void setModelVersion(ModelVersion modelVersion) {
    this.modelVersion = modelVersion;
  }

  /**
   * @return the modelVersionId
   */
  public Long getModelVersionId() {
    return modelVersionId;
  }

  /**
   * @param modelVersionId the modelVersionId to set
   */
  public void setModelVersionId(Long modelVersionId) {
    this.modelVersionId = modelVersionId;
  }

  /**
   * @return the datasetFile
   */
  public String getDatasetFile() {
    return datasetFile;
  }

  /**
   * @param datasetFile the datasetFile to set
   */
  public void setDatasetFile(String datasetFile) {
    this.datasetFile = datasetFile;
  }


  /**
   * @return the sourceFile
   */
  public String getSourceFile() {
    return sourceFile;
  }

  /**
   * @param sourceFile the sourceFile to set
   */
  public void setSourceFile(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  /**
   * @return the targetColumn
   */
  public String getTargetColumn() {
    return targetColumn;
  }

  /**
   * @param targetColumn the targetColumn to set
   */
  public void setTargetColumn(String targetColumn) {
    this.targetColumn = targetColumn;
  }

  /**
   * @return the predictionOutputUrl
   */
  public String getPredictionOutputUrl() {
    return predictionOutputUrl;
  }

  /**
   * @param predictionOutputUrl the predictionOutputUrl to set
   */
  public void setPredictionOutputUrl(String predictionOutputUrl) {
    this.predictionOutputUrl = predictionOutputUrl;
  }


  /**
   * Get the deployment is online.
   *
   * @return the isOnline
   */
  public Boolean getIsOnline() {
    return isOnline;
  }

  /**
   * Set the deployment is online.
   *
   * @param isOnline the isOnline to set
   */
  public void setIsOnline(Boolean isOnline) {
    this.isOnline = isOnline;
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
   * @return the launcher
   */
  public Launcher getLauncher() {
    return launcher;
  }

  /**
   * @param launcher the launcher to set
   */
  public void setLauncher(Launcher launcher) {
    this.launcher = launcher;
  }

  /**
   * @return the csvResponse
   */
  public CsvFileResponse getCsvResponse() {
    return csvResponse;
  }

  /**
   * @param csvResponse the csvResponse to set
   */
  public void setCsvResponse(CsvFileResponse csvResponse) {
    this.csvResponse = csvResponse;
  }

  /**
   * @return the scoringResult
   */
  public String getScoringResult() {
    return scoringResult;
  }

  /**
   * @param scoringResult the scoringResult to set
   */
  public void setScoringResult(String scoringResult) {
    this.scoringResult = scoringResult;
  }

}
