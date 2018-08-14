package az.ldap.zabbix.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.VMDashboards;
import az.ldap.sync.util.ZabbixDashBoard;
import az.ldap.zabbix.entity.Events;
import az.ldap.zabbix.service.HistoryService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/history")
public class HistoryController extends CRUDController<VMDashboards> implements ApiController {

	@Autowired
	private HistoryService historyService;

	@Override
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public VMDashboards create(@RequestBody VMDashboards vm) throws Exception {
		return historyService.save(vm);
	}

	@Override
	@RequestMapping(value = "/{id}",method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public VMDashboards update(@RequestBody VMDashboards vm, @PathVariable(PATH_ID) String id) throws Exception {
		return historyService.update(vm);
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.ACCEPTED)
	public VMDashboards updateVM(@RequestBody VMDashboards vm) throws Exception {
		return historyService.update(vm);
	}

	@Override
	public void delete(@PathVariable(PATH_ID) String id) throws Exception {
		historyService.delete(id);
	}

	@Override
	public VMDashboards read(@PathVariable(PATH_ID) String id) throws Exception {
		return historyService.find(id);
	}

	@Override
	public List<VMDashboards> list(@RequestParam String sortBy, @RequestHeader String range, @RequestParam Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, VMDashboards.class);
		Page<VMDashboards> pageResponse = historyService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}
	
	@RequestMapping(value = "/vm/{id}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public VMDashboards getVmDashBoard(@PathVariable(PATH_ID) String id) throws Exception {
        return historyService.getVmDashboard(id);
    }
	
	@RequestMapping(value = "/alarms", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Events> getAllAlarms(@RequestParam String id, @RequestParam String type) throws Exception {
        return historyService.getAllAlerts(Integer.valueOf(id), type);
    }
	
	@RequestMapping(value = "/alarms/filter", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Events> getAllAlarmsByFilter(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd-HH:mm:ss") Date from, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd-HH:mm:ss") Date to, @RequestParam String hostId, @RequestParam String status, @RequestParam String severity, @RequestParam String id, @RequestParam String type) throws Exception {
        Integer state = 3;
		if (status != null && !status.equals("none") && !status.equals("")) {
			state = Integer.valueOf(status);
		}
		return historyService.getByFilterAlerts(from, to, hostId, state, severity, Integer.valueOf(id), type);
    }
	
	@RequestMapping(value = "/recent/alarms", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Events> getAllRecentByFilter(@RequestParam String hostId, @RequestParam String status, @RequestParam String severity, @RequestParam String id, @RequestParam String type) throws Exception {
		return historyService.getRecentByFilterAlerts(hostId, status, severity, Integer.valueOf(id), type);
    }
	
	@RequestMapping(value = "/agent/{id}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ZabbixDashBoard getDashboard(@PathVariable(PATH_ID) String id, @RequestParam String ids, @RequestParam String type, @RequestParam String hostIds) throws Exception {
        return historyService.findByHost(ids, type, hostIds);
    }

}
