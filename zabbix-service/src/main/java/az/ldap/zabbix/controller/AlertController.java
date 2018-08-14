package az.ldap.zabbix.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import az.ldap.zabbix.entity.Alert;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.service.PagingAndSorting;
import az.ldap.zabbix.service.AlertService;

@RestController
@RequestMapping("/zabbix/api/alert")
public class AlertController extends CRUDController<Alert> implements ApiController {

	@Autowired
	private AlertService alertService;
	
	@Override
	public Alert create(@RequestBody Alert alert) throws Exception {
		return alertService.save(alert);
	}

	@Override
	public Alert update(@RequestBody Alert alert, @PathVariable(PATH_ID) String id) throws Exception {
		return alertService.update(alert);
	}

	@Override
	public void delete(@PathVariable(PATH_ID) String id) throws Exception {
		alertService.delete(id);
	}

	@Override
	public Alert read(@PathVariable(PATH_ID) String id) throws Exception {
		return alertService.find(id);
	}

	@Override
	public List<Alert> list(@RequestParam(required = false) String sortBy, @RequestHeader("Range") String range, @RequestParam(required = false) Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Alert.class);
		Page<Alert> pageResponse = alertService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Alert> getHosts() throws Exception {
		alertService.findAllFromZabbixServer();
        return alertService.findAll();
    }
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Alert> getAlerts(@PathVariable(PATH_ID) String id) throws Exception {
		alertService.findAllFromZabbixServer();
        return alertService.findAllByUser(id);
    }
}
