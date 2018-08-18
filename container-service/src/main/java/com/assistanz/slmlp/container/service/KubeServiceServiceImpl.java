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
import com.assistanz.slmlp.container.entity.Deployment.DeploymentType;
import com.assistanz.slmlp.container.entity.Launcher;
import com.assistanz.slmlp.container.entity.NodeServices;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import com.assistanz.slmlp.container.repository.ServiceRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import az.ancode.kubectl.connector.RestTemplateCall;
import az.ancode.kubectl.connector.service.ServiceCreateRequest;
import az.ancode.kubectl.connector.service.ServiceResponse;

@Service
public class KubeServiceServiceImpl implements  KubeServiceService {

	@Value(value = "${kubernete.agent}")
	private String agentUrl;

	@Value(value = "${kubernete.secKey}")
	private String auth;

    /** ServiceRepository  reference. */
    @Autowired
    private ServiceRepository serviceRepo;

    /** Validator attribute. */
    @Autowired
    private AppValidator validator;

    @Autowired
	private RestTemplateCall<ServiceCreateRequest, ServiceResponse> serviceCreateReq;

	@Override
	public NodeServices save(NodeServices nodeService)
			throws Exception {
	    if(nodeService.getDeploymentType() == null
	       || (nodeService.getDeploymentType() != null && !nodeService.getDeploymentType().equals(DeploymentType.OPENFAAS))) {
    		ServiceResponse serviceResponse = serviceCreateReq.restCall(agentUrl+"/create-service", auth, getCreateRequest(nodeService), ServiceResponse.class, "post");
    		if (serviceResponse.getResult() != null) {
    			if (serviceResponse.getResult().getName() == null || serviceResponse.getResult().getName().equals("")) {
    				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
    						"something went wrong");
    			}
    			nodeService.setNodePort(serviceResponse.getResult().getNodePort());
    			return serviceRepo.save(nodeService);
    		}
	    } else {
	      serviceRepo.save(nodeService);
	    }
		return nodeService;
	}

	@Override
	public NodeServices update(NodeServices service)
			throws Exception {
		return serviceRepo.save(service);
	}

	@Override
	public void delete(NodeServices service) throws Exception {
		// TODO Auto-generated method stub
		serviceRepo.delete(service);
	}

	@Override
	public void delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		serviceRepo.delete(id);
	}

	@Override
	public NodeServices find(Long id) throws Exception {
		// TODO Auto-generated method stub
		return serviceRepo.findOne(id);
	}

	@Override
	public Page<NodeServices> findAll(PagingAndSorting pagingAndSorting)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NodeServices> findAll() throws Exception {
		// TODO Auto-generated method stub
		return (List<NodeServices>) serviceRepo.findAll();
	}

	private ServiceCreateRequest getCreateRequest(NodeServices service) throws Exception {
		ServiceCreateRequest request = new ServiceCreateRequest();
		if (service.getName() != null) {
			request.setName(service.getName());
		}
		if (service.getNamespace() != null) {
			request.setNamespace(service.getNamespace());
		}
		if (service.getLabel() != null) {
			request.setLabel(service.getLabel());
		}
		if (service.getPort() != null) {
			request.setPort(service.getPort());
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String dtoAsString = mapper.writeValueAsString(request);
		return request;
	}

	@Override
	public NodeServices createKubeService(NodeServices nodeServices) throws Exception {
		return save(nodeServices);
	}

}