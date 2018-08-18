/*
 * 
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.assistanz.slmlp.container.entity.ModelVersion;

/**
 * The Interface ModelVersionRepository.
 */
public interface ModelVersionRepository extends PagingAndSortingRepository<ModelVersion, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return modelList.
   */
  @Query(value = "SELECT model FROM ModelVersion model WHERE model.isActive = :isActive ORDER BY model.id desc")
  List<ModelVersion> findAllByIsActive(@Param("isActive") Boolean isActive);
  
  /**
   * Find all by is active and model.
   *
   * @param isActive the is active
   * @param modelId the model id
   * @return the list
   */
  @Query(value = "SELECT model FROM ModelVersion model WHERE model.isActive = :isActive AND model.modelId = :modelId ORDER BY model.id desc")
  List<ModelVersion> findAllByIsActiveAndModel(@Param("isActive") Boolean isActive, @Param("modelId") Long modelId);

}
