/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.util;

import java.util.List;

public class GitActions {

  private String node;

  private String podUuid;

  private String pvc;

  private String path;

  private String branch;

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

  private String commitMessage;

  private List<String> patternFiles;

  private List<String> rmpatternFiles;

  private String fileUrl;

  public String getNode() {
    return node;
  }

  public void setNode(String node) {
    this.node = node;
  }

  public String getPodUuid() {
    return podUuid;
  }

  public void setPodUuid(String podUuid) {
    this.podUuid = podUuid;
  }

  public String getPvc() {
    return pvc;
  }

  public void setPvc(String pvc) {
    this.pvc = pvc;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<String> getPatternFiles() {
    return patternFiles;
  }

  public void setPatternFiles(List<String> patternFiles) {
    this.patternFiles = patternFiles;
  }

  public String getCommitMessage() {
    return commitMessage;
  }

  public void setCommitMessage(String commitMessage) {
    this.commitMessage = commitMessage;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public List<String> getRmpatternFiles() {
    return rmpatternFiles;
  }

  public void setRmpatternFiles(List<String> rmpatternFiles) {
    this.rmpatternFiles = rmpatternFiles;
  }

  public Long getProjectId() {
    return projectId;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  /**
   * @return the fileUrl
   */
  public String getFileUrl() {
    return fileUrl;
  }

  /**
   * @param fileUrl
   *          the fileUrl to set
   */
  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public String getRemoteUrl() {
    return remoteUrl;
  }

  public void setRemoteUrl(String remoteUrl) {
    this.remoteUrl = remoteUrl;
  }

  public String getSrcUrl() {
    return srcUrl;
  }

  public void setSrcUrl(String srcUrl) {
    this.srcUrl = srcUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
