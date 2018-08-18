/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.AppContainer;
import com.assistanz.slmlp.container.entity.AppContainer.Status;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import com.assistanz.slmlp.container.repository.ContainerRepository;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import az.ancode.kubectl.connector.RestTemplateCall;
import az.ancode.kubectl.connector.deployment.DeploymentList;
import az.ancode.kubectl.connector.deployment.DeploymentListRequest;
import az.ancode.kubectl.connector.deployment.DeploymentListResponse;

/**
 * This class is implementing all the functionalities of container service
 * interface.
 */
@Service
public class ContainerServiceImpl implements ContainerService {

  /** K8S Agent URL which is acquied from yml file. */
  @Value(value = "${kubernete.agent}")
  private String agentUrl;

  /** Secret key of K8S which is acquied from yml file. */
  @Value(value = "${kubernete.secKey}")
  private String auth;

  /** ContainerRepository reference. */
  @Autowired
  private ContainerRepository containerRepo;

  /** Deployment service reference. */
  @Autowired
  private DeploymentService deploymentService;

  /** RestTemplateCall service reference. */
  @Autowired
  private RestTemplateCall<DeploymentListRequest, DeploymentListResponse> deploymentListReq;

  @Override
  public AppContainer save(AppContainer container) throws Exception {
    container = containerRepo.save(container);
    return container;
  }

  @Override
  public AppContainer update(AppContainer container) throws Exception {
    return containerRepo.save(container);
  }

  @Override
  public void delete(AppContainer container) throws Exception {
    containerRepo.delete(container);
  }

  @Override
  public void delete(Long id) throws Exception {
    AppContainer container = find(id);
    if (container == null) {
      throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "Container is not exists");
    }
    container.setIsActive(false);
    containerRepo.save(container);
  }

  @Override
  public AppContainer find(Long id) throws Exception {
    return containerRepo.findOne(id);
  }

  @Override
  public Page<AppContainer> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return null;
  }

  @Override
  public List<AppContainer> findAll() throws Exception {
    return (List<AppContainer>) containerRepo.findAll();
  }

  @Override
  public List<AppContainer> findAllByActive(Boolean active) {
    return containerRepo.findAllByActive(true);
  }

  @Override
  public List<AppContainer> syncContainers(String name) throws Exception {
    DeploymentListRequest request = new DeploymentListRequest();
    DeploymentListResponse deployListResponse = deploymentListReq
        .restCall(agentUrl + "/list-deployments?namespace=" + name, auth, request, DeploymentListResponse.class, "get");
    if (deployListResponse.getResult() != null) {
      if (deployListResponse.getResult().size() == 0) {
        AppContainer container = new AppContainer();
      } else {
        // List<AppContainer> containers = new ArrayList<AppContainer>();
        // for (DeploymentList deployment : deployListResponse.getResult()) {
        // containers.add(containerEntityMapperUtil(deployment));
        // }
        // HashMap<String, AppContainer> launchMap = (HashMap<String,
        // AppContainer>) convert(containers);
        // List<AppContainer> appContainerList = (List<AppContainer>)
        // containerRepo.findAll();
        // for (AppContainer container : appContainerList) {
        // if (launchMap.containsKey(container.getName())) {
        // AppContainer csContainer = launchMap.get(container.getName());
        // // container.setLauncher(launcher);
        // containerRepo.save(container);
        // //launchMap.remove(container.getName());
        // } else {
        // containerRepo.delete(container);
        // }
        //
        // }
        // for (String key : launchMap.keySet()) {
        // containerRepo.save(launchMap.get(key));
        // }
        // return findAll();
      }
    }
    return findAll();
  }

  /**
   * Convert the containers.
   *
   * @param containerList
   *          the container list
   * @return the map
   */
  public Map<String, AppContainer> convert(List<AppContainer> containerList) {
    Map<String, AppContainer> containerMap = new HashMap<String, AppContainer>();
    for (AppContainer container : containerList) {
      // containerMap.put(container.getName(), container);
    }
    return containerMap;
  }

  /**
   * Container entity mapper util.
   *
   * @param deploymentListResponse
   *          the deployment list response
   * @return the app container
   * @throws Exception
   *           the exception
   */
  public AppContainer containerEntityMapperUtil(DeploymentList deploymentListResponse) throws Exception {
    AppContainer container = null;
    if (deploymentListResponse != null) {
      container = new AppContainer();
      // container.setName(deploymentListResponse.getName());
      if (deploymentListResponse.getAvailable().equals("0")) {
        container.setStatus(Status.STOPPED);
      } else {
        container.setStatus(Status.RUNNING);
      }
      container.setIsActive(true);
    }
    return container;
  }

  @Override
  public AppContainer findByDeployment(Long deploymentId) throws Exception {
    return containerRepo.findByDeployment(true, deploymentId);
  }

}
