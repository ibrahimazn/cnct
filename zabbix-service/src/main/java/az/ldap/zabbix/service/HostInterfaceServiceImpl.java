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
import az.ldap.zabbix.entity.HostInterface;
import az.ldap.zabbix.repository.HostInterfaceRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.hostinterface.HostInterfaceCUDResponse;
import az.zabbix.connector.hostinterface.HostInterfaceCUDResult;
import az.zabbix.connector.hostinterface.HostInterfaceCreateRequest;
import az.zabbix.connector.hostinterface.HostInterfaceDeleteRequest;
import az.zabbix.connector.hostinterface.HostInterfaceListRequest;
import az.zabbix.connector.hostinterface.HostInterfaceListResponse;
import az.zabbix.connector.hostinterface.HostInterfaceRequest;
import az.zabbix.connector.hostinterface.HostInterfaceResponse;
import az.zabbix.connector.hostinterface.HostInterfaceUpdateRequest;

@Service
public class HostInterfaceServiceImpl implements HostInterfaceService {

	@Autowired
	private HostInterfaceRepository hostInterfaceRepo;

	@Value(value = "${zabbix.proxy}")
	private String hostUrl;

	/** Host creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostInterfaceCreateRequest, HostInterfaceCUDResult> hostInterfaceCreateReq;

	/** Host update Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostInterfaceUpdateRequest, HostInterfaceCUDResult> hostInterfaceUpdateReq;

	/** Host delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostInterfaceDeleteRequest, HostInterfaceCUDResult> hostInterfaceDeleteReq;

	/** Host lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostInterfaceListRequest, HostInterfaceListResponse> hostInterfaceListReq;

	@Override
	public HostInterface save(HostInterface hostInterface) throws Exception {
		HostInterfaceCUDResult hostInterfaceResponse = hostInterfaceCreateReq.restCall(hostUrl,
				getCreateHostInterfaceRequest(GenericConstants.AUTH, hostInterface), HostInterfaceCUDResult.class);
		if (hostInterfaceResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostInterfaceResponse.getError().get("data"));
		}
		HostInterfaceCUDResponse hostRes = hostInterfaceResponse.getResult();
		hostInterface.setInterfaceId(hostRes.getInterfaceids().get(0));
		return hostInterfaceRepo.save(hostInterface);
	}

	@Override
	public HostInterface update(HostInterface hostInterface) throws Exception {
		HostInterfaceCUDResult hostInterfaceResponse = hostInterfaceUpdateReq.restCall(hostUrl,
				getUpdateHostInterfaceRequest(GenericConstants.AUTH, hostInterface), HostInterfaceCUDResult.class);
		if (hostInterfaceResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostInterfaceResponse.getError().get("data"));
		}
		HostInterfaceCUDResponse hostRes = hostInterfaceResponse.getResult();
		hostInterface.setHostId(hostRes.getInterfaceids().get(0));
		return hostInterfaceRepo.save(hostInterface);
	}

	@Override
	public void delete(HostInterface hostInterface) throws Exception {
		HostInterfaceCUDResult hostInterfaceResponse = hostInterfaceDeleteReq.restCall(hostUrl,
				getDeleteHostInterfaceRequest(GenericConstants.AUTH, hostInterface.getInterfaceId()), HostInterfaceCUDResult.class);
		if (hostInterfaceResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostInterfaceResponse.getError().get("data"));
		}
		HostInterfaceCUDResponse hostRes = hostInterfaceResponse.getResult();
		hostInterface.setHostId(hostRes.getInterfaceids().get(0));
		hostInterfaceRepo.delete(hostInterface);
	}

	@Override
	public void delete(String id) throws Exception {
		HostInterfaceCUDResult hostInterfaceResponse = hostInterfaceDeleteReq.restCall(hostUrl,
				getDeleteHostInterfaceRequest(GenericConstants.AUTH, id), HostInterfaceCUDResult.class);
		if (hostInterfaceResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostInterfaceResponse.getError().get("data"));
		}
		HostInterfaceCUDResponse hostRes = hostInterfaceResponse.getResult();
		HostInterface hostInterface = find(hostRes.getInterfaceids().get(0));
		hostInterface.setHostId(hostRes.getInterfaceids().get(0));
		hostInterfaceRepo.delete(hostInterface);
	}

	@Override
	public HostInterface find(String id) throws Exception {
		return hostInterfaceRepo.findByInterfaceId(id);
	}

	@Override
	public List<HostInterface> findAll() throws Exception {
		return hostInterfaceRepo.findAll();
	}

	@Override
	public Page<HostInterface> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return hostInterfaceRepo.findAll(pagingAndSorting.toPageRequest());
	}

	public HostInterface getHostInterface(String hostInterfaceId) throws Exception {
		HostInterfaceListResponse hostInterfacesResponse = hostInterfaceListReq.restCall(hostUrl,
				getHostInterfaceRequest(GenericConstants.AUTH, hostInterfaceId), HostInterfaceListResponse.class);
		if (hostInterfacesResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostInterfacesResponse.getError().get("data"));
		}
		List<HostInterfaceResponse> hostInterfaceRes = hostInterfacesResponse.getResult();
		if (hostInterfaceRes.size() > 0) {
			if (find(hostInterfaceRes.get(0).getInterfaceid()) == null) {
				HostInterface hostInterface = hostInterfaceEntityMapperUtil(hostInterfaceRes.get(0));
				return hostInterface;
			}
			return find(hostInterfaceRes.get(0).getInterfaceid());
		}
		return null;
	}

	private HostInterfaceCreateRequest getCreateHostInterfaceRequest(String authCode, HostInterface hostInterface) {
		HostInterfaceCreateRequest request = new HostInterfaceCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostinterface.create");
		az.zabbix.connector.hostinterface.HostInterface intrface = new az.zabbix.connector.hostinterface.HostInterface();
		intrface.setDns(hostInterface.getDns());
		intrface.setIp(hostInterface.getIpAddress());
		intrface.setMain(hostInterface.getDefaultAgent());
		intrface.setPort(hostInterface.getPort());
		intrface.setType(hostInterface.getType());
		intrface.setUseip(hostInterface.getUseIp());
		intrface.setHostid(hostInterface.getHostId());
		request.setParams(intrface);
		return request;
	}

	private HostInterfaceDeleteRequest getDeleteHostInterfaceRequest(String authCode, String hostInterfaceId) {
		HostInterfaceDeleteRequest request = new HostInterfaceDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostInterface.delete");
		List<String> hostInterface = new ArrayList<String>();
		hostInterface.add(hostInterfaceId);
		request.setParams(hostInterface);
		return request;
	}

	private HostInterfaceListRequest getAllHostInterfaceRequest(String authCode) {
		HostInterfaceListRequest request = new HostInterfaceListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostInterface.get");
		HostInterfaceRequest hostInterface = new HostInterfaceRequest();
		hostInterface.setOutput("extend");
		hostInterface.setSortfield("interfaceid");
		hostInterface.setSortorder("ASC");
		request.setParams(hostInterface);
		return request;
	}

	private HostInterfaceListRequest getHostInterfaceRequest(String authCode, String hostId) {
		HostInterfaceListRequest request = new HostInterfaceListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostInterface.get");
		HostInterfaceRequest hostInterface = new HostInterfaceRequest();
		hostInterface.setOutput("extend");
		hostInterface.setSortfield("hostid");
		hostInterface.setSortorder("ASC");
		request.setParams(hostInterface);
		return request;
	}

	private HostInterfaceUpdateRequest getUpdateHostInterfaceRequest(String authCode, HostInterface hostInterface) {
		HostInterfaceUpdateRequest request = new HostInterfaceUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("hostinterface.update");
		az.zabbix.connector.hostinterface.HostInterface intrface = new az.zabbix.connector.hostinterface.HostInterface();
		intrface.setDns(hostInterface.getDns());
		intrface.setIp(hostInterface.getIpAddress());
		intrface.setMain(hostInterface.getDefaultAgent());
		intrface.setPort(hostInterface.getPort());
		intrface.setType(hostInterface.getType());
		intrface.setUseip(hostInterface.getUseIp());
		intrface.setInterfaceid(hostInterface.getInterfaceId());
		request.setParams(intrface);
		return request;
	}

	public HostInterface hostInterfaceEntityMapperUtil(HostInterfaceResponse hostInterfaceResponse) throws Exception {
		HostInterface hostInterface = null;
		if (hostInterfaceResponse != null) {
			hostInterface = new HostInterface();
			hostInterface.setHostId(hostInterfaceResponse.getHostid());
			hostInterface.setInterfaceId(hostInterfaceResponse.getInterfaceid());
			hostInterface.setDns(hostInterfaceResponse.getDns());
			hostInterface.setDefaultAgent(Integer.valueOf(hostInterfaceResponse.getMain()));
			hostInterface.setIpAddress(hostInterfaceResponse.getIp());
			hostInterface.setPort(hostInterfaceResponse.getPort());
			hostInterface.setType(Integer.valueOf(hostInterfaceResponse.getType()));
			hostInterface.setUseIp(Integer.valueOf(hostInterfaceResponse.getUseip()));
		}
		return hostInterface;
	}

	public Map<String, HostInterface> convert(List<HostInterface> hostList) {
		Map<String, HostInterface> hostMap = new HashMap<String, HostInterface>();
		for (HostInterface host : hostList) {
			hostMap.put(host.getInterfaceId(), host);
		}
		return hostMap;
	}

	@Override
	public List<HostInterface> findAllFromZabbixServer() throws Exception {
		HostInterfaceListResponse hostInterfacesResponse = hostInterfaceListReq.restCall(hostUrl,
				getAllHostInterfaceRequest(GenericConstants.AUTH), HostInterfaceListResponse.class);
		if (hostInterfacesResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					hostInterfacesResponse.getError().get("data"));
		}
		List<HostInterfaceResponse> hostInterfaceRes = hostInterfacesResponse.getResult();
		List<HostInterface> hostInterfaces = new ArrayList<HostInterface>();
		for (HostInterfaceResponse hostInterface : hostInterfaceRes) {
			hostInterfaces.add(hostInterfaceEntityMapperUtil(hostInterface));
		}
		HashMap<String, HostInterface> hostInterfaceMap = (HashMap<String, HostInterface>) convert(hostInterfaces);
		List<HostInterface> appHostInterfaceList = hostInterfaceRepo.findAll();
		for (HostInterface hostInterface : appHostInterfaceList) {
			if (hostInterfaceMap.containsKey(hostInterface.getInterfaceId())) {
				HostInterface csHostInterface = hostInterfaceMap.get(hostInterface.getInterfaceId());
				hostInterface.setHostId(csHostInterface.getHostId());
				hostInterface.setInterfaceId(csHostInterface.getInterfaceId());
				hostInterface.setDns(csHostInterface.getDns());
				hostInterface.setDefaultAgent(Integer.valueOf(csHostInterface.getDefaultAgent()));
				hostInterface.setIpAddress(csHostInterface.getIpAddress());
				hostInterface.setPort(csHostInterface.getPort());
				hostInterface.setType(Integer.valueOf(csHostInterface.getType()));
				hostInterface.setUseIp(Integer.valueOf(csHostInterface.getUseIp()));
				hostInterfaceRepo.save(hostInterface);
				hostInterfaceMap.remove(hostInterface.getInterfaceId());
			} else {
				hostInterfaceRepo.delete(hostInterface);
			}
		}
		for (String key : hostInterfaceMap.keySet()) {
			hostInterfaceRepo.save(hostInterfaceMap.get(key));
		}
		return findAll();
	}

	@Override
	public HostInterface saveInterface(HostInterface intrface)  throws Exception {
		return hostInterfaceRepo.save(intrface);
	}

	@Override
	public List<HostInterface> findByHost(String hostId)  throws Exception {
		return hostInterfaceRepo.findByHostId(hostId);
	}

	@Override
	public void deleteById(HostInterface intrface)  throws Exception {
		hostInterfaceRepo.delete(intrface);
	}

	@Override
	public void deleteAll(List<HostInterface> intrface)  throws Exception {
		hostInterfaceRepo.delete(intrface);		
	}

	@Override
	public List<HostInterface> findByIpandPort(String ip, String port) throws Exception {
		return hostInterfaceRepo.findByPortAndIP(port, ip);
	}
}
