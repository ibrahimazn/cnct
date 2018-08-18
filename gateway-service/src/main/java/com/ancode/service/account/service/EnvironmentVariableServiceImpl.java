/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.EnvironmentVariable;
import com.ancode.service.account.repository.EnvironmentVariableRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class EnvironmentVariableServiceImpl.
 */
@Service
public class EnvironmentVariableServiceImpl implements EnvironmentVariableService {

  /** EnvironmentVariable Repository reference. */
  @Autowired
  private EnvironmentVariableRepository environmentVariableRepository;

  @Override
  public EnvironmentVariable save(EnvironmentVariable environmentVariable) throws Exception {
    environmentVariable.setIsActive(true);
    return environmentVariableRepository.save(environmentVariable);
  }

  @Override
  public EnvironmentVariable update(EnvironmentVariable environmentVariable) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(EnvironmentVariable environmentVariable) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public EnvironmentVariable find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<EnvironmentVariable> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<EnvironmentVariable> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void saveEnvironmentVariables(List<EnvironmentVariable> environmentVariableList) throws Exception {
    for (EnvironmentVariable environmentVariable : environmentVariableList) {
      save(environmentVariable);
    }
  }

}
