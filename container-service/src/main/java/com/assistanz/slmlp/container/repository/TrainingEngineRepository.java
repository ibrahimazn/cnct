/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.TrainingEngine;


public interface TrainingEngineRepository extends PagingAndSortingRepository<TrainingEngine, Long> {


	/**
	 * Find all by isActive.
	 * 
	 * @param isActive true.
	 * @return launcherList.
	 */
	@Query(value = "SELECT trainingEngine FROM TrainingEngine trainingEngine WHERE trainingEngine.isActive = :isActive")
	List<TrainingEngine> findAllTrainingEnginesByIsActive(@Param("isActive") Boolean isActive);	
	
	/**
	 * Find by name and by isActive.
	 * 
	 * @param name name.
	 * @param isActive true.
	 * @return TrainingEngine.
	 */
	@Query(value = "SELECT trainingEngine FROM TrainingEngine trainingEngine WHERE trainingEngine.name = :name and trainingEngine.isActive = :isActive")
	TrainingEngine findByNameAndIsActive(@Param("name") String name, @Param("isActive") Boolean isActive);
	
	
	/**
	 * Find by type and by isActive.
	 *
	 * @param type type.
	 * @param isActive true.
	 * @param platformTool the platform tool
	 * @return TrainingEngine.
	 */
    @Query(value = "SELECT trainingEngine FROM TrainingEngine trainingEngine WHERE trainingEngine.type = :type and trainingEngine.isActive = :isActive and trainingEngine.platformTool = :platformTool")
    TrainingEngine findByTypeAndIsActive(@Param("type") String type, @Param("isActive") Boolean isActive, @Param("platformTool") String platformTool);
}
