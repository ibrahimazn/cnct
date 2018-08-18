/*
 *
 */
package com.assistanz.slmlp.container.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.entity.OpenFaasFunctions;
import com.assistanz.slmlp.container.entity.Scoring;
import com.assistanz.slmlp.container.entity.TrainingEngine;
import com.assistanz.slmlp.container.error.exception.ApplicationException;
import com.assistanz.slmlp.container.repository.OpenFaasRepository;
import com.assistanz.slmlp.container.repository.ScoringRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.KubernetesService;
import com.assistanz.slmlp.container.util.ModelInput;
import com.assistanz.slmlp.container.util.error.Errors;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.fasterxml.jackson.databind.ObjectMapper;

import az.openfaas.connector.RestCall;
import az.openfaas.connector.function.EnvVariable;
import az.openfaas.connector.function.FunctionCreateRequest;
import az.openfaas.connector.function.FunctionDeleteRequest;

/**
 * The Class ScoringService Implementation.
 */
@Service
public class ScoringServiceImpl implements ScoringService {

  /** scoringRepository reference. */
  @Autowired
  private ScoringRepository scoringRepository;

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ScoringServiceImpl.class);

  /** The open faas repository. */
  @Autowired
  private OpenFaasRepository openFaasRepository;

  @Autowired
  private RestTemplate restTemplate;

  /** The training engine service. */
  @Autowired
  private TrainingEngineService trainingEngineService;

  /** Constant for scoring. */
  private static final String SCORING = "scoring";

  /** The Constant PREDICT. */
  private static final String PREDICT = "score";
  
  /** The Constant SCORE. */
  private static final String SCORE = "SCORE";
  
  @Autowired
  private RestCall<FunctionCreateRequest, String> functionCreate;

  @Autowired
  private RestCall<FunctionDeleteRequest, String> functionDelete;

  @Value(value = "${openfaas.agent}")
  private String agentUrl;

  /** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  /** Validator attribute. */
  @Autowired
  private AppValidator validator;

  @Override
  public Scoring save(Scoring scoring) throws Exception {
    scoring.setIsActive(true);
    scoring.setName(k8sService.convertNameToK8sFromat(PREDICT+"-"+scoring.getModel().getName()+"-"+scoring.getModelVersion().getId()));
    scoring.setCreatedAt(new Date());
    scoring.setProjectId(scoring.getModel().getProjectId());
    this.validateDataSet(scoring);
    Errors errors = validator.rejectIfNullEntity(SCORING, scoring);
    errors = validator.validateEntity(scoring, errors);
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
    Scoring persistScoring = scoringRepository.save(scoring);
    if (persistScoring.getProject() != null) {
      persistScoring.setPredictionOutputUrl(persistScoring.getProject().getModelPath());
      persistScoring = scoringRepository.save(scoring);
    }
    String createdBy = k8sService.convertNameToK8sFromat(scoring.getCreatedBy());
    OpenFaasFunctions openFaas = new OpenFaasFunctions();
    String envProcess = scoring.getScoreFile();
    String platFormTool = "";
    if (envProcess.contains(GenericConstants.PYTHON_EXT)) {
      envProcess = GenericConstants.SCRIPT_PYTHON + envProcess;
      platFormTool = GenericConstants.PLATFORM_PYTHON;
    } else if(envProcess.contains(GenericConstants.R_EXT)){
      envProcess = GenericConstants.SCRIPT_R + envProcess;
      platFormTool = GenericConstants.PLATFORM_R;
    }
    TrainingEngine trainingEngine = trainingEngineService.findByTypeAndIsActive(GenericConstants.CUSTOM_ENGINE, true, platFormTool);
    if (trainingEngine != null) {

      String status = functionCreate.restCall(agentUrl + GenericConstants.OPENFAAS_FUNCTION_CREATE,
          makeRequest(GenericConstants.HOME_PATH + createdBy + scoring.getScoreFile(),
              scoring.getCreatedBy(), scoring.getName(), trainingEngine.getImage()),
          String.class, createdBy, GenericConstants.METHOD_POST);
      if (status != null && !status.isEmpty()) {
        errors.addGlobalError(status);
        throw new ApplicationException(errors);
      }
      openFaas.setCreatedAt(new Date());
      openFaas.setCreatedBy(createdBy);
      openFaas.setEnvProcess(envProcess);
      openFaas.setFunctionName(persistScoring.getName());
      openFaas.setImage(trainingEngine.getImage());
      openFaas.setModelId(persistScoring.getModelId());
      openFaas.setFunctionType(SCORE);
      openFaas.setIsActive(true);
      openFaas.setProjectId(persistScoring.getModel().getProjectId());
      openFaas = openFaasRepository.save(openFaas);
    }
    return persistScoring;
  }

  @Override
  public Scoring update(Scoring scoring) throws Exception {
    return scoringRepository.save(scoring);
  }

  @Override
  public void delete(Scoring scoring) throws Exception {
    scoringRepository.delete(scoring);
  }

  @Override
  public void delete(Long id) throws Exception {
    scoringRepository.delete(id);
  }

  @Override
  public Scoring find(Long id) throws Exception {
    return scoringRepository.findOne(id);
  }

  @Override
  public Page<Scoring> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return scoringRepository.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<Scoring> findAll() throws Exception {
    return (List<Scoring>) scoringRepository.findAll();
  }

  private FunctionCreateRequest makeRequest(String envProcess, String userName, String name, String image) {
    String createdBy = k8sService.convertNameToK8sFromat(userName);
    FunctionCreateRequest req = new FunctionCreateRequest();
    if (envProcess.contains(GenericConstants.PYTHON_EXT)) {
      envProcess = GenericConstants.SCRIPT_PYTHON + envProcess;
    } else if(envProcess.contains(GenericConstants.R_EXT)){
      envProcess = GenericConstants.SCRIPT_R + envProcess;
    }
    req.setEnvProcess(envProcess);
    EnvVariable envVars = new EnvVariable();
    envVars.setNameSpace(createdBy);
    envVars.setVolumeName(createdBy + GenericConstants.HYPHEN + GenericConstants.VOLUME);
    req.setEnvVars(envVars);
    req.setService(name);
    req.setImage(image);
    return req;
  }

  private FunctionDeleteRequest deleteRequest(String name) {
    FunctionDeleteRequest deleteReq = new FunctionDeleteRequest();
    deleteReq.setFunctionName(name);
    return deleteReq;
  }

  @Override
  public List<Scoring> findAllByIsActive(Boolean isActive) throws Exception {
    return scoringRepository.findAllByIsActive(isActive);
  }

  @Override
  public Scoring softDelete(Scoring scoring) throws Exception {
    String createdBy = k8sService.convertNameToK8sFromat(scoring.getCreatedBy());
    scoring.setIsActive(false);
    Scoring persistScoring = scoringRepository.save(scoring);
    try {
      functionDelete.restCall(agentUrl + GenericConstants.OPENFAAS_FUNCTION_CREATE+ GenericConstants.NAMESPACE_REQUEST + createdBy, deleteRequest(scoring.getName()),
          String.class, createdBy, GenericConstants.METHOD_DELETE);
    } catch (Exception ce) {
      LOGGER.error("ERROR at score delete " + ce.getMessage());
      return persistScoring;
    }
    return persistScoring;
  }

  @Override
  public void savePredictions(List<Scoring> scorings, Long projectId) throws Exception {
    for (Scoring scoring : scorings) {
      if (projectId != null) {
        scoring.setProjectId(projectId);
        scoringRepository.save(scoring);
      } else {
        save(scoring);
      }
    }
  }

  @Override
  public Page<Scoring> findAllByIsActive(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive)
      throws Exception {
    return scoringRepository.findAllByIsActive(pagingAndSorting.toPageRequest(), projectId, isActive);
  }

  @Override
  public Page<Scoring> findAllBySearchText(PagingAndSorting pagingAndSorting, Long projectId, String searchText)
      throws Exception {
    return scoringRepository.findBySearchText(pagingAndSorting.toPageRequest(), projectId, searchText, true);
  }

  /**
   * Validate the scoring.
   *
   * @param dataset
   *          reference of the scoring.
   * @throws Exception
   *           error occurs.
   */
  private void validateDataSet(Scoring scoring) throws Exception {
    Errors errors = validator.rejectIfNullEntity(SCORING, scoring);
    errors = validator.validateEntity(scoring, errors);
    if (this.scoringRepository.findByNameAndIsActive(scoring.getProjectId(), scoring.getName(), true) != null) {
      errors.addGlobalError("Scoring Name already exist");
    }
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * com.assistanz.slmlp.container.service.ScoringService#runScore(com.assistanz
   * .slmlp.container.entity.Scoring)
   */
  @Override
  public Scoring runScore(Scoring scoring) throws Exception {
    scoring = scoringRepository.findByNameAndIsActive(scoring.getProjectId(),scoring.getName(), true);
    String createdBy = k8sService.convertNameToK8sFromat(scoring.getCreatedBy());
    ModelInput scoringInput = new ModelInput();
    scoringInput
        .setDataset(GenericConstants.HOME_PATH + createdBy + scoring.getDatasetFile());
    scoringInput.setSplitratio(String.valueOf(scoring.getSplitRatio()));
    scoringInput.setModelLocation(GenericConstants.HOME_PATH + createdBy
        + scoring.getModelVersion().getModelFile());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(GenericConstants.X_USER, createdBy);
    String json = new ObjectMapper().writeValueAsString(scoringInput);
    LOGGER.info("json " + json);
    LOGGER.info("headers " + headers);
    LOGGER.info("URL " + agentUrl + GenericConstants.INVOKE_FUNCTION + scoring.getName());
    HttpEntity<String> entity = new HttpEntity<>(json, headers);
    ResponseEntity<String> entityResponse = restTemplate.postForEntity(agentUrl + GenericConstants.INVOKE_FUNCTION + scoring.getName(),
        entity, String.class);
    scoring.setScoreResult(entityResponse.getBody().toString());
    LOGGER.info("Result " , scoring.getScoreResult());
    return scoringRepository.save(scoring);
  }

  @Override
  public Scoring findAllByModelId(Long modelId, Boolean isActive) throws Exception {
    return scoringRepository.findAllByModelId(modelId, isActive);
  }

  @Override
  public void deleteByProject(Long projectId) throws Exception {
    List<Scoring> scorings = findAllByIsActiveAndProject(true, projectId);
    if (scorings != null) {
      for (Scoring scoring : scorings) {
        softDelete(scoring);
      }
    }
  }

  @Override
  public List<Scoring> findAllByIsActiveAndProject(Boolean isActive, Long projectId) throws Exception {
    return scoringRepository.findAllByIsActiveAndProject(isActive, projectId);
  }
}
