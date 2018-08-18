/*
 *
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.entity.ModelVersion;
import com.assistanz.slmlp.container.util.service.CRUDService;



/**
 * The Interface ModelVersionService.
 */
@Service
public interface ModelVersionService extends CRUDService<ModelVersion> {

  /**
   * Find all Active ModelVersions.
   *
   * @param isActive
   *          isActive status.
   * @return active dataSet list.
   * @throws Exception
   *           if error occurs.
   */
  List<ModelVersion> findAllByIsActive(Boolean isActive) throws Exception;
  

  /**
   * Find all by is active and model.
   *
   * @param isActive the is active
   * @param modelId the model id
   * @return the list
   * @throws Exception the exception
   */
  List<ModelVersion> findAllByIsActiveAndModel(Boolean isActive, Long modelId) throws Exception;
}
