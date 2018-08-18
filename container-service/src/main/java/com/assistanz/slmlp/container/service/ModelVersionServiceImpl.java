/*
 *
 */
package com.assistanz.slmlp.container.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.entity.Model.TYPE;
import com.assistanz.slmlp.container.entity.ModelVersion;
import com.assistanz.slmlp.container.entity.Sampling;
import com.assistanz.slmlp.container.error.exception.ApplicationException;
import com.assistanz.slmlp.container.repository.ModelRepository;
import com.assistanz.slmlp.container.repository.ModelVersionRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.error.Errors;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

/**
 * The Class ModelService Implementation.
 */
@Service
public class ModelVersionServiceImpl implements ModelVersionService {

  /** The model version repository. */
  @Autowired
  private ModelVersionRepository modelVersionRepository;

  @Override
  public ModelVersion save(ModelVersion modelVersion) throws Exception {
    return modelVersionRepository.save(modelVersion);
  }

  @Override
  public ModelVersion update(ModelVersion modelVersion) throws Exception {
    return  modelVersionRepository.save(modelVersion);
  }

  @Override
  public void delete(ModelVersion modelVersion) throws Exception {
    modelVersionRepository.delete(modelVersion);
  }

  @Override
  public void delete(Long id) throws Exception {
    modelVersionRepository.delete(id);
  }

  @Override
  public ModelVersion find(Long id) throws Exception {
    return modelVersionRepository.findOne(id);
  }

  @Override
  public Page<ModelVersion> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return modelVersionRepository.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<ModelVersion> findAll() throws Exception {
    return (List<ModelVersion>) modelVersionRepository.findAll();
  }

  @Override
  public List<ModelVersion> findAllByIsActive(Boolean isActive) throws Exception {
    return modelVersionRepository.findAllByIsActive(isActive);
  }

  @Override
  public List<ModelVersion> findAllByIsActiveAndModel(Boolean isActive, Long modelId) throws Exception {
    return modelVersionRepository.findAllByIsActiveAndModel(isActive, modelId);
  }
  
}
