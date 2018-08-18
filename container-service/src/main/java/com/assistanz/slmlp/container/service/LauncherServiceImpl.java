/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.sql.Timestamp;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.repository.LauncherRepository;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

import az.ancode.kubectl.connector.kubernetes.KafkaKubeEvent;

import com.assistanz.slmlp.container.entity.AppContainer;
import com.assistanz.slmlp.container.entity.AppContainer.Status;
import com.assistanz.slmlp.container.entity.Deployment.DeploymentType;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Launcher;
import com.assistanz.slmlp.container.entity.NodeServices;

@Service
public class LauncherServiceImpl implements LauncherService {

	/** Launcher Repository reference. */
	@Autowired
	private LauncherRepository launcherRepo;

	/** Container service reference. */
	@Autowired
	private ContainerService containerService;

	@Value(value = "${kubernete.agent}")
	private String agentUrl;

	@Value(value = "${kubernete.secKey}")
	private String auth;

	/** Deployment service reference. */
	@Autowired
	private DeploymentService deploymentService;

	/** Kube service reference. */
	@Autowired
	private KubeServiceService kubeService;

	@Autowired
	private SimpMessagingTemplate template;


	@Override
	public Launcher save(Launcher launcher) throws Exception {
		return launcherRepo.save(launcher);
	}

	@Override
	public Launcher update(Launcher launcher) throws Exception {
		return launcherRepo.save(launcher);
	}

	@Override
	public void delete(Launcher launcher) throws Exception {
		launcherRepo.delete(launcher);
	}

	@Override
	public void delete(Long id) throws Exception {

	}

	@Override
	public Launcher find(Long id) throws Exception {
		// TODO Auto-generated method stub
		return launcherRepo.findOne(id);
	}

	@Override
	public Page<Launcher> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Launcher> findAll() throws Exception {
		// TODO Auto-generated method stub
		return (List<Launcher>) launcherRepo.findAll();
	}



	@Override
	public List<Launcher> findByName(String name) {
		return launcherRepo.findByName(name);
	}



	@Override
	public List<Launcher> findAllByActive(Boolean isActive) throws Exception {
		return launcherRepo.findAllByActive(true);
	}

	@Override
	public Deployment launch(Launcher launcherObj) throws Exception {

		Launcher launcher = find(launcherObj.getId());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Long timeMilliSeconds = timestamp.getTime();
		launcher.setDeploymentName(launcher.getImageName() + timeMilliSeconds);
		launcher.setName(launcher.getName());
		launcher.setUser(launcherObj.getUser());
		launcher.setUserId(launcherObj.getUserId());
		launcher.setProjectId(launcherObj.getProjectId());
		launcher.setType("Launchers");
		Deployment deployment = deploymentService.createDeployment(launcher, null);
		return deployment;
	}

  @Override
  public List<Launcher> findAllByIsActiveAndType(Boolean isActive, String type) throws Exception {
    return launcherRepo.findAllByIsActiveAndType(true, type);
  }

}