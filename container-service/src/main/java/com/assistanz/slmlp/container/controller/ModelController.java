/*
 *
 */
package com.assistanz.slmlp.container.controller;

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

import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.service.ModelService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * The Class ModelController.
 */
@RestController
@RequestMapping("/api/model")
@Api(value = "Model", description = "Operations with Model", produces = "application/json")
public class ModelController extends CRUDController<Model> implements ApiController {

  /** Service reference to model. */
  @Autowired
  private ModelService modelService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new Model.", response = Model.class)
  @Override
  public Model create(@RequestBody Model model) throws Exception {
    return modelService.save(model);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing Model.", response = Model.class)
  @Override
  public Model update(@RequestBody Model model, @PathVariable(PATH_ID) Long id) throws Exception {
    return modelService.update(model);
  }

  /**
   * Update model.
   *
   * @param model
   *          the model
   * @return the model
   * @throws Exception
   *           the exception
   */
  @RequestMapping(value = "/updatemodel", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Model updateModel(@RequestBody Model model, @RequestHeader(required = false) String userId,
      @RequestHeader(required = false) String userName) throws Exception {
    model.setCreatedBy(userName);
    model.setUserId(Long.valueOf(userId));
    return modelService.update(model);
  }

  /**
   * Save model.
   *
   * @param model
   *          the model
   * @return the model
   * @throws Exception
   *           the exception
   */
  @RequestMapping(value = "/savemodel", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Model saveModel(@RequestBody Model model, @RequestHeader(required = false) String userId,
      @RequestHeader(required = false) String userName) throws Exception {
    model.setCreatedBy(userName);
    model.setUserId(Long.valueOf(userId));
    return modelService.saveModel(model);
  }

  /**
   * Soft deleting the model from the table.
   *
   * @param model
   *          the model
   * @param id
   *          model's id.
   * @throws Exception
   *           unhandled errors.
   */
  @ApiOperation(value = SW_METHOD_DELETE, notes = "Delete an existing model.")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void softDelete(@RequestBody Model model, @PathVariable(PATH_ID) Long id) throws Exception {
    modelService.softDelete(model);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing model.", response = Model.class)
  @Override
  public Model read(@PathVariable(PATH_ID) Long id) throws Exception {
    return modelService.find(id);
  }

  /**
   * List all Model.
   *
   * @return models.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Model> findAllByActive(@RequestParam Long projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Model.class);
    Page<Model> pageResponse = modelService.findAllByIsActive(page, projectId, true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
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
   * @return models.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Model> listView(@RequestParam String projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Model.class);
    Page<Model> pageResponse = modelService.findAllByIsActive(page, Long.parseLong(projectId), true);
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
   * @return models.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "findbysearchtext", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Model> findBySearchText(@RequestParam String projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      @RequestParam String searchText, HttpServletRequest request, HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Model.class);
    Page<Model> pageResponse = modelService.findAllBySearchText(page, Long.parseLong(projectId), searchText);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  @RequestMapping(value = "findbyproject", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Model> findByProject(@RequestParam Long projectId) throws Exception {
    return modelService.findAllByIsActiveAndProject(true, projectId);
  }

  @RequestMapping(value = "listbyactive", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Model> findAllWithoutPagination() throws Exception {
      return modelService.findAllByIsActive(true);
  }
  
  @RequestMapping(value = "deletemodelsbyproject", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void delteModelsByProject(@RequestParam String projectId) throws Exception {
     modelService.deleteByProject(Long.parseLong(projectId));
  }
}
