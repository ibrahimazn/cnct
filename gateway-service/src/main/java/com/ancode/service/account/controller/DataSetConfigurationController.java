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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ancode.service.account.constants.GenericConstants;
import com.ancode.service.account.entity.DataSetConfiguration;
import com.ancode.service.account.entity.DataSetStorageType;
import com.ancode.service.account.service.DataSetConfigurationService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;

/**
 * DataSetConfiguration controller.
 *
 */
@RestController
@RequestMapping("/api/datasetconfig")
@Api(value = "DataSetConfiguration", description = "Operations with DataSetConfiguration", produces = "application/json")

public class DataSetConfigurationController extends CRUDController<DataSetConfiguration> implements ApiController {

  /** Service reference to DataSetConfigurationService. */
  @Autowired
  private DataSetConfigurationService dataSetConfigurationService;

  /**
   * Find all with pagination.
   * 
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
   * @return dpartments.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<DataSetConfiguration> listView(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    sortBy = "id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, DataSetConfiguration.class);
    Page<DataSetConfiguration> pageResponse = dataSetConfigurationService.findAllByIsActive(page, true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * Find by search text.
   * 
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
   * @return DataSetConfigurations.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "findbysearchtext", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<DataSetConfiguration> findBySearchText(@RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      @RequestParam String searchText, HttpServletRequest request, HttpServletResponse response) throws Exception {
    sortBy = "id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, DataSetConfiguration.class);
    Page<DataSetConfiguration> pageResponse = dataSetConfigurationService.findAllBySearchText(page, searchText);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * List all DataSetStorageType.
   *
   * @return dataSetStorageTypes.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listdatasetstoragetypebyisactive", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<DataSetStorageType> findAllDataSetStorageTypeByActive() throws Exception {
    return dataSetConfigurationService.findAllDataSetStorageTypeByIsActive(true);
  }
}
