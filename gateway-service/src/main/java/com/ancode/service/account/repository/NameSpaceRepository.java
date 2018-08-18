/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.NameSpace;

/**
 * The Interface NameSpace.
 */
public interface NameSpaceRepository extends PagingAndSortingRepository<NameSpace, Long> {

  /**
   * Find by name.
   * 
   * @param isActive
   *          true.
   * @return Namespace.
   */
  @Query(value = "SELECT namespc FROM NameSpace namespc WHERE namespc.name = :name AND namespc.isActive = :isActive")
  NameSpace findByNameANDIsActive(@Param("name") String name, @Param("isActive") Boolean isActive);
}
