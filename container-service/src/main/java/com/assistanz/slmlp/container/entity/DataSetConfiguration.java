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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class DataSetConfiguration.
 */
@Entity
@Table(name = "data_set_config")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class DataSetConfiguration implements Serializable {

  /** Id of the DataSetSource. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** URL of the Data Source. */
  @Column(name = "data_source_url")
  private String dataSourceUrl;

  /** Auth URL of the Data Source. */
  @Column(name = "data_source_auth_url")
  private String dataSourceAuthUrl;

  /** The data set storage types. */
  @ManyToMany
  private List<DataSetStorageType> dataSetStorageTypes;

  /** Dataset Version Limit. */
  @Column(name = "data_set_version_limit")
  private Long dataSetVersionLimit;

  /** Publish URL of the Data Set. */
  @Column(name = "data_set_publish_url")
  private String dataSetPublishUrl;

  /** Auth URL of the Data Set. */
  @Column(name = "data_set_auth_url")
  private String dataSetAuthUrl;

  /** Check whether datasetsource is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  /** DataSource AdditionalParam. */
  @OneToMany
  private List<DataSourceAdditionalParam> dataSourceAdditionalParam;

  /** DataSet AdditionalParam. */
  @OneToMany
  private List<DataSetAdditionalParam> dataSetAdditionalParam;

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
   * Gets the data source url.
   *
   * @return the data source url
   */
  public String getDataSourceUrl() {
    return dataSourceUrl;
  }

  /**
   * Sets the data source url.
   *
   * @param dataSourceUrl the new data source url
   */
  public void setDataSourceUrl(String dataSourceUrl) {
    this.dataSourceUrl = dataSourceUrl;
  }

  /**
   * Gets the data source auth url.
   *
   * @return the data source auth url
   */
  public String getDataSourceAuthUrl() {
    return dataSourceAuthUrl;
  }

  /**
   * Sets the data source auth url.
   *
   * @param dataSourceAuthUrl the new data source auth url
   */
  public void setDataSourceAuthUrl(String dataSourceAuthUrl) {
    this.dataSourceAuthUrl = dataSourceAuthUrl;
  }

  /**
   * Gets the data set storage types.
   *
   * @return the data set storage types
   */
  public List<DataSetStorageType> getDataSetStorageTypes() {
    return dataSetStorageTypes;
  }

  /**
   * Sets the data set storage types.
   *
   * @param dataSetStorageTypes the new data set storage types
   */
  public void setDataSetStorageTypes(List<DataSetStorageType> dataSetStorageTypes) {
    this.dataSetStorageTypes = dataSetStorageTypes;
  }

  /**
   * Gets the data set version limit.
   *
   * @return the data set version limit
   */
  public Long getDataSetVersionLimit() {
    return dataSetVersionLimit;
  }

  /**
   * Sets the data set version limit.
   *
   * @param dataSetVersionLimit the new data set version limit
   */
  public void setDataSetVersionLimit(Long dataSetVersionLimit) {
    this.dataSetVersionLimit = dataSetVersionLimit;
  }

  /**
   * Gets the data set publish url.
   *
   * @return the data set publish url
   */
  public String getDataSetPublishUrl() {
    return dataSetPublishUrl;
  }

  /**
   * Sets the data set publish url.
   *
   * @param dataSetPublishUrl the new data set publish url
   */
  public void setDataSetPublishUrl(String dataSetPublishUrl) {
    this.dataSetPublishUrl = dataSetPublishUrl;
  }

  /**
   * Gets the data set auth url.
   *
   * @return the data set auth url
   */
  public String getDataSetAuthUrl() {
    return dataSetAuthUrl;
  }

  /**
   * Sets the data set auth url.
   *
   * @param dataSetAuthUrl the new data set auth url
   */
  public void setDataSetAuthUrl(String dataSetAuthUrl) {
    this.dataSetAuthUrl = dataSetAuthUrl;
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
   * Gets the data source additional param.
   *
   * @return the data source additional param
   */
  public List<DataSourceAdditionalParam> getDataSourceAdditionalParam() {
    return dataSourceAdditionalParam;
  }

  /**
   * Sets the data source additional param.
   *
   * @param dataSourceAdditionalParam the new data source additional param
   */
  public void setDataSourceAdditionalParam(List<DataSourceAdditionalParam> dataSourceAdditionalParam) {
    this.dataSourceAdditionalParam = dataSourceAdditionalParam;
  }

  /**
   * Gets the data set additional param.
   *
   * @return the data set additional param
   */
  public List<DataSetAdditionalParam> getDataSetAdditionalParam() {
    return dataSetAdditionalParam;
  }

  /**
   * Sets the data set additional param.
   *
   * @param dataSetAdditionalParam the new data set additional param
   */
  public void setDataSetAdditionalParam(List<DataSetAdditionalParam> dataSetAdditionalParam) {
    this.dataSetAdditionalParam = dataSetAdditionalParam;
  }

}
