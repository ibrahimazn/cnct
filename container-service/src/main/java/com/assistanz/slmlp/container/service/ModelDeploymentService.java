/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.entity.ModelDeployment;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

import az.ancode.filemanager.connector.fileset.CsvFileResponse;

/**
 * Container service interface is for declaring CRUDService and declared some
 * required service functions.
 */
@Service
public interface ModelDeploymentService extends CRUDService<ModelDeployment> {

  /**
   * Get the paginated active records.
   *
   * @param pagingAndSorting
   * @param isActive
   * @return
   * @throws Exception
   */
  Page<ModelDeployment> findAllByActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception;

  /**
   * Get the paginated active records based on the given project.
   *
   * @param pagingAndSorting
   * @param projectId
   * @param isActive
   * @return
   * @throws Exception
   */
  Page<ModelDeployment> findAllPaginatedByProjectActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive) throws Exception;


  /**
   * Find all by project and active.
   *
   * @param projectId
   * @param isActive
   * @return
   * @throws Exception
   */
  List<ModelDeployment> findAllByProjectActive(Long projectId, Boolean isActive) throws Exception;

  /**
   * Soft delete the deployment.
   *
   * @param id
   * @param userName
   * @throws Exceptionl
   */
  void deleteDeployment(Long id, String userName) throws Exception;

  /**
   * Get predicted data.
   *
   * @param deploymentId
   * @param userName
   * @return
   * @throws Exception
   */
  ModelDeployment getPredictedData(Long deploymentId,  String userName) throws Exception;
}
