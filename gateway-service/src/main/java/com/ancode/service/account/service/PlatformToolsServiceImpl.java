/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.PlatformTools;
import com.ancode.service.account.repository.PlatformToolsRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class PlatformToolsServiceImpl.
 */
@Service
public class PlatformToolsServiceImpl implements PlatformToolsService {

  /** PlatformToolsRepository reference. */
  @Autowired
  private PlatformToolsRepository platformToolsRepository;

  @Override
  public PlatformTools save(PlatformTools platformTools) throws Exception {
    platformTools.setIsActive(true);
    return platformToolsRepository.save(platformTools);
  }

  @Override
  public PlatformTools update(PlatformTools platformTools) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(PlatformTools platformTools) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public PlatformTools find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<PlatformTools> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PlatformTools> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void savePlatformTools(List<PlatformTools> platformToolsList) throws Exception {
    for (PlatformTools platformTool : platformToolsList) {
      save(platformTool);
    }
  }

  @Override
  public List<PlatformTools> findAllPlatformToolsByIsActive(Boolean isActive) throws Exception {
    return platformToolsRepository.findAllPlatformToolsByIsActive(isActive);
  }

}
