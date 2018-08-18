/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.entity.Namespace;

@Service
public interface NameSpaceService  extends CRUDService<Namespace> {

	Namespace defaultNamespace(Namespace nameSpace) throws Exception;

	Namespace findByUserId(Long userId) throws Exception;

	Namespace findByName(String name, Boolean isActive) throws Exception;
}
