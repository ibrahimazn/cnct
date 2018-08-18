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
import com.assistanz.slmlp.container.entity.Scoring;
import com.assistanz.slmlp.container.service.ScoringService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * The Class Scoring Controller.
 */
@RestController
@RequestMapping("/api/scoring")
@Api(value = "Score", description = "Operations with Score", produces = "application/json")
public class ScoringController extends CRUDController<Scoring> implements ApiController {

  /** Service reference to score. */
  @Autowired
  private ScoringService scoreService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new Score.", response = Scoring.class)
  @Override
  public Scoring create(@RequestBody Scoring scoring) throws Exception {
    return scoreService.save(scoring);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing Score.", response = Scoring.class)
  @Override
  public Scoring update(@RequestBody Scoring scoring, @PathVariable(PATH_ID) Long id) throws Exception {
    return scoreService.update(scoring);
  }

  /**
   * Soft deleting the scoring from the table.
   *
   * @param scoring
   *          the scoring
   * @param id
   *          scoring's id.
   * @throws Exception
   *           unhandled errors.
   */
  @ApiOperation(value = SW_METHOD_DELETE, notes = "Delete an existing scoring.")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void softDelete(@RequestBody Scoring scoring, @PathVariable(PATH_ID) Long id) throws Exception {
    scoreService.softDelete(scoring);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing scoring.", response = Scoring.class)
  @Override
  public Scoring read(@PathVariable(PATH_ID) Long id) throws Exception {
    return scoreService.find(id);
  }

  /**
   * List all Scoring.
   *
   * @return scorings.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Scoring> findAllByActive(@RequestParam Long projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Scoring.class);
    Page<Scoring> pageResponse = scoreService.findAllByIsActive(page, projectId, true);
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
   * @return scorings.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Scoring> listView(@RequestParam String projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Scoring.class);
    Page<Scoring> pageResponse = scoreService.findAllByIsActive(page, Long.parseLong(projectId), true);
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
   * @return scorings.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "findbysearchtext", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Scoring> findBySearchText(@RequestParam String projectId, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      @RequestParam String searchText, HttpServletRequest request, HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Scoring.class);
    Page<Scoring> pageResponse = scoreService.findAllBySearchText(page, Long.parseLong(projectId), searchText);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * Save scoring.
   *
   * @param scoring the scoring
   * @param userId the user id
   * @param userName the user name
   * @return the scoring
   * @throws Exception the exception
   */
  @RequestMapping(value = "/saveScore", method = RequestMethod.POST, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Scoring saveScoring(@RequestBody Scoring scoring, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
    scoring.setCreatedBy(userName);
    scoring.setUserId(Long.valueOf(userId));
    scoring.setModelId(scoring.getModel().getId());
    scoring.setModelVersionId(scoring.getModelVersion().getId());
    return scoreService.save(scoring);
  }

  /**
   * Run scoring.
   *
   * @param scoring the scoring
   * @param userId the user id
   * @param userName the user name
   * @return the scoring
   * @throws Exception the exception
   */
  @RequestMapping(value = "/runScore", method = RequestMethod.POST, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Scoring runScoring(@RequestBody Scoring scoring, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
    return scoreService.runScore(scoring);
  }

  @RequestMapping(value = "deletescoringsbyproject", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void delteModelsByProject(@RequestParam String projectId) throws Exception {
    scoreService.deleteByProject(Long.parseLong(projectId));
  }
}
