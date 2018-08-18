/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.SourceRepo;
import com.ancode.service.account.repository.SourceRepoRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class SourceRepoServiceImpl.
 */
@Service
public class SourceRepoServiceImpl implements SourceRepoService {

  /** SourceRepoRepository reference. */
  @Autowired
  private SourceRepoRepository sourceRepoRepository;

  @Override
  public SourceRepo save(SourceRepo sourceRepo) throws Exception {
    sourceRepo.setIsActive(true);
    return sourceRepoRepository.save(sourceRepo);
  }

  @Override
  public SourceRepo update(SourceRepo sourceRepo) throws Exception {
    sourceRepo.setIsActive(false);
    return sourceRepoRepository.save(sourceRepo);
  }

  @Override
  public void delete(SourceRepo sourceRepo) throws Exception {
    sourceRepoRepository.delete(sourceRepo);
  }

  @Override
  public void delete(Long id) throws Exception {
    sourceRepoRepository.delete(id);
  }

  @Override
  public SourceRepo find(Long id) throws Exception {
    return sourceRepoRepository.findOne(id);
  }

  @Override
  public Page<SourceRepo> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return sourceRepoRepository.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<SourceRepo> findAll() throws Exception {
    return (List<SourceRepo>) sourceRepoRepository.findAll();
  }

  @Override
  public SourceRepo softDelete(SourceRepo sourceRepo) throws Exception {
    sourceRepo.setIsActive(false);
    return sourceRepoRepository.save(sourceRepo);
  }

  @Override
  public List<SourceRepo> findAllByIsActive(Boolean isActive) throws Exception {
    return sourceRepoRepository.findAllByIsActive(isActive);
  }

}
