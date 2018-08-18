/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class AppContainer is act as a model . This entity has ManyToOne
 * relationship with Launcher and Nodeservices.
 */
@Entity
@Table(name = "containers")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class AppContainer implements Serializable {

  /** Id of the Container. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Pod object of the Container. */
  @Column(name = "uid")
  private String uid;

  /** Pod Id of the Container. */
  @JoinColumn(name = "pod_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private Pod pod;

  /** Id of the pod. */
  @Column(name = "pod_id")
  private Long podId;

  /** InternalPort of the Container. */
  @Column(name = "internal_port")
  private String internalPort;

  /** Launcher object of the container. */
  @JoinColumn(name = "launcher_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private Launcher launcher;

  /** Launcher id of the container. */
  @Column(name = "launcher_id")
  private Long launcherId;

  /** Deployment reference. */
  @JoinColumn(name = "deployment_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private Deployment deployment;

  /** Deployment id of the container. */
  @Column(name = "deployment_id")
  private Long deploymentId;

  /** Service reference. */
  @JoinColumn(name = "service_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private NodeServices nodeService;
  /** Service id of the container. */
  @Column(name = "service_id")
  private Long serviceId;

  /** Transient attribute user. */
  @Transient
  private String user;

  /** Stus of the container. */
  @Column(name = "status")
  private Status status;

  /** User Id. */
  @Column(name = "user_id")
  private Long userId;

  /** Enumeration status for Launcher. */
  public enum Status {
    /** Running status of the launcher while launching the launcher. */
    RUNNING,

    /** STOPPED status of the launcher while launching the launcher. */
    STOPPED,

    /** SCHEDULED status of the POD while launching the launcher. */
    SCHEDULED,

    /** SUCCESSFULMOUNTVOLUME status of the POD while launching the launcher. */
    SUCCESSFULMOUNTVOLUME,

    /** PULLING status of the POD while launching the launcher. */
    PULLING,

    /** PULLED status of the POD while launching the launcher. */
    PULLED,

    /** CREATED status of the POD while launching the launcher. */
    CREATED,

    /** STARTED status of the POD while launching the launcher. */
    STARTED,

  }

  /** Check whether launcher is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  /** Node port of the container. */
  @Column(name = "node_port")
  private String nodePort;

  /**
   * Get the Node port.
   * 
   * @return the nodePort.
   */
  public String getNodePort() {
    return nodePort;
  }

  /**
   * Set the node port of the container.
   * 
   * @param nodePort
   *          the nodePort to set
   */
  public void setNodePort(String nodePort) {
    this.nodePort = nodePort;
  }

  /**
   * Get the id.
   * 
   * @return the id.
   */
  public Long getId() {
    return id;
  }

  /**
   * Set the id.
   * 
   * @param id
   *          the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Get the Uid.
   * 
   * @return the Uid.
   */
  public String getUid() {
    return uid;
  }

  /**
   * Set the Uid.
   * 
   * @param uid
   *          the UId to set
   */
  public void setUid(String uid) {
    this.uid = uid;
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
   * @param podId
   *          the new pod id
   */
  public void setPodId(Long podId) {
    this.podId = podId;
  }

  /**
   * Gets the internal port.
   *
   * @return the internalPort
   */
  public String getInternalPort() {
    return internalPort;
  }

  /**
   * Sets the internal port.
   *
   * @param internalPort
   *          the internalPort to set
   */
  public void setInternalPort(String internalPort) {
    this.internalPort = internalPort;
  }

  /**
   * Get the image reference.
   * 
   * @return the launcher
   */
  public Launcher getLauncher() {
    return launcher;
  }

  /**
   * Set the image reference.
   * 
   * @param launcher
   *          the launcher to set
   */
  public void setLauncher(Launcher launcher) {
    this.launcher = launcher;
  }

  /**
   * Get the launcher id.
   * 
   * @return the launcherId
   */
  public Long getLauncherId() {
    return launcherId;
  }

  /**
   * Set the launcher id.
   * 
   * @param launcherId
   *          the launcherId to set
   */
  public void setLauncherId(Long launcherId) {
    this.launcherId = launcherId;
  }

  /**
   * Get the deployment id.
   * 
   * @return the deployment
   */
  public Deployment getDeployment() {
    return deployment;
  }

  /**
   * Set the deployment id.
   * 
   * @param deployment
   *          the deployment to set
   */
  public void setDeployment(Deployment deployment) {
    this.deployment = deployment;
  }

  /**
   * Gets the deployment id.
   *
   * @return the deploymentId
   */
  public Long getDeploymentId() {
    return deploymentId;
  }

  /**
   * Sets the deployment id.
   *
   * @param deploymentId
   *          the deploymentId to set
   */
  public void setDeploymentId(Long deploymentId) {
    this.deploymentId = deploymentId;
  }

  /**
   * Get the node service.
   * 
   * @return the nodeService
   */
  public NodeServices getNodeService() {
    return nodeService;
  }

  /**
   * Set the node service.
   * 
   * @param nodeService
   *          the nodeService to set
   */
  public void setNodeService(NodeServices nodeService) {
    this.nodeService = nodeService;
  }

  /**
   * Get the service id.
   * 
   * @return the serviceId
   */
  public Long getServiceId() {
    return serviceId;
  }

  /**
   * Set the service id.
   * 
   * @param serviceId
   *          the serviceId to set
   */
  public void setServiceId(Long serviceId) {
    this.serviceId = serviceId;
  }

  /**
   * Gets the status.
   *
   * @return the status
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status
   *          the new status
   */
  public void setStatus(Status status) {
    this.status = status;
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
   * @param user
   *          the user to set
   */
  public void setUser(String user) {
    this.user = user;
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
   * @param pod
   *          the new pod
   */
  public void setPod(Pod pod) {
    this.pod = pod;
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
   * @param userId
   *          the new user id
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

}
