/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.DataSetConfiguration;
import com.ancode.service.account.entity.DataSetStorageType;
import com.ancode.service.account.util.service.CRUDService;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Interface DataSetConfigurationService.
 */
@Service
public interface DataSetConfigurationService extends CRUDService<DataSetConfiguration> {

  /**
   * Find all Active dataSetConfiguration lists with Pagination.
   *
   * @param pagingAndSorting
   *          pagination and sorting
   * @param isActive
   *          isActive status
   * @return active dataSetConfiguration list
   * @throws Exception
   *           if error occurs
   */
  Page<DataSetConfiguration> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception;

  /**
   * Find all dataSetConfiguration list by user and search text.
   *
   * @param pagingAndSorting
   *          paging and sorting
   * @param searchText
   *          search text
   * @return dataSetConfiguration list
   * @throws Exception
   *           if error occurs
   */
  Page<DataSetConfiguration> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception;

  /**
   * Find all dataset storagetype by is active.
   *
   * @param isActive
   *          true/false.
   * @return list of dataset storagetype.
   * @throws Exception
   *           if error occurs.
   */
  List<DataSetStorageType> findAllDataSetStorageTypeByIsActive(Boolean isActive) throws Exception;

}
