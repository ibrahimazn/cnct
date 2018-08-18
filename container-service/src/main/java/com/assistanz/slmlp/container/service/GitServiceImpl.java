/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.jgit.api.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.util.GitConnector;
import az.ancode.filemanager.connector.RestTemplateCall;
import az.ancode.filemanager.connector.lfs.LFSInstallRequest;
import az.ancode.filemanager.connector.lfs.LFSInstallResponse;
import az.ancode.filemanager.connector.lfs.LFSTrackRequest;
import az.ancode.filemanager.connector.lfs.LFSTrackResponse;

/**
 * The Class GitServiceImpl.
 */
@Service
public class GitServiceImpl implements GitService {

  /** The git user. */
  @Value(value = "${git.user}")
  protected String gitUser;

  /** The git pass. */
  @Value(value = "${git.pass}")
  protected String gitPass;

  /** The auth. */
  @Value(value = "${kubernete.secKey}")
  private String auth;

  /** The node port. */
  @Value(value = "${kubernete.port}")
  private String nodePort;

  /** The Constant LFS_INSTALL. */
  private static final String LFS_INSTALL = "/install-lfs";

  /** The Constant LFS_TRACK. */
  private static final String LFS_TRACK = "/lfs-track";

  /** The lfs trackreq. */
  @Autowired
  private RestTemplateCall<LFSTrackRequest, LFSTrackResponse> lfsTrackreq;

  /** The lfs installreq. */
  @Autowired
  private RestTemplateCall<LFSInstallRequest, LFSInstallResponse> lfsInstallreq;

  /** The git connector. */
  @Autowired
  private GitConnector gitConnector;

  @Override
  public List<String> getUnCommitedChanges(String path, String repo, String branch) throws Exception {
    List<String> unCommitedChanges = new ArrayList<String>();
    Status status = gitConnector.getStatus(path, repo);
    if (status != null) {
      if (status.hasUncommittedChanges()) {
        unCommitedChanges = status.getUncommittedChanges().stream().collect(Collectors.toList());
      }
    }
    return unCommitedChanges;
  }

  @Override
  public Boolean hasUncommittedChanges(String path, String repo, String branch) throws Exception {
    Status status = gitConnector.getStatus(path, repo);
    return status.hasUncommittedChanges();
  }

  @Override
  public List<String> getBranches(String path, String repo) throws Exception {
    List<String> branches = gitConnector.getBranchList(path, repo);
    return branches;
  }

  @Override
  public HashMap<String, List<String>> getStatus(String path, String repo, String branch) throws Exception {
    Status status = gitConnector.getStatus(path, repo);
    HashMap<String, List<String>> statusMap = new HashMap<String, List<String>>();
    if (status != null) {
      statusMap.put("Added", status.getAdded().stream().collect(Collectors.toList()));
      statusMap.put("UntrackedFiles", status.getUntracked().stream().collect(Collectors.toList()));
      statusMap.put("UntrackedFolders", status.getUntrackedFolders().stream().collect(Collectors.toList()));
      statusMap.put("ConflictList", status.getConflicting().stream().collect(Collectors.toList()));
      statusMap.put("Changed", status.getChanged().stream().collect(Collectors.toList()));
      statusMap.put("Modified", status.getModified().stream().collect(Collectors.toList()));
      statusMap.put("Missing", status.getMissing().stream().collect(Collectors.toList()));
      statusMap.put("Removed", status.getRemoved().stream().collect(Collectors.toList()));

    }
    return statusMap;
  }

  @Override
  public String getLogs(String path, String repo) throws Exception {
    // TODO Auto-generated method stub
    return gitConnector.getLogs(path, repo);
  }

  @Override
  public String commit(String path, String repo, String branch, String commitMessages) throws Exception {
    // TODO Auto-generated method stub
    return gitConnector.commitToRepo(path, repo, commitMessages);
  }

  @Override
  public String gitAdd(String path, String repo, String branch, List<String> patterns, List<String> rmpatterns)
      throws Exception {
    return gitConnector.addToRepo(path, repo, patterns, rmpatterns);
  }

  @Override
  public String gitPush(String path, String repo, String upstream, String branch) throws Exception {
    return gitConnector.pushToRepo(path, repo, upstream, branch);
  }

  @Override
  public String gitPull(String path, String repo, String upstream, String branch) throws Exception {
    return gitConnector.pullFromRepo(path, repo, upstream, branch);
  }

  @Override
  public String gitClone(String path, String repo) throws Exception {
    // TODO Auto-generated method stub
    return gitConnector.cloneRepo(path, repo);
  }

  @Override
  public void lfsInstall(String node, String pvc, String path, String pUuid) throws Exception {
    LFSInstallResponse lfsInstall = lfsInstallreq.restCall("http://" + node + ":" + nodePort + LFS_INSTALL, auth,
        getRequestForInstall(pvc, path, pUuid), LFSInstallResponse.class, "post");
  }

  @Override
  public void lfsTrack(String node, String pvc, String path, String pUuid) throws Exception {
    LFSTrackResponse lfsTrack = lfsTrackreq.restCall("http://" + node + ":" + nodePort + LFS_TRACK, auth,
        getRequestForTrack(pvc, path, pUuid), LFSTrackResponse.class, "post");
  }

  /**
   * Gets the request for track.
   *
   * @param pvc the pvc
   * @param path the path
   * @param pUuid the uuid
   * @return the request for track
   */
  private LFSTrackRequest getRequestForTrack(String pvc, String path, String pUuid) {
    LFSTrackRequest trackReq = new LFSTrackRequest();
    trackReq.setPath(path);
    trackReq.setPvc(pvc);
    trackReq.setUuid(pUuid);
    return trackReq;
  }

  /**
   * Gets the request for install.
   *
   * @param pvc the pvc
   * @param path the path
   * @param pUuid the uuid
   * @return the request for install
   */
  private LFSInstallRequest getRequestForInstall(String pvc, String path, String pUuid) {
    LFSInstallRequest trackReq = new LFSInstallRequest();
    trackReq.setPath(path);
    trackReq.setPvc(pvc);
    trackReq.setUuid(pUuid);
    return trackReq;
  }
}
