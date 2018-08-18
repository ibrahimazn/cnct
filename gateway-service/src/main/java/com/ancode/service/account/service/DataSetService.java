/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.DataSet;
import com.ancode.service.account.entity.DataSource;
import com.ancode.service.account.util.service.CRUDService;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Interface DataSetService.
 */
@Service
public interface DataSetService extends CRUDService<DataSet> {

  /**
   * Find all Active DataSets.
   *
   * @param isActive
   *          isActive status.
   * @return active dataSet list.
   * @throws Exception
   *           if error occurs.
   */
  List<DataSet> findAllByIsActive(Boolean isActive) throws Exception;

  /**
   * Soft deleting by selected dataset.
   *
   * @param dataSet
   *          the data set
   * @return deleted DataSet.
   * @throws Exception
   *           if error occurs.
   */
  DataSet softDelete(DataSet dataSet) throws Exception;

  /**
   * Save the list of datasets.
   *
   * @param dataSets
   *          dataSets.
   * @param id
   *          the id
   * @throws Exception
   *           if error occurs.
   */
  void saveDataSets(List<DataSet> dataSets, Long id) throws Exception;

  /**
   * Find all Active DataSet lists with Pagination.
   *
   * @param pagingAndSorting
   *          pagination and sorting
   * @param projectId
   *          the project id
   * @param isActive
   *          isActive status
   * @return active DataSet list
   * @throws Exception
   *           if error occurs
   */
  Page<DataSet> findAllByIsActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive) throws Exception;

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
  Page<DataSet> findAllBySearchText(PagingAndSorting pagingAndSorting, Long projectId, String searchText)
      throws Exception;

  /**
   * Find all data source by is active.
   *
   * @param isActive
   *          true/false.
   * @return list of datasources.
   * @throws Exception
   *           if error occurs.
   */
  List<DataSource> findAllDataSourceByIsActive(Boolean isActive) throws Exception;

}
