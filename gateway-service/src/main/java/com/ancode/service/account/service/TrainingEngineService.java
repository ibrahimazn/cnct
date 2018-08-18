/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.TrainingEngine;
import com.ancode.service.account.util.service.CRUDService;

/**
 * The Interface TrainingEngineService.
 */
@Service
public interface TrainingEngineService extends CRUDService<TrainingEngine> {

  /**
   * Find all Active TrainingEngines.
   *
   * @param isActive
   *          isActive status.
   * @return active launcher list.
   * @throws Exception
   *           if error occurs.
   */
  List<TrainingEngine> findAllTrainingEnginesByIsActive(Boolean isActive) throws Exception;

  /**
   * Find all Active TrainingEngines.
   *
   * @param isActive
   *          isActive status.
   * @param type
   *          type of engine.
   * @return active launcher list.
   * @throws Exception
   *           if error occurs.
   */
  List<TrainingEngine> findAllTrainingEnginesByIsActiveAndType(Boolean isActive, String type) throws Exception;
  
  /**
   * Find by name and active TrainingEngine.
   *
   * @param name
   *          name.
   * @param isActive
   *          isActive status.
   * @return active TrainingEngine.
   * @throws Exception
   *           if error occurs.
   */
  TrainingEngine findByNameAndIsActive(String name, Boolean isActive) throws Exception;
}
