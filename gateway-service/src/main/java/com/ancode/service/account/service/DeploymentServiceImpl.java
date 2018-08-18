/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.Deployment;
import com.ancode.service.account.entity.User;
import com.ancode.service.account.repository.DeploymentRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class DeploymentServiceImpl.
 */
@Service
public class DeploymentServiceImpl implements DeploymentService {

  /** The user service. */
  @Autowired
  private UserService userService;

  /** The deployment repo. */
  @Autowired
  private DeploymentRepository deploymentRepo;

  @Override
  public Deployment save(Deployment t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Deployment update(Deployment t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(Deployment t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public Deployment find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<Deployment> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Deployment> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<Deployment> findAllByUserProjectAndIsActive(PagingAndSorting pagingAndSorting, Long projectId,
      Boolean isActive, String type) throws Exception {
    User user = userService.getCurrentLoggedInUser();
    return deploymentRepo.findAllByUserProjectAndIsActive(pagingAndSorting.toPageRequest(), projectId, isActive,
        user.getId(), type);
  }
}
