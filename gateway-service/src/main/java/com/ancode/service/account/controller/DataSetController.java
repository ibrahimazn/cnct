/*
 * 
 */
package com.ancode.service.account.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ancode.service.account.constants.GenericConstants;
import com.ancode.service.account.entity.DataSet;
import com.ancode.service.account.entity.DataSource;
import com.ancode.service.account.service.DataSetService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * The Class DataSetController.
 */
@RestController
@RequestMapping("/api/dataset")
@Api(value = "DataSet", description = "Operations with DataSet", produces = "application/json")
public class DataSetController extends CRUDController<DataSet> implements ApiController {

  /** Service reference to DataSet. */
  @Autowired
  private DataSetService dataSetService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new Nic.", response = DataSet.class)
  @Override
  public DataSet create(@RequestBody DataSet dataSet) throws Exception {
    return dataSetService.save(dataSet);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing DataSet.", response = DataSet.class)
  @Override
  public DataSet update(@RequestBody DataSet dataSet, @PathVariable(PATH_ID) Long id) throws Exception {
    return dataSetService.update(dataSet);
  }

  /**
   * Soft deleting the dataset from the table.
   *
   * @param dataSet
   *          the data set
   * @param id
   *          dataset's id.
   * @throws Exception
   *           unhandled errors.
   */
  @ApiOperation(value = SW_METHOD_DELETE, notes = "Delete an existing DataSet.")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void softDelete(@RequestBody DataSet dataSet, @PathVariable(PATH_ID) Long id) throws Exception {
    /** Doing Soft delete from the dataset table. */
    dataSetService.softDelete(dataSet);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing DataSet.", response = DataSet.class)
  @Override
  public DataSet read(@PathVariable(PATH_ID) Long id) throws Exception {
    return dataSetService.find(id);
  }

  /**
   * List all DataSets.
   *
   * @return dataSets.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<DataSet> findAllByActive() throws Exception {
    return dataSetService.findAllByIsActive(true);
  }

  /**
   * Find all with pagination.
   *
   * @param projectId
   *          the project id
   * @param sortBy
   *          id.
   * @param range
   *          range.
   * @param limit
   *          limit.
   * @param request
   *          http request.
   * @param response
   *          http response.
   * @return datasets.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<DataSet> listView(@RequestParam String projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, DataSet.class);
    Page<DataSet> pageResponse = dataSetService.findAllByIsActive(page, Long.parseLong(projectId), true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * Find by search text.
   *
   * @param projectId
   *          the project id
   * @param sortBy
   *          sortBy.
   * @param range
   *          range.
   * @param limit
   *          limit.
   * @param searchText
   *          searchText.
   * @param request
   *          httpRequest.
   * @param response
   *          httpResponse.
   * @return datasets.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "findbysearchtext", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<DataSet> findBySearchText(@RequestParam String projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      @RequestParam String searchText, HttpServletRequest request, HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, DataSet.class);
    Page<DataSet> pageResponse = dataSetService.findAllBySearchText(page, Long.parseLong(projectId), searchText);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * List all DataSources.
   *
   * @return dataSources.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listdatasourcebyisactive", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<DataSource> findAllDataSourceByActive() throws Exception {
    return dataSetService.findAllDataSourceByIsActive(true);
  }

}
