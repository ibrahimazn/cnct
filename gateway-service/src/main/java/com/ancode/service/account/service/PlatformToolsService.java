/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.PlatformTools;
import com.ancode.service.account.util.service.CRUDService;

/**
 * The Interface PlatformToolsService.
 */
@Service
public interface PlatformToolsService extends CRUDService<PlatformTools> {

  /**
   * Save list of platformTools.
   *
   * @param platformToolsList
   *          platformToolsList.
   * @throws Exception
   *           if error occurs.
   */
  void savePlatformTools(List<PlatformTools> platformToolsList) throws Exception;

  /**
   * Find all Active platformTools.
   *
   * @param isActive
   *          isActive status.
   * @return active launcher list.
   * @throws Exception
   *           if error occurs.
   */
  List<PlatformTools> findAllPlatformToolsByIsActive(Boolean isActive) throws Exception;
}
