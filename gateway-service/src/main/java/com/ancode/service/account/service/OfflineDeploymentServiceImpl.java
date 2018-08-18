/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.OfflineDeployment;
import com.ancode.service.account.repository.OfflineDeploymentRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class OfflineDeploymentServiceImpl.
 */
@Service
public class OfflineDeploymentServiceImpl implements OfflineDeploymentService {

  /** OfflineDeploymentRepository reference. */
  @Autowired
  private OfflineDeploymentRepository offlineDeploymentRepo;

  /** EnvironmentVariableService reference. */
  @Autowired
  private EnvironmentVariableService environmentVariableService;

  @Override
  public OfflineDeployment save(OfflineDeployment offlineDeployment) throws Exception {
    if (offlineDeployment.getEnvironmentVariables() != null) {
      environmentVariableService.saveEnvironmentVariables(offlineDeployment.getEnvironmentVariables());
    }
    offlineDeployment.setIsActive(true);
    return offlineDeploymentRepo.save(offlineDeployment);
  }

  @Override
  public OfflineDeployment update(OfflineDeployment t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(OfflineDeployment t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public OfflineDeployment find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<OfflineDeployment> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<OfflineDeployment> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
