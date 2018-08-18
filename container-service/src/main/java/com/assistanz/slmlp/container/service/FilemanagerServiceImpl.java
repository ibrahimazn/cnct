/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import az.ancode.filemanager.connector.fileset.CsvFileRequest;
import az.ancode.filemanager.connector.fileset.CsvFileResponse;
import az.ancode.filemanager.connector.fileset.FileListRequest;
import az.ancode.filemanager.connector.fileset.FileListResponse;
import az.ancode.filemanager.connector.upload.FileUploadRequest;
import az.ancode.filemanager.connector.upload.FileUploadResponse;
import az.ancode.filemanager.connector.RestTemplateCall;

/**
 * The Class FilemanagerServiceImpl.
 */
@Service
public class FilemanagerServiceImpl implements FilemanagerService {

  /** The auth. */
  @Value(value = "${kubernete.secKey}")
  private String auth;

  /** The node port. */
  @Value(value = "${kubernete.port}")
  private String nodePort;

  /** The Constant FILE_SET_URL. */
  private static final String FILE_SET_URL = "/file-manager?";

  /** The Constant FILE_UPLOAD_URL. */
  private static final String FILE_UPLOAD_URL = "/upload";

  /** The file list req. */
  @Autowired
  private RestTemplateCall<FileListRequest, FileListResponse> fileListReq;

  /** The file list req. */
  @Autowired
  private RestTemplateCall<CsvFileRequest, CsvFileResponse> csvFileReq;

  /** The file upload req. */
  @Autowired
  private RestTemplateCall<FileUploadRequest, FileUploadResponse> fileUploadReq;

  /** The deployment service. */
  @Autowired
  private DeploymentService deploymentService;

  @Override
  public FileListResponse listOfFiles(String node, String podUuid, String pvc, String path) throws Exception {
    FileListResponse fileListResponse = fileListReq.restCall(
        "http://" + node + ":" + nodePort + FILE_SET_URL + "uuid=" + podUuid + "&pvc=" + pvc + "&path=" + path, auth,
        new FileListRequest(), FileListResponse.class, "get");
		if (fileListResponse != null && fileListResponse.getResult() != null) {
      return fileListResponse;
    }
    return new FileListResponse();
  }

  @Override
  public FileUploadResponse uploadFile(String node, String podUuid, String pvc, String path, MultipartFile file)
      throws Exception {
    FileUploadResponse fileUploadResponse = fileUploadReq.restCall("http://" + node + ":" + nodePort + FILE_UPLOAD_URL,
        auth, getUploadRequest(podUuid, pvc, path, file), FileUploadResponse.class, "file");
    return fileUploadResponse;
  }

  /**
   * Gets the upload request.
   *
   * @param podUuid the pod uuid
   * @param pvc the pvc
   * @param path the path
   * @param file the file
   * @return the upload request
   * @throws Exception the exception
   */
  private FileUploadRequest getUploadRequest(String podUuid, String pvc, String path, MultipartFile file)
      throws Exception {
    FileUploadRequest request = new FileUploadRequest();
    if (file.isEmpty()) {
      throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
          "No files were selected to upload please choose file first");
    }
    request.setFile(file);
    request.setPath(path);
    request.setPvc(pvc);
    request.setUuid(podUuid);
    return request;
  }

  @Override
  public CsvFileResponse getCsvFile(Long userId, String fileName, String start, String end) throws Exception {
    List<Deployment> deployments = deploymentService.findAllByActiveAndUserIdAndType(true, userId,
        "File");
    if (deployments.size() > 0) {
      CsvFileRequest req = new CsvFileRequest();
      req.setUuid(deployments.get(0).getPod().getUid());
      req.setPvc(deployments.get(0).getVolume().getPvc());
      req.setFilename(fileName);
      req.setStart(start);
      req.setEnd(end);
      CsvFileResponse csvFileResponse = csvFileReq.restCall("http://" + deployments.get(0).getPod().getHost()+  ":" + 6200 + "/read-csv", auth, req, CsvFileResponse.class, "post");
      return csvFileResponse;
    }
    return null;
  }
}
