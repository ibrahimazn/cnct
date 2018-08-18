package com.assistanz.slmlp.container.service;

import java.io.File;
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
import com.assistanz.slmlp.container.entity.ModelVersion;
import com.assistanz.slmlp.container.entity.OpenFaasFunctions;
import com.assistanz.slmlp.container.entity.TrainingEngine;
import com.assistanz.slmlp.container.entity.TrainingJob;
import com.assistanz.slmlp.container.repository.OpenFaasRepository;
import com.assistanz.slmlp.container.repository.TrainingJobRepository;
import com.assistanz.slmlp.container.util.KubernetesService;
import com.assistanz.slmlp.container.util.ModelInput;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TrainingJobServiceImpl implements TrainingJobService {

  @Autowired
  private TrainingJobRepository trainingJobRepository;

  @Autowired
  private TrainingEngineService trainingEngineService;

  @Autowired
  private OpenFaasRepository openFaasRepository;

  @Value(value = "${openfaas.agent}")
  private String agentUrl;

  /** The Constant MODEL. */
  private static final String MODEL = "MODEL";

  @Autowired
  private RestTemplate restTemplate;

  /** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ScoringServiceImpl.class);

  /** The model service. */
  @Autowired
  private ModelService modelService;

  /** The model version service. */
  @Autowired
  private ModelVersionService modelVersionService;

  @Override
  public TrainingJob save(TrainingJob trainingJob) throws Exception {
    return trainingJobRepository.save(trainingJob);
  }

  @Override
  public TrainingJob update(TrainingJob trainingJob) throws Exception {
    return trainingJobRepository.save(trainingJob);
  }

  @Override
  public void delete(TrainingJob trainingJob) throws Exception {
    trainingJobRepository.delete(trainingJob);

  }

  @Override
  public void delete(Long id) throws Exception {
    trainingJobRepository.delete(id);
  }

  @Override
  public TrainingJob find(Long id) throws Exception {
    return trainingJobRepository.findOne(id);
  }

  @Override
  public Page<TrainingJob> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return trainingJobRepository.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<TrainingJob> findAll() throws Exception {
    return (List<TrainingJob>) trainingJobRepository.findAll();
  }

  @Override
  public Page<TrainingJob> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception {
    return trainingJobRepository.findAllByIsActive(pagingAndSorting.toPageRequest(), isActive);
  }

  @Override
  public TrainingJob startTraining(TrainingJob trainingJob, Long userId, String userName) throws Exception {
    for (Model model : trainingJob.getModels()) {
      model = modelService.find(model.getId());
      String envProcess = model.getModelFileSrc();
      String platFormTool = "";
      String modelExt = "";
      if (envProcess.contains(GenericConstants.PYTHON_EXT)) {
        envProcess = GenericConstants.SCRIPT_PYTHON + envProcess;
        platFormTool = GenericConstants.PLATFORM_PYTHON;
        modelExt = GenericConstants.PYTHON_MODEL;
      } else if (envProcess.contains(GenericConstants.R_EXT)) {
        envProcess = GenericConstants.SCRIPT_R + envProcess;
        platFormTool = GenericConstants.PLATFORM_R;
        modelExt = GenericConstants.R_MODEL;
      }
      TrainingEngine trainingEngine = trainingEngineService.findByTypeAndIsActive(GenericConstants.CUSTOM_ENGINE, true,
          platFormTool);
      String createdBy = k8sService.convertNameToK8sFromat(model.getCreatedBy());
      OpenFaasFunctions openFaas = openFaasRepository.findByModelAndIsActiveAndType(model.getId(), true, MODEL);
      if (openFaas != null) {
        List<ModelVersion> modelVersions = modelVersionService.findAllByIsActiveAndModel(true, model.getId());
        trainingJob.setJobName(openFaas.getFunctionName() + GenericConstants.HYPHEN + (modelVersions.size() + 1));
        trainingJob.setIsActive(true);
        trainingJob.setDataSetId(model.getDataSetId());
        trainingJob.setTrainingEngineId(trainingEngine.getId());
        trainingJob.setFilePath(model.getDatasetFile());
        ModelInput scoringInput = new ModelInput();
        scoringInput.setDataset(GenericConstants.HOME_PATH + createdBy + model.getDatasetFile());
        scoringInput.setSplitratio(String.valueOf(trainingJob.getTestRatio()));
        scoringInput.setModelLocation(
            GenericConstants.HOME_PATH + createdBy + trainingJob.getOutputPath() + File.separator + model.getName()
                + GenericConstants.HYPHEN + GenericConstants.VERSION + (modelVersions.size() + 1) + modelExt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(GenericConstants.X_USER, createdBy);
        String json = new ObjectMapper().writeValueAsString(scoringInput);
        LOGGER.info("json " + json);
        LOGGER.info("headers " + headers);
        LOGGER.info("URL " + agentUrl + GenericConstants.INVOKE_FUNCTION + openFaas.getFunctionName());
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> entityResponse = restTemplate.postForEntity(
            agentUrl + GenericConstants.INVOKE_FUNCTION + openFaas.getFunctionName(), entity, String.class);
        
        LOGGER.info("Result", entityResponse.getBody().toString());
      }
      trainingJob = save(trainingJob);
    }
    return trainingJob;
  }

  @Override
  public TrainingJob getByJobName(String jobName) throws Exception {
    return trainingJobRepository.findByJobNameAndIsActive(jobName, true);
  }

  @Override
  public Page<TrainingJob> findAllByIsActiveAndModel(PagingAndSorting pagingAndSorting, Boolean isActive, Long modelId)
      throws Exception {
    Model model = modelService.find(modelId);
    return trainingJobRepository.findAllByIsActiveAndModel(pagingAndSorting.toPageRequest(), true, model);
  }


  @Override
  public List<TrainingJob> findByModelIdAndIsActive(Boolean active, Model model) throws Exception {
    // TODO Auto-generated method stub
    return trainingJobRepository.findAllByActiveAndModel(true, model);
  }

}
