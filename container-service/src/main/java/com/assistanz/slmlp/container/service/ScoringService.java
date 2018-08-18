/*
 *
 */
package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.slmlp.container.entity.Scoring;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;


/**
 * The Interface DataSetService.
 */
@Service
public interface ScoringService extends CRUDService<Scoring> {

  /**
   * Find all Active Scorings.
   *
   * @param isActive
   *          isActive status.
   * @return active dataSet list.
   * @throws Exception
   *           if error occurs.
   */
  List<Scoring> findAllByIsActive(Boolean isActive) throws Exception;

  /**
   * Soft deleting by selected scoring.
   *
   * @param scoring
   *          the scoring
   * @return deleted scoring.
   * @throws Exception
   *           if error occurs.
   */
  Scoring softDelete(Scoring scoring) throws Exception;

  /**
   * Run score by selected scoring.
   *
   * @param scoring
   *          the scoring
   * @return run scoring.
   * @throws Exception
   *           if error occurs.
   */
  Scoring runScore(Scoring scoring) throws Exception;

  /**
   * Save the list of Scoring.
   *
   * @param dataSets
   *          dataSets.
   * @param id
   *          the id
   * @throws Exception
   *           if error occurs.
   */
  void savePredictions(List<Scoring> scorings, Long id) throws Exception;

  /**
   * Find all Active Scorings lists with Pagination.
   *
   * @param pagingAndSorting
   *          pagination and sorting
   * @param projectId
   *          the project id
   * @param isActive
   *          isActive status
   * @return active Scoring list
   * @throws Exception
   *           if error occurs
   */
  Page<Scoring> findAllByIsActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive) throws Exception;

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
  Page<Scoring> findAllBySearchText(PagingAndSorting pagingAndSorting, Long projectId, String searchText)
      throws Exception;


  /**
   *
   * @param modelId
   * @param isActive
   * @return
   * @throws Exception
   */
  Scoring findAllByModelId(Long modelId, Boolean isActive) throws Exception;
  
  /**
   * Delete the scorings by project ID.
   * 
   * @param projectId the projectId.
   * @throws Exception if error occurs.
   */
  void deleteByProject(Long projectId) throws Exception;
  
  /**
   * 
   * @param isActive
   * @param projectId
   * @return
   * @throws Exception
   */
  List<Scoring> findAllByIsActiveAndProject(Boolean isActive, Long projectId) throws Exception;

}
