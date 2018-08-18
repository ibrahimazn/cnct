/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.EnvironmentVariable;
import com.ancode.service.account.util.service.CRUDService;

/**
 * The Interface EnvironmentVariableService.
 */
@Service
public interface EnvironmentVariableService extends CRUDService<EnvironmentVariable> {

  /**
   * Save list of EnvironmenrVariables.
   *
   * @param environmentVariableList
   *          environmentVariableList.
   * @throws Exception
   *           if error occurs.
   */
  void saveEnvironmentVariables(List<EnvironmentVariable> environmentVariableList) throws Exception;
}
