/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.ReplicaSet;

public interface ReplicaSetRepository extends PagingAndSortingRepository< ReplicaSet, Long> {
	
	
	@Query(value = "SELECT replicaSet FROM ReplicaSet replicaSet WHERE replicaSet.message LIKE %:podName% AND replicaSet.isActive = :isActive")
	ReplicaSet findByPodName(@Param("podName") String podName, @Param("isActive") Boolean isActive);
	
}