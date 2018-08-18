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
import com.assistanz.slmlp.container.entity.Scoring;

/**
 * The Interface Scoring Repository.
 */
public interface ScoringRepository extends PagingAndSortingRepository<Scoring, Long> {

  /**
   * Find all by isActive.
   *
   * @param isActive
   *          true.
   * @return scoreList.
   */
  @Query(value = "SELECT scoring FROM Scoring scoring WHERE scoring.isActive = :isActive")
  List<Scoring> findAllByIsActive(@Param("isActive") Boolean isActive);
  
  /**
   * Find all by isActive and project.
   *
   * @param isActive true.
   * @param projectId the projectId.
   * @return scoreList.
   */
  @Query(value = "SELECT scoring FROM Scoring scoring WHERE scoring.isActive = :isActive AND scoring.projectId = :projectId")
  List<Scoring> findAllByIsActiveAndProject(@Param("isActive") Boolean isActive, @Param("projectId") Long projectId);

  /**
   * Find all by isActive.
   *
   * @param pageable
   *          pageable.
   * @param projectId
   *          the project id
   * @param isActive
   *          true.
   * @return scoreList.
   */
  @Query(value = "SELECT scoring FROM Scoring scoring WHERE scoring.projectId = :projectId AND scoring.isActive = :isActive")
  Page<Scoring> findAllByIsActive(Pageable pageable, @Param("projectId") Long projectId,
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
   * @return scoreList
   */
  @Query(value = "SELECT scoring FROM Scoring scoring WHERE scoring.projectId = :projectId AND scoring.isActive = :isActive AND (scoring.name LIKE %:search%)")
  Page<Scoring> findBySearchText(Pageable pageable, @Param("projectId") Long projectId, @Param("search") String search,
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
   * @return score.
   */
  @Query(value = "SELECT scoring FROM Scoring scoring WHERE scoring.projectId = :projectId AND scoring.name = :name AND scoring.isActive = :isActive")
  Scoring findByNameAndIsActive(@Param("projectId") Long projectId, @Param("name") String name,
      @Param("isActive") Boolean isActive);

  /**
   * Find all by isactive and model id.
   *
   * @param modelId model id.
   * @param isActive true/false.
   * @return model.
   */
  @Query(value = "SELECT scoring FROM Scoring scoring WHERE scoring.isActive = :isActive AND scoring.modelId = :modelId")
  Scoring findAllByModelId(@Param("modelId") Long modelId, @Param("isActive") Boolean isActive);


}
