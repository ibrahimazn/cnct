/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.Role;

/**
 * The Interface RoleRepository.
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return roleList.
   */
  @Query(value = "SELECT role FROM Role role WHERE role.isActive = :isActive")
  List<Role> findAllRolesByIsActive(@Param("isActive") Boolean isActive);
}
