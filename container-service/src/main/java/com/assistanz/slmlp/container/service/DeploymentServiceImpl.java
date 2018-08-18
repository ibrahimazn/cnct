/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.AppContainer;
import com.assistanz.slmlp.container.entity.AppContainer.Status;
import com.assistanz.slmlp.container.entity.Deployment.DeploymentType;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Launcher;
import com.assistanz.slmlp.container.entity.Namespace;
import com.assistanz.slmlp.container.entity.NodeServices;
import com.assistanz.slmlp.container.entity.Pod;
import com.assistanz.slmlp.container.entity.ReplicaSet;
import com.assistanz.slmlp.container.entity.TrainingEngine;
import com.assistanz.slmlp.container.entity.TrainingJob;
import com.assistanz.slmlp.container.entity.Volume;
import com.assistanz.slmlp.container.error.exception.ApplicationException;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import com.assistanz.slmlp.container.repository.DeploymentRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.KubernetesService;
import com.assistanz.slmlp.container.util.Message;
import com.assistanz.slmlp.container.util.TrainingInput;
import com.assistanz.slmlp.container.util.error.Errors;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import az.ancode.kubectl.connector.RestTemplateCall;
import az.ancode.kubectl.connector.deployment.DeploymentCreateRequest;
import az.ancode.kubectl.connector.deployment.DeploymentDeleteRequest;
import az.ancode.kubectl.connector.deployment.DeploymentDeleteResponse;
import az.ancode.kubectl.connector.deployment.DeploymentResponse;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeEvent;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeInvolvedObject;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeSource;

/**
 * The Class DeploymentServiceImpl.
 */
@Service
public class DeploymentServiceImpl implements DeploymentService {

  /** The agent url. */
  @Value(value = "${kubernete.agent}")
  private String agentUrl;

  /** The auth. */
  @Value(value = "${kubernete.secKey}")
  private String auth;

  @Value(value = "${git.port}")
  private String tensorBortPort;

  /** The training job service. */
  @Autowired
  private TrainingJobService trainingJobService;

  /** The Constant TRAINING_API. */
  private static final String TRAINING_API = "/api/training";

  /** The deployment create req. */
  @Autowired
  private RestTemplateCall<DeploymentCreateRequest, DeploymentResponse> deploymentCreateReq;

  /** The deployment delete req. */
  @Autowired
  private RestTemplateCall<DeploymentDeleteRequest, DeploymentDeleteResponse> deploymentDeleteReq;

  /** ContainerRepository reference. */
  @Autowired
  private DeploymentRepository deploymentRepo;

  /** Volume service reference. */
  @Autowired
  private VolumeService volumeService;

  /** Replicaset service reference. */
  @Autowired
  private ReplicaSetService replicaSetService;

  /** Namespace service reference. */
  @Autowired
  private NameSpaceService nameSpaceService;

  @Autowired
  private RestTemplate restTemplate;

  /** The container service. */
  @Autowired
  private ContainerService containerService;

  /** Kube service reference. */
  @Autowired
  private KubeServiceService kubeService;

  /** The pod service. */
  @Autowired
  private PodService podService;

  /** The template. */
  @Autowired
  private SimpMessagingTemplate template;

  /** The train job service. */
  @Autowired
  private TrainingJobService trainJobService;

  /** The Launcher service. */
  @Autowired
  private LauncherService launcherService;

  /** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  /** Validator attribute. */
  @Autowired
  private AppValidator validator;

  @Override
  public Deployment save(Deployment deployment) throws Exception {
    try {
    if (findByName(deployment.getName()) != null) {
      throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "deployment already exist");
    }
    if (deployment.getId() == null) {
      if(deployment.getDeploymentType() == null) {
      DeploymentResponse deployResponse = deploymentCreateReq.restCall(agentUrl + "/create-deployment", auth,
          getCreateRequest(deployment), DeploymentResponse.class, "post");
        if (deployResponse.getResult() != null) {
          if (deployResponse.getResult().getName() == null || deployResponse.getResult().getName().equals("")) {
            throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "something went wrong");
          }
          deployment.setIsActive(true);
          return deploymentRepo.save(deployment);
        }
      }
    }
    } catch(Exception e) {
        Errors errors = validator.sendGlobalError(e.getMessage());
        if (errors.hasErrors()) {
          throw new ApplicationException(errors);
        }
    }
    return deploymentRepo.save(deployment);
  }

  @Override
  public Deployment update(Deployment deployment) throws Exception {
    return deploymentRepo.save(deployment);
  }

  @Override
  public void delete(Deployment deployment) throws Exception {
    deploymentRepo.delete(deployment);
  }

  @Override
  public void delete(Long id) throws Exception {
    Deployment deployment = find(id);
    if (deployment == null) {
      throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "Deployment is not exists");
    }
    deployment.setIsActive(false);
    deploymentRepo.save(deployment);
  }

  @Override
  public Deployment find(Long id) throws Exception {
    return deploymentRepo.findOne(id);
  }

  @Override
  public Page<Deployment> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return null;
  }

  @Override
  public List<Deployment> findAll() throws Exception {
    return (List<Deployment>) deploymentRepo.findAll();
  }

  /**
   * Gets the creates the request.
   *
   * @param deployment
   *          the deployment
   * @return the creates the request
   * @throws Exception
   *           the exception
   */
  private DeploymentCreateRequest getCreateRequest(Deployment deployment) throws Exception {

    Launcher appImage = launcherService.find(deployment.getAppImageId());
    deployment.setAppImage(appImage);
    DeploymentCreateRequest request = new DeploymentCreateRequest();
    if (deployment.getAppImage() != null) {
      request.setContainerPort(deployment.getAppImage().getInternalPort());
      request.setImage(deployment.getAppImage().getImage());
      request.setImageName(deployment.getAppImage().getImageName());
    }
    request.setName(deployment.getName());
    request.setNamespace(deployment.getNamespace().getName());
    request.setType(deployment.getAppImage().getType());
    request.setUser(deployment.getUser());
    request.setVolume(deployment.getVolume().getName());
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    String dtoAsString = mapper.writeValueAsString(request);
    return request;
  }

  @Override
  public Deployment findByName(String name) {
    // TODO Auto-generated method stub
    return deploymentRepo.findByName(name, true);
  }

  @Override
  public List<Deployment> findAllByActive(Boolean active) {
    // TODO Auto-generated method stub
    return deploymentRepo.findAllByActive(true);
  }

  @Override
  public Deployment createDeployment(Launcher launcher, DeploymentType deploymentType) throws Exception {
    Namespace nameSpace = nameSpaceService.findByUserId(launcher.getUserId());
    String deploymentName = k8sService.convertNameToK8sFromat(launcher.getDeploymentName());
    Deployment deployment = new Deployment();
    deployment.setName(deploymentName);
    deployment.setNamespace(nameSpace);
    deployment.setIsActive(true);
    deployment.setType(launcher.getType());
    deployment.setUser(nameSpace.getName());
    deployment.setUserId(launcher.getUserId());
    deployment.setProjectId(launcher.getProjectId());
    deployment.setAppImageId(launcher.getId());
    deployment.setCreatedAt(new Date());
    if(launcher.getEnvProcess() != null) {
      deployment.setEnvProcess(launcher.getEnvProcess());
    }
    deployment.setDeploymentType(deploymentType);
    Volume volume = volumeService.findByNameAndNameSpace(nameSpace.getName() + "-volume", nameSpace.getName());
    deployment.setVolume(volume);
    return save(deployment);
  }

  @Override
  public DeploymentDeleteResponse destroy(Deployment deployment) throws Exception {
    DeploymentDeleteRequest deploymentDelete = new DeploymentDeleteRequest();
    deploymentDelete.setName(deployment.getName());
    deploymentDelete.setNamespace(deployment.getNamespace().getName());
    DeploymentDeleteResponse deployResponse = deploymentDeleteReq.restCall(agentUrl + "/delete-deployment", auth,
        deploymentDelete, DeploymentDeleteResponse.class, "post");
    if (deployResponse.getResult() != null && deployResponse.getStatus().equals("success")) {
      delete(deployment.getId());
    }
    if (deployResponse.getResult() == null || deployResponse.getStatus().equals("error")) {
      throw new CustomGenericException(GenericConstants.FAILED, "Unabled to delete the deployment");
    }
    return deployResponse;
  }

  @Override
  public void updateKafkaEvents(KafkaKubeEvent event, String eventName) throws Exception {
    String eventType = event.getReason();
    KafkaKubeSource source = event.getSource();
    Deployment deployment = null;
    KafkaKubeInvolvedObject involvedObject = event.getInvolvedObject();
    if (eventName.equals(GenericConstants.DEPLOYMENT) && eventType.equals("ScalingReplicaSet")) {
      deployment = findByName(involvedObject.getName());
      deployment.setUid(involvedObject.getUid());
      NodeServices nodeService = new NodeServices();

      nodeService.setName(involvedObject.getName());
      nodeService.setNamespace(involvedObject.getNamespace());
      nodeService.setLabel(involvedObject.getName());
      nodeService.setPort(deployment.getAppImage().getInternalPort());
      nodeService.setDeploymentType(deployment.getDeploymentType());
      nodeService = kubeService.createKubeService(nodeService);
      event.getMetadata().setSelfLink(nodeService.getNodePort());

      if (nodeService.getId() != null) {
        deployment.setMessage(event.getMessage());
        deployment.setServiceId(nodeService.getId());
        Namespace nameSpace = nameSpaceService.findByUserId(deployment.getUserId());
        deployment.setNamespaceId(nameSpace.getId());
        List<Volume> volumeList = volumeService.findByNameSpace(nameSpace.getName());
        if (volumeList.size() > 0) {
          deployment.setVolumeId(volumeList.get(0).getId());
        }
        update(deployment);
      }
    }

    if (eventName.equals(GenericConstants.REPLICASET) && eventType.equals("SuccessfulCreate")) {
      ReplicaSet replicaSet = new ReplicaSet();
      replicaSet.setName(involvedObject.getName());
      replicaSet.setUid(involvedObject.getUid());
      replicaSet.setMessage(event.getMessage());
      replicaSet.setIsActive(true);
      deployment = this.findByReplicaSetName(replicaSet.getName());
      if (deployment != null) {
        replicaSet.setDeploymentId(deployment.getId());
      }
      replicaSetService.save(replicaSet);
    }

    if (eventName.equals(GenericConstants.POD)) {
      if (eventType.equals("Created")) {
        Pod pod = new Pod();
        pod.setHost(source.getHost());
        pod.setName(involvedObject.getName());
        pod.setIsActive(true);
        pod.setUid(involvedObject.getUid());
        pod.setMessage(event.getMessage());
        ReplicaSet replicaSet = replicaSetService.findByPodName(involvedObject.getName());
        if (replicaSet != null) {
          pod.setReplicaSetId(replicaSet.getId());
        }
        String[] deploymentArr = involvedObject.getName().split("-");
        String deployname = "";
        int i = 0;
        for (String deploy : deploymentArr) {
          if (i < (deploymentArr.length - 2)) {
            if (i == 0) {
              deployname += deploymentArr[i];
            } else {
              deployname = deployname + "-" + deploymentArr[i];
            }
          }
          i++;
        }
        deployment = findByName(deployname);
        pod = podService.save(pod);
        deployment.setPodId(pod.getId());
//        if (deployment != null && deployment.getType().equals("TrainingEngine")) {
//          TrainingJob trainedJod = trainJobService.getByJobName(deployname);
//          TrainingInput trainingInput = new TrainingInput();
//          trainingInput.setSplitratio(String.valueOf(trainedJod.getTestRatio()));
//          trainingInput.setTraining(trainedJod.getFilePath());
//          try {
//            ResponseEntity<String> entityResponse = restTemplate.postForEntity(
//                "http://" + pod.getHost() + ":" + deployment.getService().getNodePort() + TRAINING_API, trainingInput,
//                String.class);
//            entityResponse.getBody().toString();
//          } catch (Exception ce) {
//            ce.printStackTrace();
//          }
//        }
//        if (deployment != null && deployment.getType().equals("TensorBoard")) {
//          if (deployname.contains("-board-")) {
//            deployname = deployname.replaceAll("-board-", "-");
//          }
//          TrainingJob trainedJod = trainJobService.getByJobName(deployname);
//          if (trainedJod != null) {
//            trainedJod.setTensorBoardUrl("http://" + pod.getHost() + ":" + deployment.getService().getNodePort());
//            trainingJobService.save(trainedJod);
//          }
//        }

        update(deployment);
        Message message = new Message();
        message.setContent(deployment.getName()+" is completed");
        template.convertAndSend("/topic/deployment.event", message);
      }

      if (eventName.equals(GenericConstants.POD) && eventType.equals("Started")) {
        AppContainer container = new AppContainer();

        Pod pod = podService.findByUidAndIsActive(involvedObject.getUid(), true);
        if (pod != null) {
          container.setPodId(pod.getId());
        }
        container.setUid(deployment.getUid());
        container.setStatus(Status.RUNNING);
        container.setIsActive(true);
        containerService.save(container);
      }
    }

  }

  @Override
  public Deployment findByUid(String uid) {
    return deploymentRepo.findByUidAndIsActive(uid, true);
  }

  @Override
  public Deployment findByNameSpaceId(Long nameSpaceId) {
    return deploymentRepo.findByNameSpaceIdAndIsActive(nameSpaceId, true);
  }

  @Override
  public Deployment findByReplicaSetName(String replicaSetName) {
    return deploymentRepo.findByReplicaSetName(replicaSetName, true);
  }

  @Override
  public List<Deployment> findAllByActiveAndUserIdAndType(Boolean active, Long userId, String type) throws Exception {
    return deploymentRepo.findAllByActiveAndUserAndType(active, userId, type);
  }

  @Override
  public List<Deployment> findAllByActiveUserIdAndProjects(Boolean active, Long userId, List<Long> projectIds)
      throws Exception {
    if (projectIds.size() > 0) {
      return deploymentRepo.findAllByActiveUserIdAndProjects(active, userId, projectIds);
    } else {
      return null;
    }
  }

  @Override
  public List<Deployment> findAllByActiveAndUserId(Boolean active, Long userId) throws Exception {
    return deploymentRepo.findAllByActiveAndUser(active, userId);
  }

}
