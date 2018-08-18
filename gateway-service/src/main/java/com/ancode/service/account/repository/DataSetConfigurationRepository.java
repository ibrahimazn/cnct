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

import com.ancode.service.account.entity.DataSetConfiguration;
import com.ancode.service.account.entity.DataSetStorageType;

/**
 * The Interface DataSetConfigurationRepository.
 */
public interface DataSetConfigurationRepository extends PagingAndSortingRepository<DataSetConfiguration, Long> {

  /**
   * Find all by isActive.
   * 
   * @param pageable
   *          pageable.
   * @param isActive
   *          true.
   * @return dataSetConfigurationList.
   */
  @Query(value = "SELECT dataSetConfiguration FROM DataSetConfiguration dataSetConfiguration WHERE dataSetConfiguration.isActive = :isActive")
  Page<DataSetConfiguration> findAllByIsActive(Pageable pageable, @Param("isActive") Boolean isActive);

  /**
   * Find dataSetConfiguration by search text.
   *
   * @param pageable
   *          page
   * @param search
   *          search text
   * @param isActive
   *          active status
   * @return dataSetConfiguration list
   */
  @Query(value = "SELECT dataSetConfiguration FROM DataSetConfiguration dataSetConfiguration WHERE dataSetConfiguration.isActive = :isActive AND (dataSetConfiguration.dataSourceUrl LIKE %:search% OR dataSetConfiguration.dataSourceAuthUrl LIKE %:search%)")
  Page<DataSetConfiguration> findBySearchText(Pageable pageable, @Param("search") String search,
      @Param("isActive") Boolean isActive);

  /**
   * Find all dataset storage type by isActive.
   * 
   * @param isActive
   *          true.
   * @return dataset storagetypes.
   */
  @Query(value = "SELECT dataSetStorageType FROM DataSetStorageType dataSetStorageType WHERE dataSetStorageType.isActive = :isActive")
  List<DataSetStorageType> findAllDataSetStorageTypeByIsActive(@Param("isActive") Boolean isActive);

}
