/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.ProjectUser;
import com.ancode.service.account.util.service.CRUDService;

/**
 * The Interface ProjectUserService.
 */
@Service
public interface ProjectUserService extends CRUDService<ProjectUser> {

  /**
   * Save project users.
   *
   * @param projectUsers
   *          the project users
   * @param projectId
   *          the project id
   * @throws Exception
   *           the exception
   */
  void saveProjectUsers(List<ProjectUser> projectUsers, Long projectId) throws Exception;

  /**
   * Find all Project Users by userId.
   *
   * @param userId
   *          userId status.
   * @return active projectUser list.
   * @throws Exception
   *           if error occurs.
   */
  List<ProjectUser> findAllProjectUsersByUserId(Long userId) throws Exception;
}
