package com.appfiss.connector.k8s.deployment;

import com.appfiss.connector.k8s.Request;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Function to deploy.
 * 
 * eg:- API : /list-deployments Method : GET Headers : {Content-Type :
 * application/x-www-form-urlencoded}
 * 
 * Query parameters : namespace, type
 * 
 */
@JsonIgnoreProperties
public class DeploymentListRequest extends Request {

}
