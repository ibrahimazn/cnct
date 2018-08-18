/*
 * 
 */
package com.ancode.service.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ancode.service.account.entity.Role;
import com.ancode.service.account.service.RoleService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.wordnik.swagger.annotations.Api;

/**
 * Role controller.
 *
 */
@RestController
@RequestMapping("/api/role")
@Api(value = "Role", description = "Operations with Roles", produces = "application/json")
public class RoleController extends CRUDController<Role> implements ApiController {

  /** Service reference to Role. */
  @Autowired
  private RoleService roleService;

  /**
   * List all roles.
   *
   * @return roles.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Role> findAllByActive() throws Exception {
    return roleService.findAllRolesByIsActive(true);
  }
}
