/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ancode.service.account.constants.GenericConstants;
import com.ancode.service.account.entity.Deployment;
import com.ancode.service.account.entity.Launcher;
import com.ancode.service.account.entity.User;
import com.ancode.service.account.error.exception.CustomGenericException;
import com.ancode.service.account.repository.LauncherRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The Class LauncherServiceImpl.
 */
@Service
public class LauncherServiceImpl implements LauncherService {

  /** LauncherRepository reference. */
  @Autowired
  private LauncherRepository launcherRepo;

  /** DeploymentService reference. */
  @Autowired
  private DeploymentService deploymentService;

  /** The rest template. */
  @Autowired
  private RestTemplate restTemplate;

  /** The user service. */
  @Autowired
  private UserService userService;

  /** Container App URL. */
  @Value(value = "${app.containerserver.url}")
  private String containerAppUrl;

  @Override
  public List<Launcher> findAllLaunchersByIsActive(Boolean isActive) throws Exception {
    return launcherRepo.findAllLaunchersByIsActive(isActive);
  }

  @Override
  public Launcher save(Launcher launcher) throws Exception {
    return launcherRepo.save(launcher);
  }

  @Override
  public Launcher update(Launcher t) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(Launcher t) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public Launcher find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<Launcher> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Launcher> findAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Launcher findByNameAndIsActive(String name, Boolean isActive) throws Exception {
    return launcherRepo.findByNameAndIsActive(name, isActive);
  }

  @Override
  public Deployment launch(Launcher launcher) throws Exception {
    User user = userService.getCurrentLoggedInUser();
    launcher.setUser(user.getUserName());
    if (user != null) {
      launcher.setUserId(user.getId());
    } else {
      launcher.setUserId(1L);
    }
    ResponseEntity<String> userResponse = restTemplate.postForEntity(containerAppUrl + "/api/launchers/launch", launcher,
        String.class);
    String response = userResponse.getBody();
    JsonParser parser = new JsonParser();
    JsonObject resoponseJson = parser.parse(response).getAsJsonObject();
    Gson gsonRequest = new GsonBuilder().create();
    String resoponseForGson = resoponseJson.toString();
    Deployment container = gsonRequest.fromJson(resoponseForGson, Deployment.class);
    return container;
  }

  @Override
  public void destroy(Deployment deployment) throws Exception {
    ResponseEntity<String> userResponse = restTemplate.postForEntity(containerAppUrl + "/api/deployment/destroy", deployment,
        String.class);
    String response = userResponse.getBody().toString();
    JsonParser parser = new JsonParser();
    JsonObject resoponseJson = parser.parse(response).getAsJsonObject();
    if (resoponseJson.get("status").equals("error")) {
      throw new CustomGenericException(GenericConstants.FAILED, "Unabled to delete the deployment");
    }

  }

  @Override
  public Page<Deployment> listContainersByNamespace(PagingAndSorting pagingAndSorting, Long projectId, Boolean isActive)
      throws Exception {
    // TODO Auto-generated method stub
    return deploymentService.findAllByUserProjectAndIsActive(pagingAndSorting, projectId, isActive, "Launchers");
  }

  @Override
  public List<Launcher> findAllLaunchersByIsActiveAndType(Boolean isActive, String type) throws Exception {
    return launcherRepo.findAllLaunchersByIsActiveAndType(isActive, type);
  }

}
