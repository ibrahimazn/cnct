package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.Action;
import az.ldap.zabbix.repository.ActionRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.action.ActionCUDResponse;
import az.zabbix.connector.action.ActionCUDResult;
import az.zabbix.connector.action.ActionCreateRequest;
import az.zabbix.connector.action.ActionDeleteRequest;
import az.zabbix.connector.action.ActionListRequest;
import az.zabbix.connector.action.ActionListResponse;
import az.zabbix.connector.action.ActionParams;
import az.zabbix.connector.action.ActionRequest;
import az.zabbix.connector.action.ActionResponse;
import az.zabbix.connector.action.ActionUpdateRequest;

/** Action service implementation class. */
@Service
public class ActionServiceImpl implements ActionService {

	@Value(value = "${zabbix.proxy}")
	private String actionUrl;

	/** Action repository reference. */
	@Autowired
	private ActionRepository actionRepo;

	@Autowired
	private RestTemplateCall<ActionCreateRequest, ActionCUDResult> actionCreateReq;

	@Autowired
	private RestTemplateCall<ActionUpdateRequest, ActionCUDResult> actionUpdateReq;

	@Autowired
	private RestTemplateCall<ActionDeleteRequest, ActionCUDResult> actionDeleteReq;

	@Autowired
	private RestTemplateCall<ActionListRequest, ActionListResponse> actionListReq;

	@Override
	public Action save(Action action) throws Exception {
		ActionCUDResult actionResponse = actionCreateReq.restCall(actionUrl, getCreateActionRequest(GenericConstants.AUTH, action),
				ActionCUDResult.class);
		if (actionResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, actionResponse.getError().get("data"));
		}
		ActionCUDResponse actionRes = actionResponse.getResult();
		action.setActionid(actionRes.getActionids().get(0));
		return actionRepo.save(action);
	}

	@Override
	public Action update(Action action) throws Exception {
		ActionCUDResult actionResponse = actionUpdateReq.restCall(actionUrl, getUpdateActionRequest(GenericConstants.AUTH, action),
				ActionCUDResult.class);
		if (actionResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, actionResponse.getError().get("data"));
		}
		ActionCUDResponse actionRes = actionResponse.getResult();
		action.setActionid(actionRes.getActionids().get(0));
		return actionRepo.save(action);
	}

	@Override
	public void delete(Action action) throws Exception {
		if (action != null) {
			ActionCUDResult actionResponse = actionDeleteReq.restCall(actionUrl,
					getDeleteActionRequest(GenericConstants.AUTH, action.getActionid()), ActionCUDResult.class);
			if (actionResponse.getError() != null) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
						actionResponse.getError().get("data"));
			}
			ActionCUDResponse actionRes = actionResponse.getResult();
			action.setActionid(actionRes.getActionids().get(0));
			actionRepo.delete(action);
		}
	}

	@Override
	public void delete(String id) throws Exception {
		ActionCUDResult actionResponse = actionDeleteReq.restCall(actionUrl, getDeleteActionRequest(GenericConstants.AUTH, id),
				ActionCUDResult.class);
		if (actionResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, actionResponse.getError().get("data"));
		}
		ActionCUDResponse actionRes = actionResponse.getResult();
		Action action = find(actionRes.getActionids().get(0));
		action.setActionid(actionRes.getActionids().get(0));
		actionRepo.delete(action.getId());
	}

	@Override
	public Action find(String id) throws Exception {
		Action action = actionRepo.findByActionId(id);
		return action;
	}

	@Override
	public List<Action> findAll() throws Exception {
		return actionRepo.findAll();
	}

	@Override
	public Page<Action> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return actionRepo.findAll(pagingAndSorting.toPageRequest());
	}

	public Action getAction(String actionId) throws Exception {
		ActionListResponse actionsResponse = actionListReq.restCall(actionUrl, getActionRequest(GenericConstants.AUTH, actionId),
				ActionListResponse.class);
		if (actionsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, actionsResponse.getError().get("data"));
		}
		List<ActionResponse> actionRes = actionsResponse.getResult();
		if (actionRes.size() > 0) {
			if (find(actionRes.get(0).getActionid()) == null) {
				Action action = actionEntityMapperUtil(actionRes.get(0));
				return action;
			}
			return find(actionRes.get(0).getActionid());
		}
		return null;
	}

	private ActionCreateRequest getCreateActionRequest(String authCode, Action action) throws Exception {
		ActionCreateRequest request = new ActionCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("action.create");
		ActionParams params = new ActionParams();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String dtoAsString = mapper.writeValueAsString(action);
		Gson gson = new GsonBuilder().create();
		JsonParser parser = new JsonParser();
		JsonObject resoponseJson = parser.parse(dtoAsString).getAsJsonObject();
		String resoponseForGson = resoponseJson.toString();
		params = gson.fromJson(resoponseForGson, ActionParams.class);
		request.setParams(params);
		return request;
	}

	private ActionDeleteRequest getDeleteActionRequest(String authCode, String actionId) {
		ActionDeleteRequest request = new ActionDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("action.delete");
		List<String> actions = new ArrayList<String>();
		actions.add(actionId);
		request.setParams(actions);
		return request;
	}

	private ActionListRequest getAllHostRequest(String authCode) {
		ActionListRequest request = new ActionListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("action.get");
		ActionRequest action = new ActionRequest();
		action.setOutput("extend");
		request.setParams(action);
		return request;
	}

	private ActionListRequest getActionRequest(String authCode, String hostId) {
		ActionListRequest request = new ActionListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("action.get");
		ActionRequest action = new ActionRequest();
		request.setParams(action);
		return request;
	}

	private ActionUpdateRequest getUpdateActionRequest(String authCode, Action action) throws JsonProcessingException {
		ActionUpdateRequest request = new ActionUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("action.update");
		ActionParams params = new ActionParams();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String dtoAsString = mapper.writeValueAsString(action);
		Gson gson = new GsonBuilder().create();
		JsonParser parser = new JsonParser();
		JsonObject resoponseJson = parser.parse(dtoAsString).getAsJsonObject();
		String resoponseForGson = resoponseJson.toString();
		params = gson.fromJson(resoponseForGson, ActionParams.class);
		request.setParams(params);
		return request;
	}

	public Action actionEntityMapperUtil(ActionResponse actionResponse) throws Exception {
		Action action = null;
		if (actionResponse != null) {
			action = new Action();
		}
		return action;
	}

	public Map<String, Action> convert(List<Action> actionList) {
		Map<String, Action> actionMap = new HashMap<String, Action>();
		for (Action action : actionList) {
			actionMap.put(action.getActionid(), action);
		}
		return actionMap;
	}

	@Override
	public List<Action> findAllFromZabbixServer() throws Exception {
		ActionListResponse actionsResponse = actionListReq.restCall(actionUrl, getAllHostRequest(GenericConstants.AUTH), ActionListResponse.class);
		if (actionsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, actionsResponse.getError().get("data"));
		}
		List<ActionResponse> actionRes = actionsResponse.getResult();
		List<Action> actions = new ArrayList<Action>();
		for (ActionResponse action : actionRes) {
			actions.add(actionEntityMapperUtil(action));
		}
		HashMap<String, Action> actionMap = (HashMap<String, Action>) convert(actions);
		List<Action> apActionList = actionRepo.findAll();
		for (Action action : apActionList) {
			if (actionMap.containsKey(action.getActionid())) {
				Action csAction = actionMap.get(action.getActionid());
				actionRepo.save(action);
				actionMap.remove(action.getActionid());
			} else {
				actionRepo.delete(action);
			}

		}
		for (String key : actionMap.keySet()) {
			actionRepo.save(actionMap.get(key));
		}
		return findAll();
	}

	@Override
	public Action updateHost(Action action) {
		return actionRepo.save(action);
	}

	@Override
	public void deleteAll(List<Action> actions) throws Exception {
		actionRepo.delete(actions);
	}
	
	@Override
	public Action deleteAction(Action action) throws Exception {
		ActionCUDResult actionResponse = actionDeleteReq.restCall(actionUrl, getDeleteActionRequest(GenericConstants.AUTH, action.getActionid()),
				ActionCUDResult.class);
		if (actionResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, actionResponse.getError().get("data"));
		}
		ActionCUDResponse actionRes = actionResponse.getResult();
		action = find(actionRes.getActionids().get(0));
		action.setActionid(actionRes.getActionids().get(0));
		actionRepo.delete(action);
		return action;
	}

	@Override
	public Action findByTriggerId(String triggerId) throws Exception {
		return actionRepo.findByTriggerId(triggerId);
	}

	@Override
	public void removeAllByTriggers(List<String> triggers) throws Exception {
		List<Action> actions = actionRepo.findByAllNonTriggerId(triggers);
		for (Action action : actions) {
			try {
				delete(action);
			} catch (Exception ec) {
				actionRepo.delete(action);
			}
		}
	}
}
