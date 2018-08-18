/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.entity.TrainingEngine;

@Service
public interface TrainingEngineService  extends CRUDService<TrainingEngine> {
	
	/**
	 * Find all Active TrainingEngines.
	 *
	 * @param isActive isActive status.
	 * @return active launcher list.
	 * @throws Exception if error occurs.
	 */
    List<TrainingEngine> findAllTrainingEnginesByIsActive( Boolean isActive) throws Exception;
    
	/**
	 * Find by name and active TrainingEngine.
	 *
	 * @param name name.
	 * @param isActive isActive status.
	 * @return active TrainingEngine.
	 * @throws Exception if error occurs.
	 */
	TrainingEngine findByNameAndIsActive( String name, Boolean isActive) throws Exception;
	
	
	/**
	 * Find by type and active TrainingEngine.
	 *
	 * @param type type.
	 * @param isActive isActive status.
	 * @param platformTool the platform tool
	 * @return active TrainingEngine.
	 * @throws Exception if error occurs.
	 */
    TrainingEngine findByTypeAndIsActive( String type, Boolean isActive, String platformTool) throws Exception;
}
