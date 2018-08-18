/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.DataSourceAdditionalParam;
import com.ancode.service.account.repository.DataSourceAdditionalParamRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class DataSourceAdditionalParamServiceImpl.
 */
@Service
public class DataSourceAdditionalParamServiceImpl implements DataSourceAdditionalParamService {

  /** DataSource AdditionalParam Repository reference. */
  @Autowired
  private DataSourceAdditionalParamRepository dataSourceAdditionalParamRepository;

  @Override
  public DataSourceAdditionalParam save(DataSourceAdditionalParam dataSourceAdditionalParam) throws Exception {
    return dataSourceAdditionalParamRepository.save(dataSourceAdditionalParam);
  }

  @Override
  public DataSourceAdditionalParam update(DataSourceAdditionalParam t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(DataSourceAdditionalParam t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public DataSourceAdditionalParam find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<DataSourceAdditionalParam> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<DataSourceAdditionalParam> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
