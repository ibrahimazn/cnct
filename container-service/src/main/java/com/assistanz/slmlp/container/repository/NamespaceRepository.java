/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.Namespace;

public interface NamespaceRepository extends PagingAndSortingRepository<Namespace, Long> {

	@Query(value = "SELECT namespace FROM Namespace namespace WHERE namespace.createdBy = :createdBy AND namespace.isActive = :isActive")
	Namespace findByUserId(@Param("createdBy") Long createdBy, @Param("isActive") Boolean isActive);


	@Query(value = "SELECT namespace FROM Namespace namespace WHERE namespace.name = :name AND namespace.isActive = :isActive")
    Namespace findByName(@Param("name") String name, @Param("isActive") Boolean isActive);
}