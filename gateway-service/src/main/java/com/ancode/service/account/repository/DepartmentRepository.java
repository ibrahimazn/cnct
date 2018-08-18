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

import com.ancode.service.account.entity.Department;

/**
 * The Interface DepartmentRepository.
 */
public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return departmentList.
   */
  @Query(value = "SELECT department FROM Department department WHERE department.isActive = :isActive")
  List<Department> findAllDepartmentsByIsActive(@Param("isActive") Boolean isActive);

  /**
   * Find all by isActive.
   * 
   * @param pageable
   *          pageable.
   * @param isActive
   *          true.
   * @return departmentList.
   */
  @Query(value = "SELECT department FROM Department department WHERE department.isActive = :isActive")
  Page<Department> findAllByIsActive(Pageable pageable, @Param("isActive") Boolean isActive);

  /**
   * Find department by search text.
   *
   * @param pageable
   *          page
   * @param search
   *          search text
   * @param isActive
   *          active status
   * @return department list
   */
  @Query(value = "SELECT department FROM Department department WHERE department.isActive = :isActive AND (department.name LIKE %:search% OR department.description LIKE %:search%)")
  Page<Department> findBySearchText(Pageable pageable, @Param("search") String search,
      @Param("isActive") Boolean isActive);

  /**
   * Find by name of the department..
   *
   * @param name
   *          of department.
   * @param isActive
   *          get the department list based on active/inactive status.
   * @return department.
   */
  @Query(value = "SELECT department FROM Department department WHERE department.name = :name AND department.isActive = :isActive")
  Department findByNameAndIsActive(@Param("name") String name, @Param("isActive") Boolean isActive);
}
