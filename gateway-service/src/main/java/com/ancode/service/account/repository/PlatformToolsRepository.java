/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.PlatformTools;

/**
 * The Interface PlatformToolsRepository.
 */
public interface PlatformToolsRepository extends PagingAndSortingRepository<PlatformTools, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return PlatformTools.
   */
  @Query(value = "SELECT platformTools FROM PlatformTools platformTools WHERE platformTools.isActive = :isActive")
  List<PlatformTools> findAllPlatformToolsByIsActive(@Param("isActive") Boolean isActive);
}
