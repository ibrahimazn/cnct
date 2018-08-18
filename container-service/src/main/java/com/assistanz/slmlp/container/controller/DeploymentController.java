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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Deployment.DeploymentType;
import com.assistanz.slmlp.container.service.DeploymentService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import az.ancode.kubectl.connector.deployment.DeploymentDeleteResponse;

/**
 * Deployment Controller to expose REST API for list all active deployments, and
 * detroy deployments, create new deployment for to launch RStudio, tensorflow,
 * etc.,.
 */
@RestController
@RequestMapping("/api/deployment")
public class DeploymentController extends CRUDController<Deployment> implements ApiController {

  /** Service reference to Deployment. */
  @Autowired
  private DeploymentService deploymentService;

  @Override
  public Deployment create(@RequestBody Deployment deployment) throws Exception {
    return deploymentService.save(deployment);
  }

  @Override
  public Deployment update(Deployment deployment, Long id) throws Exception {
    return deploymentService.save(deployment);
  }

  @Override
  public void delete(Long id) throws Exception {
    deploymentService.delete(id);
  }

  @Override
  public Deployment read(Long id) throws Exception {
    return deploymentService.find(id);
  }

  @Override
  public List<Deployment> list(String sortBy, String range, Integer limit, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    return deploymentService.findAll();
  }

  /**
   * Find all by active.
   *
   * @param userId the user id
   * @return the list of deployments.
   * @throws Exception if error occurs.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<Deployment> findAllByActive(@RequestHeader(required = false) String userId) throws Exception {
    return deploymentService.findAllByActive(true);
  }

  /**
   * Destroy deployment By container id.
   *
   * @param deployment the deployment.
   * @param request the request.
   * @param response the response.
   * @param userId the user id.
   * @return the deployment delete response.
   * @throws Exception if error occurs.
   */
  @RequestMapping(value = "/destroy", method = RequestMethod.POST, consumes = {
      MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public DeploymentDeleteResponse destory(@RequestBody Deployment deployment, HttpServletRequest request,
      HttpServletResponse response, @RequestHeader(required = false) String userId) throws Exception {
    return deploymentService.destroy(deployment);
  }

  /**
   * Find all launchers by user.
   * 
   * @param userId the userId.
   * @return deployments based on the user.
   * @throws Exception if error occurs.
   */
  @RequestMapping(value = "findalllaunchersbyuserandprojects", method = RequestMethod.GET, consumes = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Deployment> findAllByUserIdAndProjectsAndIsActive(@RequestHeader(required = false) String userId, @RequestParam List<Long> projectIds) throws Exception {
    return deploymentService.findAllByActiveUserIdAndProjects(true, Long.parseLong(userId), projectIds);
  }

  /**
   * Find all launchers by logged in user.
   * 
   * @param userId the userId.
   * @return launchers.
   * @throws Exception if error occurs.
   */
  @RequestMapping(value = "findalllaunchersbyuser", method = RequestMethod.GET, consumes = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Deployment> findAllByUserIdAndIsActive(@RequestHeader(required = false) String userId) throws Exception {
    return deploymentService.findAllByActiveAndUserId(true, Long.parseLong(userId));
  }
  
  /**
   * Find all launchers by userId and type.
   * 
   * @param userId the userId.
   * @return launchers list.
   * @throws Exception if error occurs.
   */
  @RequestMapping(value = "findalllaunchersbyuserandtype", method = RequestMethod.GET, consumes = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Deployment> findAllByActiveAndUserIdAndType(@RequestHeader(required = false) String userId) throws Exception {
    return deploymentService.findAllByActiveAndUserIdAndType(true, Long.parseLong(userId),"Launchers");
  }
  
}
