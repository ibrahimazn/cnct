/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.ModelDeployment;
import com.assistanz.slmlp.container.util.KubernetesService;

import az.openfaas.connector.RestCall;
import az.openfaas.connector.function.EnvVariable;
import az.openfaas.connector.function.FunctionCreateRequest;
import az.openfaas.connector.function.FunctionDeleteRequest;
import az.openfaas.connector.function.PredictedDataRequest;

/**
 * Default service for the open faas conneector.
 *
 * @author azn0084
 *
 */
@Service
public class OpenFaasServiceImpl implements OpenFaasService {

  /** K8S Agent URL which is acquied from yml file. */
  @Value(value = "${openfaas.agent}")
  private String openFaasUrl;

  /** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  @Autowired
  private RestCall<FunctionCreateRequest, String> functionCreateReq;

  @Autowired
  private RestCall<FunctionDeleteRequest, String> functionDeleteReq;

  @Autowired
  private RestCall<PredictedDataRequest, String> predictedDataReq;

  @Override
  public String create(Deployment deployment, String user) throws Exception {
    String k8sName = k8sService.convertNameToK8sFromat(user);
    FunctionCreateRequest req = new FunctionCreateRequest();
    req.setEnvProcess(deployment.getEnvProcess());
    EnvVariable envVars = new EnvVariable();
    envVars.setNameSpace(k8sName);
    envVars.setVolumeName(k8sName + "-" + "volume");
    req.setEnvVars(envVars);
    req.setService(deployment.getName());
    req.setImage(deployment.getAppImage().getImage());
    String response = functionCreateReq.restCall(openFaasUrl + "system/functions",
        req, String.class, user, "POST");
    return response;

  }

  @Override
  public void delete(String name, String user) throws Exception {
    FunctionDeleteRequest request = new FunctionDeleteRequest();
    request.setFunctionName(name);
    String nameSpace = k8sService.convertNameToK8sFromat(user);
    String response = functionDeleteReq.restCall(openFaasUrl + "system/functions?namespace=" + nameSpace,
    request, String.class, user, "DELETE");



  }

  @Override
  public String getPredictedData(ModelDeployment modelDeployment, String user) throws Exception {
    // TODO Auto-generated method stub
    PredictedDataRequest request = new PredictedDataRequest();
    request.setDataset(modelDeployment.getDatasetFile());
    String nameSpace = k8sService.convertNameToK8sFromat(user);
    String modelFile = "";
    if(modelDeployment.getModelVersion() != null) {
      String tempArray[]= modelDeployment.getModelVersion().getModelFile().split("/");
      modelFile = tempArray[tempArray.length - 1];
    }
    modelFile =  "/home/" +  nameSpace + modelDeployment.getModelVersion().getModelFile();
    request.setModelLocation(modelFile);
    request.setTargetFileLocation(modelDeployment.getPredictionOutputUrl());
    String response = predictedDataReq.restCall(openFaasUrl + "function/" + modelDeployment.getName(),
        request, String.class, nameSpace, "POST");
    return response;
  }
}
