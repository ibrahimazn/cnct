/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.entity.ReplicaSet;
import com.assistanz.slmlp.container.repository.ReplicaSetRepository;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

@Service
public class ReplicaSetServiceImpl implements ReplicaSetService {

	@Autowired
	private ReplicaSetRepository replicasetRepo;
	
	@Override
	public ReplicaSet save(ReplicaSet replicaSet) throws Exception {
		return replicasetRepo.save(replicaSet);
	}

	@Override
	public ReplicaSet update(ReplicaSet replicaSet) throws Exception {
		return replicasetRepo.save(replicaSet);
	}

	@Override
	public void delete(ReplicaSet replicaSet) throws Exception {
		replicasetRepo.delete(replicaSet);
	}

	@Override
	public void delete(Long id) throws Exception {
		replicasetRepo.delete(id);
	}

	@Override
	public ReplicaSet find(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ReplicaSet> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReplicaSet> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReplicaSet findByPodName(String podName) {
		// TODO Auto-generated method stub
		return replicasetRepo.findByPodName(podName, true);
	}

	

}
