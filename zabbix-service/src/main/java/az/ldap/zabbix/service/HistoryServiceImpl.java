package az.ldap.zabbix.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import az.ldap.notification.EmailNotificationStatus;
import az.ldap.notification.NotificationService;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.sync.util.Trend;
import az.ldap.sync.util.VMDashboards;
import az.ldap.sync.util.ZabbixDashBoard;
import az.ldap.zabbix.entity.Alert;
import az.ldap.zabbix.entity.Events;
import az.ldap.zabbix.entity.Graph;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.LoginHistory;
import az.ldap.zabbix.entity.Trends;
import az.ldap.zabbix.entity.Trigger;
import az.ldap.zabbix.repository.EventRepository;
import az.ldap.zabbix.repository.GraphRepository;
import az.ldap.zabbix.repository.ItemRepository;
import az.ldap.zabbix.repository.TriggerRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.events.EventListRequest;
import az.zabbix.connector.events.EventListResponse;
import az.zabbix.connector.events.EventRequest;
import az.zabbix.connector.events.EventResponse;
import az.zabbix.connector.history.HistoryListRequest;
import az.zabbix.connector.history.HistoryListResponse;
import az.zabbix.connector.history.HistoryRequest;
import az.zabbix.connector.history.HistoryResponse;
import az.zabbix.connector.trends.TrendsListRequest;
import az.zabbix.connector.trends.TrendsListResponse;
import az.zabbix.connector.trends.TrendsRequest;
import az.zabbix.connector.trends.TrendsResponse;

/** item service implementation class. */
@Service
public class HistoryServiceImpl implements HistoryService {

	/** Item repository reference. */
	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private TriggerRepository triggerRepo;

	@Autowired
	private LoginHistoryService loginHistoryService;

	/** Alert service reference. */
	@Autowired
	private AlertService alertService;

	@Autowired
	private GraphRepository graphRepo;

	@Autowired
	private GraphService graphService;

	@Autowired
	private GraphItemService graphItemService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private EventRepository eventRepo;

	/** Item service reference. */
	@Autowired
	private ItemService itemService;

	@Autowired
	private TriggerService triggerService;

	@Autowired
	private HostService hostService;

	@Value(value = "${zabbix.proxy}")
	private String historyUrl;

	/** history lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<HistoryListRequest, HistoryListResponse> historyListReq;

	@Autowired
	private RestTemplateCall<TrendsRequest, TrendsListResponse> trendListReq;

	@Autowired
	private RestTemplateCall<EventListRequest, EventListResponse> eventsListReq;

	@Autowired
	private NotificationService notificationService;

	public VMDashboards getHistory(String hostId, Integer type) throws Exception {
		VMDashboards vmDashboard = new VMDashboards();
		return vmDashboard;
	}

	public Trends getTrends(String itemId) throws Exception {
		TrendsListResponse trendResponse = trendListReq.restCall(historyUrl,
				getTrendsRequest(GenericConstants.AUTH, itemId), TrendsListResponse.class);
		if (trendResponse.getError() == null) {
			List<TrendsResponse> trendRes = trendResponse.getResult();
			if (trendRes.size() > 0) {
				TrendsResponse trends = trendRes.get(trendRes.size() - 1);
				Trends trend = new Trends();
				trend.setClock(trends.getClock());
				trend.setItemid(trends.getItemid());
				trend.setValue_avg(trends.getValue_avg());
				trend.setValue_min(trends.getValue_min());
				trend.setValue_max(trends.getValue_max());
				return trend;
			} else {
				TrendsListResponse trendRespondse = trendListReq.restCall(historyUrl,
						getTrendsRequests(GenericConstants.AUTH, itemId), TrendsListResponse.class);
				if (trendRespondse.getError() == null) {
					List<TrendsResponse> trendList = trendResponse.getResult();
					if (trendList.size() > 0) {
						TrendsResponse trends = trendList.get(trendList.size() - 1);
						Trends trend = new Trends();
						trend.setClock(trends.getClock());
						trend.setItemid(trends.getItemid());
						trend.setValue_avg(trends.getValue_avg());
						trend.setValue_min(trends.getValue_min());
						trend.setValue_max(trends.getValue_max());
						return trend;
					}
				}
			}
		}
		return new Trends();
	}

	public List<Events> getEvents(String triggerId) throws Exception {
		EventListResponse eventResponse = eventsListReq.restCall(historyUrl,
				getEventsRequest(GenericConstants.AUTH, triggerId), EventListResponse.class);
		if (eventResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, eventResponse.getError().get("data"));
		}
		List<EventResponse> alertRes = eventResponse.getResult();
		if (alertRes.size() > 0) {
			for (EventResponse events : alertRes) {
				List<Events> listOfevents = eventRepo.findByTriggerAndEventAndValue(events.getObjectid(),
						Integer.valueOf(events.getValue()), events.getEventid());
				if (listOfevents.size() == 0) {
					Events evnt = new Events();
					evnt.setAcknowledged(Integer.valueOf(events.getAcknowledged()));
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Timestamp stamp = new Timestamp(Long.valueOf(events.getClock()) * 1000);
					Date date = new Date(stamp.getTime());
					evnt.setClock(date);
					evnt.setEventid(events.getEventid());
					evnt.setValue(Integer.valueOf(events.getValue()));
					evnt.setTriggerId(events.getObjectid());
					evnt.setUserid(events.getUserid());
					Trigger trigger = triggerRepo.findByTriggerId(evnt.getTriggerId());
					if (trigger != null) {
						evnt.setDepartmentid_(trigger.getDepartmentid_());
						evnt.setDomainid_(trigger.getDomainid_());
						evnt.setUserid_(trigger.getUserid_());
						evnt.setItemId(trigger.getMetricId());
						evnt.setHostId(trigger.getHostId());
						Host host = hostService.find(trigger.getHostId());
						evnt.setHost(host.getName());
						evnt.setName(trigger.getDescription());
						List<EmailNotificationStatus> notificationAction = notificationService
								.getEmailNotificationStatus(evnt.getEventid());
						if (notificationAction.size() > 0) {
							evnt.setEventMessage(notificationAction.get(0).getDescription());
						}
						Integer severity = trigger.getPriority();
						if (severity == 1) {
							evnt.setSeverity("Info");
						} else if (severity == 2) {
							evnt.setSeverity("Warning");
						} else {
							evnt.setSeverity("Critical");
						}
					}
					eventRepo.save(evnt);

				} else {
					for (Events event : listOfevents) {
						List<EmailNotificationStatus> notificationAction = notificationService
								.getEmailNotificationStatus(event.getEventid());
						if (notificationAction.size() > 0) {
							event.setEventMessage(notificationAction.get(0).getDescription());
							eventRepo.save(event);
						}
					}
				}
			}
		}
		Sort sort = new Sort(Direction.DESC, "clock");
		List<Events> eventsList = new ArrayList<Events>();
		List<Events> result = eventRepo.findByAllTrigger(triggerId, sort);
		if (result.size() > 0) {
			if (result.get(0).getValue() != 0) {
				eventsList.add(result.get(0));
			}
			for (Events event : result) {
				List<EmailNotificationStatus> notificationAction = notificationService
						.getEmailNotificationStatus(event.getEventid());
				if (notificationAction.size() > 0) {
					event.setEventMessage(notificationAction.get(0).getDescription());
					eventRepo.save(event);
				}
			}
		}
		return eventsList;
	}

	private EventListRequest getEventsRequest(String authCode, String triggerId) {
		EventListRequest request = new EventListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("event.get");
		EventRequest history = new EventRequest();
		history.setOutput("extend");
		history.setSortfield("clock");
		history.setSortorder("DESC");
		history.setObjectids(triggerId);
		request.setParams(history);
		return request;
	}

	public String getHistory(String itemId) throws Exception {
		HistoryListResponse historyResponse = historyListReq.restCall(historyUrl,
				getHistoryRequest(GenericConstants.AUTH, itemId), HistoryListResponse.class);
		if (historyResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, historyResponse.getError().get("data"));
		}
		List<HistoryResponse> historyRes = historyResponse.getResult();
		if (historyRes.size() > 0) {
			return historyRes.get(0).getValue();
		}
		return "";
	}

	private TrendsRequest getTrendsRequest(String authCode, String itemId) {
		TrendsRequest request = new TrendsRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("trend.get");
		TrendsListRequest trends = new TrendsListRequest();
		trends.setOutput("extend");
		trends.setItemids(itemId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, -2);
		cal.add(Calendar.MINUTE, -1);
		trends.setTime_from(String.valueOf(cal.getTimeInMillis() / 1000));
		trends.setTime_till(String.valueOf(System.currentTimeMillis() / 1000));
		request.setParams(trends);
		return request;
	}

	private TrendsRequest getTrendsRequests(String authCode, String itemId) {
		TrendsRequest request = new TrendsRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("trend.get");
		TrendsListRequest trends = new TrendsListRequest();
		trends.setOutput("extend");
		trends.setItemids(itemId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, -1);
		/*
		 * trends.setTime_from(String.valueOf(cal.getTimeInMillis()/1000));
		 * trends.setTime_till(String.valueOf(System.currentTimeMillis()/1000));
		 */
		trends.setLimit(100);
		request.setParams(trends);
		return request;
	}

	private HistoryListRequest getHistoryRequestByHost(String authCode, String hostId, Integer type) {
		HistoryListRequest request = new HistoryListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("history.get");
		HistoryRequest history = new HistoryRequest();
		history.setOutput("extend");
		history.setSortfield("clock");
		history.setSortorder("DESC");
		history.setHostids(hostId);
		history.setHistory(type);
		request.setParams(history);
		return request;
	}

	private HistoryListRequest getHistoryRequest(String authCode, String itemId) {
		HistoryListRequest request = new HistoryListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("history.get");
		HistoryRequest history = new HistoryRequest();
		history.setOutput("extend");
		history.setSortfield("clock");
		history.setSortorder("DESC");
		history.setItemids(itemId);
		history.setLimit(1);
		request.setParams(history);
		return request;
	}

	@Override
	public VMDashboards getVmDashboard(String hostId) throws Exception {
		VMDashboards vmDash = new VMDashboards();
		Host host = hostService.find(hostId);
		List<Item> items = itemService.findAllByUsage(host.getHostId());
		Integer count = itemService.zabbixItemCount(host.getHostId());
		if (count != items.size()) {
			List<Item> itemList = itemService.findAllFromZabbixServer(host.getHostId());
			graphService.findAllFromZabbixServer(host.getHostId());
			String itemids = "";
			int i = 0;
			for (Item itemx : itemList) {
				if ((items.size() - 1) == i) {
					itemids += itemx.getItemId();
				} else {
					itemids += itemx.getItemId() + ",";
				}
				i++;
			}
			graphItemService.findAllFromZabbixServer(itemids);
			items = itemService.findAllByUsage(host.getHostId());
		}
		for (Item item : items) {
			if (item.getKey_().equals("agent.ping")) {
				String status = getHistory(item.getItemId());
				if (status.equals("1")) {
					vmDash.setAgentStatus("Up");
				} else {
					vmDash.setAgentStatus("Down");
				}
			}
			if (item.getKey_().equals("agent.hostname")) {
				vmDash.setHostName(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("proc.num[,,run]")) {
				vmDash.setProcesses(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("proc.num[]") && !item.getOsType().contains("linux")) {
				vmDash.setProcesses(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("system.users.num")) {
				vmDash.setLoggedUsers(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("kernel.maxfiles")) {
				vmDash.setOpenedFiles(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("perf_counter[\\2\\250]") && !item.getOsType().contains("linux")) {
				vmDash.setOpenedFiles(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("system.uptime")) {
				vmDash.setUpTime(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("system.boottime")) {
				vmDash.setBootTime(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("system.uname")) {
				vmDash.setInformation(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("system.hostname")) {
				vmDash.setHostName(getHistory(item.getItemId()));
			}
			if (item.getKey_().equals("vm.memory.size[used]")) {
				String usedMem = getHistory(item.getItemId());
				if (usedMem != null && !usedMem.equals("")) {
					vmDash.setUsedMemory(Long.valueOf(usedMem));
				}
			}
			if (item.getKey_().equals("vm.memory.size[total]")) {
				String usedTot = getHistory(item.getItemId());
				if (usedTot != null && !usedTot.equals("")) {
					vmDash.setTotalMemory(Long.valueOf(usedTot));
				}
			}
			if (vmDash.getTotalMemory() != null && vmDash.getUsedMemory() != null) {
				vmDash.setAvailableMemory(vmDash.getTotalMemory() - vmDash.getUsedMemory());
			}
		}
		vmDash.setCreateDateTime(host.getCreatedTime());
		List<Graph> graphs = graphRepo.findByHostId(host.getHostId());
		vmDash.setGraphs(Long.valueOf(graphs.size()));
		host.setGraphs(graphs.size());
		List<Item> itemList = itemRepo.findByHost(host.getHostId());
		vmDash.setItems(Long.valueOf(itemList.size()));
		host.setItems(itemList.size());
		vmDash.setStatus(host.getState());
		List<Trigger> triggers = triggerRepo.findByHostId(host.getHostId());
		vmDash.setTriggers(Long.valueOf(triggers.size()));
		host.setTriggers(triggers.size());
		Host zbxHost = hostService.getHost(host.getHostId());
		if (zbxHost != null) {
			host.setAvailable(zbxHost.getAvailable());
			if (host.getAvailable() == 1) {
				vmDash.setAgentStatus("UP");
			} else {
				vmDash.setAgentStatus("DOWN");
			}
		}
		List<Events> eventList = new ArrayList<Events>();
		for (Trigger trigger : triggers) {
			List<Events> events = getEvents(trigger.getTriggerid());
			if (events.size() > 0 && eventList.size() < 19) {
				eventList.addAll(events);
			}
		}
		if (eventList.size() > 0) {
			host.setCriticalAlerts(eventList.stream().filter(el -> el.getSeverity().equals("Critical"))
					.collect(Collectors.toList()).size());
			host.setInfoAlerts(eventList.stream().filter(el -> el.getSeverity().equals("Info"))
					.collect(Collectors.toList()).size());
			host.setWarnAlerts(eventList.stream().filter(el -> el.getSeverity().equals("Warning"))
					.collect(Collectors.toList()).size());
		} else {
			host.setCriticalAlerts(0);
			host.setInfoAlerts(0);
			host.setWarnAlerts(0);
		}
		hostService.updateHost(host);
		vmDash.setMessages(eventList);
		return vmDash;
	}

	public Map<String, Alert> convert(List<Alert> graphList) {
		Map<String, Alert> graphMap = new HashMap<String, Alert>();
		for (Alert alert : graphList) {
			graphMap.put(alert.getAlertid(), alert);
		}
		return graphMap;
	}

	public Map<String, String> getHistory(List<HistoryResponse> historyResponses, HashMap<String, String> historyMap) {
		if (historyResponses != null) {
			for (HistoryResponse history : historyResponses) {
				if (!historyMap.containsKey(history.getItemid())) {
					historyMap.put(history.getItemid(), history.getValue());
				}
			}
		}
		return historyMap;
	}

	@Override
	public VMDashboards save(VMDashboards t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VMDashboards update(VMDashboards t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(VMDashboards t) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public VMDashboards find(String id) throws Exception {
		return null;
	}

	@Override
	public List<VMDashboards> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<VMDashboards> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String id) throws Exception {

	}

	@Override
	public az.ldap.sync.util.VMDashboards findAllByHost(String hostId) throws Exception {
		return getHistory(hostId, 3);
	}

	@Scheduled(fixedDelayString = "${metrics.heartbeat}000")
	public void updateDashboard() throws Exception {
		ZabbixDashBoard zabbixBoard = new ZabbixDashBoard();
		List<LoginHistory> logins = loginHistoryService.findByAllActiveUser();
		for (LoginHistory login : logins) {
//			List<Host> hosts = new ArrayList<Host>();
//			if (login.getUserType().equalsIgnoreCase("ROOT_ADMIN")) {
//				hosts = hostService.findAll();
//			} else if (login.getUserType().equalsIgnoreCase("DOMAIN_ADMIN")) {
//				hosts = hostService.findAllByUser(Integer.valueOf(login.getDomainId()), login.getUserType());
//			} else {
//				hosts = hostService.findAllByUser(Integer.valueOf(login.getDepartmentId()), login.getUserType());
//			}
//			List<Trend> trends = new ArrayList<Trend>();
//			for (Host host : hosts) {
//				Item cpuItems = itemService.findAllByKeyAndHost(host.getHostId(), "system.cpu.util");
//				if (host.getOsType().contains("windows") || host.getOsType().contains("Windows")) {
//					cpuItems = itemService.findAllByKeyAndHost(host.getHostId(),
//							"perf_counter[\\Processor(_Total)\\% Processor Time]");
//				}
//				if (cpuItems != null) {
//					Trend trend = new Trend();
//					Trends cpuTrend = getTrends(cpuItems.getItemId());
//					if (cpuTrend != null) {
//						trend.setCategory(cpuItems.getCategory());
//						trend.setClock(cpuTrend.getClock());
//						trend.setHostId(host.getHostId());
//						trend.setHostName(host.getName());
//						trend.setItemId(cpuItems.getItemId());
//						trend.setValue_avg(cpuTrend.getValue_avg());
//						trend.setValue_max(cpuTrend.getValue_max());
//						trend.setValue_min(cpuTrend.getValue_min());
//					}
//					if (trend != null) {
//						trends.add(trend);
//					}
//				}
//				Item memoryitems = itemService.findAllByKeyAndHost(host.getHostId(), "vm.memory.size[pused]");
//				if (memoryitems != null) {
//					Trend trend = new Trend();
//					Trends emeTrend = getTrends(memoryitems.getItemId());
//					if (emeTrend != null) {
//						trend.setCategory(memoryitems.getCategory());
//						trend.setClock(emeTrend.getClock());
//						trend.setHostId(host.getHostId());
//						trend.setHostName(host.getName());
//						trend.setItemId(memoryitems.getItemId());
//						trend.setValue_avg(emeTrend.getValue_avg());
//						trend.setValue_max(emeTrend.getValue_max());
//						trend.setValue_min(emeTrend.getValue_min());
//					}
//					if (trend != null) {
//						trends.add(trend);
//					}
//				}
//				Item diskRead = itemService.findAllByKeyAndHost(host.getHostId(), "custom.vfs.dev.read.sectors[vda]");
//				if (host.getOsType().contains("windows") || host.getOsType().contains("Windows")) {
//					diskRead = itemService.findAllByKeyAndHost(host.getHostId(), "perf_counter[\\234(_Total)\\226]");
//				}
//				if (diskRead != null) {
//					Trend trend = new Trend();
//					Trends diskTrend = getTrends(diskRead.getItemId());
//					if (diskTrend != null) {
//						trend.setCategory(diskRead.getCategory());
//						trend.setClock(diskTrend.getClock());
//						trend.setHostId(host.getHostId());
//						trend.setHostName(host.getName());
//						trend.setItemId(diskRead.getItemId());
//						trend.setValue_avg(diskTrend.getValue_avg());
//						trend.setValue_max(diskTrend.getValue_max());
//						trend.setValue_min(diskTrend.getValue_min());
//					}
//					if (trend != null) {
//						trends.add(trend);
//					}
//				}
//				Item diskWrite = itemService.findAllByKeyAndHost(host.getHostId(), "custom.vfs.dev.write.sectors[vda]");
//				if (host.getOsType().contains("windows") || host.getOsType().contains("Windows")) {
//					diskWrite = itemService.findAllByKeyAndHost(host.getHostId(), "perf_counter[\\234(_Total)\\228]");
//				}
//				if (diskWrite != null) {
//					Trend trend = new Trend();
//					Trends diskwTrend = getTrends(diskWrite.getItemId());
//					if (diskwTrend != null) {
//						trend.setCategory(diskWrite.getCategory());
//						trend.setClock(diskwTrend.getClock());
//						trend.setHostId(host.getHostId());
//						trend.setHostName(host.getName());
//						trend.setItemId(diskWrite.getItemId());
//						trend.setValue_avg(diskwTrend.getValue_avg());
//						trend.setValue_max(diskwTrend.getValue_max());
//						trend.setValue_min(diskwTrend.getValue_min());
//					}
//					if (trend != null) {
//						trends.add(trend);
//					}
//				}
//
//				Item netIn = itemService.findAllByKeyAndHost(host.getHostId(), "net.if.in[eth0]");
//				if (netIn != null) {
//					Trend trend = new Trend();
//					Trends netInTrend = getTrends(netIn.getItemId());
//					if (netInTrend != null) {
//						trend.setCategory(netIn.getCategory());
//						trend.setClock(netInTrend.getClock());
//						trend.setHostId(host.getHostId());
//						trend.setHostName(host.getName());
//						trend.setItemId(netIn.getItemId());
//						trend.setValue_avg(netInTrend.getValue_avg());
//						trend.setValue_max(netInTrend.getValue_max());
//						trend.setValue_min(netInTrend.getValue_min());
//					}
//					if (trend != null) {
//						trends.add(trend);
//					}
//				}
//				Item netOut = itemService.findAllByKeyAndHost(host.getHostId(), "net.if.out[eth0]");
//				if (netOut != null) {
//					Trend trend = new Trend();
//					Trends netOutTrend = getTrends(netOut.getItemId());
//					if (netOutTrend != null) {
//						trend.setCategory(netOut.getCategory());
//						trend.setClock(netOutTrend.getClock());
//						trend.setHostId(host.getHostId());
//						trend.setHostName(host.getName());
//						trend.setItemId(netOut.getItemId());
//						trend.setValue_avg(netOutTrend.getValue_avg());
//						trend.setValue_max(netOutTrend.getValue_max());
//						trend.setValue_min(netOutTrend.getValue_min());
//					}
//					if (trend != null) {
//						trends.add(trend);
//					}
//				}
//				Sort sort = new Sort(Direction.DESC, "clock");
//				List<Trigger> hostTriggers = triggerRepo.findByHostId(host.getHostId());
//				List<Events> hostEventList = new ArrayList<Events>();
//				for (Trigger trigger : hostTriggers) {
//					getEvents(trigger.getTriggerid());
//					List<Events> result = eventRepo.findByAllTrigger(trigger.getTriggerid(), sort);
//					if (result.size() > 0) {
//						if (result.get(0).getValue() != 0) {
//							hostEventList.add(result.get(0));
//						}
//					}
//				}
//				List<Events> hostCriticalAlerts = hostEventList.stream()
//						.filter(el -> el.getSeverity().equals("Critical")).collect(Collectors.toList());
//				List<Events> hostInfoAlerts = hostEventList.stream().filter(el -> el.getSeverity().equals("Info"))
//						.collect(Collectors.toList());
//				List<Events> hostWarnAlerts = hostEventList.stream().filter(el -> el.getSeverity().equals("Warning"))
//						.collect(Collectors.toList());
//				if (hostCriticalAlerts != null) {
//					host.setCriticalAlerts(hostCriticalAlerts.size());
//				}
//				if (hostInfoAlerts != null) {
//					host.setInfoAlerts(hostInfoAlerts.size());
//				}
//				if (hostWarnAlerts != null) {
//					host.setWarnAlerts(hostWarnAlerts.size());
//				}
//				hostService.updateHost(host);
//			}
//			zabbixBoard.setTrends(trends);
//			List<Item> items = new ArrayList<>();
//			if (login.getUserType().equalsIgnoreCase("ROOT_ADMIN")) {
//				items = itemService.findAll();
//			} else if (login.getUserType().equalsIgnoreCase("DOMAIN_ADMIN")) {
//				items = itemService.findAllByUser(Integer.valueOf(login.getDomainId()), login.getUserType());
//			} else {
//				items = itemService.findAllByUser(Integer.valueOf(login.getDepartmentId()), login.getUserType());
//			}
//			List<Trigger> triggers = new ArrayList<Trigger>();
//			if (login.getUserType().equalsIgnoreCase("ROOT_ADMIN")) {
//				triggers = triggerRepo.findAll();
//			} else if (login.getUserType().equalsIgnoreCase("DOMAIN_ADMIN")) {
//				triggers = triggerRepo.findByDomain(Integer.valueOf(Integer.valueOf(login.getDomainId())));
//			} else {
//				triggers = triggerRepo.findByUser(Integer.valueOf(Integer.valueOf(login.getDepartmentId())));
//			}
//			List<Graph> graphs = new ArrayList<Graph>();
//			if (login.getUserType().equalsIgnoreCase("ROOT_ADMIN")) {
//				graphs = graphRepo.findAll();
//			} else if (login.getUserType().equalsIgnoreCase("DOMAIN_ADMIN")) {
//				graphs = graphRepo.findByDomain(Integer.valueOf(Integer.valueOf(login.getDomainId())));
//			} else {
//				graphs = graphRepo.findByUser(Integer.valueOf(Integer.valueOf(login.getDepartmentId())));
//			}
//			if (hosts != null) {
//				List<Host> activeHost = hosts.stream().filter(el -> el.getStatus() == 0).collect(Collectors.toList());
//				List<Host> inActiveHost = hosts.stream().filter(el -> el.getStatus() == 1).collect(Collectors.toList());
//				if (activeHost != null) {
//					zabbixBoard.setActiveHosts(activeHost.size());
//				}
//				if (inActiveHost != null) {
//					zabbixBoard.setInActiveHosts(inActiveHost.size());
//				}
//			}
//			if (items != null) {
//				List<Item> inActiveItems = items.stream().filter(el -> el.getStatus() == 1)
//						.collect(Collectors.toList());
//				List<Item> activeItems = items.stream().filter(el -> el.getStatus() == 0).collect(Collectors.toList());
//				if (inActiveItems != null) {
//					zabbixBoard.setInActiveItems(inActiveItems.size());
//					zabbixBoard.setInActiveGraphs(inActiveItems.size());
//				}
//				if (activeItems != null) {
//					zabbixBoard.setActiveItems(activeItems.size());
//				}
//			}
//			if (triggers != null) {
//				List<Trigger> activeTriggers = triggers.stream().filter(el -> el.getStatus() == 0)
//						.collect(Collectors.toList());
//				List<Trigger> inActiveTriggers = triggers.stream().filter(el -> el.getStatus() == 1)
//						.collect(Collectors.toList());
//				if (activeTriggers != null) {
//					zabbixBoard.setActiveTriggers(activeTriggers.size());
//				}
//				if (inActiveTriggers != null) {
//					zabbixBoard.setInActiveTriggers(inActiveTriggers.size());
//				}
//			}
//			if (graphs != null) {
//				zabbixBoard.setActiveGraphs(graphs.size());
//			}
//
//			Sort sort = new Sort(Direction.DESC, "clock");
//			List<Events> eventList = new ArrayList<Events>();
//			for (Trigger trigger : triggers) {
//				List<Events> result = eventRepo.findByAllTrigger(trigger.getTriggerid(), sort);
//				if (result.size() > 0) {
//					if (result.get(0).getValue() != 0) {
//						eventList.add(result.get(0));
//					}
//				}
//			}
//			if (eventList != null) {
//				List<Events> criticalAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Critical"))
//						.collect(Collectors.toList());
//				List<Events> infoAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Info"))
//						.collect(Collectors.toList());
//				List<Events> warnAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Warning"))
//						.collect(Collectors.toList());
//				if (criticalAlerts != null) {
//					zabbixBoard.setCriticalAlerts(criticalAlerts.size());
//				}
//				if (infoAlerts != null) {
//					zabbixBoard.setInfoAlerts(infoAlerts.size());
//				}
//				if (warnAlerts != null) {
//					zabbixBoard.setWarnAlerts(warnAlerts.size());
//				}
//			}
//			List<Graph> favGraphs = graphRepo.findByFavorite(true);
//			zabbixBoard.setFavGraphs(favGraphs);
			messagingTemplate.convertAndSend("/zabbix/dashboard/" + login.getUserId() + "/agent", zabbixBoard);
		}
	}

	@Scheduled(fixedDelayString = "${metrics.heartbeat}000")
	public void updateVMDashboard() throws Exception {
		List<LoginHistory> logins = loginHistoryService.findByAllActiveHost();
		for (LoginHistory login : logins) {
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put(GenericConstants.SIMP_SESSION_ID, login.getSessionId());
			Host host = hostService.find(login.getHostId());
			VMDashboards vmDash = new VMDashboards();
			List<Item> items = itemService.findAllByUsage(host.getHostId());
			if (items.size() > 0) {
				for (Item item : items) {
					if (item.getKey_().equals("agent.ping")) {
						String status = getHistory(item.getItemId());
						if (status.equals("1")) {
							vmDash.setAgentStatus("UP");
							// host.setAvailable(Integer.valueOf(status));
						} else {
							vmDash.setAgentStatus("DOWN");
							// host.setAvailable(Integer.valueOf(status));
						}
					}
					if (item.getKey_().equals("agent.hostname")) {
						vmDash.setHostName(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("proc.num[,,run]")) {
						vmDash.setProcesses(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("proc.num[]") && !item.getOsType().contains("linux")) {
						vmDash.setProcesses(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("system.users.num")) {
						vmDash.setLoggedUsers(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("kernel.maxfiles")) {
						vmDash.setOpenedFiles(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("perf_counter[\\2\\250]") && !item.getOsType().contains("linux")) {
						vmDash.setOpenedFiles(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("system.uptime")) {
						vmDash.setUpTime(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("system.boottime")) {
						vmDash.setBootTime(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("system.uname")) {
						vmDash.setInformation(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("system.hostname")) {
						vmDash.setHostName(getHistory(item.getItemId()));
					}
					if (item.getKey_().equals("vm.memory.size[used]")) {
						String usedMem = getHistory(item.getItemId());
						if (usedMem != null && !usedMem.isEmpty()) {
							vmDash.setUsedMemory(Long.valueOf(usedMem));
						}
					}
					if (item.getKey_().equals("vm.memory.size[total]")) {
						String usedTot = getHistory(item.getItemId());
						if (usedTot != null && !usedTot.isEmpty()) {
							vmDash.setTotalMemory(Long.valueOf(usedTot));
						}
					}
					if (vmDash.getTotalMemory() != null && vmDash.getUsedMemory() != null) {
						vmDash.setAvailableMemory(vmDash.getTotalMemory() - vmDash.getUsedMemory());
					}
				}
				vmDash.setCreateDateTime(host.getCreatedTime());
				List<Graph> graphs = graphRepo.findByHostId(host.getHostId());
				vmDash.setGraphs(Long.valueOf(graphs.size()));
				host.setGraphs(graphs.size());
				List<Item> itemList = itemRepo.findByHost(host.getHostId());
				vmDash.setItems(Long.valueOf(itemList.size()));
				host.setItems(itemList.size());
				vmDash.setStatus(host.getState());
				List<Trigger> triggers = triggerRepo.findByHostId(host.getHostId());
				vmDash.setTriggers(Long.valueOf(triggers.size()));
				host.setTriggers(triggers.size());
				Host zbxHost = hostService.getHost(host.getHostId());
				if (zbxHost != null) {
					host.setAvailable(zbxHost.getAvailable());
					if (host.getAvailable() == 1) {
						vmDash.setAgentStatus("UP");
					} else {
						vmDash.setAgentStatus("DOWN");
					}
				}
				if (vmDash.getAgentStatus().equals("UP")) {
					// host.setAvailable(0);
				} else {
					// host.setAvailable(1);
				}
				List<Events> eventList = new ArrayList<Events>();
				for (Trigger trigger : triggers) {
					List<Events> events = getEvents(trigger.getTriggerid());
					eventList.addAll(events);
				}
				if (eventList.size() > 0) {
					List<Events> criticalAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Critical"))
							.collect(Collectors.toList());
					List<Events> infoAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Info"))
							.collect(Collectors.toList());
					List<Events> warnAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Warning"))
							.collect(Collectors.toList());
					if (criticalAlerts != null) {
						host.setCriticalAlerts(criticalAlerts.size());
					}
					if (infoAlerts != null) {
						host.setInfoAlerts(infoAlerts.size());
					}
					if (warnAlerts != null) {
						host.setWarnAlerts(warnAlerts.size());
					}
				} else {
					host.setCriticalAlerts(0);
					host.setInfoAlerts(0);
					host.setWarnAlerts(0);
				}
				hostService.updateHost(host);
				vmDash.setMessages(eventList);
			}
			messagingTemplate.convertAndSend(
					"/zabbix/vmdashboard/" + login.getUserId() + "/" + host.getHostId() + "/agent", vmDash);
		}
	}

	@Override
	public ZabbixDashBoard findByHost(String id, String type, String hostIds) throws Exception {
		ZabbixDashBoard zabbixBoard = new ZabbixDashBoard();
		List<Host> hosts = new ArrayList<Host>();
		List<Host> hostList = null;
		if (type.equalsIgnoreCase("ROOT_ADMIN")) {
			hostList = hostService.findAll();
		} else if (type.equalsIgnoreCase("DOMAIN_ADMIN")) {
			hostList = hostService.findAllByUser(Integer.valueOf(id), type);
		} else {
			hostList = hostService.findAllByUser(Integer.valueOf(id), type);
		}
		for (Host persistHost : hostList) {
			if (hostIds.contains(","+persistHost.getHostId()+",")) {
				hosts.add(persistHost);
			}
		}
		
		List<Trend> trends = new ArrayList<Trend>();
		for (Host host : hosts) {
			Item cpuItems = itemService.findAllByKeyAndHost(host.getHostId(), "system.cpu.util");
			if (host.getOsType().contains("windows") || host.getOsType().contains("Windows")) {
				cpuItems = itemService.findAllByKeyAndHost(host.getHostId(),
						"perf_counter[\\Processor(_Total)\\% Processor Time]");
			}
			if (cpuItems != null) {
				Trend trend = new Trend();
				Trends cpuTrend = getTrends(cpuItems.getItemId());
				if (cpuTrend != null) {
					trend.setCategory(cpuItems.getCategory());
					trend.setClock(cpuTrend.getClock());
					trend.setHostId(host.getHostId());
					trend.setHostName(host.getName());
					trend.setItemId(cpuItems.getItemId());
					trend.setValue_avg(cpuTrend.getValue_avg());
					trend.setValue_max(cpuTrend.getValue_max());
					trend.setValue_min(cpuTrend.getValue_min());
				}
				if (trend != null) {
					trends.add(trend);
				}
			}
			Item memoryitems = itemService.findAllByKeyAndHost(host.getHostId(), "vm.memory.size[pused]");
			if (memoryitems != null) {
				Trend trend = new Trend();
				Trends emeTrend = getTrends(memoryitems.getItemId());
				if (emeTrend != null) {
					trend.setCategory(memoryitems.getCategory());
					trend.setClock(emeTrend.getClock());
					trend.setHostId(host.getHostId());
					trend.setHostName(host.getName());
					trend.setItemId(memoryitems.getItemId());
					trend.setValue_avg(emeTrend.getValue_avg());
					trend.setValue_max(emeTrend.getValue_max());
					trend.setValue_min(emeTrend.getValue_min());
				}
				if (trend != null) {
					trends.add(trend);
				}
			}
			Item diskRead = itemService.findAllByKeyAndHost(host.getHostId(), "custom.vfs.dev.read.sectors[vda]");
			if (host.getOsType().contains("windows") || host.getOsType().contains("Windows")) {
				diskRead = itemService.findAllByKeyAndHost(host.getHostId(), "perf_counter[\\234(_Total)\\226]");
			}
			if (diskRead != null) {
				Trend trend = new Trend();
				Trends diskTrend = getTrends(diskRead.getItemId());
				if (diskTrend != null) {
					trend.setCategory(diskRead.getCategory());
					trend.setClock(diskTrend.getClock());
					trend.setHostId(host.getHostId());
					trend.setHostName(host.getName());
					trend.setItemId(diskRead.getItemId());
					trend.setValue_avg(diskTrend.getValue_avg());
					trend.setValue_max(diskTrend.getValue_max());
					trend.setValue_min(diskTrend.getValue_min());
				}
				if (trend != null) {
					trends.add(trend);
				}
			}
			Item diskWrite = itemService.findAllByKeyAndHost(host.getHostId(), "custom.vfs.dev.write.sectors[vda]");
			if (host.getOsType().contains("windows") || host.getOsType().contains("Windows")) {
				diskWrite = itemService.findAllByKeyAndHost(host.getHostId(), "perf_counter[\\234(_Total)\\228]");
			}
			if (diskWrite != null) {
				Trend trend = new Trend();
				Trends diskwTrend = getTrends(diskWrite.getItemId());
				if (diskwTrend != null) {
					trend.setCategory(diskWrite.getCategory());
					trend.setClock(diskwTrend.getClock());
					trend.setHostId(host.getHostId());
					trend.setHostName(host.getName());
					trend.setItemId(diskWrite.getItemId());
					trend.setValue_avg(diskwTrend.getValue_avg());
					trend.setValue_max(diskwTrend.getValue_max());
					trend.setValue_min(diskwTrend.getValue_min());
				}
				if (trend != null) {
					trends.add(trend);
				}
			}

			Item netIn = itemService.findAllByKeyAndHost(host.getHostId(), "net.if.in[eth0]");
			if (netIn != null) {
				Trend trend = new Trend();
				Trends netInTrend = getTrends(netIn.getItemId());
				if (netInTrend != null) {
					trend.setCategory(netIn.getCategory());
					trend.setClock(netInTrend.getClock());
					trend.setHostId(host.getHostId());
					trend.setHostName(host.getName());
					trend.setItemId(netIn.getItemId());
					trend.setValue_avg(netInTrend.getValue_avg());
					trend.setValue_max(netInTrend.getValue_max());
					trend.setValue_min(netInTrend.getValue_min());
				}
				if (trend != null) {
					trends.add(trend);
				}
			}
			Item netOut = itemService.findAllByKeyAndHost(host.getHostId(), "net.if.out[eth0]");
			if (netOut != null) {
				Trend trend = new Trend();
				Trends netOutTrend = getTrends(netOut.getItemId());
				if (netOutTrend != null) {
					trend.setCategory(netOut.getCategory());
					trend.setClock(netOutTrend.getClock());
					trend.setHostId(host.getHostId());
					trend.setHostName(host.getName());
					trend.setItemId(netOut.getItemId());
					trend.setValue_avg(netOutTrend.getValue_avg());
					trend.setValue_max(netOutTrend.getValue_max());
					trend.setValue_min(netOutTrend.getValue_min());
				}
				if (trend != null) {
					trends.add(trend);
				}
			}
			Sort sort = new Sort(Direction.DESC, "clock");
			List<Trigger> hostTriggers = triggerRepo.findByHostId(host.getHostId());
			List<Events> hostEventList = new ArrayList<Events>();
			for (Trigger trigger : hostTriggers) {
				getEvents(trigger.getTriggerid());
				List<Events> result = eventRepo.findByAllTrigger(trigger.getTriggerid(), sort);
				if (result.size() > 0) {
					if (result.get(0).getValue() != 0) {
						hostEventList.add(result.get(0));
					}
				}
			}
			List<Events> hostCriticalAlerts = hostEventList.stream().filter(el -> el.getSeverity().equals("Critical"))
					.collect(Collectors.toList());
			List<Events> hostInfoAlerts = hostEventList.stream().filter(el -> el.getSeverity().equals("Info"))
					.collect(Collectors.toList());
			List<Events> hostWarnAlerts = hostEventList.stream().filter(el -> el.getSeverity().equals("Warning"))
					.collect(Collectors.toList());
			if (hostCriticalAlerts != null) {
				host.setCriticalAlerts(hostCriticalAlerts.size());
			}
			if (hostInfoAlerts != null) {
				host.setInfoAlerts(hostInfoAlerts.size());
			}
			if (hostWarnAlerts != null) {
				host.setWarnAlerts(hostWarnAlerts.size());
			}
			hostService.updateHost(host);
		}
		zabbixBoard.setTrends(trends);
		List<Item> items = new ArrayList<>();
		List<Item> itemList = null;
		if (type.equalsIgnoreCase("ROOT_ADMIN")) {
			itemList = itemService.findAll();
		} else if (type.equalsIgnoreCase("DOMAIN_ADMIN")) {
			itemList = itemService.findAllByUser(Integer.valueOf(id), type);
		} else {
			itemList = itemService.findAllByUser(Integer.valueOf(id), type);
		}
		for (Item persistItem : itemList) {
			if (hostIds.contains(","+persistItem.getHostId()+",")) {
				items.add(persistItem);
			}
		}
		
		List<Trigger> triggers = new ArrayList<Trigger>();
		List<Trigger> triggerList = null;
		if (type.equalsIgnoreCase("ROOT_ADMIN")) {
			triggerList = triggerRepo.findAll();
		} else if (type.equalsIgnoreCase("DOMAIN_ADMIN")) {
			triggerList = triggerRepo.findByDomain(Integer.valueOf(Integer.valueOf(id)));
		} else {
			triggerList = triggerRepo.findByUser(Integer.valueOf(Integer.valueOf(id)));
		}
		for (Trigger persistTrigger : triggerList) {
			if (hostIds.contains(","+persistTrigger.getHostId()+",")) {
				triggers.add(persistTrigger);
			}
		}
		
		List<Graph> graphs = new ArrayList<Graph>();
		List<Graph> graphsList = null;
		if (type.equalsIgnoreCase("ROOT_ADMIN")) {
			graphsList = graphRepo.findAll();
		} else if (type.equalsIgnoreCase("DOMAIN_ADMIN")) {
			graphsList = graphRepo.findByDomain(Integer.valueOf(Integer.valueOf(id)));
		} else {
			graphsList = graphRepo.findByUser(Integer.valueOf(Integer.valueOf(id)));
		}
		for (Graph persistGraph : graphsList) {
			if (hostIds.contains(","+persistGraph.getHostId()+",")) {
				graphs.add(persistGraph);
			}
		}
		
		if (hosts != null) {
			List<Host> activeHost = hosts.stream().filter(el -> el.getStatus() == 0).collect(Collectors.toList());
			List<Host> inActiveHost = hosts.stream().filter(el -> el.getStatus() == 1).collect(Collectors.toList());
			if (activeHost != null) {
				zabbixBoard.setActiveHosts(activeHost.size());
			}
			if (inActiveHost != null) {
				zabbixBoard.setInActiveHosts(inActiveHost.size());
			}
		}
		if (items != null) {
			List<Item> inActiveItems = items.stream().filter(el -> el.getStatus() == 1).collect(Collectors.toList());
			List<Item> activeItems = items.stream().filter(el -> el.getStatus() == 0).collect(Collectors.toList());
			if (inActiveItems != null) {
				zabbixBoard.setInActiveItems(inActiveItems.size());
				zabbixBoard.setInActiveGraphs(inActiveItems.size());
			}
			if (activeItems != null) {
				zabbixBoard.setActiveItems(activeItems.size());
			}
		}
		if (triggers != null) {
			List<Trigger> activeTriggers = triggers.stream().filter(el -> el.getStatus() == 0)
					.collect(Collectors.toList());
			List<Trigger> inActiveTriggers = triggers.stream().filter(el -> el.getStatus() == 1)
					.collect(Collectors.toList());
			if (activeTriggers != null) {
				zabbixBoard.setActiveTriggers(activeTriggers.size());
			}
			if (inActiveTriggers != null) {
				zabbixBoard.setInActiveTriggers(inActiveTriggers.size());
			}
		}
		if (graphs != null) {
			zabbixBoard.setActiveGraphs(graphs.size());
		}

		Sort sort = new Sort(Direction.DESC, "clock");
		List<Events> eventList = new ArrayList<Events>();
		for (Trigger trigger : triggers) {
			List<Events> result = eventRepo.findByAllTrigger(trigger.getTriggerid(), sort);
			if (result.size() > 0) {
				if (result.get(0).getValue() != 0) {
					eventList.add(result.get(0));
				}
			}
		}
		if (eventList != null) {
			List<Events> criticalAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Critical"))
					.collect(Collectors.toList());
			List<Events> infoAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Info"))
					.collect(Collectors.toList());
			List<Events> warnAlerts = eventList.stream().filter(el -> el.getSeverity().equals("Warning"))
					.collect(Collectors.toList());
			if (criticalAlerts != null) {
				zabbixBoard.setCriticalAlerts(criticalAlerts.size());
			}
			if (infoAlerts != null) {
				zabbixBoard.setInfoAlerts(infoAlerts.size());
			}
			if (warnAlerts != null) {
				zabbixBoard.setWarnAlerts(warnAlerts.size());
			}
		}
		List<Graph> graphList = new ArrayList<Graph>();
		List<Graph> favGraphs = graphRepo.findByFavorite(true);
		for (Graph graph : favGraphs) {
			if (hostIds.contains(","+graph.getHostId()+",")) {
				if (graph.getGraphItemsList().size() > 0 && graph.getGraphItemsList().get(0) != null) {
					List<Trigger> persistTriggers = triggerService
							.findAllByItem(graph.getGraphItemsList().get(0).getItemId(), graph.getHostId());
					Item item = itemService.find(graph.getGraphItemsList().get(0).getItemId());
					graph.setTriggerCount(persistTriggers.size());
					graph.setTriggerStatus(item.getTriggerStatus());
				}
				graphList.add(graph);
			}
		}
		zabbixBoard.setFavGraphs(graphList);
		return zabbixBoard;
	}

	@Override
	public List<Events> getAllAlerts(Integer id, String type) throws Exception {
		Sort sort = new Sort(Direction.DESC, "clock");
		if (type.equalsIgnoreCase("ROOT_ADMIN")) {
			List<Events> result = eventRepo.findByAllUsingSort(sort);
			return result;
		} else if (type.equalsIgnoreCase("DOMAIN_ADMIN")) {
			List<Events> result = eventRepo.findByAllUsingSortAndDomain(id, sort);
			return result;
		} else {
			List<Events> result = eventRepo.findByAllUsingSortAndUser(id, sort);
			return result;
		}
	}

	@Override
	public List<Events> getByFilterAlerts(Date from, Date to, String hostId, Integer status, String severity,
			Integer id, String type) throws Exception {
		if (type.equalsIgnoreCase("ROOT_ADMIN")) {
			List<Events> events = new ArrayList<Events>();
			if (status != 3 && events.size() > 0) {
				events = events.stream().filter(el -> el.getValue() == status).collect(Collectors.toList());
			} else if (events.size() == 0 && status != 3) {
				events = eventRepo.findAllByState(status);
			}

			if (!severity.equals("none") && events.size() > 0 && !severity.equals("")) {
				events = events.stream().filter(el -> el.getSeverity().equals(severity)).collect(Collectors.toList());
			} else if (events.size() == 0 && !severity.equals("")) {
				events = eventRepo.findAllBySeverity(severity);
			}
			if (!hostId.equals("none") && events.size() > 0 && !hostId.equals("") && !hostId.equals("null")) {
				events = events.stream().filter(el -> el.getHostId().equals(hostId)).collect(Collectors.toList());
			} else if (events.size() == 0 && !hostId.equals("") && !hostId.equals("null")) {
				events = eventRepo.findAllByHost(hostId);
			} else if (hostId.equals("null") && events.size() == 0) {
				events = eventRepo.findAll();
			}
			if (from != null && to != null) {
				List<Events> eventS = eventRepo.findbyEventDateBetweenForDate(from, to);
				List<Events> tempEvents = new ArrayList<Events>();
				for (Events event : eventS) {
					if (events.size() > 0) {
						tempEvents.addAll(events.stream().filter(el -> el.getId().equals(event.getId()))
								.collect(Collectors.toList()));
					}
				}
				if (events.size() > 0) {
					events = tempEvents;
				} else if ((hostId.equals("none") || hostId.equals(""))
						&& (severity.equals("none") || severity.equals("")) && status == 3) {
					events = eventS;
				}
			}
			return events;
		} else if (type.equalsIgnoreCase("DOMAIN_ADMIN")) {
			List<Events> events = new ArrayList<Events>();
			if (status != 3 && events.size() > 0) {
				events = events.stream().filter(el -> el.getValue() == status).collect(Collectors.toList());
			} else if (events.size() == 0 && status != 3) {
				events = eventRepo.findAllByStateAndDomain(status, id);
			}
			if (!severity.equals("none") && events.size() > 0 && !severity.equals("")) {
				events = events.stream().filter(el -> el.getSeverity().equals(severity)).collect(Collectors.toList());
			} else if (events.size() == 0 && !severity.equals("")) {
				events = eventRepo.findAllBySeverityAndDomain(severity, id);
			}
			if (!hostId.equals("none") && events.size() > 0 && !hostId.equals("") && !hostId.equals("null")) {
				events = events.stream().filter(el -> el.getHostId().equals(hostId)).collect(Collectors.toList());
			} else if (events.size() == 0 && !hostId.equals("") && !hostId.equals("null")) {
				events = eventRepo.findAllByHost(hostId);
			} else if (hostId.equals("null") && events.size() == 0) {
				events = eventRepo.findAllByDomain(id);
			}
			if (from != null && to != null) {
				List<Events> eventS = eventRepo.findbyEventDateBetweenForDateAndDomain(from, to, id);
				List<Events> tempEvents = new ArrayList<Events>();
				for (Events event : eventS) {
					if (events.size() > 0) {
						tempEvents.addAll(events.stream().filter(el -> el.getId().equals(event.getId()))
								.collect(Collectors.toList()));
					}
				}
				if (events.size() > 0) {
					events = tempEvents;
				} else if ((hostId.equals("none") || hostId.equals(""))
						&& (severity.equals("none") || severity.equals("")) && status == 3) {
					events = eventS;
				}
			}
			return events;
		} else {
			List<Events> events = new ArrayList<Events>();
			if (status != 3 && events.size() > 0) {
				events = events.stream().filter(el -> el.getValue() == status).collect(Collectors.toList());
			} else if (events.size() == 0 && status != 3) {
				events = eventRepo.findAllByStateAndUser(status, id);
			}
			if (!severity.equals("none") && events.size() > 0 && !severity.equals("")) {
				events = events.stream().filter(el -> el.getSeverity().equals(severity)).collect(Collectors.toList());
			} else if (events.size() == 0 && !severity.equals("")) {
				events = eventRepo.findAllBySeverityAndUser(severity, id);
			}
			if (!hostId.equals("none") && events.size() > 0 && !hostId.equals("") && !hostId.equals("null")) {
				events = events.stream().filter(el -> el.getHostId().equals(hostId)).collect(Collectors.toList());
			} else if (events.size() == 0 && !hostId.equals("") && !hostId.equals("null")) {
				events = eventRepo.findAllByHost(hostId);
			} else if (hostId.equals("null") && events.size() == 0) {
				events = eventRepo.findAllByDept(id);
			}

			if (from != null && to != null) {
				List<Events> eventS = eventRepo.findbyEventDateBetweenForDateAndUser(from, to, id);
				List<Events> tempEvents = new ArrayList<Events>();
				for (Events event : eventS) {
					if (events.size() > 0) {
						tempEvents.addAll(events.stream().filter(el -> el.getId().equals(event.getId()))
								.collect(Collectors.toList()));
					}
				}
				if (events.size() > 0) {
					events = tempEvents;
				} else if ((hostId.equals("none") || hostId.equals(""))
						&& (severity.equals("none") || severity.equals("")) && status == 3) {
					events = eventS;
				}
			}
			return events;
		}
	}

	@Override
	public List<Events> getRecentByFilterAlerts(String hostId, String status, String severity, Integer id, String type)
			throws Exception {
		if (type.equalsIgnoreCase("ROOT_ADMIN")) {
			List<Trigger> triggers = triggerRepo.findAll();
			Sort sort = new Sort(Direction.DESC, "clock");
			List<Events> eventList = new ArrayList<Events>();
			for (Trigger trigger : triggers) {
				List<Events> result = eventRepo.findByAllTrigger(trigger.getTriggerid(), sort);
				if (result.size() > 0) {
					if (result.get(0).getValue() != 0) {
						eventList.add(result.get(0));
					}
				}
			}
			if (!hostId.equalsIgnoreCase("unknown")) {
				eventList = eventList.stream().filter(el -> el.getHostId().equals(hostId)).collect(Collectors.toList());
			}
			if (!severity.equalsIgnoreCase("unknown")) {
				eventList = eventList.stream().filter(el -> el.getSeverity().equals(severity))
						.collect(Collectors.toList());
			}
			return eventList;
		} else if (type.equalsIgnoreCase("DOMAIN_ADMIN")) {
			List<Trigger> triggers = triggerRepo.findByDomain(id);
			Sort sort = new Sort(Direction.DESC, "clock");
			List<Events> eventList = new ArrayList<Events>();
			for (Trigger trigger : triggers) {
				List<Events> result = eventRepo.findByAllTrigger(trigger.getTriggerid(), sort);
				if (result.size() > 0) {
					if (result.get(0).getValue() != 0) {
						eventList.add(result.get(0));
					}
				}
			}
			if (!hostId.equalsIgnoreCase("unknown")) {
				eventList = eventList.stream().filter(el -> el.getHostId().equals(hostId)).collect(Collectors.toList());
			}
			if (!severity.equalsIgnoreCase("unknown")) {
				eventList = eventList.stream().filter(el -> el.getSeverity().equals(severity))
						.collect(Collectors.toList());
			}
			return eventList;
		} else {
			List<Trigger> triggers = triggerRepo.findByUser(id);
			Sort sort = new Sort(Direction.DESC, "clock");
			List<Events> eventList = new ArrayList<Events>();
			for (Trigger trigger : triggers) {
				List<Events> result = eventRepo.findByAllTrigger(trigger.getTriggerid(), sort);
				if (result.size() > 0) {
					if (result.get(0).getValue() != 0) {
						eventList.add(result.get(0));
					}
				}
			}
			if (!hostId.equalsIgnoreCase("unknown")) {
				eventList = eventList.stream().filter(el -> el.getHostId().equals(hostId)).collect(Collectors.toList());
			}
			if (!severity.equalsIgnoreCase("unknown")) {
				eventList = eventList.stream().filter(el -> el.getSeverity().equals(severity))
						.collect(Collectors.toList());
			}
			return eventList;
		}
	}

	@Override
	public List<Events> syncEventService(String hostId) throws Exception {
		List<Events> eventList = new ArrayList<Events>();
		List<Trigger> triggers = triggerRepo.findByHostId(hostId);
		for (Trigger trigger : triggers) {
			List<Events> events = getEvents(trigger.getTriggerid());
			eventList.addAll(events);
		}
		return eventList;
	}

	@Override
	public void syncEvent(List<String> hostIds) throws Exception {
		List<Events> events = eventRepo.findEventByNonHost(hostIds);
		eventRepo.delete(events);
	}

	@Override
	public void removeAllEvent(String hostIds) throws Exception {
		List<Events> events = eventRepo.findAll();
		eventRepo.delete(events);
	}

	@Override
	public void syncEventTrigger(List<String> triggers) throws Exception {
		List<Events> events = eventRepo.findEventByNonTrigger(triggers);
		eventRepo.delete(events);
	}
}
