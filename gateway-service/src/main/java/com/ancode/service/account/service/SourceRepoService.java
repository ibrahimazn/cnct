/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.SourceRepo;
import com.ancode.service.account.util.service.CRUDService;

/**
 * The Interface SourceRepoService.
 */
@Service
public interface SourceRepoService extends CRUDService<SourceRepo> {

  /**
   * Soft deleting by selected sourceRepo.
   *
   * @param sourceRepo
   *          the source repo
   * @return deleted sourceRepo.
   * @throws Exception
   *           if error occurs.
   */
  SourceRepo softDelete(SourceRepo sourceRepo) throws Exception;

  /**
   * Find all Active sourceRepos.
   *
   * @param isActive
   *          isActive status.
   * @return active sourceRepo list.
   * @throws Exception
   *           if error occurs.
   */
  List<SourceRepo> findAllByIsActive(Boolean isActive) throws Exception;
}
