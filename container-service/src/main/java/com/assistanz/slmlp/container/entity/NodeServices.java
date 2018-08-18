/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.assistanz.slmlp.container.entity.Deployment.DeploymentType;

/**
 * The Class NodeServices.
 *
 * @author ibrahim
 */
@Entity
@Table(name = "services")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class NodeServices {

  /** The id. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** The name. */
  @Column(name = "name")
  private String name;

  /** The namespace. */
  @Column(name = "namespace")
  private String namespace;

  /** The label. */
  @Column(name = "label")
  private String label;

  /** The port. */
  @Column(name = "port")
  private String port;

  /** The node port. */
  @Column(name = "node_port")
  private String nodePort;

  /** The user id. */
  @Column(name = "user_id")
  private Long userId;

  @Transient
  private DeploymentType deploymentType;

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
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the namespace.
   *
   * @return the namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * Sets the namespace.
   *
   * @param namespace the new namespace
   */
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * Gets the label.
   *
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label.
   *
   * @param label the new label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Gets the port.
   *
   * @return the port
   */
  public String getPort() {
    return port;
  }

  /**
   * Sets the port.
   *
   * @param port the new port
   */
  public void setPort(String port) {
    this.port = port;
  }

  /**
   * Gets the node port.
   *
   * @return the node port
   */
  public String getNodePort() {
    return nodePort;
  }

  /**
   * Sets the node port.
   *
   * @param nodePort the new node port
   */
  public void setNodePort(String nodePort) {
    this.nodePort = nodePort;
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
   * @return the deploymentType
   */
  public DeploymentType getDeploymentType() {
    return deploymentType;
  }

  /**
   * @param deploymentType the deploymentType to set
   */
  public void setDeploymentType(DeploymentType deploymentType) {
    this.deploymentType = deploymentType;
  }



}
