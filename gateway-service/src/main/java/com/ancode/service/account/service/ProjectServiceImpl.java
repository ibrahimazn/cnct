/*
 *
 */
package com.ancode.service.account.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ancode.service.account.constants.GenericConstants;
import com.ancode.service.account.entity.DataSet;
import com.ancode.service.account.entity.Department;
import com.ancode.service.account.entity.Project;
import com.ancode.service.account.entity.ProjectUser;
import com.ancode.service.account.entity.User;
import com.ancode.service.account.error.exception.ApplicationException;
import com.ancode.service.account.repository.DeploymentRepository;
import com.ancode.service.account.repository.ProjectRepository;
import com.ancode.service.account.util.AppValidator;
import com.ancode.service.account.util.GitActions;
import com.ancode.service.account.util.KubernetesService;
import com.ancode.service.account.util.error.Errors;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class ProjectServiceImpl.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

	/** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  /** ProjectRepository reference. */
  @Autowired
  private ProjectRepository projectRepo;

  /** Service reference to SourceRepo. */
  @Autowired
  private SourceRepoService sourceRepoService;

  /** Service reference to Dataset. */
  @Autowired
  private DataSetService dataSetService;

  /** Service reference to OfflineDeployment. */
  @Autowired
  private OfflineDeploymentService offlineDeploymentService;

  /** Service reference to ProjectUser. */
  @Autowired
  private ProjectUserService projectUserService;

  /** The deployrepo. */
  @Autowired
  private DeploymentRepository deployRepo;

  /** Container App URL. */
  @Value(value = "${app.gitconnector.url}")
  private String gitConnectUrl;

  /** Validator attribute. */
  @Autowired
  private AppValidator validator;

  /** The rest template. */
  @Autowired
  private RestTemplate restTemplate;

  /** Constant for Project. */
  private static final String PROJECT = "project";

  /** The user service. */
  @Autowired
  private UserService userService;

  @Override
  public Project save(Project project) throws Exception {

    this.validateProject(project);
    Errors errors = validator.rejectIfNullEntity(PROJECT, project);
    errors = validator.validateEntity(project, errors);
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
    project.setIsActive(true);
    if (project.getSourceRepo() != null) {
      project.setSourceRepoId(sourceRepoService.save(project.getSourceRepo()).getId());
    }

    if (project.getOfflineDeployment() != null) {
      project.setOfflineDeploymentId(offlineDeploymentService.save(project.getOfflineDeployment()).getId());
    }
    Project projectObj = new Project();
    User user = userService.getCurrentLoggedInUser();
    if (project.getDataSets() != null && project.getProjectUsers() != null && user != null) {
      project.setCreatedUser(user);
      project.setCreatedUserId(user.getId());
      if (project.getDataSets() != null) {
        dataSetService.saveDataSets(project.getDataSets(), null);
      }
      if (project.getProjectUsers() != null) {
        projectUserService.saveProjectUsers(project.getProjectUsers(), null);
      }
      projectObj = projectRepo.save(project);
    }
    String k8sName = k8sService.convertNameToK8sFromat(projectObj.getCreatedUser().getUserName());
    GitActions git = new GitActions();
    git.setSrcUrl(projectObj.getSourceRepo().getUrl());
    git.setUserName(k8sName);
    git.setProjectName(projectObj.getName());
    List<com.ancode.service.account.entity.Deployment> deployments = deployRepo.findAllByActiveAndUserAndType(true, user.getId(),
        "File");
    if (deployments.size() > 0) {
      restTemplate.postForEntity("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/clone", git, String.class);
      for (DataSet dataset : projectObj.getDataSets()) {
        git = new GitActions();
        git.setUserName(k8sName);
        git.setSrcUrl("");
        git.setRemoteUrl(dataset.getDataSetConfig().getDataSetPublishUrl());
        git.setDatasetName(dataset.getName());
        git.setProjectName(projectObj.getName());
        restTemplate.postForEntity("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/clone", git, String.class);
      }
    }
    if (projectObj != null) {
      if (projectObj.getDataSets() != null) {
        dataSetService.saveDataSets(projectObj.getDataSets(), projectObj.getId());
      }
      if (projectObj.getProjectUsers() != null) {
        projectUserService.saveProjectUsers(projectObj.getProjectUsers(), projectObj.getId());
      }
    }
    return projectObj;
  }

  @Override
  public Project update(Project project) throws Exception {
    return projectRepo.save(project);
  }

  @Override
  public void delete(Project project) throws Exception {
    projectRepo.delete(project);
  }

  @Override
  public void delete(Long id) throws Exception {
    projectRepo.delete(id);
  }

  @Override
  public Project find(Long id) throws Exception {
    return projectRepo.findOne(id);
  }

  @Override
  public Page<Project> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return projectRepo.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<Project> findAll() throws Exception {
    return (List<Project>) projectRepo.findAll();
  }

  /**
   * Validate the project.
   *
   * @param project
   *          the project
   * @throws Exception
   *           error occurs.
   */
  private void validateProject(Project project) throws Exception {
    Errors errors = validator.rejectIfNullEntity(PROJECT, project);
    errors = validator.validateEntity(project, errors);
    Project projects = projectRepo.findByNameAndIsActive(project.getName(), true);
    if (projects != null && !projects.getId().equals(project.getId())) {
      errors.addFieldError(GenericConstants.NAME, "Project name already exist");
    }
    if (project.getName().length() < 3) {
      errors.addFieldError(GenericConstants.NAME, "Project name should be have minimum 3 character");
    }
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
  }

  @Override
  public Project softDelete(Project project) throws Exception {
    project.setIsActive(false);
    return projectRepo.save(project);
  }

  @Override
  public List<Project> findAllProjectsByIsActive(Boolean isActive) throws Exception {
    User user = userService.getCurrentLoggedInUser();
    return projectRepo.findAllByUserAndIsActive(isActive, user.getId());
  }

  @Override
  public Page<Project> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception {
    User user = userService.getCurrentLoggedInUser();
    return projectRepo.findAllByIsActive(pagingAndSorting.toPageRequest(), isActive, user.getId());
  }

  @Override
  public Page<Project> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception {
    User user = userService.getCurrentLoggedInUser();
    return projectRepo.findBySearchText(pagingAndSorting.toPageRequest(), searchText, true, user.getId());
  }

  @Override
  public Project findByProjectName(String projectName) {
    return projectRepo.findByNameAndIsActive(projectName, true);
  }

}
