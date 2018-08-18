/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.Department;
import com.ancode.service.account.util.service.CRUDService;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Interface DepartmentService.
 */
@Service
public interface DepartmentService extends CRUDService<Department> {

  /**
   * Find all Active Departments.
   *
   * @param isActive
   *          isActive status.
   * @return active department list.
   * @throws Exception
   *           if error occurs.
   */
  List<Department> findAllDepartmentsByIsActive(Boolean isActive) throws Exception;

  /**
   * Find all Active Department lists with Pagination.
   *
   * @param pagingAndSorting
   *          pagination and sorting
   * @param isActive
   *          isActive status
   * @return active department list
   * @throws Exception
   *           if error occurs
   */
  Page<Department> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception;

  /**
   * Find all department list by user and search text.
   *
   * @param pagingAndSorting
   *          paging and sorting
   * @param searchText
   *          search text
   * @return department list
   * @throws Exception
   *           if error occurs
   */
  Page<Department> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception;

  /**
   * Soft deleting by selected department.
   *
   * @param department
   *          department.
   * @return delted department.
   * @throws Exception
   *           if error occurs.
   */
  Department softDelete(Department department) throws Exception;
}
