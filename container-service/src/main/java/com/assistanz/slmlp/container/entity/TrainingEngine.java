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
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class TrainingEngine.
 */
@Entity
@Table(name = "training_engine")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class TrainingEngine implements Serializable {

  /** Id of the TraingEngine. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Name of the TraingEngine. */
  @Column(name = "name")
  private String name;

  /** Tool of the TraingEngine. */
  @Column(name = "tools")
  private String tools;

  /** The platform tool. */
  @Column(name = "platform_tool")
  private String platformTool;

  /** Description of the Container. */
  @Column(name = "description")
  private String description;

  /** The image name. */
  @Column(name = "image_name")
  private String imageName;

  /** The image. */
  @Column(name = "image")
  private String image;

  /** The internal port. */
  @Column(name = "internal_port")
  private String internalPort;

  /** The type. */
  @Column(name = "type")
  private String type;

  /** Check whether TraingEngine is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  @Transient
  private String user;

  /** The user id. */
  @Transient
  private Long userId;

  /** The project id. */
  @Transient
  private Long projectId;

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
   * Gets the tools.
   *
   * @return the tools
   */
  public String getTools() {
    return tools;
  }

  /**
   * Sets the tools.
   *
   * @param tools the new tools
   */
  public void setTools(String tools) {
    this.tools = tools;
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
   * Gets the platform tool.
   *
   * @return the platform tool
   */
  public String getPlatformTool() {
    return platformTool;
  }

  /**
   * Sets the platform tool.
   *
   * @param platformTool the new platform tool
   */
  public void setPlatformTool(String platformTool) {
    this.platformTool = platformTool;
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
   * @param description the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the image name.
   *
   * @return the image name
   */
  public String getImageName() {
    return imageName;
  }

  /**
   * Sets the image name.
   *
   * @param imageName the new image name
   */
  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  /**
   * Gets the image.
   *
   * @return the image
   */
  public String getImage() {
    return image;
  }

  /**
   * Sets the image.
   *
   * @param image the new image
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * Gets the internal port.
   *
   * @return the internal port
   */
  public String getInternalPort() {
    return internalPort;
  }

  /**
   * Sets the internal port.
   *
   * @param internalPort the new internal port
   */
  public void setInternalPort(String internalPort) {
    this.internalPort = internalPort;
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
 * @return the user
 */
public String getUser() {
	return user;
}

/**
 * @param user the user to set
 */
public void setUser(String user) {
	this.user = user;
}

/**
 * @return the userId
 */
public Long getUserId() {
	return userId;
}

/**
 * @param userId the userId to set
 */
public void setUserId(Long userId) {
	this.userId = userId;
}

/**
 * @return the projectId
 */
public Long getProjectId() {
	return projectId;
}

/**
 * @param projectId the projectId to set
 */
public void setProjectId(Long projectId) {
	this.projectId = projectId;
}

@Override
public String toString() {
  return "TrainingEngine [id=" + id + ", name=" + name + ", tools=" + tools + ", platformTool=" + platformTool
      + ", description=" + description + ", imageName=" + imageName + ", image=" + image + ", internalPort="
      + internalPort + ", type=" + type + ", isActive=" + isActive + ", user=" + user + ", userId=" + userId
      + ", projectId=" + projectId + "]";
}
}
