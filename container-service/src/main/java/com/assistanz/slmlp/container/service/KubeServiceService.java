/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.entity.NodeServices;
import com.assistanz.slmlp.container.util.service.CRUDService;

@Service
public interface KubeServiceService  extends CRUDService<NodeServices> {
	
	NodeServices createKubeService(NodeServices nodeServices) throws Exception;
	
}
