/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.Deployment;
import com.ancode.service.account.entity.Launcher;
import com.ancode.service.account.util.service.CRUDService;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Interface LauncherService.
 */
@Service
public interface LauncherService extends CRUDService<Launcher> {

  /**
   * Find all Active Launchers.
   *
   * @param isActive
   *          isActive status.
   * @return active launcher list.
   * @throws Exception
   *           if error occurs.
   */
  List<Launcher> findAllLaunchersByIsActive(Boolean isActive) throws Exception;
  
  /**
   * Find all Active Launchers by type.
   *
   * @param isActive
   *          isActive status.
   * @param type
   *          type type of laumcher.         
   * @return active launcher list.
   * @throws Exception
   *           if error occurs.
   */
  List<Launcher> findAllLaunchersByIsActiveAndType(Boolean isActive, String type) throws Exception;

  /**
   * Find by name and active Launcher.
   *
   * @param name
   *          name.
   * @param isActive
   *          isActive status.
   * @return active launcher.
   * @throws Exception
   *           if error occurs.
   */
  Launcher findByNameAndIsActive(String name, Boolean isActive) throws Exception;

  /**
   * Launch the editor.
   *
   * @param launcher
   *          editor object.
   * @return the deployment
   * @throws Exception
   *           if error.
   */
  Deployment launch(Launcher launcher) throws Exception;

  /**
   * Destroy the launcher container.
   *
   * @param deployment
   *          the deployment
   * @throws Exception
   *           if error.
   */
  void destroy(Deployment deployment) throws Exception;

  /**
   * List containers by namespace.
   *
   * @param pagingAndSorting
   *          the paging and sorting
   * @param projectId
   *          the project id
   * @param isActive
   *          the is active
   * @return the page
   * @throws Exception
   *           the exception
   */
  Page<Deployment> listContainersByNamespace(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive)
      throws Exception;
}
