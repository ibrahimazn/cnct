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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Class SourceRepo.
 */
@Entity
@Table(name = "source_repo")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class SourceRepo implements Serializable {

  /** Id of the SourceRepo. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Repository of the SourceRepo. */
  @Column(name = "repository")
  private Repository repository;

  /** URL of the SourceRepo. */
  @Column(name = "url")
  private String url;

  /** Check whether SourceRepo is in active state or in active state. */
  @Column(name = "is_active")
  private Boolean isActive;

  /**
   * Enumeration name for Git, Bit Bucket.
   */
  public enum Repository {

    /** The git. */
    GIT,

    /** The bit bucket. */
    BIT_BUCKET
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
   * @param id the new id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the repository.
   *
   * @return the repository
   */
  public Repository getRepository() {
    return repository;
  }

  /**
   * Sets the repository.
   *
   * @param repository the new repository
   */
  public void setRepository(Repository repository) {
    this.repository = repository;
  }

  /**
   * Gets the url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the url.
   *
   * @param url the new url
   */
  public void setUrl(String url) {
    this.url = url;
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
