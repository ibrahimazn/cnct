/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
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
import com.assistanz.slmlp.container.entity.ModelDeployment;
import com.assistanz.slmlp.container.service.ModelDeploymentService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;



@RestController
@RequestMapping("/api/modelDeployment")
public class ModelDeploymentController extends CRUDController<ModelDeployment> implements ApiController {

  /** Service reference to Deployment. */
  @Autowired
  private ModelDeploymentService deploymentService;

  @Override
  public ModelDeployment create(@RequestBody ModelDeployment deployment) throws Exception {
    return deploymentService.save(deployment);
  }

  @Override
  public ModelDeployment update(ModelDeployment deployment, Long id) throws Exception {
    return deploymentService.save(deployment);
  }

  @Override
  public void delete(Long id) throws Exception {
    deploymentService.delete(id);
  }

  @Override
  public ModelDeployment read(Long id) throws Exception {
    return deploymentService.find(id);
  }

  @Override
  public List<ModelDeployment> list(String sortBy, String range, Integer limit, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    return deploymentService.findAll();
  }

  @RequestMapping(value = "/saveOffline", method = RequestMethod.POST, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ModelDeployment saveOfflineDeployment(@RequestBody ModelDeployment deployment,
      @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
    deployment.setCreatedBy(userName);
    deployment.setUserId(Long.valueOf(userId));
    return deploymentService.save(deployment);
  }

  @RequestMapping(value = "listByActive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ModelDeployment> findAllByActive(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, ModelDeployment.class);
    Page<ModelDeployment> pageResponse = deploymentService.findAllByActive(page, true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }


  @RequestMapping(value = "listByPagingActive/{projectId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ModelDeployment> findAllPaginatedByProjectActive(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit,  @PathVariable("projectId") Long projectId, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, ModelDeployment.class);
    Page<ModelDeployment> pageResponse = deploymentService.findAllPaginatedByProjectActive(page, projectId, true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  @RequestMapping(value = "predictedData", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ModelDeployment getPredictedData(@RequestParam("deploymentId") Long deploymentId,  @RequestHeader(required = false) String userName) throws Exception {
    return deploymentService.getPredictedData(deploymentId, userName);
  }

  @RequestMapping(value = "listByProject", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ModelDeployment> findAllByProjectActive(@RequestParam("projectId") Long projectId) throws Exception {
    return deploymentService.findAllByProjectActive(projectId, true);
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void deleteDeployment(@RequestBody ModelDeployment deployment, @RequestHeader(required = false) String userName) throws Exception {
    deploymentService.deleteDeployment(deployment.getId(), userName);
  }


}
