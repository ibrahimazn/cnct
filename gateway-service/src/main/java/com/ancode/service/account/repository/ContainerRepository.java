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

import com.ancode.service.account.entity.AppContainer;

/**
 * The Interface ContainerRepository.
 */
public interface ContainerRepository extends PagingAndSortingRepository<AppContainer, Long> {

  /**
   * Find all by active.
   *
   * @param active
   *          the active
   * @return the list
   */
  @Query(value = "SELECT container FROM AppContainer container WHERE container.isActive = :isActive")
  List<AppContainer> findAllByActive(@Param("isActive") Boolean active);

  /**
   * Find all by active and user id.
   *
   * @param pageable
   *          the pageable
   * @param isActive
   *          the is active
   * @param userId
   *          the user id
   * @param type
   *          the type
   * @return the page
   */
  @Query(value = "SELECT container FROM AppContainer container " + " LEFT JOIN container.deployment deployment"
      + " WHERE container.isActive = :isActive " + " AND deployment.userId = :userId " + " AND deployment.type = :type"
      + " AND deployment.isActive = :isActive")
  Page<AppContainer> findAllByActiveAndUserId(Pageable pageable, @Param("isActive") Boolean isActive,
      @Param("userId") Long userId, @Param("type") String type);

}
