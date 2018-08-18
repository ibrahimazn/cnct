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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class OfflineDeployment.
 */
@Entity
@Table(name = "offline_deployment")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class OfflineDeployment implements Serializable {

  /** Id of the OfflineDeployment. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** The platform tools. */
  @ManyToMany
  private List<PlatformTools> platformTools;

  /** Offline Deployment- Environment Variables. */
  @JoinColumn(name = "environment_variable_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @OneToMany
  private List<EnvironmentVariable> environmentVariables;

  /**
   * Check whether EnvironmentVariable is in active state or in active state.
   */
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
   * Gets the platform tools.
   *
   * @return the platform tools
   */
  public List<PlatformTools> getPlatformTools() {
    return platformTools;
  }

  /**
   * Sets the platform tools.
   *
   * @param platformTools the new platform tools
   */
  public void setPlatformTools(List<PlatformTools> platformTools) {
    this.platformTools = platformTools;
  }

  /**
   * Gets the environment variables.
   *
   * @return the environment variables
   */
  public List<EnvironmentVariable> getEnvironmentVariables() {
    return environmentVariables;
  }

  /**
   * Sets the environment variables.
   *
   * @param environmentVariables the new environment variables
   */
  public void setEnvironmentVariables(List<EnvironmentVariable> environmentVariables) {
    this.environmentVariables = environmentVariables;
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
