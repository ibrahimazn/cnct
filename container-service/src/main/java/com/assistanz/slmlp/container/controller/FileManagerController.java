/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.service.DeploymentService;
import com.assistanz.slmlp.container.service.FilemanagerService;
import com.assistanz.slmlp.container.service.GitService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.assistanz.slmlp.container.util.GitActions;
import com.assistanz.slmlp.container.util.KubernetesService;
import com.wordnik.swagger.annotations.Api;
import az.ancode.filemanager.connector.fileset.CsvFileResponse;
import az.ancode.filemanager.connector.fileset.FileList;
import az.ancode.filemanager.connector.fileset.FileListResponse;
import az.ancode.filemanager.connector.upload.FileUploadResponse;

/**
 * FileManager controller will expose REST API call for list users files and
 * folder and can upload local files to container volume mount point. Right now
 * FAAS not luanched so we do git gui actions though this container. git gui
 * actions are git status, git add, git commit, git pull, git lfs install, git lfs track,
 * git push , git pull. list branches, get home path, etc.,.
 *
 */
@RestController
@RequestMapping("/api/files")
@Api(value = "files", description = "Operations with Service", produces = "application/json")
public class FileManagerController extends CRUDController<FileList> implements ApiController {

  /** Service reference to file manager. */
  @Autowired
  private FilemanagerService fileManagerService;

  /** The git service. */
  @Autowired
  private GitService gitService;

  /** The kubernetes service. */
  @Autowired
  private KubernetesService k8sService;

  /** The rest template. */
  @Autowired
  private RestTemplate restTemplate;

  /** The deployment service. */
  @Autowired
  private DeploymentService deploymentService;

  /** The Constant USER_HOME. */
  public static final String USER_HOME = "user.home";

  /** Fileimport temp directory. */
  @Value("${fileimport.temp.dir}")
  private String importTempDir;

  /**
   * List of files.
   *
   * @param node the node
   * @param podUuid the pod uuid
   * @param pvc the pvc
   * @param path the path
   * @param userId the user id
   * @return the file list response
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/lists", produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public FileListResponse listOfFiles(
      @RequestParam String path, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      return fileManagerService.listOfFiles(deployments.get(0).getPod().getHost(), deployments.get(0).getPod().getUid(),
          deployments.get(0).getVolume().getPvc(), path);
    }
    return null;
  }

  /**
   * Gets the home path of container.
   *
   * @param node the node
   * @param podUuid the pod uuid
   * @param pvc the pvc
   * @param path the path
   * @param userId the user id
   * @return the home path
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/home", produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String getHomePath(@RequestParam String node, @RequestParam String podUuid, @RequestParam String pvc,
      @RequestParam String path, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      List<FileList> files = fileManagerService.listOfFiles(deployments.get(0).getPod().getHost(),
          deployments.get(0).getPod().getUid(), deployments.get(0).getVolume().getPvc(), path).getResult();
      if (files != null && files.size() > 0) {
        return "{\"path\":\"" + files.get(0).getName() + "\"}";
      }
    }
    return "{\"path\":\"" + System.getProperty(USER_HOME) + "\"}";
  }

  /**
   * Git uncommited files.
   *
   * @param gitAction the git action
   * @param userId the user id
   * @return the list of un commited files.
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/commitfiles", produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<String> gitUncommitedFiles(@RequestBody GitActions gitAction,
      @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
	 String k8sName = k8sService.convertNameToK8sFromat(userName);
    gitAction.setUserName(k8sName);
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      ResponseEntity<String>  entityResponse = restTemplate.postForEntity("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/commitfiles", gitAction,
          String.class);
      System.out.println(entityResponse.getBody());
      /*return entityResponse.getBody();*/
    }
    return null;
  }

  /**
   * Gets the status.
   *
   * @param gitAction the git action
   * @param userId the user id
   * @return the status
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/status", produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public HashMap<String, List<String>> getStatus(@RequestBody GitActions gitAction,
      @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
      String k8sName = k8sService.convertNameToK8sFromat(userName);
      gitAction.setUserName(k8sName);
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      ResponseEntity<HashMap<String, List<String>>>  entityResponse = restTemplate.exchange("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/status", HttpMethod.POST, new HttpEntity<>(gitAction),
          new ParameterizedTypeReference<HashMap<String, List<String>>>() {});
      return entityResponse.getBody();
    }
    return null;
  }

  /**
   * Gets the files from main git repository.
   *
   * @param gitAction the git action
   * @param userId the user id
   * @return the files from repo
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/pull", produces = { MediaType.ALL_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String getFilesFromRepo(@RequestBody GitActions gitAction, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName)
      throws Exception {
	  String k8sName = k8sService.convertNameToK8sFromat(userName);
    gitAction.setUserName(k8sName);
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      ResponseEntity<String>  entityResponse = restTemplate.postForEntity("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/pull", gitAction,
          String.class);
      return entityResponse.getBody();
    }
    return "{\"message\":\"" + "something went wrong" + "\"}";
  }

  /**
   * Push to main git repository.
   *
   * @param gitAction the git action
   * @param userId the user id
   * @return the string
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/push", produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String pushToRepo(@RequestBody GitActions gitAction, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName)
      throws Exception {
      String k8sName = k8sService.convertNameToK8sFromat(userName);
      gitAction.setUserName(k8sName);
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
//      ResponseEntity<String>  entityResponse = restTemplate.postForEntity("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/push", gitAction,
//          String.class);
      ResponseEntity<String>  entityResponse = restTemplate.postForEntity("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/push", gitAction,
          String.class);
      System.out.println(entityResponse.getBody());
      return entityResponse.getBody();
    }

   return "{\"message\":\"" + "something went wrong\"}";
  }

  /**
   * Lfs config for install and track file for push dataset to main git repository.
   *
   * @param gitAction the git action
   * @param userId the user id
   * @return the string
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/lfs", produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String lfsConfig(@RequestBody GitActions gitAction, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName)
      throws Exception {
	  String k8sName = k8sService.convertNameToK8sFromat(userName);
    gitAction.setUserName(k8sName);
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      gitAction.setNode(deployments.get(0).getPod().getHost());
      gitAction.setPvc(deployments.get(0).getVolume().getPvc());
      gitAction.setPodUuid(deployments.get(0).getPod().getUid());
      ResponseEntity<String>  entityResponse = restTemplate.postForEntity("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/lfs", gitAction,
          String.class);
      return entityResponse.getBody();
    }
    return "{\"message\":\"" + "something went wrong" + "\"}";
  }

  /**
   * List branches of current local repository.
   *
   * @param gitAction the git action
   * @param userId the user id
   * @return the branchList.
   * @throws Exception if error occurs.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/branches", produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<String> listBranch(@RequestBody GitActions gitAction, @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName)
      throws Exception {
	  String k8sName = k8sService.convertNameToK8sFromat(userName);
	    gitAction.setUserName(k8sName);
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      ResponseEntity<List<String>>  entityResponse = restTemplate.exchange("http://" + deployments.get(0).getPod().getHost()+  ":" + deployments.get(0).getService().getNodePort() + "/git-connector/api/git/branches", HttpMethod.POST, new HttpEntity<>(gitAction),
          new ParameterizedTypeReference<List<String>>() {});
      return entityResponse.getBody();
    }
    return null;
  }

  /**
   * Handle file upload to move local files to container mount path.
   *
   * @param file the file
   * @param uuid the uuid
   * @param pvc the pvc
   * @param path the path
   * @param nodeUrl the node url
   * @param userId the user id
   * @return the file upload response
   * @throws Exception if error occurs.
   */
  @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
  public @ResponseBody FileUploadResponse handleFileUpload(@RequestParam(value = "file") MultipartFile file,
      @RequestParam(value = "uuid") String uuid, @RequestParam(value = "pvc") String pvc,
      @RequestParam(value = "path") String path, @RequestParam(value = "node") String nodeUrl,
      @RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId),
        "File");
    if (deployments.size() > 0) {
      return fileManagerService.uploadFile(deployments.get(0).getPod().getHost(), deployments.get(0).getPod().getUid(),
          deployments.get(0).getVolume().getPvc(), path, file);
    }
    return fileManagerService.uploadFile(nodeUrl, uuid, pvc, path, file);
	}


	@RequestMapping(value = "/importFile", method = RequestMethod.POST)
	public @ResponseBody FileUploadResponse importFile(
			@RequestBody GitActions gitAction,
			@RequestHeader(required = false) String userId, @RequestHeader(required = false) String userName) throws Exception {
	     String k8sName = k8sService.convertNameToK8sFromat(userName);
	        gitAction.setUserName(k8sName);
		String[] filePath = gitAction.getFileUrl().split("/");
		int urlSize = filePath.length;
		String fileName = filePath[urlSize-1];
		URL url = new URL(gitAction.getFileUrl());


		File destination = new File(importTempDir + fileName);
	      // Copy bytes from the URL to the destination file.
		FileUtils.copyURLToFile(url, destination);
		URLConnection conn = url.openConnection();
		String type = conn.getContentType();

		Path paths = Paths.get(importTempDir + fileName);
		byte[] content = null;
		try {
		    content = Files.readAllBytes(paths);
		} catch (final IOException e) {
		}
		MultipartFile result = new MockMultipartFile(fileName,
				fileName, type, content);

		FileUploadResponse fileResponse = new FileUploadResponse();

		List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, Long.valueOf(userId), "File");
		if (deployments.size() > 0) {
			fileResponse = fileManagerService.uploadFile(deployments.get(0).getPod().getHost(), deployments.get(0).getPod().getUid(),
						deployments.get(0).getVolume().getPvc(), gitAction.getPath(), result);
		}
		if(fileResponse != null) {
			destination.delete();
		}
		return fileResponse;

  }

	/**
	 * get CSv file for explore csv to table.
	 *
	 * @param fileName the csv filename.
	 * @param start starting range.
	 * @param end ending range.
	 * @param userId the userId.
	 * @return csv file.
	 * @throws Exception if error occurs.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getcsvfile", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CsvFileResponse getCSVFile(@RequestParam String fileName, @RequestParam String start, @RequestParam String end, @RequestHeader(required = false) String userId) throws Exception {
	    CsvFileResponse csvFileResponse = new CsvFileResponse();
	    csvFileResponse = fileManagerService.getCsvFile(Long.valueOf(userId), fileName, start, end);
	    return csvFileResponse;
	}
}
