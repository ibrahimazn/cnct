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
import com.ancode.service.account.entity.Department;
import com.ancode.service.account.service.DepartmentService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Department controller.
 *
 */
@RestController
@RequestMapping("/api/department")
@Api(value = "Department", description = "Operations with Department", produces = "application/json")
public class DepartmentController extends CRUDController<Department> implements ApiController {

  /** Service reference to Department. */
  @Autowired
  private DepartmentService departmentService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new department.", response = Department.class)
  @Override
  public Department create(@RequestBody Department department) throws Exception {
    return departmentService.save(department);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing Department.", response = Department.class)
  @Override
  public Department read(@PathVariable(PATH_ID) Long id) throws Exception {
    return departmentService.find(id);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing Department.", response = Department.class)
  @Override
  public Department update(@RequestBody Department department, @PathVariable(PATH_ID) Long id) throws Exception {
    return departmentService.update(department);
  }

  /**
   * Soft deleting the department from the table.
   *
   * @param department
   *          the department
   * @param id
   *          Department's id.
   * @throws Exception
   *           unhandled errors.
   */
  @ApiOperation(value = SW_METHOD_DELETE, notes = "Delete an existing Department.")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void softDelete(@RequestBody Department department, @PathVariable(PATH_ID) Long id) throws Exception {
    /** Doing Soft delete from the department table. */
    departmentService.softDelete(department);
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
   * @return dpartments.
   * @throws Exception
   *           if error occurs.
   */
  // @PreAuthorize("hasPermission(#projectId, 'ROLE_DEPARTMENT_LIST')")
  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Department> listView(@RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Department.class);
    Page<Department> pageResponse = departmentService.findAllByIsActive(page, true);
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
   * @return departments.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "findbysearchtext", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Department> findBySearchText(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit, @RequestParam String searchText, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Department.class);
    Page<Department> pageResponse = departmentService.findAllBySearchText(page, searchText);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * List all departments.
   *
   * @return departments.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Department> findAllByActive() throws Exception {
    return departmentService.findAllDepartmentsByIsActive(true);
  }


}
