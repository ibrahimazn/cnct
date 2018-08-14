package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import az.ldap.notification.NotificationAction;
import az.ldap.notification.NotificationService;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.Action;
import az.ldap.zabbix.entity.Alarms;
import az.ldap.zabbix.entity.Conditions;
import az.ldap.zabbix.entity.DefaultTriggers;
import az.ldap.zabbix.entity.FilterResponse;
import az.ldap.zabbix.entity.Groups;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.Message;
import az.ldap.zabbix.entity.Operations;
import az.ldap.zabbix.entity.RecoveryOperations;
import az.ldap.zabbix.entity.Trigger;
import az.ldap.zabbix.entity.Users;
import az.ldap.zabbix.repository.DefaultTriggersRepository;
import az.ldap.zabbix.repository.GroupsRepository;
import az.ldap.zabbix.repository.ItemRepository;
import az.ldap.zabbix.repository.TriggerRepository;
import az.ldap.zabbix.repository.UsersRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.trigger.FilterRequest;
import az.zabbix.connector.trigger.TriggerCUDResponse;
import az.zabbix.connector.trigger.TriggerCUDResult;
import az.zabbix.connector.trigger.TriggerCreateRequest;
import az.zabbix.connector.trigger.TriggerDeleteRequest;
import az.zabbix.connector.trigger.TriggerListRequest;
import az.zabbix.connector.trigger.TriggerListResponse;
import az.zabbix.connector.trigger.TriggerParams;
import az.zabbix.connector.trigger.TriggerProtoCreateRequest;
import az.zabbix.connector.trigger.TriggerRequest;
import az.zabbix.connector.trigger.TriggerResponse;
import az.zabbix.connector.trigger.TriggerUpdateRequest;

/** Trigger service implementation class. */
@Service
public class TriggerServiceImpl implements TriggerService {
	
	/** Logger attribute. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerServiceImpl.class);

	/** Trigger repository reference. */
	@Autowired
	private TriggerRepository triggerRepo;

	@Value(value = "${zabbix.proxy}")
	private String triggerUrl;
	
	@Value(value = "${zabbix.mediatype}")
	private String mediaType;
	
	@Value(value = "${zabbix.defaultuser}")
	private String defaultUser;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private GroupsRepository groupsRepository;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private HostService hostService;
	
	@Autowired
	private DefaultTriggersRepository defaultTriggerRepo;
	
	@Autowired 
	private ActionService actionService;
	
	@Autowired
	private NotificationService notificationService;
	
	/** Trigger creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<TriggerProtoCreateRequest, TriggerCUDResult> triggerProtoCreateReq;

	/** Trigger creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<TriggerCreateRequest, TriggerCUDResult> triggerCreateReq;

	/** Trigger update Connection processor reference. */
	@Autowired
	private RestTemplateCall<TriggerUpdateRequest, TriggerCUDResult> triggerUpdateReq;

	/** Trigger delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<TriggerDeleteRequest, TriggerCUDResult> triggerDeleteReq;

	/** Trigger lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<TriggerListRequest, TriggerListResponse> triggerListReq;
	
	@Override
	public Trigger save(Trigger trigger) throws Exception {
		List<DefaultTriggers> triggers = defaultTriggerRepo.findAllByDefaultAndHost(trigger.getOsType());
		Item item = itemRepository.findByItemId(trigger.getMetricId());
		Host host = hostService.find(trigger.getHostId());
		for (DefaultTriggers exTrigger : triggers) {
			if (exTrigger.getDescription().contains("#")) {
				if (item.getKey_().contains("[") && item.getKey_().split("\\[")[0]
						.equalsIgnoreCase((exTrigger.getExpression().split(":")[1]).split("\\[")[0])) {
					if (exTrigger.getDescription().contains("{#DEVICENAME}")) {
						int ite = item.getKey_().indexOf("[");
						int itew = item.getKey_().indexOf("]");
						trigger.setDescription(exTrigger.getDescription().replaceAll("\\{#DEVICENAME\\}",
								item.getKey_().substring(item.getKey_().indexOf("["), item.getKey_().indexOf("]"))));
						String expression = exTrigger.getExpression().replaceAll("\\{#DEVICENAME\\}",
								item.getKey_().substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf("]")));
						if (trigger.getOsType().contains("linux")) {
							expression = expression.replaceAll("Template OS Linux", host.getName());
						} else {
							expression = expression.replaceAll("Template OS Windows", host.getName());
						}
						String[] expressions = expression.split(":");
						expression = expressions[0] + ":" + expressions[1];
						String thresold = expressions[2].replaceAll("\\(T\\)", "(" + trigger.getInterval() + ")");
						thresold = thresold.replaceAll("N", trigger.getThresold());
						expression = expression + "." + thresold;
						trigger.setExpression(expression);
						String message = exTrigger.getMessage().replaceAll("\\{#DEVICENAME\\}",
								item.getKey_().substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf("]")));
						String severity = null;
						if (trigger.getPriority() == 1) {
							severity = "Info";
						} else if (trigger.getPriority() == 2) {
							severity = "Warning";
						}  else if (trigger.getPriority() == 4) {
							severity = "Critical";
						}
						message = message.replaceAll("\\{#SEVERITY\\}", severity);
						String triggerMessage = message.replaceAll("\\{#N\\}", trigger.getThresold());
						trigger.setMessage(triggerMessage.replaceAll("\\{#T\\}", trigger.getInterval()));
					}
					if (exTrigger.getDescription().contains("{#IFNAME}")) {
						trigger.setDescription(exTrigger.getDescription().replaceAll("\\{#IFNAME\\}", item.getKey_()
								.substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf("]"))));
						String expression = exTrigger.getExpression().replaceAll("\\{#IFNAME\\}",
								item.getKey_().substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf("]")));
						if (trigger.getOsType().contains("linux")) {
							expression = expression.replaceAll("Template OS Linux", host.getName());
						} else {
							expression = expression.replaceAll("Template OS Windows", host.getName());
						}
						String[] expressions = expression.split(":");
						expression = expressions[0] + ":" + expressions[1];
						String thresold = expressions[2].replaceAll("\\(T\\)", "(" + trigger.getInterval() + ")");
						thresold = thresold.replaceAll("N", trigger.getThresold());
						expression = expression + "." + thresold;
						trigger.setExpression(expression);						
						String message = exTrigger.getMessage().replaceAll("\\{#IFNAME\\}",
								item.getKey_().substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf("]")));
						String severity = null;
						if (trigger.getPriority() == 1) {
							severity = "Info";
						} else if (trigger.getPriority() == 2) {
							severity = "Warning";
						}  else if (trigger.getPriority() == 4) {
							severity = "Critical";
						}	
						message = message.replaceAll("\\{#SEVERITY\\}", severity);
						String triggerMessage = message.replaceAll("\\{#N\\}", trigger.getThresold());
						trigger.setMessage(triggerMessage.replaceAll("\\{#T\\}", trigger.getInterval()));
					}
					if (exTrigger.getDescription().contains("{#FSNAME}")) {
						trigger.setDescription(exTrigger.getDescription().replaceAll("\\{#FSNAME\\}", item.getKey_()
								.substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf("]"))));
						String expression = exTrigger.getExpression().replaceAll("\\{#FSNAME\\}",
								item.getKey_().substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf(",")));
						if (trigger.getOsType().contains("linux")) {
							expression = expression.replaceAll("Template OS Linux", host.getName());
						} else {
							expression = expression.replaceAll("Template OS Windows", host.getName());
						}
						String[] expressions = expression.split(":");
						expression = expressions[0] + ":" + expressions[1];
						String thresold = expressions[2].replaceAll("\\(T\\)", "(" + trigger.getInterval() + ")");
						thresold = thresold.replaceAll("N", trigger.getThresold());
						expression = expression + "." + thresold;
						trigger.setExpression(expression);
						String message = exTrigger.getMessage().replaceAll("\\{#FSNAME\\}",
								item.getKey_().substring(item.getKey_().indexOf("[") + 1, item.getKey_().indexOf("]")));
						String severity = null;
						if (trigger.getPriority() == 1) {
							severity = "Info";
						} else if (trigger.getPriority() == 2) {
							severity = "Warning";
						}  else if (trigger.getPriority() == 4) {
							severity = "Critical";
						}
						message = message.replaceAll("\\{#SEVERITY\\}", severity);
						String triggerMessage = message.replaceAll("\\{#N\\}", trigger.getThresold());
						trigger.setMessage(triggerMessage.replaceAll("\\{#T\\}", trigger.getInterval()));
					}

				}
			} else {
				if (item.getKey_().equalsIgnoreCase(exTrigger.getExpression().split(":")[1])) {
					if (item.getKey_().equalsIgnoreCase(exTrigger.getExpression().split(":")[1])) {
						trigger.setDescription(exTrigger.getDescription());
						String expression = exTrigger.getExpression();
						if (trigger.getOsType().contains("linux")) {
							expression = expression.replaceAll("Template OS Linux", host.getName());
						} else {
							expression = expression.replaceAll("Template OS Windows", host.getName());
						}
						String[] expressions = expression.split(":");
						expression = expressions[0] + ":" + expressions[1];
						String thresold = expressions[2].replaceAll("\\(T\\)", "(" + trigger.getInterval() + ")");
						thresold = thresold.replaceAll("N", trigger.getThresold());
						expression = expression + "." + thresold;
						trigger.setExpression(expression);
						String message = exTrigger.getMessage();
						String severity = null;
						if (trigger.getPriority() == 1) {
							severity = "Info";
						} else if (trigger.getPriority() == 2) {
							severity = "Warning";
						}  else if (trigger.getPriority() == 4) {
							severity = "Critical";
						}
						message = message.replaceAll("\\{#SEVERITY\\}", severity);
						String triggerMessage = message.replaceAll("\\{#N\\}", trigger.getThresold());
						trigger.setMessage(triggerMessage.replaceAll("\\{#T\\}", trigger.getInterval()));
					}
				}
			}
		}
		if (trigger.getName() != null) {
			trigger.setDescription(trigger.getName());
	    }
		LOGGER.debug("Create trigger request : " + triggerUrl + " : Auth : " + GenericConstants.AUTH + " : trigger : " + trigger);
		TriggerCUDResult triggerResponse = triggerCreateReq.restCall(triggerUrl, getCreateTriggerRequest(GenericConstants.AUTH, trigger),
				TriggerCUDResult.class);
		if (triggerResponse.getError() != null) {
			LOGGER.debug("Create trigger error : "+triggerResponse.getError().toString());
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, triggerResponse.getError().get("data"));
		}
		LOGGER.debug("Create trigger response id : "+triggerResponse.getId());
		TriggerCUDResponse triggerRes = triggerResponse.getResult();
		LOGGER.debug("Create trigger result response id : "+triggerRes.getTriggerids().get(0));
		trigger.setTriggerid(triggerRes.getTriggerids().get(0));
		Action action = trigger.getAction();
		if (trigger.getNotificationPlanId() != null) {
			LOGGER.debug("Trigger notification plan started");
			action.setDef_longdata("{TRIGGER.STATUS} | {TRIGGER.NAME} | {HOST.NAME} | {TRIGGER.SEVERITY} | {ITEM.VALUE} | {ITEM.KEY} | {TRIGGER.ID} | {ACTION.ID} | {EVENT.DATE} | {EVENT.TIME} | {TRIGGER.DESCRIPTION} | {EVENT.ID}");
			action.setDef_shortdata("{TRIGGER.STATUS} | {TRIGGER.NAME}");
			action.setR_longdata("{TRIGGER.STATUS} | {TRIGGER.NAME} | {HOST.NAME} | {TRIGGER.SEVERITY} | {ITEM.VALUE} | {ITEM.KEY} | {TRIGGER.ID} | {ACTION.ID} | {EVENT.DATE} | {EVENT.TIME} | {EVENT.RECOVERY.VALUE} | {EVENT.RECOVERY.DATE} | {EVENT.RECOVERY.TIME} | {TRIGGER.DESCRIPTION} | {EVENT.RECOVERY.ID}");
			action.setR_shortdata("{TRIGGER.STATUS} | {TRIGGER.NAME}");
			action.setDepartmentid_(trigger.getDepartmentid_());
			action.setUserid_(trigger.getUserid_());
			action.setDomainid_(trigger.getDomainid_());
			action.setEsc_period("60");
			action.setEventsource(0);
			action.setName(trigger.getDescription()+" for value "+trigger.getThresold());
			action.setMaintenance_mode(1);
			FilterResponse filter = new FilterResponse();
			filter.setEvaltype(0);
			Conditions condition = new Conditions();
			condition.setConditiontype(2);
			condition.setOperator(0);
			condition.setValue(trigger.getTriggerid());
			List<Conditions> condtions = new ArrayList<Conditions>();
			condtions.add(condition);
			filter.setConditions(condtions);
			action.setFilter(filter);
			List<Operations> operations = new ArrayList<Operations>();
			Operations operation = new Operations();
			operation.setEsc_period("0");
			operation.setEsc_step_from(1);
			operation.setEsc_step_to(2);
			operation.setEvaltype(0);
			operation.setOperationtype(0);
			List<Users> users = new ArrayList<Users>();
			Users user = new Users();
			user.setUserid(defaultUser);
			users.add(user);
			operation.setOpmessage_usr(users);
			Message message = new Message();
			message.setMediatypeid(mediaType);
			message.setDefault_msg(1);
			operation.setOpmessage(message);
			operations.add(operation);
			action.setOperations(operations);
			List<RecoveryOperations> recovery = new ArrayList<RecoveryOperations>();
			RecoveryOperations recoveryOperations = new RecoveryOperations();
			recoveryOperations.setOperationtype(0);
			List<Users> usrs = new ArrayList<Users>();
			Users usr = new Users();
			usr.setUserid(defaultUser);
			usrs.add(usr);
			recoveryOperations.setOpmessage_usr(usrs);
			message = new Message();
			message.setMediatypeid(mediaType);
			message.setDefault_msg(0);
			message.setSubject("{TRIGGER.STATUS} | {TRIGGER.NAME}");
			message.setMessage("{TRIGGER.STATUS} | {TRIGGER.NAME} | {HOST.NAME} | {TRIGGER.SEVERITY} | {ITEM.VALUE} | {ITEM.KEY} | {TRIGGER.ID} | {ACTION.ID} | {EVENT.DATE} | {EVENT.TIME} | {EVENT.RECOVERY.VALUE} | {EVENT.RECOVERY.DATE} | {EVENT.RECOVERY.TIME} | {TRIGGER.DESCRIPTION} | {EVENT.RECOVERY.ID}");
			recoveryOperations.setOpmessage(message);
			recovery.add(recoveryOperations);
			action.setRecovery_operations(recovery);
			action.setTriggerid(trigger.getTriggerid());
			actionService.save(action);
			updateNotificationAction(trigger, "CREATE");
		}
		LOGGER.debug("Create trigger save : "+trigger.getTriggerid());
		return triggerRepo.save(trigger);
	}
	
	public void updateNotificationAction(Trigger trigger, String fromValue) throws Exception {
		LOGGER.debug("Update notification action started");
		if (trigger.getNotificationPlanId() != null || fromValue.equals("UPDATE")) {
			NotificationAction notificationAction = new NotificationAction();
			notificationAction.setObjectId(trigger.getTriggerid());
			if (trigger.getNotificationPlanId() != null) {
				notificationAction.setGroupId(Long.parseLong(trigger.getNotificationPlanId()));
			}
			notificationAction.setThresold(trigger.getThresold());
			notificationService.updateNotificationAction(notificationAction);
		}
		LOGGER.debug("Update notification action completed");
	}
	
	@Override
	public List<Alarms> getAllAlarms(String hostId) throws Exception {
		List<Alarms> alarms = new ArrayList<>();
		List<Trigger> triggers = triggerRepo.findByHostId(hostId);
		for (Trigger trigger : triggers) {
			Action action = actionService.findByTriggerId(trigger.getTriggerid());
			List<Users> users = usersRepository.findByTriggerId(trigger.getTriggerid());
			List<Groups> groups = groupsRepository.findByTriggerId(trigger.getTriggerid());
			if (action != null) {
				Alarms alarm = convertToAlarms(trigger, action, users, groups);
				if (alarm != null) {
					alarms.add(alarm);
				}
			} else {
				Alarms alarm = convertAlarms(trigger, users, groups);
				if (alarm != null) {
					alarms.add(alarm);
				}
			}
		}
		return alarms;
	}
	
	public Alarms convertToAlarms(Trigger trigger, Action action, List<Users> users, List<Groups> groups) throws Exception{
		Alarms alarm = new Alarms();
		alarm.setActionid(action.getActionid());
		alarm.setTriggerId(trigger.getTriggerid());
		alarm.setDepartmentid_(trigger.getDepartmentid_());
		alarm.setUserid_(trigger.getUserid_());
		alarm.setDomainid_(trigger.getDomainid_());
		alarm.setItemId(trigger.getMetricId());
		alarm.setItemName(trigger.getMetricName());
		alarm.setTriggerName(trigger.getDescription());
		alarm.setMessage(action.getName());
		alarm.setGroups(groups);
		alarm.setUsers(users);
		if (trigger.getNotificationPlanId() != null) {
			alarm.setNotificationPlanId(trigger.getNotificationPlanId());
		}
		Item item = itemRepository.findByItemId(trigger.getMetricId());
		if (item != null) {
			alarm.setUnits(item.getUnits());
		}
		alarm.setHostId(trigger.getHostId());
		alarm.setThresold(trigger.getThresold());
		alarm.setInterval(trigger.getInterval());
		alarm.setPriority(trigger.getPriority());
		return alarm;
	}
	
	public Alarms convertAlarms(Trigger trigger, List<Users> users, List<Groups> groups) throws Exception{
		Alarms alarm = new Alarms();
		alarm.setActionid(null);
		alarm.setTriggerId(trigger.getTriggerid());
		alarm.setDepartmentid_(trigger.getDepartmentid_());
		alarm.setUserid_(trigger.getUserid_());
		alarm.setDomainid_(trigger.getDomainid_());
		alarm.setItemId(trigger.getMetricId());
		alarm.setItemName(trigger.getMetricName());
		alarm.setTriggerName(trigger.getDescription());
		alarm.setMessage(trigger.getDescription());
		alarm.setGroups(groups);
		alarm.setUsers(users);
		if (trigger.getNotificationPlanId() != null) {
			alarm.setNotificationPlanId(trigger.getNotificationPlanId());
		}
		Item item = itemRepository.findByItemId(trigger.getMetricId());
		if (item != null) {
			alarm.setUnits(item.getUnits());
		}
		alarm.setHostId(trigger.getHostId());
		alarm.setThresold(trigger.getThresold());
		alarm.setInterval(trigger.getInterval());
		alarm.setPriority(trigger.getPriority());
		return alarm;
	}

	@Override
	public Trigger update(Trigger trigger) throws Exception {
		TriggerCUDResult triggerResponse = triggerUpdateReq.restCall(triggerUrl, getUpdateTriggerRequest(GenericConstants.AUTH, trigger),
				TriggerCUDResult.class);
		if (triggerResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, triggerResponse.getError().get("data"));
		}
		TriggerCUDResponse triggerRes = triggerResponse.getResult();
		trigger.setTriggerid(triggerRes.getTriggerids().get(0));
		trigger = triggerRepo.save(trigger);
		updateNotificationAction(trigger, "UPDATE");		
		return trigger;
	}

	@Override
	public void delete(Trigger trigger) throws Exception {
		TriggerCUDResult triggerResponse = triggerDeleteReq.restCall(triggerUrl,
				getDeleteTriggerRequest(GenericConstants.AUTH, trigger.getTriggerid()), TriggerCUDResult.class);
		if (triggerResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, triggerResponse.getError().get("data"));
		}
		TriggerCUDResponse triggerRes = triggerResponse.getResult();
		trigger.setTriggerid(triggerRes.getTriggerids().get(0));
		historyService.syncEventService(trigger.getHostId());
		triggerRepo.delete(trigger);
	}

	@Override
	public void delete(String id) throws Exception {
		TriggerCUDResult triggerResponse = triggerDeleteReq.restCall(triggerUrl, getDeleteTriggerRequest(GenericConstants.AUTH, id),
				TriggerCUDResult.class);
		if (triggerResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, triggerResponse.getError().get("data"));
		}
		TriggerCUDResponse triggerRes = triggerResponse.getResult();
		Trigger trigger = find(triggerRes.getTriggerids().get(0));
		trigger.setTriggerid(triggerRes.getTriggerids().get(0));
		historyService.syncEventService(trigger.getHostId());
		triggerRepo.delete(id);
	}

	@Override
	public Trigger find(String id) throws Exception {
		Trigger trigger = triggerRepo.findByTriggerId(id);
		return trigger;
	}

	@Override
	public List<Trigger> findAll() throws Exception {
		return triggerRepo.findAll();
	}

	@Override
	public Page<Trigger> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return triggerRepo.findAll(pagingAndSorting.toPageRequest());
	}

	public Trigger getTrigger(String triggerId) throws Exception {
		LOGGER.debug("Get trigger request : " + triggerUrl + " : triggerid : " + triggerId + " : Auth : " + GenericConstants.AUTH);
		TriggerListResponse triggersResponse = triggerListReq.restCall(triggerUrl, getTriggerRequest(GenericConstants.AUTH, triggerId),
				TriggerListResponse.class);
		if (triggersResponse.getError() != null) {
			LOGGER.debug("Get trigger error : "+triggersResponse.getError().toString());
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, triggersResponse.getError().get("data"));
		}
		LOGGER.debug("Get trigger response id : "+triggersResponse.getId());
		List<TriggerResponse> triggerRes = triggersResponse.getResult();
		if (triggerRes.size() > 0) {
			if (find(triggerRes.get(0).getTriggerid()) == null) {
				Trigger trigger = triggerEntityMapperUtil(triggerRes.get(0));
				return trigger;
			}
			return find(triggerRes.get(0).getTriggerid());
		}
		return null;
	}

	private TriggerCreateRequest getCreateTriggerRequest(String authCode, Trigger trigger) {
		TriggerCreateRequest request = new TriggerCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("trigger.create");
		TriggerParams triggerParams = new TriggerParams();
		triggerParams.setDescription(trigger.getDescription());
		triggerParams.setComments(trigger.getMessage());
		triggerParams.setExpression(trigger.getExpression());
		triggerParams.setPriority(trigger.getPriority());
		triggerParams.setStatus(trigger.getStatus());
		request.setParams(triggerParams);
		return request;
	}

	private TriggerProtoCreateRequest getCreateProtoTriggerRequest(String authCode, Trigger trigger) {
		TriggerProtoCreateRequest request = new TriggerProtoCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("triggerprototype.create");
		TriggerParams triggerParams = new TriggerParams();
		triggerParams.setDescription(trigger.getDescription());
		triggerParams.setExpression(trigger.getExpression());
		triggerParams.setPriority(trigger.getPriority());
		triggerParams.setStatus(trigger.getStatus());
		request.setParams(triggerParams);
		return request;
	}
	
	private TriggerDeleteRequest getDeleteTriggerRequest(String authCode, String triggerId) {
		TriggerDeleteRequest request = new TriggerDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("trigger.delete");
		List<String> trigger = new ArrayList<String>();
		trigger.add(triggerId);
		request.setParams(trigger);
		return request;
	}

	private TriggerListRequest getAllTriggerRequest(String authCode, String hostId) {
		TriggerListRequest request = new TriggerListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("trigger.get");
		TriggerRequest trigger = new TriggerRequest();
		trigger.setOutput("extend");
		trigger.setSelectFunctions("extend");
		trigger.setSortfield("triggerid");
		List<String> hosts = new ArrayList<String>(Arrays.asList(hostId.split(",")));
		trigger.setHostids(hosts);
		trigger.setSortorder("ASC");
		request.setParams(trigger);
		return request;
	}

	private TriggerListRequest getTriggerRequest(String authCode, String triggerId) {
		TriggerListRequest request = new TriggerListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("trigger.get");
		TriggerRequest trigger = new TriggerRequest();
		trigger.setOutput("extend");
		trigger.setSortfield("triggerid");
		trigger.setSortorder("ASC");
		FilterRequest filter = new FilterRequest();
		filter.setTriggerid(triggerId);
		trigger.setSearch(filter);
		request.setParams(trigger);
		return request;
	}

	private TriggerUpdateRequest getUpdateTriggerRequest(String authCode, Trigger trigger) {
		TriggerUpdateRequest request = new TriggerUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("trigger.update");
		TriggerParams triggerParams = new TriggerParams();
		triggerParams.setExpression(trigger.getExpression());
		triggerParams.setTriggerid(trigger.getTriggerid());
		request.setParams(triggerParams);
		return request;
	}

	public Trigger triggerEntityMapperUtil(TriggerResponse triggerResponse) throws Exception {
		Trigger trigger = null;
		if (triggerResponse != null) {
			trigger = new Trigger();
			trigger.setComments(triggerResponse.getComments());
			trigger.setMetricId(triggerResponse.getFunctions().get(0).getItemid());
			Item item = itemRepository.findByItemId(trigger.getMetricId());
			trigger.setDescription(triggerResponse.getDescription());
			trigger.setExpression(triggerResponse.getExpression());
			if (item != null) {
				Host host = hostService.find(item.getHostId());
				String expression = host.getName() + ":" + item.getKey_() + "."
						+ triggerResponse.getFunctions().get(0).getFunction() + "("
						+ triggerResponse.getFunctions().get(0).getParameter() + ")";
				String exprs = trigger.getExpression();
				exprs = exprs.replaceAll(triggerResponse.getFunctions().get(0).getFunctionid(), expression.trim());
				trigger.setExpression(exprs);
				trigger.setHostId(host.getHostId());
				trigger.setThresold(trigger.getExpression().substring(trigger.getExpression().indexOf("}")+2, trigger.getExpression().length()));
				trigger.setInterval(triggerResponse.getFunctions().get(0).getParameter());
				trigger.setOsType(item.getOsType());
				trigger.setCategory(item.getCategory());
				trigger.setDepartmentid_(host.getDepartmentid_());
				trigger.setDomainid_(host.getDomainid_());
				trigger.setUserid_(host.getUserid_());
				trigger.setMetricKey(item.getKey_());
				trigger.setMetricName(item.getName());
			}
			trigger.setPriority(Integer.valueOf(triggerResponse.getPriority()));
			trigger.setStatus(Integer.valueOf(triggerResponse.getStatus()));
			trigger.setTriggerid(triggerResponse.getTriggerid());
		}
		return trigger;
	}

	public Map<String, Trigger> convert(List<Trigger> triggerList) {
		Map<String, Trigger> triggerMap = new HashMap<String, Trigger>();
		for (Trigger trigger : triggerList) {
			triggerMap.put(trigger.getTriggerid(), trigger);
		}
		return triggerMap;
	}

	@Override
	public List<Trigger> findAllFromZabbixServer(String hostId) throws Exception {
		LOGGER.debug("Host trigger sync request : " + triggerUrl + " : hostid : " + hostId + " : Auth : " + GenericConstants.AUTH);
		TriggerListResponse triggersResponse = triggerListReq.restCall(triggerUrl, getAllTriggerRequest(GenericConstants.AUTH, hostId),
				TriggerListResponse.class);
		if (triggersResponse.getError() != null) {
			LOGGER.debug("Host trigger sync error : "+triggersResponse.getError().toString());
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, triggersResponse.getError().get("data"));
		}
		LOGGER.debug("Trigger sync response id : "+triggersResponse.getId());
		List<TriggerResponse> triggerRes = triggersResponse.getResult();
		List<Trigger> triggers = new ArrayList<Trigger>();
		for (TriggerResponse trigger : triggerRes) {
			LOGGER.debug("Trigger entity mapper loop running");
			triggers.add(triggerEntityMapperUtil(trigger));
		}
		LOGGER.debug("Trigger entity mapper loop end");
		HashMap<String, Trigger> triggerMap = (HashMap<String, Trigger>) convert(triggers);
		LOGGER.debug("Trigger map size : " + triggerMap.size());
		List<Trigger> appTriggerList = triggerRepo.findByHostId(hostId);
		LOGGER.debug("Trigger entity size : " + appTriggerList.size());
		for (Trigger trigger : appTriggerList) {
			if (trigger.getTriggerid() != null) {
				if (triggerMap.containsKey(trigger.getTriggerid())) {
					LOGGER.debug("Trigger update started : " + trigger.getTriggerid());
					Trigger csTrigger = triggerMap.get(trigger.getTriggerid());
					trigger.setComments(csTrigger.getComments());
					trigger.setDescription(csTrigger.getDescription());
					trigger.setExpression(csTrigger.getExpression());
					trigger.setHostId(csTrigger.getHostId());
					trigger.setPriority(csTrigger.getPriority());
					trigger.setStatus(csTrigger.getStatus());
					trigger.setTriggerid(csTrigger.getTriggerid());
					trigger.setCategory(csTrigger.getCategory());
					trigger.setOsType(csTrigger.getOsType());
					trigger.setThresold(csTrigger.getThresold());
					trigger.setInterval(csTrigger.getInterval());
					trigger.setMetricId(csTrigger.getMetricId());
					trigger.setMetricKey(csTrigger.getMetricKey());
					trigger.setMetricName(csTrigger.getMetricName());
					trigger.setDepartmentid_(csTrigger.getDepartmentid_());
					trigger.setDomainid_(csTrigger.getDomainid_());
					trigger.setUserid_(csTrigger.getUserid_());
					triggerRepo.save(trigger);
					triggerMap.remove(trigger.getTriggerid());
				} else {
					//if (trigger.getTriggerid() != null) {
					triggerRepo.delete(trigger);
					//}
				}
			}
		}
		for (String key : triggerMap.keySet()) {
			LOGGER.debug("Trigger save started : " + triggerMap.get(key).getTriggerid());
			triggerRepo.save(triggerMap.get(key));
		}
		LOGGER.debug("Trigger sync completed");
		return findAll();
	}

	@Override
	public List<Trigger> findAllByHost(String hostId) throws Exception {
		return triggerRepo.findByHostId(hostId);
	}

	@Override
	public void deleteAll(List<Trigger> trigger) throws Exception {
		triggerRepo.delete(trigger);
	}

	@Override
	public List<Trigger> findAllFromConfig(String osType) throws Exception {
		List<Trigger> resultTrigger = new ArrayList<Trigger>();
		return triggerRepo.findByHostId(null);
	}

	@Override
	public List<Trigger> updateByHost(String osType, String hostName, String hostId) throws Exception {
		List<Trigger> triggers = findAllFromConfig(osType);
		for (Trigger trigger : triggers) {
			String expression = trigger.getExpression();
			String description = trigger.getDescription();
			trigger.setExpression(expression);
			trigger.setDescription(description);
			trigger.setHostId(hostId);
			List<Item> items = itemRepository.findByHost(hostId);
			for (Item item : items) {
				if (trigger.getExpression().contains(item.getKey_())) {
					save(trigger);
				}
			}
		}
		return triggerRepo.findByHostId(hostId);
	}

	@Override
	public List<Trigger> findAllByItem(String itemId, String hostId) throws Exception {
		return triggerRepo.findByItemId(itemId, hostId);
	}

	@Override
	public List<Alarms> saveTrigger(Trigger trigger) throws Exception {
		Trigger triggers = save(trigger);
		return getAllAlarms(triggers.getHostId());
	}
	
	@Override
	public List<Alarms> updateTrigger(Trigger trigger) throws Exception {
		Trigger triggers = update(trigger);
		return getAllAlarms(triggers.getHostId());
	}
	
	@Override
	public List<Alarms> deleteTriggers(Trigger trigger, Action action) throws Exception {
		delete(trigger);
		List<String> triggerStatus = new ArrayList<String>();
		List<Trigger> triggers = findAll();
		for (Trigger triggerS : triggers) {
			triggerStatus.add(triggerS.getTriggerid());
		}
		historyService.syncEventTrigger(triggerStatus);
		actionService.delete(action);
		actionService.removeAllByTriggers(triggerStatus);
		return getAllAlarms(trigger.getHostId());
	}

	@Override
	public void syncTriggerFromZabbixServer(List<String> hostIds) throws Exception {
		List<Trigger> triggers = triggerRepo.findTriggerByNonHost(hostIds);
		triggerRepo.delete(triggers);
	}
}
