/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.Pod;

public interface PodRepository extends PagingAndSortingRepository<Pod, Long> {

	@Query(value = "SELECT pod FROM Pod pod WHERE pod.uid = :uid AND replicaSet.isActive = :isActive")
	Pod findByUidAndIsActive(@Param("uid") String uid, @Param("isActive") Boolean isActive);
	
}