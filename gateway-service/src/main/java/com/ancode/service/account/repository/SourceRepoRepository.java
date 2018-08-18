/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.SourceRepo;

/**
 * The Interface SourceRepoRepository.
 */
public interface SourceRepoRepository extends PagingAndSortingRepository<SourceRepo, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return sourceRepoList.
   */
  @Query(value = "SELECT sourceRepo FROM SourceRepo sourceRepo WHERE sourceRepo.isActive = :isActive")
  List<SourceRepo> findAllByIsActive(@Param("isActive") Boolean isActive);
}
