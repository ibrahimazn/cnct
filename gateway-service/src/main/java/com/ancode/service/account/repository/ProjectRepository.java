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

import com.ancode.service.account.entity.Project;

/**
 * The Interface ProjectRepository.
 */
public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {

  /**
   * Find by name of the project..
   *
   * @param name of project.
   * @param isActive
   *          get the project list based on active/inactive status.
   * @return project.
   */
  @Query(value = "SELECT project FROM Project project WHERE project.name = :name AND project.isActive = :isActive")
  Project findByNameAndIsActive(@Param("name") String name, @Param("isActive") Boolean isActive);

  /**
   * Find by isActive.
   *
   * @param isActive
   *          true.
   * @param userId user id.
   * @return project.
   */
  @Query(value = "SELECT project FROM Project project LEFT JOIN project.projectUsers projectUser WHERE projectUser.userId = :userId AND project.id = projectUser.projectId AND project.isActive = :isActive")
  List<Project> findAllByUserAndIsActive(@Param("isActive") Boolean isActive,
      @Param("userId") Long userId);

  /**
   * Find all by isActive.
   *
   * @param pageable
   *          pageable.
   * @param isActive
   *          true.
   * @param userId the user ids
   * @return projectList.
   */
  @Query(value = "SELECT project FROM Project project LEFT JOIN project.projectUsers projectUser WHERE projectUser.userId = :userId AND project.id = projectUser.projectId AND project.isActive = :isActive")
  Page<Project> findAllByIsActive(Pageable pageable, @Param("isActive") Boolean isActive, @Param("userId") Long userId);

  /**
   * Find project by search text.
   *
   * @param pageable
   *          page
   * @param search
   *          search text
   * @param isActive
   *          active status
   * @param userId user id
   * @return project list
   */
  @Query(value = "SELECT project FROM Project project LEFT JOIN project.projectUsers projectUser WHERE projectUser.userId = :userId AND project.id = projectUser.projectId AND project.isActive = :isActive AND (project.name LIKE %:search% OR project.description LIKE %:search%)")
  Page<Project> findBySearchText(Pageable pageable, @Param("search") String search, @Param("isActive") Boolean isActive, @Param("userId") Long userId);
}
