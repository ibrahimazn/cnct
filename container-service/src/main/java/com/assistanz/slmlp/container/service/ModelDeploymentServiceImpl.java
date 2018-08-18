/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Launcher;
import com.assistanz.slmlp.container.entity.ModelDeployment;
import com.assistanz.slmlp.container.entity.Scoring;
import com.assistanz.slmlp.container.entity.Deployment.DeploymentType;
import com.assistanz.slmlp.container.error.exception.ApplicationException;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import com.assistanz.slmlp.container.repository.ModelDeploymentRepository;
import com.assistanz.slmlp.container.repository.OpenFaasRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.KubernetesService;
import com.assistanz.slmlp.container.util.error.Errors;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

import az.ancode.filemanager.connector.fileset.CsvFileResponse;
import az.ancode.kubectl.connector.RestTemplateCall;
import az.ancode.kubectl.connector.deployment.DeploymentListRequest;
import az.ancode.kubectl.connector.deployment.DeploymentListResponse;

/**
 * This class is implementing all the functionalities of Ancode model deployments service
 * interface.
 */
@Service
public class ModelDeploymentServiceImpl implements ModelDeploymentService {

  /** K8S Agent URL which is acquied from yml file. */
  @Value(value = "${kubernete.agent}")
  private String agentUrl;

  /** Secret key of K8S which is acquied from yml file. */
  @Value(value = "${kubernete.secKey}")
  private String auth;

  /** ContainerRepository reference. */
  @Autowired
  private ModelDeploymentRepository modelDeploymentRepo;

  @Autowired
  private OpenFaasRepository openFaasRepository;

  /** Openfaas service. */
  @Autowired
  OpenFaasService openFaasService;

  /** Scoring service. */
  @Autowired
  ScoringService scoringService;

  /** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  /** The Constant DEPLOYMENT. */
  private static final String DEPLOYMENT = "deployment";

  /** Validator attribute. */
  @Autowired
  private AppValidator validator;

  /** Launcher service. */
  @Autowired
  LauncherService launcherService;

  /** Deployment service. */
  @Autowired
  DeploymentService deploymentService;

  @Autowired
  FilemanagerService fileManagerService;


  /** RestTemplateCall service reference. */
  @Autowired
  private RestTemplateCall<DeploymentListRequest, DeploymentListResponse> deploymentListReq;

  @Override
  public ModelDeployment save(ModelDeployment modelDeployment) throws Exception {
    String user = k8sService.convertNameToK8sFromat(modelDeployment.getCreatedBy());
    modelDeployment.setSourceFile("/home/" + user + modelDeployment.getSourceFile());
    modelDeployment.setDatasetFile("/home/" + user + modelDeployment.getDatasetFile());
//    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String deploymentName = k8sService.convertNameToK8sFromat(modelDeployment.getModel().getName());
    modelDeployment.setName(DEPLOYMENT + deploymentName);
    modelDeployment.setPredictionOutputUrl("/home/" + user + modelDeployment.getPredictionOutputUrl() + "/" + modelDeployment.getName() + ".csv");
    modelDeployment.setProjectId(modelDeployment.getModel().getProjectId());
    modelDeployment.setModelId(modelDeployment.getModel().getId());
    modelDeployment.setModelVersionId(modelDeployment.getModelVersion().getId());
    modelDeployment.setCreatedAt(new Date());
    this.validateModelDeployment(modelDeployment);
    modelDeployment.setIsActive(true);
    Errors errors = validator.rejectIfNullEntity(DEPLOYMENT, modelDeployment);
    errors = validator.validateEntity(modelDeployment, errors);
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }

    String envProcess = modelDeployment.getSourceFile();

    if (envProcess.contains(GenericConstants.PYTHON_EXT)) {
      envProcess = GenericConstants.SCRIPT_PYTHON + envProcess;
    } else if(envProcess.contains(GenericConstants.R_EXT)){
      envProcess = GenericConstants.SCRIPT_R + envProcess;
    }



    Launcher launcher = modelDeployment.getLauncher();
    launcher.setName(launcher.getName());
    launcher.setUserId(modelDeployment.getUserId());
    launcher.setType("MODEL_PREDICTION");
    launcher.setDeploymentName(modelDeployment.getName());
    launcher.setProjectId(modelDeployment.getModel().getProjectId());
    launcher.setEnvProcess(envProcess);
    Deployment deployment = deploymentService.createDeployment(launcher, DeploymentType.OPENFAAS);
    deployment.setAppImage(launcher);
    openFaasService.create(deployment, modelDeployment.getCreatedBy());
    modelDeployment.setDeploymentId(deployment.getId());
    modelDeployment.setIsOnline(false);
    modelDeployment = modelDeploymentRepo.save(modelDeployment);
    return modelDeployment;
  }

  /**
   * Validate the model deployment.
   *
   * @param dataset
   *          reference of the scoring.
   * @throws Exception
   *           error occurs.
   */
  private void validateModelDeployment(ModelDeployment modelDeployment) throws Exception {
    Errors errors = validator.rejectIfNullEntity(DEPLOYMENT, modelDeployment);
    errors = validator.validateEntity(modelDeployment, errors);
    if (this.modelDeploymentRepo.findByNameAndIsActive(modelDeployment.getProjectId(), modelDeployment.getName(), true) != null) {
      errors.addGlobalError("Deployment already exist");
    }
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
  }

  @Override
  public ModelDeployment update(ModelDeployment container) throws Exception {
    return modelDeploymentRepo.save(container);
  }

  @Override
  public void delete(ModelDeployment container) throws Exception {
    modelDeploymentRepo.delete(container);
  }

  @Override
  public void delete(Long id) throws Exception {
    ModelDeployment modelDeployment = find(id);
    if (modelDeployment == null) {
      throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "Model deployment is not exists");
    }
    modelDeployment.setIsActive(false);
    modelDeploymentRepo.save(modelDeployment);
  }

  @Override
  public ModelDeployment find(Long id) throws Exception {
    return modelDeploymentRepo.findOne(id);
  }

  @Override
  public Page<ModelDeployment> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return null;
  }

  @Override
  public List<ModelDeployment> findAll() throws Exception {
    return (List<ModelDeployment>) modelDeploymentRepo.findAll();
  }

  public Page<ModelDeployment> findAllByActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception {
    return modelDeploymentRepo.findAllByActive(pagingAndSorting.toPageRequest(), true);
  }

  public Page<ModelDeployment> findAllPaginatedByProjectActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive) throws Exception {
    return modelDeploymentRepo.findAllPaginatedByProjectActive(pagingAndSorting.toPageRequest(), projectId, true);
  }

  @Override
  public List<ModelDeployment> findAllByProjectActive(Long projectId, Boolean isActive) throws Exception {
    return modelDeploymentRepo.findAllByProjectActive(projectId, true);
  }

  @Override
  public void deleteDeployment(Long id, String userName) throws Exception {
    ModelDeployment modelDeployment = find(id);
    modelDeployment.setIsActive(false);
    openFaasService.delete(modelDeployment.getName(), userName);
    Deployment deployment = modelDeployment.getDeployment();
    deployment.setIsActive(false);
    deployment.setDeploymentType(DeploymentType.OPENFAAS);
    deploymentService.save(deployment);
    modelDeploymentRepo.save(modelDeployment);
  }

  @Override
  public ModelDeployment getPredictedData(Long deploymentId, String user) throws Exception {
    ModelDeployment modelDeployment = find(deploymentId);
    Scoring scoring = scoringService.findAllByModelId(modelDeployment.getModelId(), true);
    String nameSpace = k8sService.convertNameToK8sFromat(user);
    String filePath  = modelDeployment.getPredictionOutputUrl().replace("/home/" +  nameSpace, "");
    String response = openFaasService.getPredictedData(modelDeployment, user);
    CsvFileResponse csvFileResponse = new CsvFileResponse();
    csvFileResponse = fileManagerService.getCsvFile(Long.valueOf(modelDeployment.getUserId()), filePath, "1", "10000");
    modelDeployment.setCsvResponse(csvFileResponse);
    if(scoring != null) {
      modelDeployment.setScoringResult(scoring.getScoreResult());
    }
    return modelDeployment;
  }


}
