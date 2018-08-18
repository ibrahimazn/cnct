/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Launcher;

@Service
public interface LauncherService extends CRUDService<Launcher> {



	Deployment launch(Launcher request) throws Exception;

	List<Launcher> findByName(String name) throws Exception;

	List<Launcher> findAllByActive(Boolean isActive) throws Exception;

	List<Launcher> findAllByIsActiveAndType(Boolean isActive, String type) throws Exception;

}
