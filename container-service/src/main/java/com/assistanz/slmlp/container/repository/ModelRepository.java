/*
 * 
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.Model;

/**
 * The Interface Model Repository.
 */
public interface ModelRepository extends PagingAndSortingRepository<Model, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return modelList.
   */
  @Query(value = "SELECT model FROM Model model WHERE model.isActive = :isActive")
  List<Model> findAllByIsActive(@Param("isActive") Boolean isActive);
  
  
  /**
   * @param isActive
   * @return
   */
  @Query(value = "SELECT model FROM Model model WHERE model.isActive = :isActive AND model.projectId = :projectId")
  List<Model> findAllByIsActiveAndProject(@Param("isActive") Boolean isActive, @Param("projectId") Long projectId);

  /**
   * Find all by isActive.
   *
   * @param pageable
   *          pageable.
   * @param projectId
   *          the project id
   * @param isActive
   *          true.
   * @return modelList.
   */
  @Query(value = "SELECT model FROM Model model WHERE model.projectId = :projectId AND model.isActive = :isActive")
  Page<Model> findAllByIsActive(Pageable pageable, @Param("projectId") Long projectId,
      @Param("isActive") Boolean isActive);

  /**
   * Find model by search text.
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
  @Query(value = "SELECT model FROM Model model WHERE model.projectId = :projectId AND model.isActive = :isActive AND (model.name LIKE %:search%)")
  Page<Model> findBySearchText(Pageable pageable, @Param("projectId") Long projectId, @Param("search") String search,
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
  @Query(value = "SELECT model FROM Model model WHERE model.projectId = :projectId AND model.name = :name AND model.isActive = :isActive")
  Model findByNameAndIsActive(@Param("projectId") Long projectId, @Param("name") String name,
      @Param("isActive") Boolean isActive);

}
