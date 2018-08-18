/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
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

/**
 * The Class DataSet.
 */
@Entity
@Table(name = "dataset")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class DataSet implements Serializable {

  /** Id of the Dataset. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Name of the Dataset. */
  @Column(name = "name")
  private String name;

  /** Id of the project. */
  @Column(name = "project_id")
  private Long projectId;

  /** The Data sources. */
  @ManyToMany
  private List<DataSource> DataSources;

  /** Data Set Configuration. */
  @JoinColumn(name = "data_set_config_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private DataSetConfiguration dataSetConfig;

  /** Data Set Configuration id. */
  @Column(name = "data_set_config_id")
  private Long dataSetSourceId;

  /** Check whether dataset is in active state or in active state. */
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
   * Gets the project id.
   *
   * @return the project id
   */
  public Long getProjectId() {
    return projectId;
  }

  /**
   * Sets the project id.
   *
   * @param projectId the new project id
   */
  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  /**
   * Gets the data sources.
   *
   * @return the data sources
   */
  public List<DataSource> getDataSources() {
    return DataSources;
  }

  /**
   * Sets the data sources.
   *
   * @param dataSources the new data sources
   */
  public void setDataSources(List<DataSource> dataSources) {
    DataSources = dataSources;
  }

  /**
   * Gets the data set config.
   *
   * @return the data set config
   */
  public DataSetConfiguration getDataSetConfig() {
    return dataSetConfig;
  }

  /**
   * Sets the data set config.
   *
   * @param dataSetConfig the new data set config
   */
  public void setDataSetConfig(DataSetConfiguration dataSetConfig) {
    this.dataSetConfig = dataSetConfig;
  }

  /**
   * Gets the data set source id.
   *
   * @return the data set source id
   */
  public Long getDataSetSourceId() {
    return dataSetSourceId;
  }

  /**
   * Sets the data set source id.
   *
   * @param dataSetSourceId the new data set source id
   */
  public void setDataSetSourceId(Long dataSetSourceId) {
    this.dataSetSourceId = dataSetSourceId;
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
