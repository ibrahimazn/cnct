/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.TrainingEngine;
import com.ancode.service.account.repository.TrainingEngineRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class TrainingEngineServiceImpl.
 */
@Service
public class TrainingEngineServiceImpl implements TrainingEngineService {

  /** TrainingEngineRepo reference. */
  @Autowired
  private TrainingEngineRepository trainingEngineRepo;

  @Override
  public TrainingEngine save(TrainingEngine t) throws Exception {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
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
  public List<TrainingEngine> findAllTrainingEnginesByIsActiveAndType(Boolean isActive, String type) throws Exception {
    return trainingEngineRepo.findAllTrainingEnginesByIsActiveAndType(isActive, type);
  }

}
