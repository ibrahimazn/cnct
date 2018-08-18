/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Launcher;
import com.assistanz.slmlp.container.service.LauncherService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Launcher controller will expose REST API for launch RStudio, Jupyter,
 * Tensorflow engine, etc.,. List of active launchers.
 *
 */
@RestController
@RequestMapping("/api/launchers")
@Api(value = "Container", description = "Operations with Container", produces = "application/json")
public class LauncherController extends CRUDController<Launcher> implements ApiController {

  /** Service reference to Launcher. */
  @Autowired
  private LauncherService launcherService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new Container.", response = Launcher.class)
  @Override
  public Launcher create(@RequestBody Launcher container) throws Exception {
    return launcherService.save(container);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing Container.", response = Launcher.class)
  @Override
  public Launcher read(@PathVariable(PATH_ID) Long id) throws Exception {
    return launcherService.find(id);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing Container.", response = Launcher.class)
  @Override
  public Launcher update(@RequestBody Launcher container, @PathVariable(PATH_ID) Long id) throws Exception {
    return launcherService.update(container);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "delete an existing Container.", response = Launcher.class)
  @Override
  public void delete(@PathVariable(PATH_ID) Long id) throws Exception {
    launcherService.delete(id);
  }

  /**
   * List all containers.
   *
   * @param userId the user id
   * @return departments.
   * @throws Exception unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Launcher> findAllByActive(@RequestHeader(required = false) String userId) throws Exception {
    return launcherService.findAllByActive(true);
  }

  /**
   * List all containers.
   *
   * @param userId the user id
   * @return departments.
   * @throws Exception unhandled errors.
   */
  @RequestMapping(value = "listbytype", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Launcher> findAllByType(@RequestHeader(required = false) String userId, @RequestParam("type") String type) throws Exception {
    return launcherService.findAllByIsActiveAndType(true, type);
  }

  /**
   * Launch the launchers Like RStudio, Jupyter, Tensorflow, etc.,.
   *
   * @param launcher the launcher
   * @param userId the user id
   * @param userName the user name
   * @param request the request
   * @param response the response
   * @return the deployment
   * @throws Exception if error occurs.
   */
  @RequestMapping(value = "/launch", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Deployment launch(@RequestBody Launcher launcher, @RequestHeader(required = false) String userId,
      @RequestHeader(required = false) String userName, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    launcher.setUserId(Long.parseLong(userId));
    launcher.setUser(userName);
    return launcherService.launch(launcher);
  }

}
