/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.util.service.CRUDService;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeEvent;
import com.assistanz.slmlp.container.entity.Namespace;
import com.assistanz.slmlp.container.entity.Volume;

@Service
public interface VolumeService  extends CRUDService<Volume> {
	
	
	Volume findByNameAndNameSpace(String name, String nameSpace);
	
	List<Volume> findByNameSpace(String nameSpace);
	
	Volume saveDefaultVolume(Namespace nameSpace) throws Exception;
	
	void updateKafkaEvents(KafkaKubeEvent event,  String eventName) throws Exception;
}
