/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import com.assistanz.slmlp.container.repository.TrainingEngineRepository;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.assistanz.slmlp.container.entity.TrainingEngine;

@Service
public class TrainingEngineServiceImpl implements TrainingEngineService {

  /** TrainingEngineRepo reference. */
  @Autowired
  private TrainingEngineRepository trainingEngineRepo;

  @Override
  public TrainingEngine save(TrainingEngine trainingEngine) throws Exception {
    if (findByNameAndIsActive(trainingEngine.getName(), true) != null) {
      throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
          trainingEngine.getName() + " training engine is already exist");
    }
    // TODO Auto-generated method stub
    return trainingEngineRepo.save(trainingEngine);
  }

  @Override
  public TrainingEngine update(TrainingEngine t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(TrainingEngine t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public TrainingEngine find(Long id) throws Exception {
    return trainingEngineRepo.findOne(id);
  }

  @Override
  public Page<TrainingEngine> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<TrainingEngine> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<TrainingEngine> findAllTrainingEnginesByIsActive(Boolean isActive) throws Exception {
    return trainingEngineRepo.findAllTrainingEnginesByIsActive(isActive);
  }

  @Override
  public TrainingEngine findByNameAndIsActive(String name, Boolean isActive) throws Exception {
    return trainingEngineRepo.findByNameAndIsActive(name, isActive);
  }

  @Override
  public TrainingEngine findByTypeAndIsActive(String type, Boolean isActive, String platformTool) throws Exception {
    return trainingEngineRepo.findByTypeAndIsActive(type, isActive, platformTool);
  }

}
