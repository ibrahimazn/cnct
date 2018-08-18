/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import org.springframework.stereotype.Service;

import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.ModelDeployment;


/**
 * Container service interface is for declaring CRUDService and declared some
 * required service functions.
 */
@Service
public interface OpenFaasService {

    String create(Deployment deployment, String user)  throws Exception;

    void delete(String name, String user) throws Exception;

    String getPredictedData(ModelDeployment modelDeployment, String user) throws Exception;
}
