/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.entity.DataSet;
import com.ancode.service.account.entity.DataSetAdditionalParam;
import com.ancode.service.account.entity.DataSetConfiguration;
import com.ancode.service.account.entity.DataSource;
import com.ancode.service.account.entity.DataSourceAdditionalParam;
import com.ancode.service.account.error.exception.ApplicationException;
import com.ancode.service.account.repository.DataSetRepository;
import com.ancode.service.account.util.AppValidator;
import com.ancode.service.account.util.error.Errors;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class DataSetServiceImpl.
 */
@Service
public class DataSetServiceImpl implements DataSetService {

  /** DataSetRepository reference. */
  @Autowired
  private DataSetRepository dataSetRepository;

  /** DataSetSourceService reference. */
  @Autowired
  private DataSetConfigurationService dataSetConfigurationService;

  /** DataSetSourceService reference. */
  @Autowired
  private DataSourceAdditionalParamService dataSourceAdditionalParamService;

  /** DataSetSourceService reference. */
  @Autowired
  private DataSetAdditionalParamService dataSetAdditionalParamService;

  /** Constant for DataSet. */
  private static final String DATASET = "dataset";

  /** Validator attribute. */
  @Autowired
  private AppValidator validator;

  @Override
  public DataSet save(DataSet dataSet) throws Exception {
    DataSetConfiguration dataSetSource = updateDataSetSource(dataSet);
    dataSet.setDataSetConfigId(dataSetSource.getId());
    dataSet.setIsActive(true);
    this.validateDataSet(dataSet);
    Errors errors = validator.rejectIfNullEntity(DATASET, dataSet);
    errors = validator.validateEntity(dataSet, errors);
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
    DataSet dataSetObj = dataSetRepository.save(dataSet);
    return dataSetObj;
  }

  @Override
  public DataSet update(DataSet dataSet) throws Exception {
    return save(dataSet);
  }

  @Override
  public void delete(DataSet t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public DataSet find(Long id) throws Exception {
    return dataSetRepository.findOne(id);
  }

  @Override
  public Page<DataSet> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<DataSet> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Update data set source.
   *
   * @param dataSet
   *          the data set
   * @return the data set configuration
   * @throws Exception
   *           the exception
   */
  private DataSetConfiguration updateDataSetSource(DataSet dataSet) throws Exception {
    if (dataSet.getDataSetConfig() != null) {
      DataSetConfiguration dataSetConfiguration = dataSet.getDataSetConfig();
      dataSetConfiguration.setIsActive(true);
      if (dataSetConfiguration.getDataSourceAdditionalParam() != null) {
        for (DataSourceAdditionalParam dataSourceAdditionalParam : dataSetConfiguration
            .getDataSourceAdditionalParam()) {
          if (dataSourceAdditionalParam != null) {
            dataSourceAdditionalParam.setIsActive(true);
            dataSourceAdditionalParamService.save(dataSourceAdditionalParam);
          }
        }
      }
      if (dataSetConfiguration.getDataSetAdditionalParam() != null) {
        for (DataSetAdditionalParam dataSetAdditionalParam : dataSetConfiguration.getDataSetAdditionalParam()) {
          if (dataSetAdditionalParam != null) {
            dataSetAdditionalParam.setIsActive(true);
            dataSetAdditionalParamService.save(dataSetAdditionalParam);
          }
        }
      }
      return dataSetConfigurationService.save(dataSetConfiguration);
    }
    return null;
  }

  @Override
  public List<DataSet> findAllByIsActive(Boolean isActive) throws Exception {
    return dataSetRepository.findAllByIsActive(isActive);
  }

  @Override
  public DataSet softDelete(DataSet dataSet) throws Exception {
    dataSet.setIsActive(false);
    return dataSetRepository.save(dataSet);
  }

  @Override
  public void saveDataSets(List<DataSet> dataSets, Long projectId) throws Exception {
    for (DataSet dataSet : dataSets) {
      if (projectId != null) {
        dataSet.setProjectId(projectId);
        dataSetRepository.save(dataSet);
      } else {
        save(dataSet);
      }
    }
  }

  @Override
  public Page<DataSet> findAllByIsActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive)
      throws Exception {
    return dataSetRepository.findAllByIsActive(pagingAndSorting.toPageRequest(), projectId, isActive);
  }

  @Override
  public Page<DataSet> findAllBySearchText(PagingAndSorting pagingAndSorting, Long projectId, String searchText)
      throws Exception {
    return dataSetRepository.findBySearchText(pagingAndSorting.toPageRequest(), projectId, searchText, true);
  }

  /**
   * Validate the dataset.
   *
   * @param dataset
   *          reference of the dataset.
   * @throws Exception
   *           error occurs.
   */
  private void validateDataSet(DataSet dataset) throws Exception {
    Errors errors = validator.rejectIfNullEntity(DATASET, dataset);
    errors = validator.validateEntity(dataset, errors);
    if (this.dataSetRepository.findByNameAndIsActive(dataset.getProjectId(), dataset.getName(), true) != null) {
      errors.addGlobalError("DataSet Name already exist");
    }
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
  }

  @Override
  public List<DataSource> findAllDataSourceByIsActive(Boolean isActive) throws Exception {
    return dataSetRepository.findAllDataSourceByIsActive(isActive);
  }
}
