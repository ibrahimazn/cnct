/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import com.assistanz.slmlp.container.repository.VolumeRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.assistanz.slmlp.container.entity.Deployment;
import com.assistanz.slmlp.container.entity.Namespace;
import com.assistanz.slmlp.container.entity.NodeServices;
import com.assistanz.slmlp.container.entity.Volume;
import az.ancode.kubectl.connector.RestTemplateCall;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeEvent;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeInvolvedObject;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeSource;
import az.ancode.kubectl.connector.volume.VolumeCreateRequest;
import az.ancode.kubectl.connector.volume.VolumeResponse;

@Service
public class VolumeServiceImpl implements VolumeService {

	/** VolumeRepository reference. */
	@Autowired
	private VolumeRepository volumeRepo;

	@Value(value = "${kubernete.agent}")
	private String agentUrl;

	@Value(value = "${kubernete.secKey}")
	private String auth;

	@Autowired
	private RestTemplateCall<VolumeCreateRequest, VolumeResponse> volumeCreateReq;

	/** Validator attribute. */
	@Autowired
	private AppValidator validator;

	@Override
	public Volume save(Volume volume) throws Exception {
		VolumeResponse volumeResponse = volumeCreateReq.restCall(agentUrl+"/create-volume", auth, getCreateRequest(volume), VolumeResponse.class, "post");
		if (volumeResponse.getResult() != null) {
			if (volumeResponse.getResult().getName() == null || volumeResponse.getResult().getName().equals("")) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						"something went wrong");
			}
			volume.setName(volumeResponse.getResult().getName());
			volume.setNamespace(volumeResponse.getResult().getNamespace());
			volume.setPvc(volumeResponse.getResult().getPvc());
			return volumeRepo.save(volume);
		}
		return volume;
	}

	@Override
	public Volume update(Volume volume) throws Exception {
		// TODO Auto-generated method stub
		return volumeRepo.save(volume);
	}

	@Override
	public void delete(Volume volume) throws Exception {
		// TODO Auto-generated method stub
		volumeRepo.delete(volume);
	}

	@Override
	public void delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		volumeRepo.delete(id);
	}

	@Override
	public Volume find(Long id) throws Exception {
		// TODO Auto-generated method stub
		return volumeRepo.findOne(id);
	}

	@Override
	public Page<Volume> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Volume> findAll() throws Exception {
		// TODO Auto-generated method stub
		return (List<Volume>) volumeRepo.findAll();
	}
	
	private VolumeCreateRequest getCreateRequest(Volume volume) throws Exception {
		VolumeCreateRequest request = new VolumeCreateRequest();
		if (volume.getName() != null) {
			request.setName(volume.getName());
		}
		if (volume.getNamespace() != null) {
			request.setNamespace(volume.getNamespace());
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String dtoAsString = mapper.writeValueAsString(request);
		return request;
	}

	@Override
	public Volume findByNameAndNameSpace(String name, String nameSpace) {
		return volumeRepo.findByNameAndNameSpace(name, nameSpace);
	}

	@Override
	public Volume saveDefaultVolume(Namespace nameSpace) throws Exception {
		Volume volume = new Volume();
		volume.setCreatedBy(nameSpace.getCreatedBy());
		volume.setName(nameSpace.getName() + "-volume");
		volume.setNamespace(nameSpace.getName());
		volume.setStatus(Volume.Status.EXTERNALPROVISIONING);
		return save(volume);
	}

	@Override
	public void updateKafkaEvents(KafkaKubeEvent event, String eventName) throws Exception {
		String eventType = event.getReason();
		KafkaKubeSource source = event.getSource();
		KafkaKubeInvolvedObject involvedObject = event.getInvolvedObject();
		if(eventName.equals(GenericConstants.PERSISTENTVOLUMECLAIM) && eventType.equals("ProvisioningSucceeded")) {
			Volume volume = this.findByNameAndNameSpace(involvedObject.getNamespace() + "-volume", involvedObject.getNamespace());
			volume.setUid(involvedObject.getUid());
			volume.setStatus(Volume.Status.PROVISIONINGSUCCEEDED);
			volumeRepo.save(volume);
		}
		
	}

	@Override
	public List<Volume> findByNameSpace(String nameSpace) {
		return volumeRepo.findByNameSpace(nameSpace);
	}
}