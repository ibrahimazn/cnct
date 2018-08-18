/*
 * 
 */
package com.ancode.service.account.util;

import java.util.List;

/**
 * The Class GitActions.
 */
public class GitActions {

  /** The node. */
  private String node;

  /** The pod uuid. */
  private String podUuid;

  /** The pvc. */
  private String pvc;

  /** The path. */
  private String path;

  /** The branch. */
  private String branch;

  /** The project id. */
  private Long projectId;

  /** The project name. */
  private String projectName;

  /** The dataset name. */
  private String datasetName;
  
  /** The user name. */
  private String userName;

  /** The remote url. */
  private String remoteUrl;

  /** The src url. */
  private String srcUrl;

  /** The commit message. */
  private String commitMessage;

  /** The pattern files. */
  private List<String> patternFiles;

  /** The rmpattern files. */
  private List<String> rmpatternFiles;

  /**
   * Gets the node.
   *
   * @return the node
   */
  public String getNode() {
    return node;
  }

  /**
   * Sets the node.
   *
   * @param node
   *          the new node
   */
  public void setNode(String node) {
    this.node = node;
  }

  /**
   * Gets the pod uuid.
   *
   * @return the pod uuid
   */
  public String getPodUuid() {
    return podUuid;
  }

  /**
   * Sets the pod uuid.
   *
   * @param podUuid
   *          the new pod uuid
   */
  public void setPodUuid(String podUuid) {
    this.podUuid = podUuid;
  }

  /**
   * Gets the pvc.
   *
   * @return the pvc
   */
  public String getPvc() {
    return pvc;
  }

  /**
   * Sets the pvc.
   *
   * @param pvc
   *          the new pvc
   */
  public void setPvc(String pvc) {
    this.pvc = pvc;
  }

  /**
   * Gets the path.
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets the path.
   *
   * @param path
   *          the new path
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Gets the pattern files.
   *
   * @return the pattern files
   */
  public List<String> getPatternFiles() {
    return patternFiles;
  }

  /**
   * Sets the pattern files.
   *
   * @param patternFiles
   *          the new pattern files
   */
  public void setPatternFiles(List<String> patternFiles) {
    this.patternFiles = patternFiles;
  }

  /**
   * Gets the commit message.
   *
   * @return the commit message
   */
  public String getCommitMessage() {
    return commitMessage;
  }

  /**
   * Sets the commit message.
   *
   * @param commitMessage
   *          the new commit message
   */
  public void setCommitMessage(String commitMessage) {
    this.commitMessage = commitMessage;
  }

  /**
   * Gets the branch.
   *
   * @return the branch
   */
  public String getBranch() {
    return branch;
  }

  /**
   * Sets the branch.
   *
   * @param branch
   *          the new branch
   */
  public void setBranch(String branch) {
    this.branch = branch;
  }

  /**
   * Gets the rmpattern files.
   *
   * @return the rmpattern files
   */
  public List<String> getRmpatternFiles() {
    return rmpatternFiles;
  }

  /**
   * Sets the rmpattern files.
   *
   * @param rmpatternFiles
   *          the new rmpattern files
   */
  public void setRmpatternFiles(List<String> rmpatternFiles) {
    this.rmpatternFiles = rmpatternFiles;
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
   * Gets the project name.
   *
   * @return the project name
   */
  public String getProjectName() {
    return projectName;
  }

  /**
   * Sets the project name.
   *
   * @param projectName
   *          the new project name
   */
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * Gets the remote url.
   *
   * @return the remote url
   */
  public String getRemoteUrl() {
    return remoteUrl;
  }

  /**
   * Sets the remote url.
   *
   * @param remoteUrl
   *          the new remote url
   */
  public void setRemoteUrl(String remoteUrl) {
    this.remoteUrl = remoteUrl;
  }

  /**
   * Gets the src url.
   *
   * @return the src url
   */
  public String getSrcUrl() {
    return srcUrl;
  }

  /**
   * Sets the src url.
   *
   * @param srcUrl
   *          the new src url
   */
  public void setSrcUrl(String srcUrl) {
    this.srcUrl = srcUrl;
  }

  /**
   * Gets the dataset name.
   *
   * @return the dataset name
   */
  public String getDatasetName() {
    return datasetName;
  }

  /**
   * Sets the dataset name.
   *
   * @param datasetName
   *          the new dataset name
   */
  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
