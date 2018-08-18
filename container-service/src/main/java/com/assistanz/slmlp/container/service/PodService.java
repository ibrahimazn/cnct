/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.stereotype.Service;
import  com.assistanz.slmlp.container.entity.AppContainer;
import com.assistanz.slmlp.container.entity.Pod;
import com.assistanz.slmlp.container.util.service.CRUDService;

import az.ancode.kubectl.connector.deployment.DeploymentDeleteResponse;

@Service
public interface PodService extends CRUDService<Pod> {
	
	Pod findByReplicaSetName(String replicaSetName);
	
	Pod findByUidAndIsActive(String uid, Boolean isActive);
}
