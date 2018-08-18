/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteListCommand;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

/**
 *
 * Git Connector for git related actions
 *
 */
@Component
public class GitConnector {

	private String localPath, remotePath;
	private Repository localRepo;
	private Git git;
	private CredentialsProvider cp;
	private String name = "ibrahim";
	private String password = "l3tm3in@#$";

	public void initialize(String localPath, String remotePath) throws IOException {
		this.localPath = localPath;
		this.remotePath = remotePath;
		this.localRepo = new FileRepository(localPath + "/.git");
		cp = new UsernamePasswordCredentialsProvider(this.name, this.password);
		git = new Git(localRepo);
	}

	public GitConnector() throws IOException {

	}

	public String cloneRepo(String path, String repo) throws IOException, NoFilepatternException, GitAPIException {
		initialize(path, repo);
		Git.cloneRepository().setURI(repo).setCredentialsProvider(cp).setDirectory(new File(path)).call();
		return "";
	}

	public Status getStatus(String path, String repo) throws IOException, NoHeadException, NoMessageException,
			ConcurrentRefUpdateException, JGitInternalException, WrongRepositoryStateException, GitAPIException {
		initialize(path, repo);
		Status status = git.status().call();
		return status;
	}

	public String addToRepo(String path, String repo, List<String> patterns, List<String> rmpatterns)
			throws IOException, NoFilepatternException, GitAPIException {
		initialize(path, repo);
		AddCommand add = git.add();
		for (String pattern : patterns) {
			add.addFilepattern(pattern).call();
		}
		
		RmCommand rm = git.rm();
		for (String pattern : rmpatterns) {
			rm.addFilepattern(pattern).call();
		}
		return "Added Successfully";
	}

	public String commitToRepo(String path, String repo, String message)
			throws IOException, NoHeadException, NoMessageException, ConcurrentRefUpdateException,
			JGitInternalException, WrongRepositoryStateException, GitAPIException {
		initialize(path, repo);
		return git.commit().setMessage(message).call().getTree().toString();
	}

	public List<String> getBranchList(String path, String repo)
			throws IOException, JGitInternalException, InvalidRemoteException, GitAPIException {
		initialize(path, repo);
		List<String> branchList = new ArrayList<String>();
		Iterator<Ref> itr = git.branchList().setListMode(ListMode.ALL).call().iterator();
		while (itr.hasNext()) {
			branchList.add(itr.next().getName());
		}
		return branchList;

	}

	public String pushToRepo(String path, String repo, String upstream, String branch)
			throws IOException, JGitInternalException, InvalidRemoteException, GitAPIException {
		initialize(path, repo);
		RemoteListCommand remoteList = git.remoteList();
		List<RemoteConfig> remotes = remoteList.call();
		for (RemoteConfig remoteConfig : remotes) {
			System.out.println(remoteConfig.getName());
		}
		PushCommand pc = git.push().setRemote(upstream);
		pc.setCredentialsProvider(cp).setForce(true).setPushAll();
		try {
			Iterator<PushResult> pushResult =  pc.call().iterator();
			String pushMessage = "";
			while (pushResult.hasNext()) {
				pushMessage += pushResult.next().getMessages();
			}
			return pushMessage;
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		}
		return "Push To Repo was successful";
	}

	public String pullFromRepo(String path, String repo, String upstream, String branch)
			throws IOException, WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException,
			InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException, GitAPIException {
		initialize(path, repo);
		PullResult pullResult =  git.pull().setRemote(upstream).setRemoteBranchName(branch).setCredentialsProvider(cp).call();
		String message = "Fetched From " +pullResult.getFetchedFrom() + " "+pullResult.toString().replaceAll("\\n", "\\s");
		return message;
	}

	public String getLogs(String path, String repo)
			throws IOException, WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException,
			InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException, GitAPIException {
		initialize(path, repo);
		return git.log().all().call().toString();
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public Repository getLocalRepo() {
		return localRepo;
	}

	public void setLocalRepo(Repository localRepo) {
		this.localRepo = localRepo;
	}

	public Git getGit() {
		return git;
	}

	public void setGit(Git git) {
		this.git = git;
	}

	public CredentialsProvider getCp() {
		return cp;
	}

	public void setCp(CredentialsProvider cp) {
		this.cp = cp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
