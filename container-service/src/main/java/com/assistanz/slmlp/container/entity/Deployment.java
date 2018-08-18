/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class Deployment.
 *
 * @author ibrahim
 */
@Entity
@Table(name = "deployments")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Deployment {

  /** The id. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** The uid. */
  @Column(name = "uid")
  private String uid;

  /** The name. */
  @Column(name = "name")
  private String name;

  /** The user. */
  @Column(name = "user")
  private String user;

  /** The user id. */
  @Column(name = "user_id")
  private Long userId;

  /** The project id. */
  @Column(name = "project_id")
  private Long projectId;

  /** The namespace. */
  @JoinColumn(name = "namespace_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Namespace namespace;

  /** The namespace id. */
  @Column(name = "namespace_id")
  private Long namespaceId;

  /** The volume. */
  @JoinColumn(name = "volume_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Volume volume;

  /** Id of the Container. */
  @Column(name = "volume_id")
  private Long volumeId;

  /** The image. */
  @JoinColumn(name = "app_image_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private Launcher appImage;

  /** Id of the image. */
  @Column(name = "app_image_id")
  private Long appImageId;

  /** The service. */
  @JoinColumn(name = "service_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToOne
  private NodeServices service;

  /** The service id. */
  @Column(name = "service_id")
  private Long serviceId;

  /** The type. */
  @Column(name = "type")
  private String type;

  /** The is active. */
  @Column(name = "is_active")
  private Boolean isActive;

  /** The message. */
  @Column(name = "message")
  private String message;

  /** The pod. */
  @JoinColumn(name = "pod_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private Pod pod;

  /** Id of the pod. */
  @Column(name = "pod_id")
  private Long podId;

  /** Deployment type. */
  @Column(name = "deployment_type")
  private DeploymentType deploymentType;

  @Column(name = "env_process")
  private String envProcess;

  /** name of the user. */
  @Column(name = "creation_date")
  private Date createdAt;

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
   * Gets the uid.
   *
   * @return the uid
   */
  public String getUid() {
    return uid;
  }

  /**
   * Sets the uid.
   *
   * @param uid the new uid
   */
  public void setUid(String uid) {
    this.uid = uid;
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
   * Gets the user.
   *
   * @return the user
   */
  public String getUser() {
    return user;
  }

  /**
   * Sets the user.
   *
   * @param user the new user
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * Gets the user id.
   *
   * @return the user id
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * Sets the user id.
   *
   * @param userId the new user id
   */
  public void setUserId(Long userId) {
    this.userId = userId;
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
   * Gets the namespace.
   *
   * @return the namespace
   */
  public Namespace getNamespace() {
    return namespace;
  }

  /**
   * Sets the namespace.
   *
   * @param namespace the new namespace
   */
  public void setNamespace(Namespace namespace) {
    this.namespace = namespace;
  }

  /**
   * Gets the namespace id.
   *
   * @return the namespace id
   */
  public Long getNamespaceId() {
    return namespaceId;
  }

  /**
   * Sets the namespace id.
   *
   * @param namespaceId the new namespace id
   */
  public void setNamespaceId(Long namespaceId) {
    this.namespaceId = namespaceId;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type the new type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets the volume.
   *
   * @return the volume
   */
  public Volume getVolume() {
    return volume;
  }

  /**
   * Sets the volume.
   *
   * @param volume the new volume
   */
  public void setVolume(Volume volume) {
    this.volume = volume;
  }

  /**
   * Get the app image.
   *
   * @return the appImage
   */
  public Launcher getAppImage() {
    return appImage;
  }

  /**
   * Set the app image.
   *
   * @param appImage the appImage to set
   */
  public void setAppImage(Launcher appImage) {
    this.appImage = appImage;
  }

  /**
   * Get the app image id.
   *
   * @return the appImageId
   */
  public Long getAppImageId() {
    return appImageId;
  }

  /**
   * Set the app image id.
   *
   * @param appImageId the appImageId to set
   */
  public void setAppImageId(Long appImageId) {
    this.appImageId = appImageId;
  }

  /**
   * Gets the service.
   *
   * @return the service
   */
  public NodeServices getService() {
    return service;
  }

  /**
   * Sets the service.
   *
   * @param service the new service
   */
  public void setService(NodeServices service) {
    this.service = service;
  }

  /**
   * Gets the service id.
   *
   * @return the service id
   */
  public Long getServiceId() {
    return serviceId;
  }

  /**
   * Sets the service id.
   *
   * @param serviceId the new service id
   */
  public void setServiceId(Long serviceId) {
    this.serviceId = serviceId;
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
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message.
   *
   * @param message the new message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Gets the volume id.
   *
   * @return the volume id
   */
  public Long getVolumeId() {
    return volumeId;
  }

  /**
   * Sets the volume id.
   *
   * @param volumeId the new volume id
   */
  public void setVolumeId(Long volumeId) {
    this.volumeId = volumeId;
  }

  /**
   * Gets the pod.
   *
   * @return the pod
   */
  public Pod getPod() {
    return pod;
  }

  /**
   * Sets the pod.
   *
   * @param pod the new pod
   */
  public void setPod(Pod pod) {
    this.pod = pod;
  }

  /**
   * Gets the pod id.
   *
   * @return the pod id
   */
  public Long getPodId() {
    return podId;
  }

  /**
   * Sets the pod id.
   *
   * @param podId the new pod id
   */
  public void setPodId(Long podId) {
    this.podId = podId;
  }

  /**
   * Get the deployment type.
   *
   * @return the deploymentType
   */
  public DeploymentType getDeploymentType() {
    return deploymentType;
  }

  /**
   * Set the deployment type.
   *
   * @param deploymentType the deploymentType to set
   */
  public void setDeploymentType(DeploymentType deploymentType) {
    this.deploymentType = deploymentType;
  }

  /**
   * Get the process name of the funciton.
   *
   * @return the envProcess
   */
  public String getEnvProcess() {
    return envProcess;
  }

  /**
   * Set the process name.
   *
   * @param envProcess the envProcess to set
   */
  public void setEnvProcess(String envProcess) {
    this.envProcess = envProcess;
  }

  /**
   * Created date.
   *
   * @return the createdAt
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * Set the created date.
   *
   * @param createdAt the createdAt to set
   */
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }



  public enum DeploymentType {

    /** Kubernetes deployment. */
    K8S,

    /** Open faas deployment. */
    OPENFAAS

  }
}
