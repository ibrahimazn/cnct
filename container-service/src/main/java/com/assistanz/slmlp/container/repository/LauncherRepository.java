/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.Launcher;

public interface LauncherRepository extends PagingAndSortingRepository<Launcher, Long> {

	@Query(value = "SELECT launcher FROM Launcher launcher WHERE launcher.name = :name")
	List<Launcher> findByName(@Param("name") String name);

	@Query(value = "SELECT launcher FROM Launcher launcher WHERE launcher.isActive = :isActive")
	List<Launcher> findAllByActive(@Param("isActive") Boolean isActive);

	@Query(value = "SELECT launcher FROM Launcher launcher WHERE launcher.isActive = :isActive AND launcher.type = :type")
    List<Launcher> findAllByIsActiveAndType(@Param("isActive") Boolean isActive, @Param("type") String type);

}