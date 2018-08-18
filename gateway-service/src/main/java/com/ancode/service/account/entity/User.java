/*
 *
 */
package com.ancode.service.account.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** Entity reference for the User. */
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class User implements Serializable {

  /** Id of the User. */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id")
  private Long id;

  /** Department of the user. */
  @JoinColumn(name = "department_id", referencedColumnName = "Id", updatable = false, insertable = false)
  @ManyToOne
  private Department department;

  /** Department id of the user. */
  @Column(name = "department_id")
  private Long departmentId;

  /** Email id of the User. */
  @Column(name = "email")
  @Email(message = "*Please provide a valid Email")
  private String email;

  /** Password of the User. */
  @Column(name = "password")
  @Length(min = 5, message = "*Your password must have at least 5 characters")
  @Transient
  private String password;

  /** First name of the User. */
  @Column(name = "first_name")
  private String firstName;

  /** Last Name of the User. */
  @Column(name = "last_name")
  private String lastName;

  /** User Name of the User. */
  @Column(name = "user_name", nullable = false)
  private String userName;

  /** Active Status of the User. */
  @Column(name = "active")
  private int active;

  /** Role object of the User. */
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles;

  /** Primary phone of the User. */
  @Column(name = "primary_phone")
  private String phone;

  /** Type of the user. */
  @Column(name = "user_type")
  private UserType userType;

  /** File path for profile image. */
  @Column(name = "profile_img_path")
  private String profileImgPath;

  /** File name for profile image. */
  @Column(name = "profile_img_file")
  private String profileImgFile;
  
  /** URL of the Grafana. **/
  @Transient
  private String grafanaUrl;
  
  /**
   * Set the firstName.
   *
   * @param firstName
   *          the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Get the id.
   *
   * @return the id
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
   * Gets the department.
   *
   * @return the department
   */
  public Department getDepartment() {
    return department;
  }

  /**
   * Sets the department.
   *
   * @param department
   *          the new department
   */
  public void setDepartment(Department department) {
    this.department = department;
  }

  /**
   * Gets the department id.
   *
   * @return the department id
   */
  public Long getDepartmentId() {
    return departmentId;
  }

  /**
   * Sets the department id.
   *
   * @param departmentId
   *          the new department id
   */
  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }

  /**
   * Get the email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the email.
   *
   * @param email
   *          the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Get the password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the password.
   *
   * @param password
   *          the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Get the first name.
   *
   * @return the firstname
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Set the first name.
   *
   * @param firstName
   *          the name to set
   */
  public void setName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Get the lastName.
   *
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Set the lastName.
   *
   * @param lastName
   *          the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Get the active.
   *
   * @return the active
   */
  public int getActive() {
    return active;
  }

  /**
   * Set the active.
   *
   * @param active
   *          the active to set
   */
  public void setActive(int active) {
    this.active = active;
  }

  /**
   * Get the roles.
   *
   * @return the roles
   */
  public List<Role> getRoles() {
    return roles;
  }

  /**
   * Set the roles.
   *
   * @param roles
   *          the roles to set
   */
  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  /**
   * Get the userName.
   *
   * @return the userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Set the userName.
   *
   * @param userName
   *          the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Get the phone.
   *
   * @return the phone
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Set the phone.
   *
   * @param phone
   *          the phone to set
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Gets the profile img path.
   *
   * @return the profile img path
   */
  public String getProfileImgPath() {
    return profileImgPath;
  }

  /**
   * Sets the profile img path.
   *
   * @param profileImgPath
   *          the new profile img path
   */
  public void setProfileImgPath(String profileImgPath) {
    this.profileImgPath = profileImgPath;
  }

  /**
   * Gets the profile img file.
   *
   * @return the profile img file
   */
  public String getProfileImgFile() {
    return profileImgFile;
  }

  /**
   * Sets the profile img file.
   *
   * @param profileImgFile
   *          the new profile img file
   */
  public void setProfileImgFile(String profileImgFile) {
    this.profileImgFile = profileImgFile;
  }

  /**
   * Get the userType.
   *
   * @return the userType
   */
  public UserType getUserType() {
    return userType;
  }

  /**
   * Set the userType.
   *
   * @param userType
   *          the userType to set
   */
  public void setUserType(UserType userType) {
    this.userType = userType;
  }

  /**
   * @return the grafanaUrl
   */
  public String getGrafanaUrl() {
    return grafanaUrl;
  }

  /**
   * @param grafanaUrl the grafanaUrl to set
   */
  public void setGrafanaUrl(String grafanaUrl) {
    this.grafanaUrl = grafanaUrl;
  }


  /**
   * Enumeration user type for admin, data scientist, data engineer.
   */
  public enum UserType {

    /** The admin. */
    ADMIN,

    /** The user. */
    USER,

    /** The qa. */
    QA,

    /** The analyzer. */
    ANALYZER,

    /** The data scientist. */
    DATA_SCIENTIST
  }
}
