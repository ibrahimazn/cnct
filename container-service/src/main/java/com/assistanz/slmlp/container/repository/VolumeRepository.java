/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.Volume;


public interface VolumeRepository extends PagingAndSortingRepository<Volume, Long> {

	@Query(value = "SELECT volume FROM Volume volume WHERE volume.name = :name AND volume.namespace = :namespace")
    Volume findByNameAndNameSpace(@Param("name") String name, @Param("namespace") String namespace);
	
	@Query(value = "SELECT volume FROM Volume volume WHERE volume.namespace = :namespace")
    List<Volume> findByNameSpace(@Param("namespace") String namespace);
}