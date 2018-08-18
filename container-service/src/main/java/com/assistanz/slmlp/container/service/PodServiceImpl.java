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
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.repository.LauncherRepository;
import com.assistanz.slmlp.container.repository.PodRepository;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

import com.assistanz.slmlp.container.entity.AppContainer;
import com.assistanz.slmlp.container.entity.AppContainer.Status;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Launcher;
import com.assistanz.slmlp.container.entity.NodeServices;
import com.assistanz.slmlp.container.entity.Pod;

@Service
public class PodServiceImpl implements PodService {

	/** Pod Repository reference. */
	@Autowired
	private PodRepository podRepo;

	@Override
	public Pod save(Pod pod) throws Exception {
		return podRepo.save(pod);
	}

	@Override
	public Pod update(Pod pod) throws Exception {
		return podRepo.save(pod);
	}

	@Override
	public void delete(Pod pod) throws Exception {
		podRepo.delete(pod);
	}

	@Override
	public void delete(Long id) throws Exception {
		podRepo.delete(id);
	}

	@Override
	public Pod find(Long id) throws Exception {
		return podRepo.findOne(id);
	}

	@Override
	public Page<Pod> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pod> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pod findByReplicaSetName(String replicaSetName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pod findByUidAndIsActive(String uid, Boolean isActive) {
		return podRepo.findByUidAndIsActive(uid, isActive);
	}
	

}