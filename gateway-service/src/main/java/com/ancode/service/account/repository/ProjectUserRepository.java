/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.ProjectUser;

/**
 * The Interface ProjectUserRepository.
 */
public interface ProjectUserRepository extends PagingAndSortingRepository<ProjectUser, Long> {

  /**
   * Find all by userId.
   * 
   * @param userId
   *          userId.
   * @return projectUsers.
   */
  @Query(value = "SELECT projectUser FROM ProjectUser projectUser WHERE projectUser.userId = :userId")
  List<ProjectUser> findAllByUserId(@Param("userId") Long userId);
}
