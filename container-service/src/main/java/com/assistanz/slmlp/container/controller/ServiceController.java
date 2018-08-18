/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assistanz.slmlp.container.entity.NodeServices;
import com.assistanz.slmlp.container.service.KubeServiceService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.wordnik.swagger.annotations.Api;

/**
 * Service controller will expose REST API for service creation at k8s end to
 * access launchers in public by using nodePort.
 *
 */
@RestController
@RequestMapping("/api/services")
@Api(value = "Service", description = "Operations with Service", produces = "application/json")
public class ServiceController extends CRUDController<NodeServices> implements ApiController {

  /** Service reference to Launcher. */
  @Autowired
  private KubeServiceService kubeService;

  @Override
  public NodeServices create(NodeServices service) throws Exception {
    return kubeService.save(service);
  }

  @Override
  public NodeServices update(NodeServices service, Long id) throws Exception {
    return kubeService.save(service);
  }

  @Override
  public void delete(Long id) throws Exception {
    kubeService.delete(id);
  }

  @Override
  public NodeServices read(Long id) throws Exception {
    return kubeService.find(id);
  }

  @Override
  public List<NodeServices> list(String sortBy, String range, Integer limit, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    return kubeService.findAll();
  }
}
