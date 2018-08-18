/*
 * 
 */
package com.ancode.service.account.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class Project.
 */
@Entity
@Table(name = "project")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Project implements Serializable {

  /** Id of the Project. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Name of the Project. */
  @Column(name = "name")
  private String name;

  /** Delivery date of the Project. */
  @Column(name = "delivery_date")
  private Date deliveryDate;

  /** Description of the Project. */
  @Column(name = "description")
  @Type(type = "text")
  private String description;

  /** Check whether project is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  /** The source repo. */
  @JoinColumn(name = "source_repo_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private SourceRepo sourceRepo;

  /** The source repo id. */
  @Column(name = "source_repo_id")
  private Long sourceRepoId;

  /** Launchers of the Project. */
  @ManyToMany
  private List<Launcher> launchers;

  /** TrainingEngines of the Project. */
  @ManyToMany
  @OrderBy("id DESC")
  private List<TrainingEngine> trainingEngines;

  /** Datasets of the Project. */
  @OneToMany
  private List<DataSet> dataSets;

  /** ProjectUser of the Project. */
  @OneToMany
  private List<ProjectUser> projectUsers;

  /** Model Path of the Project. */
  @Column(name = "model_path")
  private String modelPath;

  /** Application Path of the Project. */
  @Column(name = "application_path")
  private String applicationPath;

  /** The offline deployment. */
  @JoinColumn(name = "offline_deployment", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private OfflineDeployment offlineDeployment;

  /** The offline deployment id. */
  @Column(name = "offline_deployment_id")
  private Long offlineDeploymentId;

  /** The created user. */
  @JoinColumn(name = "created_user_id", referencedColumnName = "user_id", updatable = false, insertable = false)
  @ManyToOne
  private User createdUser;

  /** The created user id. */
  @Column(name = "created_user_id")
  private Long createdUserId;

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
   * Gets the delivery date.
   *
   * @return the delivery date
   */
  public Date getDeliveryDate() {
    return deliveryDate;
  }

  /**
   * Sets the delivery date.
   *
   * @param deliveryDate
   *          the new delivery date
   */
  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  /**
   * Gets the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the new description
   */
  public void setDescription(String description) {
    this.description = description;
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

  /**
   * Gets the launchers.
   *
   * @return the launchers
   */
  public List<Launcher> getLaunchers() {
    return launchers;
  }

  /**
   * Sets the launchers.
   *
   * @param launchers
   *          the new launchers
   */
  public void setLaunchers(List<Launcher> launchers) {
    this.launchers = launchers;
  }

  /**
   * Gets the training engines.
   *
   * @return the training engines
   */
  public List<TrainingEngine> getTrainingEngines() {
    return trainingEngines;
  }

  /**
   * Sets the training engines.
   *
   * @param trainingEngines
   *          the new training engines
   */
  public void setTrainingEngines(List<TrainingEngine> trainingEngines) {
    this.trainingEngines = trainingEngines;
  }

  /**
   * Gets the data sets.
   *
   * @return the data sets
   */
  public List<DataSet> getDataSets() {
    return dataSets;
  }

  /**
   * Sets the data sets.
   *
   * @param dataSets
   *          the new data sets
   */
  public void setDataSets(List<DataSet> dataSets) {
    this.dataSets = dataSets;
  }

  /**
   * Gets the model path.
   *
   * @return the model path
   */
  public String getModelPath() {
    return modelPath;
  }

  /**
   * Sets the model path.
   *
   * @param modelPath
   *          the new model path
   */
  public void setModelPath(String modelPath) {
    this.modelPath = modelPath;
  }

  /**
   * Gets the application path.
   *
   * @return the application path
   */
  public String getApplicationPath() {
    return applicationPath;
  }

  /**
   * Sets the application path.
   *
   * @param applicationPath
   *          the new application path
   */
  public void setApplicationPath(String applicationPath) {
    this.applicationPath = applicationPath;
  }

  /**
   * Gets the source repo.
   *
   * @return the source repo
   */
  public SourceRepo getSourceRepo() {
    return sourceRepo;
  }

  /**
   * Sets the source repo.
   *
   * @param sourceRepo
   *          the new source repo
   */
  public void setSourceRepo(SourceRepo sourceRepo) {
    this.sourceRepo = sourceRepo;
  }

  /**
   * Gets the source repo id.
   *
   * @return the source repo id
   */
  public Long getSourceRepoId() {
    return sourceRepoId;
  }

  /**
   * Sets the source repo id.
   *
   * @param sourceRepoId
   *          the new source repo id
   */
  public void setSourceRepoId(Long sourceRepoId) {
    this.sourceRepoId = sourceRepoId;
  }

  /**
   * Gets the offline deployment.
   *
   * @return the offline deployment
   */
  public OfflineDeployment getOfflineDeployment() {
    return offlineDeployment;
  }

  /**
   * Sets the offline deployment.
   *
   * @param offlineDeployment
   *          the new offline deployment
   */
  public void setOfflineDeployment(OfflineDeployment offlineDeployment) {
    this.offlineDeployment = offlineDeployment;
  }

  /**
   * Gets the offline deployment id.
   *
   * @return the offline deployment id
   */
  public Long getOfflineDeploymentId() {
    return offlineDeploymentId;
  }

  /**
   * Sets the offline deployment id.
   *
   * @param offlineDeploymentId
   *          the new offline deployment id
   */
  public void setOfflineDeploymentId(Long offlineDeploymentId) {
    this.offlineDeploymentId = offlineDeploymentId;
  }

  /**
   * Gets the project users.
   *
   * @return the project users
   */
  public List<ProjectUser> getProjectUsers() {
    return projectUsers;
  }

  /**
   * Sets the project users.
   *
   * @param projectUsers
   *          the new project users
   */
  public void setProjectUsers(List<ProjectUser> projectUsers) {
    this.projectUsers = projectUsers;
  }

  /**
   * Gets the created user.
   *
   * @return the created user
   */
  public User getCreatedUser() {
    return createdUser;
  }

  /**
   * Sets the created user.
   *
   * @param createdUser
   *          the new created user
   */
  public void setCreatedUser(User createdUser) {
    this.createdUser = createdUser;
  }

  /**
   * Gets the created user id.
   *
   * @return the created user id
   */
  public Long getCreatedUserId() {
    return createdUserId;
  }

  /**
   * Sets the created user id.
   *
   * @param createdUserId
   *          the new created user id
   */
  public void setCreatedUserId(Long createdUserId) {
    this.createdUserId = createdUserId;
  }

}
