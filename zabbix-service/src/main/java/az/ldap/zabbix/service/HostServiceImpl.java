package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.DiscoveryRule;
import az.ldap.zabbix.entity.Graph;
import az.ldap.zabbix.entity.GraphItem;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.HostGroup;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.UserGroup;
import az.ldap.zabbix.repository.DiscoveryRuleRepository;
import az.ldap.zabbix.repository.HostRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.discovery.ConditionsParams;
import az.zabbix.connector.discovery.DiscoveryCUDResponse;
import az.zabbix.connector.discovery.DiscoveryCUDResult;
import az.zabbix.connector.discovery.DiscoveryCreateRequest;
import az.zabbix.connector.discovery.DiscoveryParams;
import az.zabbix.connector.discovery.FilterResponseParams;
import az.zabbix.connector.host.HostCUDResponse;
import az.zabbix.connector.host.HostCUDResult;
import az.zabbix.connector.host.HostCreateRequest;
import az.zabbix.connector.host.HostDeleteRequest;
import az.zabbix.connector.host.HostGroupRequest;
import az.zabbix.connector.host.HostInterface;
import az.zabbix.connector.host.HostListRequest;
import az.zabbix.connector.host.HostListResponse;
import az.zabbix.connector.host.HostParams;
import az.zabbix.connector.host.HostRequest;
import az.zabbix.connector.host.HostResponse;
import az.zabbix.connector.host.HostUpdateRequest;

/** Host service implementation class. */
@Service
public class HostServiceImpl implements HostService {

	@Value(value = "${zabbix.proxy}")
	private String hostUrl;
	
	@Value(value = "${zabbix.graph.height}")
    private Integer height;
	
	@Value(value = "${zabbix.graph.width}")
    private Integer width;
	
	@Value("#{'${zabbix.hosts}'.split(',')}")
	private List<String> hostIds;
	
	@Autowired
	private HistoryService historyService;
	
	public String[] mColors = { "39add1", // light blue
			"3079ab", // dark blue
			"c25975", // mauve
			"e15258", // red
			"f9845b", // orange
			"838cc7", // lavender
			"7d669e", // purple
			"53bbb4", // aqua
			"51b46d", // green
			"e0ab18", // mustard
			"637a91", // dark gray
			"f092b0", // pink
			"b7c0c7" // light gray
	};

	/** Host repository reference. */
	@Autowired
	private HostRepository hostRepo;
	
	@Autowired
	private DiscoveryRuleRepository discoveryRepo;

	@Autowired
	private HostGroupService hostGroupService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private GraphService graphService;
	
	@Autowired
	private GraphItemService graphItemService;
	
	@Autowired
	private TriggerService triggerService;	

	@Autowired
	private HostInterfaceService hostInterfaceService;
	
	@Value("#{'${zabbix.netRule}'.split('--')}")
	private List<String> netRule;
	
	@Value("#{'${zabbix.fsRule}'.split('--')}")
	private List<String> fsRule;
	
	@Value("#{'${zabbix.diskRule}'.split('--')}")
	private List<String> diskRule;

	/** Host creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<DiscoveryCreateRequest, DiscoveryCUDResult> discoveryCreateReq;
	
	/** Host creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostCreateRequest, HostCUDResult> hostCreateReq;

	/** Host update Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostUpdateRequest, HostCUDResult> hostUpdateReq;

	/** Host delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostDeleteRequest, HostCUDResult> hostDeleteReq;

	/** Host lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<HostListRequest, HostListResponse> hostListReq;

	@Override
	public Host save(Host host) throws Exception {
		List<az.ldap.zabbix.entity.HostInterface> hostInteface = hostInterfaceService.findByIpandPort(host.getHostInterfaceList().get(0).getIpAddress(), host.getHostInterfaceList().get(0).getPort());
		if (hostInteface.size() > 0) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "Ipaddress and port already exists in another host");
		}
		HostCUDResult hostResponse = hostCreateReq.restCall(hostUrl, getCreateHostRequest(GenericConstants.AUTH, host),
				HostCUDResult.class);
		if (hostResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostResponse.getError().get("data"));
		}
		HostCUDResponse hostRes = hostResponse.getResult();
		host.setHostId(hostRes.getHostids().get(0));
		List<HostGroup> hostGrps = new ArrayList<HostGroup>();
		for (HostGroup hostGroup : host.getHostGroupList()) {
			HostGroup hostgrp = new HostGroup();
			hostgrp = hostGroupService.findByName(hostGroup.getName());
			hostGrps.add(hostgrp);
		}
		host.setHostGroupList(hostGrps);
		hostInterfaceService.findAllFromZabbixServer();
		host.setHostInterfaceList(hostInterfaceService.findByHost(host.getHostId()));
		UserGroup userGroup =  userGroupService.findByHost(hostGrps.get(0).getGroupId());
		if (userGroup != null) {
			host.setUserId(userGroup.getUsrgrpId());
		}
		if (host.getDescription().contains("|")) {
			String[] desc = host.getDescription().split("\\|");
			if (desc[0].contains("windows") || desc[0].contains("Windows")) {
				host.setOsType("Windows");
			} else {
				host.setOsType("linux");
			}
			host.setDomainid_(Integer.valueOf(desc[1]));
			host.setDepartmentid_(Integer.valueOf(desc[2]));
			host.setUserid_(Integer.valueOf(desc[3]));
			host.setCreatedTime(desc[4]);
			host.setState(desc[5]);
			host.setUuid(desc[6]);
			if (desc.length > 7) {
				host.setServicesId(Integer.valueOf(desc[7]));
			}
		}
		host = hostRepo.save(host);
		DiscoveryRule dRule = new DiscoveryRule();
		dRule.setHostId(host.getHostId());
		dRule.setInterfaceId(host.getHostInterfaceList().get(0).getInterfaceId());
		dRule.setStatus(0);
		dRule.setType(0);
		dRule.setDelay(Integer.valueOf(netRule.get(2)));
		dRule.setName(netRule.get(1));
		dRule.setKey_(netRule.get(0));
		dRule.setCategory("Networks");
		DiscoveryCUDResult discoveryResponse = discoveryCreateReq.restCall(hostUrl, getCreateNetDiscoveryRequest(GenericConstants.AUTH, host),
				DiscoveryCUDResult.class);
		if (discoveryResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, discoveryResponse.getError().get("data"));
		}
		DiscoveryCUDResponse discoveryRes = discoveryResponse.getResult();
		dRule.setRuleId(discoveryRes.getItemids().get(0));
		discoveryRepo.save(dRule);
		if (!host.getOsType().contains("Windows") && !host.getOsType().contains("windows")) {
			dRule = new DiscoveryRule();
			dRule.setHostId(host.getHostId());
			dRule.setInterfaceId(host.getHostInterfaceList().get(0).getInterfaceId());
			dRule.setStatus(0);
			dRule.setType(0);
			dRule.setDelay(Integer.valueOf(diskRule.get(2)));
			dRule.setName(diskRule.get(1));
			dRule.setKey_(diskRule.get(0));
			dRule.setCategory("Disk");
			discoveryResponse = discoveryCreateReq.restCall(hostUrl, getCreateDiskDiscoveryRequest(GenericConstants.AUTH, host),
					DiscoveryCUDResult.class);
			if (discoveryResponse.getError() != null) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						discoveryResponse.getError().get("data"));
			}
			discoveryRes = discoveryResponse.getResult();
			dRule.setRuleId(discoveryRes.getItemids().get(0));
			discoveryRepo.save(dRule);
		}
		dRule = new DiscoveryRule();
		dRule.setHostId(host.getHostId());
		dRule.setInterfaceId(host.getHostInterfaceList().get(0).getInterfaceId());
		dRule.setStatus(0);
		dRule.setType(0);
		dRule.setDelay(Integer.valueOf(fsRule.get(2)));
		dRule.setName(fsRule.get(1));
		dRule.setKey_(fsRule.get(0));
		dRule.setCategory("FileSystem");
		discoveryResponse = discoveryCreateReq.restCall(hostUrl, getCreateFileDiscoveryRequest(GenericConstants.AUTH, host),
				DiscoveryCUDResult.class);
		if (discoveryResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, discoveryResponse.getError().get("data"));
		}
		discoveryRes = discoveryResponse.getResult();
		dRule.setRuleId(discoveryRes.getItemids().get(0));
		discoveryRepo.save(dRule);
		List<Item> items = itemService.loadDefaultItems(host);
		for (Item item : items) {
			if (item.getGraph()) {
				List<GraphItem> graphItems = new ArrayList<GraphItem>();
				GraphItem graphItem = new GraphItem();
				Random randomGenerator = new Random();
				int randomNumber = randomGenerator.nextInt(mColors.length);
				String color = mColors[randomNumber];
				graphItem.setColor(color);
				graphItem.setItemId(item.getItemId());
				graphItem.setHostId(item.getHostId());
				graphItems.add(graphItem);
				Graph graph = new Graph();
				graph.setGraphItemsList(graphItems);
				graph.setHeight(height);
				graph.setHostId(item.getHostId());
				graph.setWidth(width);
				if (item.getKey_().contains("{#FSNAME}")) {
					String name = item.getName().replace("$1", "{#FSNAME}");
					graph.setName(name);
				} else if (item.getKey_().contains("{#IFNAME}")) {
					String name = item.getName().replace("$1", "{#IFNAME}");
					graph.setName(name);
				} else if (item.getKey_().contains("{#DEVICENAME}")) {
					String name = item.getName().replace("$1", "{#DEVICENAME}");
					graph.setName(name);
				} else {
					graph.setName(item.getName());
				}
				graph.setUserid_(item.getUserid_());
				graph.setDomainid_(item.getDomainid_());
				graph.setDepartmentid_(item.getDepartmentid_());
				graphService.save(graph);
			}
		}
		return hostRepo.save(host);
	}

	@Override
	public Host update(Host host) throws Exception {
		List<az.ldap.zabbix.entity.HostInterface> hostInteface = hostInterfaceService.findByIpandPort(
				host.getHostInterfaceList().get(0).getIpAddress(), host.getHostInterfaceList().get(0).getPort());
		if (hostInteface.size() > 0) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					"Ipaddress and port already exists in another host");
		}
		HostCUDResult hostResponse = hostUpdateReq.restCall(hostUrl, getUpdateHostRequest(GenericConstants.AUTH, host),
				HostCUDResult.class);
		if (hostResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostResponse.getError().get("data"));
		}
		HostCUDResponse hostRes = hostResponse.getResult();
		host.setHostId(hostRes.getHostids().get(0));
		List<HostGroup> hostGrps = new ArrayList<HostGroup>();
		for (HostGroup hostGroup : host.getHostGroupList()) {
			HostGroup hostgrp = new HostGroup();
			hostgrp = hostGroupService.findByName(hostGroup.getName());
			hostGrps.add(hostgrp);
		}
		host.setHostGroupList(hostGrps);
		hostInterfaceService.findAllFromZabbixServer();
		host.setHostInterfaceList(hostInterfaceService.findByHost(host.getHostId()));
		return hostRepo.save(host);
	}

	@Override
	public Host updateStateHost(Host host) throws Exception {
		HostCUDResult hostResponse = hostUpdateReq.restCall(hostUrl, getUpdateHostStateRequest(GenericConstants.AUTH, host),
				HostCUDResult.class);
		if (hostResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostResponse.getError().get("data"));
		}
		HostCUDResponse hostRes = hostResponse.getResult();
		host.setHostId(hostRes.getHostids().get(0));
		List<HostGroup> hostGrps = new ArrayList<HostGroup>();
		for (HostGroup hostGroup : host.getHostGroupList()) {
			HostGroup hostgrp = new HostGroup();
			hostgrp = hostGroupService.findByName(hostGroup.getName());
			hostGrps.add(hostgrp);
		}
		host.setHostGroupList(hostGrps);
		hostInterfaceService.findAllFromZabbixServer();
		host.setHostInterfaceList(hostInterfaceService.findByHost(host.getHostId()));
		return hostRepo.save(host);
	}

	@Override
	public void delete(Host host) throws Exception {
		HostCUDResult hostResponse = hostDeleteReq.restCall(hostUrl, getDeleteHostRequest(GenericConstants.AUTH, host.getHostId()),
				HostCUDResult.class);
		if (hostResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostResponse.getError().get("data"));
		}
		HostCUDResponse hostRes = hostResponse.getResult();
		host.setHostId(hostRes.getHostids().get(0));
		hostRepo.delete(host);
		List<Item> lists = itemService.findAllByHost(host.getHostId());
		itemService.deleteAllItem(lists);
		triggerService.findAllFromZabbixServer(host.getHostId());
		historyService.syncEventService(host.getHostId());
		hostInterfaceService.findAllFromZabbixServer();
		List<Item> items = itemService.findAllByHost(host.getHostId());
		String itemids = "";
		int i  = 0;
		for (Item item : lists) {
			if ((items.size() - 1) == i) {
				itemids += item.getItemId();
			} else {
				itemids += item.getItemId() + ",";
			}
			i++;
		}
		graphItemService.deleteAllByItems(itemids);
		graphService.findAllFromZabbixServer(host.getHostId());
	}

	@Override
	public void delete(String id) throws Exception {
		HostCUDResult hostResponse = hostDeleteReq.restCall(hostUrl, getDeleteHostRequest(GenericConstants.AUTH, id),
				HostCUDResult.class);
		if (hostResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostResponse.getError().get("data"));
		}
		HostCUDResponse hostRes = hostResponse.getResult();
		Host host = find(hostRes.getHostids().get(0));
		host.setHostId(hostRes.getHostids().get(0));
		hostRepo.delete(id);
		List<Item> lists = itemService.findAllByHost(host.getHostId());
		itemService.deleteAllItem(lists);
		triggerService.findAllFromZabbixServer(host.getHostId());
		hostInterfaceService.findAllFromZabbixServer();
		List<Item> items = itemService.findAllByHost(host.getHostId());
		String itemids = "";
		int i  = 0;
		for(Item item: lists){
			if ((items.size()-1) == i) {
				itemids += item.getItemId();
			} else {
				itemids += item.getItemId() + ",";
			}
			i++;
		}
		graphItemService.deleteAllByItems(itemids);
		graphService.findAllFromZabbixServer(host.getHostId());
	}

	@Override
	public Host find(String id) throws Exception {
		Host host = hostRepo.findByHostId(id);
		return host;
	}

	@Override
	public List<Host> findAll() throws Exception {
		return hostRepo.findAll();
	}

	@Override
	public Page<Host> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return hostRepo.findAll(pagingAndSorting.toPageRequest());
	}

	@Override
	public Host getHost(String hostId) throws Exception {
		HostListResponse hostsResponse = hostListReq.restCall(hostUrl, getHostRequest(GenericConstants.AUTH, hostId),
				HostListResponse.class);
		if (hostsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostsResponse.getError().get("data"));
		}
		List<HostResponse> hostRes = hostsResponse.getResult();
		if (hostRes.size() > 0) {
			Host host = getHostEntityMapperUtil(hostRes.get(0));
			return host;
		}
		return null;
	}

	private HostCreateRequest getCreateHostRequest(String authCode, Host host) throws Exception {
		HostCreateRequest request = new HostCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("host.create");
		HostParams hostParams = new HostParams();
		hostParams.setHost(host.getName());
		hostParams.setDescription(host.getDescription());
		hostParams.setHostid(host.getHostId());
		List<HostGroupRequest> groups = new ArrayList<>();
		for (HostGroup hostGroup : host.getHostGroupList()) {
			HostGroup hostgrp = new HostGroup();
			hostGroup.setName(hostGroup.getName());
			hostgrp = hostGroupService.findByName(hostGroup.getName());
			if (hostgrp == null) {
				hostgrp = hostGroupService.save(hostGroup);
			}
			HostGroupRequest hostGrp = new HostGroupRequest();
			hostGrp.setGroupid(hostgrp.getGroupId());
			groups.add(hostGrp);
		}
		hostParams.setGroups(groups);
		hostParams.setProxy_hostid(host.getProxyId());
		List<HostInterface> intrfaces = new ArrayList<HostInterface>();
		for (az.ldap.zabbix.entity.HostInterface hostInt : host.getHostInterfaceList()) {
			HostInterface intrface = new HostInterface();
			intrface.setDns(hostInt.getDns());
			intrface.setIp(hostInt.getIpAddress());
			intrface.setMain(hostInt.getDefaultAgent());
			intrface.setPort(hostInt.getPort());
			intrface.setType(hostInt.getType());
			intrface.setUseip(hostInt.getUseIp());
			intrfaces.add(intrface);
		}
		hostParams.setInterfaces(intrfaces);
		request.setParams(hostParams);
		return request;
	}

	private HostDeleteRequest getDeleteHostRequest(String authCode, String hostId) {
		HostDeleteRequest request = new HostDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("host.delete");
		List<String> host = new ArrayList<String>();
		host.add(hostId);
		request.setParams(host);
		return request;
	}

	private HostListRequest getAllHostRequest(String authCode) {
		HostListRequest request = new HostListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("host.get");
		HostRequest host = new HostRequest();
		host.setOutput("extend");
		host.setSortfield("hostid");
		host.setSortorder("ASC");
		request.setParams(host);
		return request;
	}

	private HostListRequest getHostRequest(String authCode, String hostId) {
		HostListRequest request = new HostListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("host.get");
		HostRequest host = new HostRequest();
		List<String> hostIds = new ArrayList<String>();
		hostIds.add(hostId);
		host.setOutput("extend");
		host.setHostids(hostIds);
		request.setParams(host);
		return request;
	}
	
	private HostUpdateRequest getUpdateHostStateRequest(String authCode, Host host) throws Exception {
		HostUpdateRequest request = new HostUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("host.update");
		HostParams hostParams = new HostParams();
		hostParams.setHost(host.getName());
		hostParams.setStatus(String.valueOf(host.getStatus()));
		hostParams.setDescription(host.getDescription());
		hostParams.setHostid(host.getHostId());
		request.setParams(hostParams);
		return request;
	}

	private HostUpdateRequest getUpdateHostRequest(String authCode, Host host) throws Exception {
		HostUpdateRequest request = new HostUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("host.update");
		HostParams hostParams = new HostParams();
		hostParams.setHost(host.getName());
		hostParams.setStatus(String.valueOf(host.getStatus()));
		hostParams.setDescription(host.getDescription());
		hostParams.setHostid(host.getHostId());
		HostGroupRequest hostGrp = new HostGroupRequest();
		hostGrp.setGroupid(host.getHostGroupList().get(0).getGroupId());
		List<HostGroupRequest> groups = new ArrayList<>();
		groups.add(hostGrp);
		hostParams.setGroups(groups);
		hostParams.setProxy_hostid(host.getProxyId());
		Host hosts = find(host.getHostId());
		if (!checkIpExists(hosts.getHostInterfaceList(), host.getHostInterfaceList().get(0).getIpAddress())) {
			az.ldap.zabbix.entity.HostInterface intrface = new az.ldap.zabbix.entity.HostInterface();
			intrface.setDns(host.getHostInterfaceList().get(0).getDns());
			intrface.setIpAddress(host.getHostInterfaceList().get(0).getIpAddress());
			intrface.setDefaultAgent(0);
			intrface.setPort(host.getHostInterfaceList().get(0).getPort());
			intrface.setType(host.getHostInterfaceList().get(0).getType());
			intrface.setUseIp(host.getHostInterfaceList().get(0).getUseIp());
			intrface.setHostId(host.getHostId());
			intrface = hostInterfaceService.save(intrface);
			for (az.ldap.zabbix.entity.HostInterface intrfaces : hosts.getHostInterfaceList()) {
				intrfaces.setDefaultAgent(0);
				hostInterfaceService.update(intrfaces);
			}
			intrface.setDefaultAgent(1);
			hostInterfaceService.update(intrface);
		} else if (checkPortAndIpExists(hosts.getHostInterfaceList(), host.getHostInterfaceList().get(0).getPort(),
				host.getHostInterfaceList().get(0).getIpAddress())) {
			az.ldap.zabbix.entity.HostInterface intrfaces = getInterface(hosts.getHostInterfaceList(),
					host.getHostInterfaceList().get(0).getPort(), host.getHostInterfaceList().get(0).getIpAddress());
			if (intrfaces != null) {
				for (az.ldap.zabbix.entity.HostInterface intrface : hosts.getHostInterfaceList()) {
					intrface.setDefaultAgent(0);
					hostInterfaceService.update(intrface);
				}
				intrfaces.setPort(host.getHostInterfaceList().get(0).getPort());
				intrfaces.setDefaultAgent(1);
				hostInterfaceService.update(intrfaces);
			}
		} else {
			az.ldap.zabbix.entity.HostInterface intrfaces = getExistInterface(hosts.getHostInterfaceList(),
					host.getHostInterfaceList().get(0).getPort(), host.getHostInterfaceList().get(0).getIpAddress());
			if (intrfaces != null) {
				for (az.ldap.zabbix.entity.HostInterface intrface : hosts.getHostInterfaceList()) {
					intrface.setDefaultAgent(0);
					hostInterfaceService.update(intrface);
				}
				intrfaces.setDefaultAgent(1);
				hostInterfaceService.update(intrfaces);
			}
		}
		request.setParams(hostParams);
		return request;
	}
	
	private Boolean checkIpExists(List<az.ldap.zabbix.entity.HostInterface> list, String ip) {
	    for (az.ldap.zabbix.entity.HostInterface item : list) {
	        if (item.getIpAddress().equals(ip)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private Boolean checkPortAndIpExists(List<az.ldap.zabbix.entity.HostInterface> list, String name, String ip) {
	    for (az.ldap.zabbix.entity.HostInterface item : list) {
	        if (!item.getPort().equals(name) && item.getIpAddress().equals(ip)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private az.ldap.zabbix.entity.HostInterface getInterface(List<az.ldap.zabbix.entity.HostInterface> list, String name, String ip) {
	    for (az.ldap.zabbix.entity.HostInterface item : list) {
	        if (!item.getPort().equals(name) && item.getIpAddress().equals(ip)) {
	            return item;
	        }
	    }
	    return null;
	}
	
	private az.ldap.zabbix.entity.HostInterface getExistInterface(List<az.ldap.zabbix.entity.HostInterface> list, String name, String ip) {
	    for (az.ldap.zabbix.entity.HostInterface item : list) {
	        if (item.getPort().equals(name) && item.getIpAddress().equals(ip)) {
	            return item;
	        }
	    }
	    return null;
	}
	
	public Host getHostEntityMapperUtil(HostResponse hostResponse) throws Exception {
		Host host = null;
		if (hostResponse != null) {
			host = new Host();
			host.setName(hostResponse.getName());
			host.setHost(hostResponse.getHost());
			host.setHostId(hostResponse.getHostid());
			host.setStatus(Integer.valueOf(hostResponse.getStatus()));
			host.setDescription(hostResponse.getDescription());
			host.setProxyId(hostResponse.getProxy_hostid());
			host.setAvailable(Integer.valueOf(hostResponse.getAvailable()));
		}
		return host;
	}

	public Host hostEntityMapperUtil(HostResponse hostResponse) throws Exception {
		Host host = null;
		if (hostResponse != null) {
			host = new Host();
			host.setName(hostResponse.getName());
			host.setHost(hostResponse.getHost());
			host.setHostId(hostResponse.getHostid());
			host.setStatus(Integer.valueOf(hostResponse.getStatus()));
			host.setDescription(hostResponse.getDescription());
			host.setProxyId(hostResponse.getProxy_hostid());
			host.setAvailable(Integer.valueOf(hostResponse.getAvailable()));
			host.setHostInterfaceList(hostInterfaceService.findByHost(hostResponse.getHostid()));
			if (host.getDescription().contains("|")) {
				String[] desc = host.getDescription().split("\\|");
				if (desc[0].contains("windows") || desc[0].contains("Windows")) {
					host.setOsType("Windows");
				} else {
					host.setOsType("linux");
				}
				host.setDomainid_(Integer.valueOf(desc[1]));
				host.setDepartmentid_(Integer.valueOf(desc[2]));
				host.setUserid_(Integer.valueOf(desc[3]));
				host.setCreatedTime(desc[4]);
				host.setState(desc[5]);
				if (desc.length > 6 && desc.length < 8) {
					host.setUuid(desc[6]);
				}
				if (desc.length > 7) {
					host.setServicesId(Integer.valueOf(desc[7]));
				}
			} else {
				if (host.getDescription().contains("windows") || host.getDescription().contains("Windows")) {
					host.setOsType("Windows");
				} else {
					host.setOsType("linux");
				}	
			}
		}
		return host;
	}

	public Map<String, Host> convert(List<Host> hostList) {
		Map<String, Host> hostMap = new HashMap<String, Host>();
		for (Host host : hostList) {
			hostMap.put(host.getHostId(), host);
		}
		return hostMap;
	}

	@Override
	public List<Host> findAllFromZabbixServer() throws Exception {
		HostListResponse hostsResponse = hostListReq.restCall(hostUrl, getAllHostRequest(GenericConstants.AUTH), HostListResponse.class);
		if (hostsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostsResponse.getError().get("data"));
		}
		List<HostResponse> hostRes = hostsResponse.getResult();
		List<Host> hosts = new ArrayList<Host>();
		for (HostResponse host : hostRes) {
			if (!hostIds.contains(host.getHostid())) {
				hosts.add(hostEntityMapperUtil(host));
			}
		}
		HashMap<String, Host> hostMap = (HashMap<String, Host>) convert(hosts);
		List<Host> appHostList = hostRepo.findAll();
		for (Host host : appHostList) {
			if (hostMap.containsKey(host.getHostId())) {
				Host csHost = hostMap.get(host.getHostId());
				host.setName(csHost.getName());
				host.setHostId(csHost.getHostId());
				host.setHostGroupList(csHost.getHostGroupList());
				host.setHostInterfaceList(csHost.getHostInterfaceList());
				host.setAvailable(csHost.getAvailable());
				host.setStatus(csHost.getStatus());
				host.setName(csHost.getName());
				host.setHost(csHost.getHost());
				host.setDescription(csHost.getDescription());
				host.setProxyId(csHost.getProxyId());
				host.setUserid_(csHost.getUserid_());
				host.setDomainid_(csHost.getDomainid_());
				host.setDepartmentid_(csHost.getDepartmentid_());
				host.setState(csHost.getState());
				host.setCreatedTime(csHost.getCreatedTime());
				hostRepo.save(host);
				hostMap.remove(host.getHostId());
			} else {
				hostRepo.delete(host);
			}

		}
		for (String key : hostMap.keySet()) {
			hostRepo.save(hostMap.get(key));
		}
		return findAll();
	}

	@Override
	public Host updateHost(Host host) {
		return hostRepo.save(host);
	}

	@Override
	public void deleteAll(List<Host> host) throws Exception {
		hostRepo.delete(host);
	}

	@Override
	public Host findByName(String name) throws Exception {
		return hostRepo.findByName(name);
	}
	
	private DiscoveryCreateRequest getCreateNetDiscoveryRequest(String authCode, Host host) {
		DiscoveryCreateRequest request = new DiscoveryCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("discoveryrule.create");
		DiscoveryParams discoveryParams = new DiscoveryParams();
		discoveryParams.setName(netRule.get(1));
		discoveryParams.setKey_(netRule.get(0));
		discoveryParams.setHostid(host.getHostId());
		discoveryParams.setInterfaceid(host.getHostInterfaceList().get(0).getInterfaceId());
		discoveryParams.setDelay(Integer.valueOf(netRule.get(2)));
		discoveryParams.setType(0);
		discoveryParams.setStatus(0);
		List<ConditionsParams> conditions = new ArrayList<ConditionsParams>();
		ConditionsParams condition = new ConditionsParams();
		condition.setMacro("{#IFNAME}");
		condition.setValue("@Network interfaces for discovery");
		conditions.add(condition);
		FilterResponseParams filter = new FilterResponseParams();
		filter.setEvaltype(1);
		filter.setConditions(conditions);
		discoveryParams.setFilter(filter);
		request.setParams(discoveryParams);
		return request;
	}
	
	private DiscoveryCreateRequest getCreateFileDiscoveryRequest(String authCode, Host host) {
		DiscoveryCreateRequest request = new DiscoveryCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("discoveryrule.create");
		DiscoveryParams discoveryParams = new DiscoveryParams();
		discoveryParams.setName(fsRule.get(1));
		discoveryParams.setKey_(fsRule.get(0));
		discoveryParams.setHostid(host.getHostId());
		discoveryParams.setInterfaceid(host.getHostInterfaceList().get(0).getInterfaceId());
		discoveryParams.setDelay(Integer.valueOf(fsRule.get(2)));
		discoveryParams.setType(0);
		discoveryParams.setStatus(0);
		List<ConditionsParams> conditions = new ArrayList<ConditionsParams>();
		ConditionsParams condition = new ConditionsParams();
		condition.setMacro("{#FSTYPE}");
		condition.setValue("@File systems for discovery");
		conditions.add(condition);
		FilterResponseParams filter = new FilterResponseParams();
		filter.setEvaltype(1);
		filter.setConditions(conditions);
		discoveryParams.setFilter(filter);
		request.setParams(discoveryParams);
		return request;
	}

	private DiscoveryCreateRequest getCreateDiskDiscoveryRequest(String authCode, Host host) {
		DiscoveryCreateRequest request = new DiscoveryCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("discoveryrule.create");
		DiscoveryParams discoveryParams = new DiscoveryParams();
		discoveryParams.setName(diskRule.get(1));
		discoveryParams.setKey_(diskRule.get(0));
		discoveryParams.setHostid(host.getHostId());
		discoveryParams.setInterfaceid(host.getHostInterfaceList().get(0).getInterfaceId());
		discoveryParams.setDelay(Integer.valueOf(diskRule.get(2)));
		discoveryParams.setType(0);
		discoveryParams.setStatus(0);
		request.setParams(discoveryParams);
		return request;
	}
	
	@Override
	public Host deleteHost(Host host) throws Exception {
		HostCUDResult hostResponse = hostDeleteReq.restCall(hostUrl, getDeleteHostRequest(GenericConstants.AUTH, host.getHostId()),
				HostCUDResult.class);
		if (hostResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, hostResponse.getError().get("data"));
		}
		HostCUDResponse hostRes = hostResponse.getResult();
		host = find(hostRes.getHostids().get(0));
		host.setHostId(hostRes.getHostids().get(0));
		hostRepo.delete(host);
		List<Item> lists = itemService.findAllByHost(host.getHostId());
		itemService.deleteAllItem(lists);
		triggerService.findAllFromZabbixServer(host.getHostId());
		hostInterfaceService.findAllFromZabbixServer();
		List<Item> items = itemService.findAllByHost(host.getHostId());
		String itemids = "";
		int i  = 0;
		for(Item item: lists){
			if ((items.size()-1) == i) {
				itemids += item.getItemId();
			} else {
				itemids += item.getItemId() + ",";
			}
			i++;
		}
		graphItemService.deleteAllByItems(itemids);
		graphService.findAllFromZabbixServer(host.getHostId());
		return host;
	}

	@Override
	public List<Host> findAllByState(Integer state) throws Exception {
		return null;
	}

	@Override
	public List<Host> findAllByUser(Integer id, String type) throws Exception {
		if(type.equalsIgnoreCase("ROOT_ADMIN")){
			return hostRepo.findAll();
		} else if(type.equalsIgnoreCase("DOMAIN_ADMIN")){
			return hostRepo.findByDomain(id);
		} else {
			return hostRepo.findByUser(id);
		}
	}

	@Override
	public Host findByHostUuid(String uuid) throws Exception {
		return hostRepo.findByHostUuid(uuid);
	}
}
