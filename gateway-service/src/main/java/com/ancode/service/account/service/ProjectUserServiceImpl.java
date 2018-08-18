/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.ProjectUser;
import com.ancode.service.account.repository.ProjectUserRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class ProjectUserServiceImpl.
 */
@Service
public class ProjectUserServiceImpl implements ProjectUserService {

  /** ProjectUserRepository reference. */
  @Autowired
  private ProjectUserRepository projectUserRepository;

  @Override
  public ProjectUser save(ProjectUser projectUser) throws Exception {
    return projectUserRepository.save(projectUser);
  }

  @Override
  public ProjectUser update(ProjectUser projectUser) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(ProjectUser projectUser) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public ProjectUser find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<ProjectUser> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<ProjectUser> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void saveProjectUsers(List<ProjectUser> projectUsers, Long projectId) throws Exception {
    for (ProjectUser projectUser : projectUsers) {
      if (projectId != null) {
        projectUser.setProjectId(projectId);
      }
      save(projectUser);
    }
  }

  @Override
  public List<ProjectUser> findAllProjectUsersByUserId(Long userId) throws Exception {
    return projectUserRepository.findAllByUserId(userId);
  }
}
