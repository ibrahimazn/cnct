/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.DataSet;
import com.ancode.service.account.entity.DataSource;

/**
 * The Interface DataSetRepository.
 */
public interface DataSetRepository extends PagingAndSortingRepository<DataSet, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return dataSetList.
   */
  @Query(value = "SELECT dataSet FROM DataSet dataSet WHERE dataSet.isActive = :isActive")
  List<DataSet> findAllByIsActive(@Param("isActive") Boolean isActive);

  /**
   * Find all by isActive.
   *
   * @param pageable
   *          pageable.
   * @param projectId
   *          the project id
   * @param isActive
   *          true.
   * @return dataSetList.
   */
  @Query(value = "SELECT dataSet FROM DataSet dataSet WHERE dataSet.projectId = :projectId AND dataSet.isActive = :isActive")
  Page<DataSet> findAllByIsActive(Pageable pageable, @Param("projectId") Long projectId,
      @Param("isActive") Boolean isActive);

  /**
   * Find dataSet by search text.
   *
   * @param pageable
   *          page
   * @param projectId
   *          the project id
   * @param search
   *          search text
   * @param isActive
   *          active status
   * @return dataSet list
   */
  @Query(value = "SELECT dataSet FROM DataSet dataSet WHERE dataSet.projectId = :projectId AND dataSet.isActive = :isActive AND (dataSet.name LIKE %:search%)")
  Page<DataSet> findBySearchText(Pageable pageable, @Param("projectId") Long projectId, @Param("search") String search,
      @Param("isActive") Boolean isActive);

  /**
   * Find by name of the dataset..
   *
   * @param projectId
   *          the project id
   * @param name
   *          of dataset.
   * @param isActive
   *          get the dataset list based on active/inactive status.
   * @return dataset.
   */
  @Query(value = "SELECT dataset FROM DataSet dataset WHERE dataset.projectId = :projectId AND dataset.name = :name AND dataset.isActive = :isActive")
  DataSet findByNameAndIsActive(@Param("projectId") Long projectId, @Param("name") String name,
      @Param("isActive") Boolean isActive);

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return datasourceList.
   */
  @Query(value = "SELECT dataSource FROM DataSource dataSource WHERE dataSource.isActive = :isActive")
  List<DataSource> findAllDataSourceByIsActive(@Param("isActive") Boolean isActive);

}
