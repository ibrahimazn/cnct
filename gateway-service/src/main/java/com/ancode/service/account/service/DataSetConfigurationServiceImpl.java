/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.DataSetConfiguration;
import com.ancode.service.account.entity.DataSetStorageType;
import com.ancode.service.account.repository.DataSetConfigurationRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class DataSetConfigurationServiceImpl.
 */
@Service
public class DataSetConfigurationServiceImpl implements DataSetConfigurationService {

  /** DataSetSourceRepository reference. */
  @Autowired
  private DataSetConfigurationRepository dataSetConfigurationRepository;

  @Override
  public DataSetConfiguration save(DataSetConfiguration dataSetConfiguration) throws Exception {
    return dataSetConfigurationRepository.save(dataSetConfiguration);
  }

  @Override
  public DataSetConfiguration update(DataSetConfiguration dataSetConfiguration) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(DataSetConfiguration dataSetConfiguration) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public DataSetConfiguration find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<DataSetConfiguration> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<DataSetConfiguration> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<DataSetConfiguration> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive)
      throws Exception {
    return dataSetConfigurationRepository.findAllByIsActive(pagingAndSorting.toPageRequest(), isActive);
  }

  @Override
  public Page<DataSetConfiguration> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText)
      throws Exception {
    return dataSetConfigurationRepository.findBySearchText(pagingAndSorting.toPageRequest(), searchText, true);
  }

  @Override
  public List<DataSetStorageType> findAllDataSetStorageTypeByIsActive(Boolean isActive) throws Exception {
    return dataSetConfigurationRepository.findAllDataSetStorageTypeByIsActive(isActive);
  }

}
