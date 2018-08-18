/*
 * 
 */
package com.ancode.service.account.entity;

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
 * The Class AppContainer.
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

  /** The internal port. */
  @Column(name = "internal_port")
  private String internalPort;

  /** Application image reference. */
  @JoinColumn(name = "launcher_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private Launcher launcher;

  /** The launcher id. */
  @Column(name = "launcher_id")
  private Long launcherId;

  /** Deployment reference. */
  @JoinColumn(name = "deployment_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private Deployment deployment;

  /** The deployment id. */
  @Column(name = "deployment_id")
  private Long deploymentId;

  /** Service reference. */
  @JoinColumn(name = "service_id", referencedColumnName = "id", updatable = false, insertable = false)
  @ManyToOne
  private NodeServices nodeService;

  /** The service id. */
  @Column(name = "service_id")
  private Long serviceId;

  /** The user. */
  @Transient
  private String user;

  /** Launcher current state. */
  @Column(name = "status")
  private Status status;

  /** Enumeration status for Launcher. */
  public enum Status {

    /** The running. */
    RUNNING,

    /** The stopped. */
    STOPPED
  }

  /** Check whether launcher is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  /** The node port. */
  @Column(name = "node_port")
  private String nodePort;

  /**
   * Gets the node port.
   *
   * @return the nodePort
   */
  public String getNodePort() {
    return nodePort;
  }

  /**
   * Sets the node port.
   *
   * @param nodePort
   *          the nodePort to set
   */
  public void setNodePort(String nodePort) {
    this.nodePort = nodePort;
  }

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

}
