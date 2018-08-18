/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import com.assistanz.slmlp.container.entity.ReplicaSet;
import com.assistanz.slmlp.container.util.service.CRUDService;

public interface ReplicaSetService  extends CRUDService<ReplicaSet> {
	
	ReplicaSet findByPodName(String podName);
	
}
