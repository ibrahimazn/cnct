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

import com.ancode.service.account.entity.Deployment;

/**
 * The Interface DeploymentRepository.
 */
public interface DeploymentRepository extends PagingAndSortingRepository<Deployment, Long> {

  /**
   * Find all by user project and is active.
   *
   * @param pageable
   *          the pageable
   * @param projectId
   *          the project id
   * @param isActive
   *          the is active
   * @param userId
   *          the user id
   * @param type
   *          the type
   * @return the page
   */
  @Query(value = "SELECT deployment FROM Deployment deployment " + " WHERE deployment.isActive = :isActive "
      + " AND deployment.projectId = :projectId " + " AND deployment.userId = :userId " + " AND deployment.type = :type"
      + " AND deployment.isActive = :isActive")
  Page<Deployment> findAllByUserProjectAndIsActive(Pageable pageable, @Param("projectId") Long projectId,
      @Param("isActive") Boolean isActive, @Param("userId") Long userId, @Param("type") String type);

  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.isActive = :isActive AND deployment.userId = :userId AND deployment.type = :type")
  List<Deployment> findAllByActiveAndUserAndType(@Param("isActive") Boolean active, @Param("userId") Long userId,
      @Param("type") String type);
}
