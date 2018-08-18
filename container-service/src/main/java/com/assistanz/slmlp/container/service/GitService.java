/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * The Interface GitService.
 */
@Service
public interface GitService {

  /**
   * Gets the un commited changes.
   *
   * @param path the path
   * @param repo the repo
   * @param branch the branch
   * @return the un commited changes
   * @throws Exception the exception
   */
  List<String> getUnCommitedChanges(String path, String repo, String branch) throws Exception;

  /**
   * Checks for uncommitted changes.
   *
   * @param path the path
   * @param repo the repo
   * @param branch the branch
   * @return the boolean
   * @throws Exception the exception
   */
  Boolean hasUncommittedChanges(String path, String repo, String branch) throws Exception;

  /**
   * Gets the branches.
   *
   * @param path the path
   * @param repo the repo
   * @return the branches
   * @throws Exception the exception
   */
  List<String> getBranches(String path, String repo) throws Exception;

  /**
   * Gets the status.
   *
   * @param path the path
   * @param repo the repo
   * @param branch the branch
   * @return the status
   * @throws Exception the exception
   */
  HashMap<String, List<String>> getStatus(String path, String repo, String branch) throws Exception;

  /**
   * Gets the logs.
   *
   * @param path the path
   * @param repo the repo
   * @return the logs
   * @throws Exception the exception
   */
  String getLogs(String path, String repo) throws Exception;

  /**
   * Commit the changes.
   *
   * @param path the path
   * @param repo the repo
   * @param branch the branch
   * @param commitMessages the commit messages
   * @return the string
   * @throws Exception the exception
   */
  String commit(String path, String repo, String branch, String commitMessages) throws Exception;

  /**
   * Git add.
   *
   * @param path the path
   * @param repo the repo
   * @param branch the branch
   * @param patterns the patterns
   * @param rmpatterns the rmpatterns
   * @return the string
   * @throws Exception the exception
   */
  String gitAdd(String path, String repo, String branch, List<String> patterns, List<String> rmpatterns)
      throws Exception;

  /**
   * Git push the changes of commits.
   *
   * @param path the path
   * @param repo the repo
   * @param upstream the upstream
   * @param branch the branch
   * @return the string
   * @throws Exception the exception
   */
  String gitPush(String path, String repo, String upstream, String branch) throws Exception;

  /**
   * Git pull the code.
   *
   * @param path the path
   * @param repo the repo
   * @param upstream the upstream
   * @param branch the branch
   * @return the string
   * @throws Exception the exception
   */
  String gitPull(String path, String repo, String upstream, String branch) throws Exception;

  /**
   * Git clone the project.
   *
   * @param path the path
   * @param repo the repo
   * @return the string
   * @throws Exception the exception
   */
  String gitClone(String path, String repo) throws Exception;

  /**
   * Lfs install.
   *
   * @param node the node
   * @param pvc the pvc
   * @param path the path
   * @param pUuid the uuid
   * @throws Exception the exception
   */
  void lfsInstall(String node, String pvc, String path, String pUuid) throws Exception;

  /**
   * Lfs track.
   *
   * @param node the node
   * @param pvc the pvc
   * @param path the path
   * @param pUuid the uuid
   * @throws Exception the exception
   */
  void lfsTrack(String node, String pvc, String path, String pUuid) throws Exception;
}
