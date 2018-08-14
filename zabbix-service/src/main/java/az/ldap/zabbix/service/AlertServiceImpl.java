package az.ldap.zabbix.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.Alert;
import az.ldap.zabbix.repository.AlertRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.alert.AlertListRequest;
import az.zabbix.connector.alert.AlertListResponse;
import az.zabbix.connector.alert.AlertRequest;
import az.zabbix.connector.alert.AlertResponse;
import az.zabbix.connector.alert.FilterRequest;

/** Alert service implementation class. */
@Service
public class AlertServiceImpl implements AlertService {

	/** Host repository reference. */
	@Autowired
	private AlertRepository alertRepo;

	@Value(value = "${zabbix.proxy}")
	private String alertUrl;

	/** alert lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<AlertListRequest, AlertListResponse> alertListReq;

	@Override
	public Alert find(String id) throws Exception {
		Alert alert = alertRepo.findByAlert(id);
		return alert;
	}

	@Override
	public List<Alert> findAll() throws Exception {
		return alertRepo.findAll();
	}

	@Override
	public Page<Alert> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return alertRepo.findAll(pagingAndSorting.toPageRequest());
	}

	public Alert getAlert(String alertId) throws Exception {
		AlertListResponse alertResponse = alertListReq.restCall(alertUrl, getAlertRequest(GenericConstants.AUTH, alertId),
				AlertListResponse.class);
		if (alertResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, alertResponse.getError().get("data"));
		}
		List<AlertResponse> alertRes = alertResponse.getResult();
		if (alertRes.size() > 0) {
			if (find(alertRes.get(0).getAlertid()) == null) {
				Alert alert = AlertEntityMapperUtil(alertRes.get(0));
				return alert;
			}
			return find(alertRes.get(0).getAlertid());
		}
		return null;
	}

	private AlertListRequest getAllAlertRequest(String authCode) {
		AlertListRequest request = new AlertListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("alert.get");
		AlertRequest alert = new AlertRequest();
		alert.setOutput("extend");
		alert.setSortfield("clock");
		alert.setSortorder("DESC");
		request.setParams(alert);
		return request;
	}

	private AlertListRequest getAllAlertRequestByHost(String authCode, String hostId) {
		AlertListRequest request = new AlertListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("alert.get");
		AlertRequest alert = new AlertRequest();
		List<String> hosts = new ArrayList<String>(Arrays.asList(hostId.split(",")));
		alert.setHostids(hosts);
		alert.setOutput("extend");
		alert.setSortfield("clock");
		alert.setSortorder("DESC");
		request.setParams(alert);
		return request;
	}


	private AlertListRequest getAlertRequest(String authCode, String alertId) {
		AlertListRequest request = new AlertListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("alert.get");
		AlertRequest alert = new AlertRequest();
		alert.setOutput("extend");
		alert.setSortfield("clock");
		alert.setSortorder("DESC");
		FilterRequest filter = new FilterRequest();
		filter.setAlertid(alertId);
		alert.setSearch(filter);
		request.setParams(alert);
		return request;
	}

	public Alert AlertEntityMapperUtil(AlertResponse alertResponse) throws Exception {
		Alert alert = null;
		if (alertResponse != null) {
			alert = new Alert();
			alert.setActionid(alertResponse.getActionid());
			alert.setAlertid(alertResponse.getAlertid());
			alert.setAlerttype(alertResponse.getAlerttype());
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(Long.parseLong(alertResponse.getClock())*1000);
			alert.setClock(date);
			alert.setEventid(alertResponse.getEventid());
			alert.setMediatypeid(alertResponse.getMediatypeid());
			alert.setMessage(alertResponse.getMessage());
			alert.setSendto(alertResponse.getSendto());
			alert.setSubject(alertResponse.getSubject());
			alert.setStatus(alertResponse.getStatus());
			alert.setUserid(alertResponse.getUserid());
		}
		return alert;
	}

	public Map<String, Alert> convert(List<Alert> graphList) {
		Map<String, Alert> graphMap = new HashMap<String, Alert>();
		for (Alert alert : graphList) {
			graphMap.put(alert.getAlertid(), alert);
		}
		return graphMap;
	}

	@Override
	public List<Alert> findAllFromZabbixServer() throws Exception {
		AlertListResponse alertResponse = alertListReq.restCall(alertUrl, getAllAlertRequest(GenericConstants.AUTH),
				AlertListResponse.class);
		if (alertResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, alertResponse.getError().get("data"));
		}
		List<AlertResponse> alertRes = alertResponse.getResult();
		List<Alert> alerts = new ArrayList<Alert>();
		for (AlertResponse alert : alertRes) {
			alerts.add(AlertEntityMapperUtil(alert));
		}
		HashMap<String, Alert> graphMap = (HashMap<String, Alert>) convert(alerts);
		List<Alert> appAlertList = alertRepo.findAll();
		for (Alert alert : appAlertList) {
			if (graphMap.containsKey(alert.getAlertid())) {
				Alert csAlert = graphMap.get(alert.getAlertid());
				alert.setActionid(csAlert.getActionid());
				alert.setAlertid(csAlert.getAlertid());
				alert.setAlerttype(csAlert.getAlerttype());
				alert.setClock(csAlert.getClock());
				alert.setEventid(csAlert.getEventid());
				alert.setMediatypeid(csAlert.getMediatypeid());
				alert.setMessage(csAlert.getMessage());
				alert.setSendto(csAlert.getSendto());
				alert.setSubject(csAlert.getSubject());
				alert.setStatus(csAlert.getStatus());
				alert.setUserid(csAlert.getUserid());
				alertRepo.save(alert);
				graphMap.remove(alert.getAlertid());
			} else {
				alertRepo.delete(alert);
			}

		}
		for (String key : graphMap.keySet()) {
			alertRepo.save(graphMap.get(key));
		}
		return findAll();
	}

	@Override
	public Alert save(Alert t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Alert update(Alert t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Alert t) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteAllItem(List<Alert> items) throws Exception {
		alertRepo.delete(items);
	}

	@Override
	public void deleteItem(Alert items) throws Exception {
		alertRepo.delete(items);
	}

	@Override
	public List<Alert> findAllByHost(String hostId) throws Exception {
		AlertListResponse alertResponse = alertListReq.restCall(alertUrl, getAllAlertRequestByHost(GenericConstants.AUTH,hostId),
				AlertListResponse.class);
		if (alertResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, alertResponse.getError().get("data"));
		}
		List<AlertResponse> alertRes = alertResponse.getResult();
		List<Alert> alerts = new ArrayList<Alert>();
		for (AlertResponse alert : alertRes) {
			alerts.add(AlertEntityMapperUtil(alert));
		}
		HashMap<String, Alert> graphMap = (HashMap<String, Alert>) convert(alerts);
		List<Alert> appAlertList = alertRepo.findAll();
		for (Alert alert : appAlertList) {
			if (graphMap.containsKey(alert.getAlertid())) {
				Alert csAlert = graphMap.get(alert.getAlertid());
				alert.setActionid(csAlert.getActionid());
				alert.setAlertid(csAlert.getAlertid());
				alert.setAlerttype(csAlert.getAlerttype());
				alert.setClock(csAlert.getClock());
				alert.setEventid(csAlert.getEventid());
				alert.setMediatypeid(csAlert.getMediatypeid());
				alert.setMessage(csAlert.getMessage());
				alert.setSendto(csAlert.getSendto());
				alert.setSubject(csAlert.getSubject());
				alert.setStatus(csAlert.getStatus());
				alert.setUserid(csAlert.getUserid());
				alertRepo.save(alert);
				graphMap.remove(alert.getAlertid());
			} else {
				alertRepo.delete(alert);
			}

		}
		for (String key : graphMap.keySet()) {
			alertRepo.save(graphMap.get(key));
		}
		return alertRepo.findAllByHost(hostId);
	}

	@Override
	public List<Alert> findAllByUser(String userId) throws Exception {
		return alertRepo.findByUserId(userId);
	}
}
