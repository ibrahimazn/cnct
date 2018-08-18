/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.DataSetAdditionalParam;
import com.ancode.service.account.repository.DataSetAdditionalParamRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class DataSetAdditionalParamServiceImpl.
 */
@Service
public class DataSetAdditionalParamServiceImpl implements DataSetAdditionalParamService {

  /** DataSetAdditionalParamRepository reference. */
  @Autowired
  private DataSetAdditionalParamRepository dataSetAdditionalParamRepository;

  @Override
  public DataSetAdditionalParam save(DataSetAdditionalParam dataSetAdditionalParam) throws Exception {
    return dataSetAdditionalParamRepository.save(dataSetAdditionalParam);
  }

  @Override
  public DataSetAdditionalParam update(DataSetAdditionalParam t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(DataSetAdditionalParam t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public DataSetAdditionalParam find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<DataSetAdditionalParam> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<DataSetAdditionalParam> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
