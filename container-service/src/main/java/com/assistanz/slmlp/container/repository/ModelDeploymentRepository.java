/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.ModelDeployment;

public interface ModelDeploymentRepository extends PagingAndSortingRepository<ModelDeployment, Long> {

  @Query(value = "SELECT modelDeployment FROM ModelDeployment modelDeployment WHERE modelDeployment.projectId = :projectId "
      + "AND modelDeployment.name = :name AND modelDeployment.isActive = :isActive")
  ModelDeployment findByNameAndIsActive(@Param("projectId") Long projectId, @Param("name") String name,
      @Param("isActive") Boolean isActive);

  @Query(value = "SELECT modelDeployment FROM ModelDeployment modelDeployment WHERE modelDeployment.isActive = :isActive")
  Page<ModelDeployment> findAllByActive(Pageable pageable, @Param("isActive") Boolean isActive);

  @Query(value = "SELECT modelDeployment FROM ModelDeployment modelDeployment WHERE modelDeployment.projectId = :projectId  "
      + "AND modelDeployment.projectId = :projectId AND modelDeployment.isActive = :isActive")
  Page<ModelDeployment> findAllPaginatedByProjectActive(Pageable pageable, @Param("projectId") Long projectId, @Param("isActive") Boolean isActive);



  @Query(value = "SELECT modelDeployment FROM ModelDeployment modelDeployment WHERE modelDeployment.projectId = :projectId  "
      + "AND modelDeployment.projectId = :projectId AND modelDeployment.isActive = :isActive order by id desc")
  List<ModelDeployment> findAllByProjectActive(@Param("projectId") Long projectId, @Param("isActive") Boolean isActive);
}