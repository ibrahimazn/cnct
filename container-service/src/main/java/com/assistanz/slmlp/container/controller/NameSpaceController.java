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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assistanz.slmlp.container.entity.Namespace;
import com.assistanz.slmlp.container.service.NameSpaceService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.wordnik.swagger.annotations.Api;

/**
 * NameSpace controller to expose Rest API for list all name spaces created at
 * k8s. choose default name space to create deployment under that name space by
 * default.
 *
 */
@RestController
@RequestMapping("/api/namespace")
@Api(value = "Namespace", description = "Operations with Namespace", produces = "application/json")
public class NameSpaceController extends CRUDController<Namespace> implements ApiController {

  /** Service reference to Namespace. */
  @Autowired
  private NameSpaceService nameSpaceService;

  @Override
  public Namespace create(Namespace namespace) throws Exception {
    return nameSpaceService.save(namespace);
  }

  @Override
  public Namespace update(Namespace namespace, Long id) throws Exception {
    return nameSpaceService.update(namespace);
  }

  @Override
  public void delete(Long id) throws Exception {
    super.delete(id);
  }

  @Override
  public Namespace read(Long id) throws Exception {
    return super.read(id);
  }

  @Override
  public List<Namespace> list(String sortBy, String range, Integer limit, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    return super.list(sortBy, range, limit, request, response);
  }

  /**
   * Set Default Namespace to launch new container like launchers and functions .
   *
   * @param nameSpace the name space.
   * @param userId the user id.
   * @return the namespace.
   * @throws Exception the exception will be handled at exception handling controller. 
   */
  @RequestMapping(value = "/default", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Namespace defaultNamespace(@RequestBody Namespace nameSpace, @RequestHeader(required = false) String userId)
      throws Exception {
    return nameSpaceService.defaultNamespace(nameSpace);
  }
}
