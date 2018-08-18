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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ancode.service.account.constants.GenericConstants;
import com.ancode.service.account.entity.Deployment;
import com.ancode.service.account.entity.Launcher;
import com.ancode.service.account.service.LauncherService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * The Class LauncherController.
 */
@RestController
@RequestMapping("/api/launcher")
@Api(value = "Launcher", description = "Operations with Launcher", produces = "application/json")
public class LauncherController extends CRUDController<Launcher> implements ApiController {

  /** Service reference to Launcher. */
  @Autowired
  private LauncherService launcherService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new Project.", response = Launcher.class)
  @Override
  public Launcher create(@RequestBody Launcher launcher) throws Exception {
    return launcherService.save(launcher);
  }

  /**
   * List all launchers.
   *
   * @return launchers.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Launcher> findAllByActive() throws Exception {
    return launcherService.findAllLaunchersByIsActive(true);
  }
  
  /**
   * List all launchers by type.
   *
   * @return launchers.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbytype", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Launcher> findAllByActiveAndType(@RequestParam String type) throws Exception {
    return launcherService.findAllLaunchersByIsActiveAndType(true, type);
  }

  /**
   * Launch.
   *
   * @param launcher
   *          the launcher
   * @return the deployment
   * @throws Exception
   *           the exception
   */
  @RequestMapping(value = "launch", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Deployment launch(@RequestBody Launcher launcher) throws Exception {
    return launcherService.launch(launcher);
  }

  /**
   * Destroy.
   *
   * @param deployment
   *          the deployment
   * @throws Exception
   *           the exception
   */
  @RequestMapping(value = "destroy", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void destroy(@RequestBody Deployment deployment) throws Exception {
    launcherService.destroy(deployment);
  }

  /**
   * Find all with pagination.
   *
   * @param sortBy
   *          id.
   * @param range
   *          range.
   * @param limit
   *          limit.
   * @param projectId
   *          the project id
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
  public List<Deployment> listLaunchers(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit, @RequestParam("projectId") Long projectId,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Deployment.class);
    Page<Deployment> pageResponse = launcherService.listContainersByNamespace(page, projectId, true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

}
