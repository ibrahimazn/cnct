/*
 *
 */
package com.ancode.service.account.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.Deployment;
import com.ancode.service.account.util.service.CRUDService;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Interface DeploymentService.
 */
@Service
public interface DeploymentService extends CRUDService<Deployment> {

  /**
   * Find all Active Deployments lists with Pagination.
   *
   * @param pagingAndSorting
   *          pagination and sorting
   * @param projectId
   *          the project id
   * @param isActive
   *          isActive status
   * @param type
   *          type of the container
   * @return the page
   * @throws Exception
   *           if error occurs
   */
  Page<Deployment> findAllByUserProjectAndIsActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive,
      String type) throws Exception;
}
