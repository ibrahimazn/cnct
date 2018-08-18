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
import com.ancode.service.account.entity.Project;
import com.ancode.service.account.entity.Role;
import com.ancode.service.account.service.ProjectService;
import com.ancode.service.account.service.RoleService;
import com.ancode.service.account.service.UserService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * The Class ProjectController.
 */
@RestController
@RequestMapping("/api/project")
@Api(value = "Project", description = "Operations with Project", produces = "application/json")
public class ProjectController extends CRUDController<Project> implements ApiController {

  /** Service reference to Project. */
  @Autowired
  private ProjectService projectService;

  /** Service reference to User. */
  @Autowired
  private UserService userService;

  /** Service reference to Role. */
  @Autowired
  private RoleService roleService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new Project.", response = Project.class)
  @Override
  public Project create(@RequestBody Project project) throws Exception {
    return projectService.save(project);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing Project.", response = Project.class)
  @Override
  public Project read(@PathVariable(PATH_ID) Long id) throws Exception {
    return projectService.find(id);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing Project.", response = Project.class)
  @Override
  public Project update(@RequestBody Project project, @PathVariable(PATH_ID) Long id) throws Exception {
    return projectService.update(project);
  }

  /**
   * Soft deleting the project from the table.
   *
   * @param project
   *          the project
   * @param id
   *          Project's id.
   * @throws Exception
   *           unhandled errors.
   */
  @ApiOperation(value = SW_METHOD_DELETE, notes = "Delete an existing Project.")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void softDelete(@RequestBody Project project, @PathVariable(PATH_ID) Long id) throws Exception {
    /** Doing Soft delete from the project table. */
    projectService.softDelete(project);
  }

  /**
   * List all projects.
   *
   * @return projects.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Project> findAllByActive() throws Exception {
    return projectService.findAllProjectsByIsActive(true);
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
   * @param request
   *          http request.
   * @param response
   *          http response.
   * @return projects.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Project> listView(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Project.class);
    Page<Project> pageResponse = projectService.findAllByIsActive(page, true);
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
   * @return projects.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "findbysearchtext", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Project> findBySearchText(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit, @RequestParam String searchText, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Project.class);
    Page<Project> pageResponse = projectService.findAllBySearchText(page, searchText);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * Method to list all the available user lists.
   *
   * @return user list
   * @throws Exception
   *           if any error occurs
   */
  @RequestMapping(value = "listroles", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Role> findAllByIsActive() throws Exception {
    return roleService.findAllRolesByIsActive(true);
  }

}
