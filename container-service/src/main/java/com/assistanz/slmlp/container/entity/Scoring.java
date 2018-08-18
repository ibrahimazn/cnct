/*
 * 
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

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 
 * @author ibrahim.
 *
 */
@Entity
@Table(name = "scoring")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Scoring implements Serializable {

  /** Id of the Model. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Name of the Prediction/Scoring. */
  @Column(name = "name")
  private String name;

  /** The project. */
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

  /** to be predict score file. */
  @Column(name = "score_file")
  private String scoreFile;

  /** The score result. */
  @Type(type="text")
  @Column(name = "score_result")
  private String scoreResult;

  /** The split ratio. */
  @Column(name = "split_ratio")
  private Float splitRatio;

  /** to be predict dataset file. */
  @Column(name = "dataset_file")
  private String datasetFile;

  @Column(name = "target_column")
  private String targetColumn;

  /** prediction output folder. */
  @Column(name = "prediction_output_url")
  private String predictionOutputUrl;

  /** Check whether dataset is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getDatasetFile() {
    return datasetFile;
  }

  public void setDatasetFile(String datasetFile) {
    this.datasetFile = datasetFile;
  }

  public String getPredictionOutputUrl() {
    return predictionOutputUrl;
  }

  public void setPredictionOutputUrl(String predictionOutputUrl) {
    this.predictionOutputUrl = predictionOutputUrl;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public ModelVersion getModelVersion() {
    return modelVersion;
  }

  public void setModelVersion(ModelVersion modelVersion) {
    this.modelVersion = modelVersion;
  }

  public Long getModelVersionId() {
    return modelVersionId;
  }

  public void setModelVersionId(Long modelVersionId) {
    this.modelVersionId = modelVersionId;
  }

  public String getScoreFile() {
    return scoreFile;
  }

  public void setScoreFile(String scoreFile) {
    this.scoreFile = scoreFile;
  }

  public String getScoreResult() {
    return scoreResult;
  }

  public void setScoreResult(String scoreResult) {
    this.scoreResult = scoreResult;
  }

  public String getTargetColumn() {
    return targetColumn;
  }

  public void setTargetColumn(String targetColumn) {
    this.targetColumn = targetColumn;
  }

  public Float getSplitRatio() {
    return splitRatio;
  }

  public void setSplitRatio(Float splitRatio) {
    this.splitRatio = splitRatio;
  }

}
