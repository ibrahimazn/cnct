/*
 * 
 */
package com.ancode.service.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** Entity reference for the ProjectUser. */
@Entity
@Table(name = "project_user")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class ProjectUser {

  /** Id of the ProjectUser. */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  /** Id of the project. */
  @Column(name = "project_id")
  private Long projectId;

  /** Id of the user. */
  @Column(name = "user_id")
  private Long userId;

  /** Id of the role. */
  @Column(name = "role_id")
  private Long roleId;

  /** User Name of the User. */
  @Column(name = "user_name", nullable = false)
  private String userName;

  /** Role Name of the User. */
  @Column(name = "role_name", nullable = false)
  private String roleName;

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
   * @param projectId
   *          the new project id
   */
  public void setProjectId(Long projectId) {
    this.projectId = projectId;
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

  /**
   * Gets the role id.
   *
   * @return the role id
   */
  public Long getRoleId() {
    return roleId;
  }

  /**
   * Sets the role id.
   *
   * @param roleId
   *          the new role id
   */
  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  /**
   * Gets the user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Sets the user name.
   *
   * @param userName
   *          the new user name
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Gets the role name.
   *
   * @return the role name
   */
  public String getRoleName() {
    return roleName;
  }

  /**
   * Sets the role name.
   *
   * @param roleName
   *          the new role name
   */
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

}
