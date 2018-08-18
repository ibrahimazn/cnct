/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.ancode.service.account.entity.Deployment;
import com.ancode.service.account.entity.Launcher;
import com.ancode.service.account.entity.NameSpace;
import com.ancode.service.account.entity.User;
import com.ancode.service.account.entity.User.UserType;
import com.ancode.service.account.entity.Volume;
import com.ancode.service.account.repository.NameSpaceRepository;
import com.ancode.service.account.repository.UserRepository;
import com.ancode.service.account.repository.VolumeRepository;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.ancode.service.account.error.exception.CustomGenericException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ancode.service.account.util.KubernetesService;
import com.ancode.service.account.util.error.RestErrorHandler;
import com.ancode.service.account.util.error.RestUtil;

/**
 * User service implementation class.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

  /** DepartmentRepository reference. */
  @Autowired
  private UserRepository userRepo;

  @Autowired
  private LauncherService launcherService;

  /** Kubernetes Service. */
  @Autowired
  private KubernetesService k8sService;

  /** The name space repo. */
  @Autowired
  private NameSpaceRepository nameSpaceRepo;


  /** The volume repo. */
  @Autowired
  private VolumeRepository volumeRepo;

  /** The rest template. */
  @Autowired
  private RestTemplate restTemplate;

  /** The rest restErrorHandler. */
  @Autowired
  private RestErrorHandler restErrorHandler;

  /** Container App URL. */
  @Value(value = "${app.containerserver.url}")
  private String containerAppUrl;

  /** AuthServer URL. */
  @Value(value = "${app.authserver.url}")
  private String authUrl;

  /** Git function image URL. */
  @Value(value = "${git.function}")
  private String gitImage;
  
  @Override
  public User save(User user) throws CustomGenericException, Exception {
	  String k8sName = k8sService.convertNameToK8sFromat(user.getUserName());
    restTemplate.setErrorHandler(restErrorHandler);
    ResponseEntity<String> userResponse = restTemplate.postForEntity(authUrl + "/api/user", user, String.class);
    String response = userResponse.getBody().toString();
    JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
    if (RestUtil.isError(userResponse.getStatusCode())) {
      throw new RestClientException(jsonObj.get("globalError").getAsJsonArray().get(0).toString());
    }

    JsonParser parser = new JsonParser();
    JsonObject resoponseJson = parser.parse(response).getAsJsonObject();
    Gson gsonRequest = new GsonBuilder().create();
    String resoponseForGson = resoponseJson.toString();
    user = gsonRequest.fromJson(resoponseForGson, User.class);


    NameSpace nameSpace = new NameSpace();
    nameSpace.setName(k8sName);
    nameSpace.setCreatedBy(user.getId());
    ResponseEntity<String> entityResponse = restTemplate.postForEntity(containerAppUrl + "/api/namespace/default", nameSpace,
        String.class);
    createFunction(user.getId(), k8sName);
    return user;
  }

  @Override
  public User update(User user) throws Exception {
    ResponseEntity<String> userResponse = restTemplate.postForEntity(authUrl + "/api/user/updateUser", user, String.class);
    String response = userResponse.getBody().toString();
    JsonParser parser = new JsonParser();
    JsonObject resoponseJson = parser.parse(response).getAsJsonObject();
    Gson gsonRequest = new GsonBuilder().create();
    String resoponseForGson = resoponseJson.toString();
    user = gsonRequest.fromJson(resoponseForGson, User.class);
    return user;
  }

  @Override
  public void delete(User user) throws Exception {
    userRepo.delete(user);
  }

  @Override
  public void delete(Long id) throws Exception {
    userRepo.delete(id);
  }

  @Override
  public User find(Long id) throws Exception {
    return userRepo.findOne(id);
  }

  @Override
  public Page<User> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return userRepo.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<User> findAll() throws Exception {
    return (List<User>) userRepo.findAll();
  }

  @Override
  public Page<User> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception {
    return userRepo.findAllByIsActive(pagingAndSorting.toPageRequest(), 1);
  }

  @Override
  public User softDelete(User user) throws Exception {
    user.setActive(0);
    restTemplate.delete(authUrl + "/api/user/delete/"+user.getId());
    return userRepo.save(user);
  }

  @Override
  public List<User> findAllByIsActive() {
    return userRepo.findUserByActive(1);
  }

  @Override
  public User findByUserNameAndIsActive(String userName, int isActive) {
    // TODO Auto-generated method stub
    return userRepo.findByNameAndIsActive(userName, isActive);
  }

  @Override
  public User getCurrentLoggedInUser() throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = findByUserNameAndIsActive(authentication.getName(), 1);
    return user;
  }

  /* (non-Javadoc)
   * @see com.ancode.service.account.service.UserService#createFunction()
   */
  @Override
  public User createFunction(Long userId, String namespace) throws Exception {
    User user = find(userId);
    NameSpace nameSpace = nameSpaceRepo.findByNameANDIsActive(namespace, true);
    Deployment deployment = new Deployment();
    deployment.setAppImage(null);
    deployment.setIsActive(true);
    deployment.setName(nameSpace.getName());
    deployment.setType("File");
    if (nameSpace != null) {
      deployment.setNamespaceId(nameSpace.getId());
      deployment.setNamespace(nameSpace);
    }
    List<Volume> volumes = volumeRepo.findByNameSpace(nameSpace.getName());
    if(volumes.size() > 0){
      deployment.setVolume(volumes.get(0));
    }
    deployment.setUserId(user.getId());
    Launcher launcher = new Launcher();
    launcher.setDescription("file");
    launcher.setImage(gitImage);
    launcher.setImageName(nameSpace.getName()+"-gitfunction");
    launcher.setName(nameSpace.getName()+"-file");
    launcher.setInternalPort("8089");
    launcher.setType("File");
    launcher.setUserId(user.getId());
    launcher.setPlatformTool("java");
    launcher.setIsActive(true);
    launcher = launcherService.save(launcher);
    deployment.setUser(nameSpace.getName());
    deployment.setAppImage(launcher);
    if(launcher != null){
      deployment.setAppImageId(launcher.getId());
    }
    ResponseEntity<String> entityResponse = restTemplate.postForEntity(containerAppUrl + "/api/deployment", deployment,
        String.class);
    entityResponse.getBody().toString();
    return user;
  }

  @Override
  public Page<User> findAllBySearchTextWithUserType(PagingAndSorting pagingAndSorting, String searchText,
      String userType) throws Exception {
    return userRepo.findBySearchText(pagingAndSorting.toPageRequest(), searchText, 1,
        UserType.valueOf(userType.toUpperCase()));
  }

  @Override
  public Page<User> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception {
    return userRepo.findBySearchText(pagingAndSorting.toPageRequest(), searchText, 1);
  }

}
