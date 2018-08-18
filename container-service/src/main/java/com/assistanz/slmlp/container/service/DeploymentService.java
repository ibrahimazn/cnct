/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Launcher;
import com.assistanz.slmlp.container.entity.Deployment.DeploymentType;

import az.ancode.kubectl.connector.deployment.DeploymentDeleteResponse;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeEvent;

/**
 * The Interface DeploymentService.
 */
@Service
public interface DeploymentService extends CRUDService<Deployment> {

  /**
   * Find Deployment by name.
   *
   * @param name the name
   * @return the deployment
   */
  Deployment findByName(String name);

  /**
   * Find all by active.
   *
   * @param active the active
   * @return the list
   */
  List<Deployment> findAllByActive(Boolean active);

  /**
   * Creates the deployment.
   *
   * @param launcher the launcher
   * @param deploymentType type either openfaas / k8s
   * @return the deployment
   * @throws Exception the exception
   */
  Deployment createDeployment(Launcher launcher, DeploymentType deploymentType) throws Exception;

  /**
   * Destroy the Deployment.
   *
   * @param deployment the deployment
   * @return the deployment delete response
   * @throws Exception the exception
   */
  DeploymentDeleteResponse destroy(Deployment deployment) throws Exception;

  /**
   * Update kafka events.
   *
   * @param event the event
   * @param kindName the kind name
   * @throws Exception the exception
   */
  void updateKafkaEvents(KafkaKubeEvent event, String kindName) throws Exception;

  /**
   * Find Deployment by unique id.
   *
   * @param uid the unique id
   * @return the deployment
   */
  Deployment findByUid(String uid);

  /**
   * Find by name space id.
   *
   * @param nameSpaceId the name space id
   * @return the deployment
   */
  Deployment findByNameSpaceId(Long nameSpaceId);

  /**
   * Find by replica set name.
   *
   * @param replicaSetName the replica set name
   * @return the deployment
   */
  Deployment findByReplicaSetName(String replicaSetName);

  /**
   * Find all by active and user id and type.
   *
   * @param active the active
   * @param userId the user id
   * @param type the type
   * @return the list
   * @throws Exception the exception
   */
  List<Deployment> findAllByActiveUserIdAndProjects(Boolean active, Long userId, List<Long> projectIds) throws Exception;

  /**
   * Find all active deployments based on the user and type.
   *
   * @param active the active boolean value
   * @param userId id of the user.
   * @param type the type
   * @return the type
   * @throws Exception
   */
  List<Deployment> findAllByActiveAndUserIdAndType(Boolean active, Long userId, String type) throws Exception;
  
  List<Deployment> findAllByActiveAndUserId(Boolean active, Long userId) throws Exception;



}
