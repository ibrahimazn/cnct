/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.entity.AppContainer;
import com.assistanz.slmlp.container.util.service.CRUDService;

/**
 * Container service interface is for declaring CRUDService and declared some
 * required service functions.
 */
@Service
public interface ContainerService extends CRUDService<AppContainer> {

  /**
   * Find all active containers.
   * 
   * @param active true passed fir retrieving active containers .
   * @return containers.
   */
  List<AppContainer> findAllByActive(Boolean active);

  /**
   * Sync all containers from K8S to database.
   * 
   * @param name the namespace.
   * @return list of containers.
   * @throws Exception if error occurs.
   */
  List<AppContainer> syncContainers(String name) throws Exception;

  /**
   * Find the container by deployment id.
   * 
   * @param deploymentId the deploymentId.
   * @return the container which was mapped by the deployment id.
   * @throws Exception if error occurs.
   */
  AppContainer findByDeployment(Long deploymentId) throws Exception;

}
