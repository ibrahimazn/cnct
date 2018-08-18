/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.AppContainer;

public interface ContainerRepository extends PagingAndSortingRepository<AppContainer, Long> {
	
	@Query(value = "SELECT container FROM AppContainer container WHERE container.isActive = :isActive")
	List<AppContainer> findAllByActive(@Param("isActive") Boolean active);
	
	@Query(value = "SELECT container FROM AppContainer container WHERE container.isActive = :isActive AND container.deploymentId = :deployment")
	AppContainer findByDeployment(@Param("isActive") Boolean active, @Param("deployment") Long deploymentId);
	
}