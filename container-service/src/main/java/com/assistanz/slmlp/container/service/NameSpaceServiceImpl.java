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
import com.assistanz.slmlp.container.repository.NamespaceRepository;
import com.assistanz.slmlp.container.util.AppValidator;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.assistanz.slmlp.container.entity.Namespace;
import az.ancode.kubectl.connector.RestTemplateCall;
import az.ancode.kubectl.connector.namespace.NamespaceCreateRequest;
import az.ancode.kubectl.connector.namespace.NamespaceResponse;

@Service
public class NameSpaceServiceImpl implements  NameSpaceService {

    /** NameSpaceRepository  reference. */
    @Autowired
    private NamespaceRepository nameSpaceRepo;

    @Autowired
    private VolumeService volumeService;

    @Value(value = "${kubernete.agent}")
	private String agentUrl;

	@Value(value = "${kubernete.secKey}")
	private String auth;

	@Autowired
	private RestTemplateCall<NamespaceCreateRequest, NamespaceResponse> nameSpaceCreateReq;

    /** Validator attribute. */
    @Autowired
    private AppValidator validator;

	@Override
	public Namespace save(Namespace nameSpace) throws Exception {
		NamespaceResponse namespaceResponse = nameSpaceCreateReq.restCall(agentUrl+"/create-namespace", auth, getCreateRequest(nameSpace), NamespaceResponse.class, "post");
		if (namespaceResponse.getResult() != null) {
			if (namespaceResponse.getResult().getName() == null || namespaceResponse.getResult().getName().equals("")) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						"something went wrong");
			}
			nameSpace.setIsActive(true);
			nameSpace.setName(namespaceResponse.getResult().getName());
			return nameSpaceRepo.save(nameSpace);
		}
		return nameSpace;
	}

	@Override
	public Namespace update(Namespace nameSpace) throws Exception {
		// TODO Auto-generated method stub
		return nameSpaceRepo.save(nameSpace);
	}

	@Override
	public void delete(Namespace nameSpace) throws Exception {
		// TODO Auto-generated method stub
		nameSpaceRepo.delete(nameSpace);
	}

	@Override
	public void delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		nameSpaceRepo.delete(id);
	}

	@Override
	public Namespace find(Long id) throws Exception {
		// TODO Auto-generated method stub
		return nameSpaceRepo.findOne(id);
	}

	@Override
	public Page<Namespace> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Namespace> findAll() throws Exception {
		// TODO Auto-generated method stub
		return (List<Namespace>) nameSpaceRepo.findAll();
	}


	private NamespaceCreateRequest getCreateRequest(Namespace nameSpace) throws Exception {
		NamespaceCreateRequest request = new NamespaceCreateRequest();
		if (nameSpace.getName() != null) {
			request.setName(nameSpace.getName());
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String dtoAsString = mapper.writeValueAsString(request);
		return request;
	}

	@Override
	public Namespace defaultNamespace(Namespace nameSpace) throws Exception {
	    Namespace existingNameSpace = this.findByName(nameSpace.getName(), true);
	    if(existingNameSpace != null && existingNameSpace.getId() != null) {
	        existingNameSpace.setCreatedBy(nameSpace.getCreatedBy());
	        nameSpace = nameSpaceRepo.save(existingNameSpace);
	    }

		Namespace nameSpaceObj = save(nameSpace);
		volumeService.saveDefaultVolume(nameSpaceObj);
		return nameSpaceObj;
	}

	@Override
	public Namespace findByUserId(Long userId) throws Exception {
		return nameSpaceRepo.findByUserId(userId, true);
	}

  @Override
  public Namespace findByName(String name, Boolean isActive) throws Exception {
    return nameSpaceRepo.findByName(name, isActive);
  }
}