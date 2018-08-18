package com.assistanz.slmlp.container.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author ibrahim
 *
 */
@Entity
@Table(name = "model")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Model implements Serializable {

  /** Id of the Model. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Name of the Model. */
  @Column(name = "name")
  private String name;

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

  /** Sampling dataset Set Configuration. */
  @JoinColumn(name = "sampling_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Sampling sampling;

  /** Id of the project. */
  @Column(name = "sampling_id")
  private Long samplingId;

  /** Data Set Configuration. */
  @JoinColumn(name = "data_set_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private DataSet dataSet;

  /** Data Set Configuration id. */
  @Column(name = "data_set_id")
  private Long dataSetId;

  /** Processed dataset file. */
  @Column(name = "dataset_file")
  private String datasetFile;

  @Column(name = "model_publish_url")
  private String publishUrl;

  /** model file. */
  @Column(name = "model_path")
  private String modelPath;

  /** algorithm . */
  @Column(name = "algorithm")
  private String algorithm;

  /** model script file. */
  @Column(name = "model_file_src")
  private String modelFileSrc;

  @Column(name = "model_file")
  private String modelFile;

  @Column(name = "latestVersion")
  private String latestVersion;

  /** Type of the Model. */
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private TYPE modelType;

  /** Check whether dataset is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  /**
   * Enum type for Model type.
   *
   */
  public enum TYPE {
    /** Predefined algorithm. */
    PREDEFINED,
    /** custom model creation. */
    CUSTOM
  }

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

  public Long getProjectId() {
    return projectId;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Sampling getSampling() {
    return sampling;
  }

  public void setSampling(Sampling sampling) {
    this.sampling = sampling;
  }

  public Long getSamplingId() {
    return samplingId;
  }

  public void setSamplingId(Long samplingId) {
    this.samplingId = samplingId;
  }

  public DataSet getDataSet() {
    return dataSet;
  }

  public void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  public Long getDataSetId() {
    return dataSetId;
  }

  public void setDataSetId(Long dataSetId) {
    this.dataSetId = dataSetId;
  }

  public TYPE getModelType() {
    return modelType;
  }

  public void setModelType(TYPE modelType) {
    this.modelType = modelType;
  }

  public String getDatasetFile() {
    return datasetFile;
  }

  public void setDatasetFile(String datasetFile) {
    this.datasetFile = datasetFile;
  }

  public String getModelPath() {
    return modelPath;
  }

  public void setModelPath(String modelPath) {
    this.modelPath = modelPath;
  }

  public String getModelFileSrc() {
    return modelFileSrc;
  }

  public void setModelFileSrc(String modelFileSrc) {
    this.modelFileSrc = modelFileSrc;
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

  public String getPublishUrl() {
    return publishUrl;
  }

  public void setPublishUrl(String publishUrl) {
    this.publishUrl = publishUrl;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public String getModelFile() {
    return modelFile;
  }

  public void setModelFile(String modelFile) {
    this.modelFile = modelFile;
  }

  public String getLatestVersion() {
    return latestVersion;
  }

  public void setLatestVersion(String latestVersion) {
    this.latestVersion = latestVersion;
  }

  @Override
  public String toString() {
    return "Model [id=" + id + ", name=" + name + ", project=" + project + ", projectId=" + projectId + ", userId="
        + userId + ", createdBy=" + createdBy + ", createdAt=" + createdAt + ", sampling=" + sampling + ", samplingId="
        + samplingId + ", dataSet=" + dataSet + ", dataSetId=" + dataSetId + ", datasetFile=" + datasetFile
        + ", publishUrl=" + publishUrl + ", modelPath=" + modelPath + ", algorithm=" + algorithm + ", modelFileSrc="
        + modelFileSrc + ", modelFile=" + modelFile + ", latestVersion=" + latestVersion + ", modelType=" + modelType
        + ", isActive=" + isActive + "]";
  }

}
