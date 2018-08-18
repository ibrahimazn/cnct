/*
 *
 */
package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;


/**
 * The Interface ModelService.
 */
@Service
public interface ModelService extends CRUDService<Model> {

  /**
   * Find all Active Models.
   *
   * @param isActive
   *          isActive status.
   * @return active dataSet list.
   * @throws Exception
   *           if error occurs.
   */
  List<Model> findAllByIsActive(Boolean isActive) throws Exception;
  
  /**
   * Find all Active Models And project.
   *
   * @param isActive
   *          isActive status.
   * @return active dataSet list.
   * @throws Exception
   *           if error occurs.
   */
  List<Model> findAllByIsActiveAndProject(Boolean isActive, Long projectId) throws Exception;


  /**
   * Soft deleting by selected model.
   *
   * @param model
   *          the model
   * @return deleted model.
   * @throws Exception
   *           if error occurs.
   */
  Model softDelete(Model model) throws Exception;
  
  /**
   * Delete the models by project ID.
   * 
   * @param projectId the projectId.
   * @throws Exception if error occurs.
   */
  void deleteByProject(Long projectId) throws Exception;

  /**
   * Save the list of model.
   *
   * @param dataSets
   *          dataSets.
   * @param id
   *          the id
   * @throws Exception
   *           if error occurs.
   */
  void saveModels(List<Model> models, Long id) throws Exception;
  
  /**
   * Save model.
   *
   * @param model the model
   * @return the model
   * @throws Exception the exception
   */
  Model saveModel(Model model)  throws Exception;

  /**
   * Find all Active models lists with Pagination.
   *
   * @param pagingAndSorting
   *          pagination and sorting
   * @param projectId
   *          the project id
   * @param isActive
   *          isActive status
   * @return active Model list
   * @throws Exception
   *           if error occurs
   */
  Page<Model> findAllByIsActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive) throws Exception;

  /**
   * Find all DataSet list by user and search text.
   *
   * @param pagingAndSorting
   *          paging and sorting
   * @param projectId
   *          the project id
   * @param searchText
   *          search text
   * @return DataSet list
   * @throws Exception
   *           if error occurs
   */
  Page<Model> findAllBySearchText(PagingAndSorting pagingAndSorting, Long projectId, String searchText)
      throws Exception;

}
