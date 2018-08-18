/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.TrainingEngine;

/**
 * The Interface TrainingEngineRepository.
 */
public interface TrainingEngineRepository extends PagingAndSortingRepository<TrainingEngine, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return launcherList.
   */
  @Query(value = "SELECT trainingEngine FROM TrainingEngine trainingEngine WHERE trainingEngine.isActive = :isActive")
  List<TrainingEngine> findAllTrainingEnginesByIsActive(@Param("isActive") Boolean isActive);

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return launcherList.
   */
  @Query(value = "SELECT trainingEngine FROM TrainingEngine trainingEngine WHERE trainingEngine.isActive = :isActive AND trainingEngine.type = :type")
  List<TrainingEngine> findAllTrainingEnginesByIsActiveAndType(@Param("isActive") Boolean isActive, @Param("type") String type);

  /**
   * Find by name and by isActive.
   * 
   * @param name
   *          name.
   * @param isActive
   *          true.
   * @return TrainingEngine.
   */
  @Query(value = "SELECT trainingEngine FROM TrainingEngine trainingEngine WHERE trainingEngine.name = :name and trainingEngine.isActive = :isActive")
  TrainingEngine findByNameAndIsActive(@Param("name") String name, @Param("isActive") Boolean isActive);
}
