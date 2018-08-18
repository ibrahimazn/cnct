package com.assistanz.slmlp.container.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "training_jobs")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class TrainingJob implements Serializable {

  /** Id of the TraingEngine. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "job_name")
  private String jobName;

  @JoinColumn(name = "dataset_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private DataSet dataSet;

  @Column(name = "dataset_id")
  private Long dataSetId;

  @Column(name = "testRatio")
  private Float testRatio;

  @Column(name = "file_path")
  private String filePath;

  @JoinColumn(name = "training_engine_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private TrainingEngine trainingEngine;

  @Column(name = "training_engine_id")
  private Long trainingEngineId;

  @ManyToMany
  private List<Model> models;

  @Column(name = "output_path")
  private String outputPath;
  
  @Column(name = "tensor_board")
  private String tensorBoardUrl;

  @Column(name = "is_active")
  private Boolean isActive;

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the jobName
   */
  public String getJobName() {
    return jobName;
  }

  /**
   * @param jobName
   *          the jobName to set
   */
  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  /**
   * @return the dataSet
   */
  public DataSet getDataSet() {
    return dataSet;
  }

  /**
   * @param dataSet
   *          the dataSet to set
   */
  public void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  /**
   * @return the dataSetId
   */
  public Long getDataSetId() {
    return dataSetId;
  }

  /**
   * @param dataSetId
   *          the dataSetId to set
   */
  public void setDataSetId(Long dataSetId) {
    this.dataSetId = dataSetId;
  }

  /**
   * Gets the test ratio.
   *
   * @return the test ratio
   */
  public Float getTestRatio() {
    return testRatio;
  }

  /**
   * Sets the test ratio.
   *
   * @param testRatio the new test ratio
   */
  public void setTestRatio(Float testRatio) {
    this.testRatio = testRatio;
  }

  /**
   * @return the filePath
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * @param filePath
   *          the filePath to set
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * @return the trainingEngine
   */
  public TrainingEngine getTrainingEngine() {
    return trainingEngine;
  }

  /**
   * @param trainingEngine
   *          the trainingEngine to set
   */
  public void setTrainingEngine(TrainingEngine trainingEngine) {
    this.trainingEngine = trainingEngine;
  }

  /**
   * @return the trainingEngineId
   */
  public Long getTrainingEngineId() {
    return trainingEngineId;
  }

  /**
   * @param trainingEngineId
   *          the trainingEngineId to set
   */
  public void setTrainingEngineId(Long trainingEngineId) {
    this.trainingEngineId = trainingEngineId;
  }

  /**
   * @return the model
   */
  public List<Model> getModels() {
    return models;
  }

  /**
   * @param model
   *          the model to set
   */
  public void setModels(List<Model> models) {
    this.models = models;
  }

  /**
   * @param outputPath
   *          the outputPath to set
   */
  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  /**
   * @return the outpuptPath
   */
  public String getOutputPath() {
    return outputPath;
  }

  /**
   * @return the isActive
   */
  public Boolean getIsActive() {
    return isActive;
  }

  /**
   * @param isActive
   *          the isActive to set
   */
  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public String getTensorBoardUrl() {
    return tensorBoardUrl;
  }

  public void setTensorBoardUrl(String tensorBoardUrl) {
    this.tensorBoardUrl = tensorBoardUrl;
  }
}
