package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.HostGroup;
import az.ldap.zabbix.repository.HostGroupRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.hostgroup.FilterRequest;
import az.zabbix.connector.hostgroup.HostGroupCUDResponse;
import az.zabbix.connector.hostgroup.HostGroupCUDResult;
import az.zabbix.connector.hostgroup.HostGroupCreateRequest;
import az.zabbix.connector.hostgroup.HostGroupDeleteRequest;
import az.zabbix.connector.hostgroup.HostGroupListRequest;
import az.zabbix.connector.hostgroup.HostGroupListResponse;
import az.zabbix.connector.hostgroup.HostGroupParams;
import az.zabbix.connector.hostgroup.HostGroupRequest;
import az.zabbix.connector.hostgroup.HostGroupResponse;
import az.zabbix.connector.hostgroup.HostGroupUpdateRequest;

/** HostGroup Group service implementation class. */
@Service
public class HostGroupServiceImpl implements HostGroupService {

	/** HostGroup Group repository reference. */
	@Autowired
	private HostGroupRepository hostGroupRepo;

	@Value(value = "${zabbix.proxy}")
	private String hostGroupUrl;

	/** HostGroup creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostGroupCreateRequest, HostGroupCUDResult> hostGroupCreateReq;

	/** HostGroup update Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostGroupUpdateRequest, HostGroupCUDResult> hostGroupUpdateReq;

	/** HostGroup delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostGroupDeleteRequest, HostGroupCUDResult> hostGroupDeleteReq;

	/** HostGroup lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostGroupListRequest, HostGroupListResponse> hostGroupListReq;

	@Override
	public HostGroup save(HostGroup hostGroup) throws Exception {
		HostGroupCUDResult hostGroupResponse = hostGroupCreateReq.restCall(hostGroupUrl,
				getCreateHostGroupRequest(GenericConstants.AUTH, hostGroup), HostGroupCUDResult.class);
		if (hostGroupResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupResponse.getError().get("data"));
		}
		HostGroupCUDResponse hostGroupRes = hostGroupResponse.getResult();
		hostGroup.setGroupId(hostGroupRes.getGroupids().get(0));
		return hostGroupRepo.save(hostGroup);
	}

	@Override
	public HostGroup update(HostGroup hostGroup) throws Exception {
		HostGroupCUDResult hostGroupResponse = hostGroupUpdateReq.restCall(hostGroupUrl,
				getUpdateHostGroupRequest(GenericConstants.AUTH, hostGroup), HostGroupCUDResult.class);
		if (hostGroupResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupResponse.getError().get("data"));
		}
		HostGroupCUDResponse hostGroupRes = hostGroupResponse.getResult();
		hostGroup.setGroupId(hostGroupRes.getGroupids().get(0));
		return hostGroupRepo.save(hostGroup);
	}

	@Override
	public void delete(HostGroup hostGroup) throws Exception {
		HostGroupCUDResult hostGroupResponse = hostGroupDeleteReq.restCall(hostGroupUrl,
				getDeleteHostGroupRequest(GenericConstants.AUTH, hostGroup.getGroupId()), HostGroupCUDResult.class);
		if (hostGroupResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupResponse.getError().get("data"));
		}
		HostGroupCUDResponse hostGroupRes = hostGroupResponse.getResult();
		hostGroup.setGroupId(hostGroupRes.getGroupids().get(0));
		hostGroupRepo.delete(hostGroup);
	}

	@Override
	public void delete(String id) throws Exception {
		HostGroupCUDResult hostGroupResponse = hostGroupDeleteReq.restCall(hostGroupUrl,
				getDeleteHostGroupRequest(GenericConstants.AUTH, id), HostGroupCUDResult.class);
		if (hostGroupResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupResponse.getError().get("data"));
		}
		HostGroupCUDResponse hostGroupRes = hostGroupResponse.getResult();
		HostGroup hostGroup = find(id);
		hostGroupRepo.delete(hostGroup);
	}

	@Override
	public HostGroup find(String id) throws Exception {
		HostGroup hostGroup = hostGroupRepo.findByGroupId(id);
		return hostGroup;
	}

	@Override
	public List<HostGroup> findAll() throws Exception {
		return hostGroupRepo.findAll();
	}

	@Override
	public Page<HostGroup> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return hostGroupRepo.findAll(pagingAndSorting.toPageRequest());
	}

	public HostGroup getHostGroup(String hostGroupId) throws Exception {
		HostGroupListResponse hostGroupsResponse = hostGroupListReq.restCall(hostGroupUrl,
				getHostGroupRequest(GenericConstants.AUTH, hostGroupId), HostGroupListResponse.class);
		if (hostGroupsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupsResponse.getError().get("data"));
		}
		List<HostGroupResponse> hostGroupRes = hostGroupsResponse.getResult();
		if (hostGroupRes.size() > 0) {
			if (find(hostGroupRes.get(0).getGroupid()) == null) {
				HostGroup hostGroup = hostGroupEntityMapperUtil(hostGroupRes.get(0));
				return hostGroup;
			}
			return find(hostGroupRes.get(0).getGroupid());
		}
		return null;
	}

	private HostGroupCreateRequest getCreateHostGroupRequest(String authCode, HostGroup hostGroup) {
		HostGroupCreateRequest request = new HostGroupCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostgroup.create");
		HostGroupParams hostGroupParams = new HostGroupParams();
		hostGroupParams.setName(hostGroup.getName());
		request.setParams(hostGroupParams);
		return request;
	}

	private HostGroupDeleteRequest getDeleteHostGroupRequest(String authCode, String hostGroupId) {
		HostGroupDeleteRequest request = new HostGroupDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostgroup.delete");
		List<String> hostGroup = new ArrayList<String>();
		hostGroup.add(hostGroupId);
		request.setParams(hostGroup);
		return request;
	}

	private HostGroupListRequest getAllHostGroupRequest(String authCode) {
		HostGroupListRequest request = new HostGroupListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostgroup.get");
		HostGroupRequest hostGroup = new HostGroupRequest();
		hostGroup.setOutput("extend");
		hostGroup.setSortfield("groupid");
		hostGroup.setSortorder("ASC");
		request.setParams(hostGroup);
		return request;
	}

	private HostGroupListRequest getHostGroupRequest(String authCode, String hostGroupId) {
		HostGroupListRequest request = new HostGroupListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostgroup.get");
		HostGroupRequest hostGroup = new HostGroupRequest();
		hostGroup.setOutput("extend");
		hostGroup.setSortfield("hostGroupid");
		hostGroup.setSortorder("ASC");
		FilterRequest filter = new FilterRequest();
		List<String> groups = new ArrayList<String>();
		groups.add(hostGroupId);
		filter.setGroupid(groups);
		hostGroup.setFilter(filter);
		request.setParams(hostGroup);
		return request;
	}
	
	private HostGroupListRequest getHostGroupRequestByName(String authCode, String name) {
		HostGroupListRequest request = new HostGroupListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostgroup.get");
		HostGroupRequest hostGroup = new HostGroupRequest();
		hostGroup.setOutput("extend");
		hostGroup.setStartSearch(true);
		FilterRequest filter = new FilterRequest();
		List<String> names = new ArrayList<String>();
		names.add(name);
		filter.setName(names);
		hostGroup.setFilter(filter);
		request.setParams(hostGroup);
		return request;
	}

	private HostGroupUpdateRequest getUpdateHostGroupRequest(String authCode, HostGroup hostGroup) {
		HostGroupUpdateRequest request = new HostGroupUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostgroup.update");
		HostGroupParams hostGroupParams = new HostGroupParams();
		hostGroupParams.setGroupid(hostGroup.getGroupId());
		hostGroupParams.setName(hostGroup.getName());
		request.setParams(hostGroupParams);
		return request;
	}

	public HostGroup hostGroupEntityMapperUtil(HostGroupResponse hostGroupResponse) throws Exception {
		HostGroup hostGroup = null;
		if (hostGroupResponse != null) {
			hostGroup = new HostGroup();
			hostGroup.setName(hostGroupResponse.getName());
			hostGroup.setGroupId(hostGroupResponse.getGroupid());
			hostGroup.setInternal(Integer.parseInt(hostGroupResponse.getInternal()));
		}
		return hostGroup;
	}

	public Map<String, HostGroup> convert(List<HostGroup> hostGroupList) {
		Map<String, HostGroup> hostGroupMap = new HashMap<String, HostGroup>();
		for (HostGroup hostGroup : hostGroupList) {
			hostGroupMap.put(hostGroup.getGroupId(), hostGroup);
		}
		return hostGroupMap;
	}

	@Override
	public List<HostGroup> findAllFromZabbixServer() throws Exception {
		HostGroupListResponse hostGroupsResponse = hostGroupListReq.restCall(hostGroupUrl, getAllHostGroupRequest(GenericConstants.AUTH),
				HostGroupListResponse.class);
		if (hostGroupsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupsResponse.getError().get("data"));
		}
		List<HostGroupResponse> hostGroupRes = hostGroupsResponse.getResult();
		List<HostGroup> hostGroups = new ArrayList<HostGroup>();
		for (HostGroupResponse hostGroup : hostGroupRes) {
			hostGroups.add(hostGroupEntityMapperUtil(hostGroup));
		}
		HashMap<String, HostGroup> hostGroupMap = (HashMap<String, HostGroup>) convert(hostGroups);
		List<HostGroup> appHostGroupList = hostGroupRepo.findAll();
		for (HostGroup hostGroup : appHostGroupList) {
			if (hostGroupMap.containsKey(hostGroup.getGroupId())) {
				HostGroup csHostGroup = hostGroupMap.get(hostGroup.getGroupId());
				hostGroup.setName(csHostGroup.getName());
				hostGroup.setInternal(csHostGroup.getInternal());
				hostGroup.setGroupId(csHostGroup.getGroupId());
				hostGroupRepo.save(hostGroup);
				hostGroupMap.remove(hostGroup.getGroupId());
			} else {
				hostGroupRepo.delete(hostGroup);
			}

		}
		for (String key : hostGroupMap.keySet()) {
			hostGroupRepo.save(hostGroupMap.get(key));
		}
		return findAll();
	}

	@Override
	public HostGroup findByName(String name)  throws Exception {
		return hostGroupRepo.findByName(name);
	}

	@Override
	public HostGroup syncSave(HostGroup hostGroup) throws Exception {
		HostGroupListResponse hostGroupsResponse = hostGroupListReq.restCall(hostGroupUrl,
				getHostGroupRequestByName(GenericConstants.AUTH, hostGroup.getName()), HostGroupListResponse.class);
		if (hostGroupsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupsResponse.getError().get("data"));
		}
		List<HostGroupResponse> hostGroupRes = hostGroupsResponse.getResult();
		List<HostGroup> hostGroups = new ArrayList<HostGroup>();
		for (HostGroupResponse hostGrup : hostGroupRes) {
			HostGroup hstGrp = hostGroupEntityMapperUtil(hostGrup);
			hstGrp.setType(hostGroup.getType());
			hostGroups.add(hstGrp);
		}
		if (hostGroups.size() == 0) {
			return save(hostGroup);
		}
		if (find(hostGroups.get(0).getGroupId()) != null) {
			return find(hostGroups.get(0).getGroupId());
		}
		return hostGroupRepo.save(hostGroups).get(0);
	}

	@Override
	public void removeHostGroup(String name) throws Exception {
		HostGroupListResponse hostGroupsResponse = hostGroupListReq.restCall(hostGroupUrl,
				getHostGroupRequestByName(GenericConstants.AUTH, name), HostGroupListResponse.class);
		if (hostGroupsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostGroupsResponse.getError().get("data"));
		}
		List<HostGroupResponse> hostGroupRes = hostGroupsResponse.getResult();
		if (hostGroupRes.size() > 0) {
			delete(hostGroupRes.get(0).getGroupid());
		} else {
			HostGroup hostGrp = findByName(name);
			if (hostGrp != null) {
				hostGroupRepo.delete(hostGrp);
			}
		}
	}

	@Override
	public List<HostGroup> findAllByType(String type) throws Exception {
		// TODO Auto-generated method stub
		return hostGroupRepo.findByType(type);
	}
}
