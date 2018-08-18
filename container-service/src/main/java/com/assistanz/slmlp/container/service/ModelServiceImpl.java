/*
 *
 */
package com.assistanz.slmlp.container.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.entity.Model.TYPE;
import com.assistanz.slmlp.container.entity.ModelVersion;
import com.assistanz.slmlp.container.entity.OpenFaasFunctions;
import com.assistanz.slmlp.container.entity.Sampling;
import com.assistanz.slmlp.container.entity.TrainingEngine;
import com.assistanz.slmlp.container.entity.TrainingJob;
import com.assistanz.slmlp.container.error.exception.ApplicationException;
import com.assistanz.slmlp.container.repository.ModelRepository;
import com.assistanz.slmlp.container.repository.ModelVersionRepository;
import com.assistanz.slmlp.container.repository.OpenFaasRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.KubernetesService;
import com.assistanz.slmlp.container.util.error.Errors;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

import az.ancode.kubectl.connector.CustomGenericException;
import az.openfaas.connector.RestCall;
import az.openfaas.connector.function.EnvVariable;
import az.openfaas.connector.function.FunctionCreateRequest;
import az.openfaas.connector.function.FunctionDeleteRequest;

/**
 * The Class ModelService Implementation.
 */
@Service
public class ModelServiceImpl implements ModelService {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ModelServiceImpl.class);

  /** ModelRepository reference. */
  @Autowired
  private ModelRepository modelRepository;

  /** The training job service. */
  @Autowired
  private TrainingJobService trainingJobService;

  /** The training engine service. */
  @Autowired
  private TrainingEngineService trainingEngineService;

  /** The open faas repository. */
  @Autowired
  private OpenFaasRepository openFaasRepository;

  /** The model version repository. */
  @Autowired
  private ModelVersionRepository modelVersionRepository;

  /** The function create. */
  @Autowired
  private RestCall<FunctionCreateRequest, String> functionCreate;

  /** The function delete. */
  @Autowired
  private RestCall<FunctionDeleteRequest, String> functionDelete;

  /** The agent url. */
  @Value(value = "${openfaas.agent}")
  private String agentUrl;

  /** The sampling service. */
  @Autowired
  private SamplingService samplingService;

  /** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  /** Constant for model. */
  private static final String MODEL = "model";

  /** The Constant OPENFAAS_MODEL. */
  private static final String OPENFAAS_MODEL = "MODEL";

  /** The model version service. */
  @Autowired
  private ModelVersionService modelVersionService;

  /** Validator attribute. */
  @Autowired
  private AppValidator validator;

  @Override
  public Model save(Model model) throws Exception {
    LOGGER.debug("new model creation " + model.toString());
    Sampling sampling = addSampling(model);
    if (sampling != null) {
      LOGGER.debug("sampling added for new model creation " + sampling.getId());
      model.setSamplingId(sampling.getId());
    }
    model.setIsActive(true);
    if (model.getDataSet() != null) {
      model.setDataSetId(model.getDataSet().getId());
    }
    if (model.getModelType().equals(TYPE.CUSTOM)) {
      model.setAlgorithm(GenericConstants.CUSTOM_MODEL);
    }
    model.setCreatedAt(new Date());
    this.validateDataSet(model);
    Errors errors = validator.rejectIfNullEntity(MODEL, model);
    errors = validator.validateEntity(model, errors);
    if (errors.hasErrors()) {
      LOGGER.error("model creation error " + errors.toString());
      throw new ApplicationException(errors);
    }
    Model persistModel = modelRepository.save(model);
    if (persistModel.getProject() != null) {
      persistModel.setPublishUrl(persistModel.getProject().getModelPath());
      persistModel.setUserId(persistModel.getProject().getCreatedUserId());
      persistModel = modelRepository.save(model);
    }
    if (persistModel.getModelFile() != null) {
      LOGGER.debug("import new model file " + persistModel.getModelFile());
      persistModel = saveModel(persistModel);
    }
    if (persistModel.getModelFileSrc() != null) {
      LOGGER.debug("import new model script " + persistModel.getModelFileSrc());
      persistModel = update(persistModel);
    }
    return persistModel;
  }

  private Sampling addSampling(Model model) throws Exception {
    Sampling sampling = model.getSampling();
    if (sampling != null) {
      sampling = samplingService.save(sampling);
    }
    return sampling;
  }

  @Override
  public Model update(Model model) throws Exception {
    LOGGER.debug("model openfaas function creation", model.toString());
    model = modelRepository.save(model);
    OpenFaasFunctions openFaas = new OpenFaasFunctions();
    String envProcess = model.getModelFileSrc();
    String platFormTool = "";
    if (envProcess.contains(GenericConstants.PYTHON_EXT)) {
      LOGGER.debug("model openfaas function creation for python", envProcess);
      envProcess = GenericConstants.SCRIPT_PYTHON + envProcess;
      platFormTool = GenericConstants.PLATFORM_PYTHON;
    } else if (envProcess.contains(GenericConstants.R_EXT)) {
      LOGGER.debug("model openfaas function creation for R", envProcess);
      envProcess = GenericConstants.SCRIPT_R + envProcess;
      platFormTool = GenericConstants.PLATFORM_R;
    }
    List<TrainingEngine> trainingEngines = model.getProject().getTrainingEngines();
    String trainingEngineType = GenericConstants.CUSTOM_ENGINE;
    openFaas.setEngineType(GenericConstants.CUSTOM_ENGINE);
    if (trainingEngines.size() > 0) {
      trainingEngineType = trainingEngines.get(0).getType();
      openFaas.setEngineType(trainingEngines.get(0).getType());
    }
    TrainingEngine trainingEngine = trainingEngineService.findByTypeAndIsActive(GenericConstants.CUSTOM_ENGINE, true,
        platFormTool);
    if (trainingEngine != null) {
      LOGGER.debug("training engine for platform", trainingEngine.toString());
      List<ModelVersion> modelVersions = modelVersionService.findAllByIsActiveAndModel(true, model.getId());
      String createdBy = k8sService.convertNameToK8sFromat(model.getCreatedBy());
      String status = functionCreate.restCall(agentUrl + GenericConstants.OPENFAAS_FUNCTION_CREATE,
          makeRequest(GenericConstants.HOME_PATH + createdBy + model.getModelFileSrc(), model.getCreatedBy(),
              k8sService.convertNameToK8sFromat(
                  model.getName() + GenericConstants.HYPHEN + GenericConstants.VERSION + (modelVersions.size() + 1)),
              trainingEngine.getImage()),
          String.class, createdBy, GenericConstants.METHOD_POST);
      if (status != null && !status.isEmpty()) {
        throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED_EXCEPTION, status);
      }
      openFaas.setCreatedAt(new Date());
      openFaas.setCreatedBy(createdBy);
      openFaas.setEnvProcess(envProcess);
      openFaas.setFunctionName(k8sService.convertNameToK8sFromat(
          model.getName() + GenericConstants.HYPHEN + GenericConstants.VERSION + (modelVersions.size() + 1)));
      openFaas.setImage(trainingEngine.getImage());
      openFaas.setModelId(model.getId());
      openFaas.setFunctionType(OPENFAAS_MODEL);
      openFaas.setIsActive(true);
      openFaas.setProjectId(model.getProjectId());
      openFaas = openFaasRepository.save(openFaas);
    }
    return model;
  }

  private FunctionCreateRequest makeRequest(String envProcess, String userName, String name, String image) {
    String k8sName = k8sService.convertNameToK8sFromat(userName);
    FunctionCreateRequest req = new FunctionCreateRequest();
    if (envProcess.contains(GenericConstants.PYTHON_EXT)) {
      envProcess = GenericConstants.SCRIPT_PYTHON + envProcess;
    } else if (envProcess.contains(GenericConstants.R_EXT)) {
      envProcess = GenericConstants.SCRIPT_R + envProcess;
    }
    req.setEnvProcess(envProcess);
    EnvVariable envVars = new EnvVariable();
    envVars.setNameSpace(k8sName);
    envVars.setVolumeName(k8sName + GenericConstants.HYPHEN + GenericConstants.VOLUME);
    req.setEnvVars(envVars);
    req.setService(name);
    req.setImage(image);
    LOGGER.debug("function create request for model", req.toString());
    return req;
  }

  private FunctionDeleteRequest deleteRequest(String name) {
    FunctionDeleteRequest deleteReq = new FunctionDeleteRequest();
    deleteReq.setFunctionName(name);
    return deleteReq;
  }

  @Override
  public void delete(Model model) throws Exception {
    modelRepository.delete(model);
  }

  @Override
  public void delete(Long id) throws Exception {
    modelRepository.delete(id);
  }

  @Override
  public Model find(Long id) throws Exception {
    return modelRepository.findOne(id);
  }

  @Override
  public Page<Model> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return modelRepository.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<Model> findAll() throws Exception {
    return (List<Model>) modelRepository.findAll();
  }

  @Override
  public List<Model> findAllByIsActive(Boolean isActive) throws Exception {
    return modelRepository.findAllByIsActive(isActive);
  }

  @Override
  public Model softDelete(Model model) throws Exception {
    OpenFaasFunctions openFaas = openFaasRepository.findByModelAndIsActiveAndType(model.getId(), true, "MODEL");
    String k8sName = k8sService.convertNameToK8sFromat(model.getCreatedBy());
    model.setIsActive(false);
    if (openFaas != null) {
      LOGGER.debug("delete model openfaas function", openFaas.toString());
      LOGGER.debug("model delete", model.toString());
      functionDelete.restCall(
          agentUrl + GenericConstants.OPENFAAS_FUNCTION_CREATE + GenericConstants.NAMESPACE_REQUEST + k8sName,
          deleteRequest(openFaas.getFunctionName()), String.class, k8sName, GenericConstants.METHOD_DELETE);
      LOGGER.debug("openfaas model delete call", agentUrl + GenericConstants.OPENFAAS_FUNCTION_CREATE + GenericConstants.NAMESPACE_REQUEST + k8sName);
      LOGGER.debug("function name "+ openFaas.getFunctionName());
      List<TrainingJob> trainingJobs = trainingJobService.findByModelIdAndIsActive(true, model);
      for (TrainingJob trainingJob : trainingJobs) {
        if (trainingJob != null) {
          List<Model> models = new ArrayList<Model>();
          for (Model permodel : trainingJob.getModels()) {
            if (permodel.getId() != model.getId()) {
              models.add(permodel);
            } else if (trainingJob.getModels().size() == 1 && permodel.getId() == model.getId()) {
              trainingJob.setIsActive(false);
            }
          }
        }
        trainingJobService.save(trainingJob);
      }
    }
    return modelRepository.save(model);
  }

  @Override
  public void saveModels(List<Model> models, Long projectId) throws Exception {
    for (Model model : models) {
      if (projectId != null) {
        model.setProjectId(projectId);
        modelRepository.save(model);
      } else {
        save(model);
      }
    }
  }

  @Override
  public Page<Model> findAllByIsActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive)
      throws Exception {
    return modelRepository.findAllByIsActive(pagingAndSorting.toPageRequest(), projectId, isActive);
  }

  @Override
  public Page<Model> findAllBySearchText(PagingAndSorting pagingAndSorting, Long projectId, String searchText)
      throws Exception {
    return modelRepository.findBySearchText(pagingAndSorting.toPageRequest(), projectId, searchText, true);
  }

  /**
   * Validate the model.
   *
   * @param dataset
   *          reference of the model.
   * @throws Exception
   *           error occurs.
   */
  private void validateDataSet(Model model) throws Exception {
    Errors errors = validator.rejectIfNullEntity(MODEL, model);
    errors = validator.validateEntity(model, errors);
    if (this.modelRepository.findByNameAndIsActive(model.getProjectId(), model.getName(), true) != null) {
      errors.addGlobalError("Model Name already exist");
    }
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
  }

  @Override
  public List<Model> findAllByIsActiveAndProject(Boolean isActive, Long projectId) throws Exception {
    return modelRepository.findAllByIsActiveAndProject(isActive, projectId);
  }

  @Override
  public Model saveModel(Model model) throws Exception {
    LOGGER.debug("model version updated here after update model file in existing model " + model.toString());
    List<ModelVersion> modelVersions = modelVersionService.findAllByIsActiveAndModel(true, model.getId());
    ModelVersion modelVersion = new ModelVersion();
    modelVersion.setUuid(UUID.randomUUID().toString());
    modelVersion.setCreatedAt(new Date());
    modelVersion.setVersion(GenericConstants.VERSION + (modelVersions.size() + 1));
    LOGGER.debug("updated model version is " + GenericConstants.VERSION + (modelVersions.size() + 1) + " "
        + modelVersion.getUuid());
    modelVersion.setModelId(model.getId());
    modelVersion.setUserId(model.getUserId());
    modelVersion.setIsActive(true);
    modelVersion.setModelFile(model.getModelFile());
    modelVersion = modelVersionRepository.save(modelVersion);
    model.setLatestVersion(modelVersion.getUuid());
    return modelRepository.save(model);
  }

  @Override
  public void deleteByProject(Long projectId) throws Exception {
    List<Model> models = findAllByIsActiveAndProject(true, projectId);
    if (models != null) {
      for (Model model : models) {
        softDelete(model);
      }
    }
    
  }

}
