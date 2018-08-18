/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.Project;
import com.ancode.service.account.util.service.CRUDService;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Interface ProjectService.
 */
@Service
public interface ProjectService extends CRUDService<Project> {

  /**
   * Soft deleting by selected project.
   *
   * @param project
   *          the project
   * @return deleted Project.
   * @throws Exception
   *           if error occurs.
   */
  Project softDelete(Project project) throws Exception;

  /**
   * Find all Active Projects.
   *
   * @param isActive
   *          isActive status.
   * @return active project list.
   * @throws Exception
   *           if error occurs.
   */
  List<Project> findAllProjectsByIsActive(Boolean isActive) throws Exception;

  /**
   * Find all Active Project lists with Pagination.
   *
   * @param pagingAndSorting
   *          pagination and sorting
   * @param isActive
   *          isActive status
   * @return active Project list
   * @throws Exception
   *           if error occurs
   */
  Page<Project> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception;

  /**
   * Find all Project list by user and search text.
   *
   * @param pagingAndSorting
   *          paging and sorting
   * @param searchText
   *          search text
   * @return Project list
   * @throws Exception
   *           if error occurs
   */
  Page<Project> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception;

  /**
   * Find by project name.
   *
   * @param userName
   *          the user name
   * @return project.
   */
  Project findByProjectName(String userName);
}
