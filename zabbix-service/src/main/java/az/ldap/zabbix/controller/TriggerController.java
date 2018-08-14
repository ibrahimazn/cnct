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
import az.ldap.zabbix.entity.Action;
import az.ldap.zabbix.entity.Alarms;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.Trigger;
import az.ldap.zabbix.service.ActionService;
import az.ldap.zabbix.service.PagingAndSorting;
import az.ldap.zabbix.service.TriggerService;

@RestController
@RequestMapping("/zabbix/api/trigger")
public class TriggerController extends CRUDController<Trigger> implements ApiController {

	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private ActionService actionService;
	
	@Override
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public Trigger create(@RequestBody Trigger trigger) throws Exception {
		return triggerService.save(trigger);
	}

	@Override
	public Trigger update(@RequestBody Trigger trigger, @PathVariable(PATH_ID) String id) throws Exception {
		return triggerService.update(trigger);
	}

	@Override
	public void delete(@PathVariable(PATH_ID) String id) throws Exception {
		triggerService.delete(id);
	}

	@Override
	public Trigger read(@PathVariable(PATH_ID) String id) throws Exception {
		return triggerService.find(id);
	}

	@Override
	public List<Trigger> list(@RequestParam(required = false) String sortBy, @RequestHeader("Range") String range, @RequestParam(required = false) Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Trigger.class);
		Page<Trigger> pageResponse = triggerService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Trigger> getItems(@RequestParam String ostype, @RequestParam String id, @RequestParam String name) throws Exception {
        return triggerService.updateByHost(ostype, name, id);
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public List<Alarms> createTrigger(@RequestBody Trigger trigger) throws Exception {
		return triggerService.saveTrigger(trigger);
	}
	
	@RequestMapping(value = "/alarms", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Alarms> getAllItems(@RequestParam String hostId) throws Exception {
        return triggerService.getAllAlarms(hostId);
    }
	@RequestMapping(value = "/update", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Alarms> updateTriggers(@RequestParam String triggerId, @RequestParam String thresold) throws Exception {
		Trigger persistTrigger = triggerService.find(triggerId);
		String[] expressions = persistTrigger.getExpression().split("\\}");
		String expression = expressions[0]+"}";
		String symbol = expressions[1].substring(0, 1);
		String expressionss = expression + symbol + thresold;
		persistTrigger.setExpression(expressionss);
		persistTrigger.setThresold(thresold);
		return triggerService.updateTrigger(persistTrigger);
    }
	
	@RequestMapping(value = "/updateTrigger",method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.ACCEPTED)
	public List<Alarms> updateTrigger(@RequestBody Trigger trigger) throws Exception {
		String[] expressions = trigger.getExpression().split("\\}");
		String expression = expressions[0]+"}";
		String symbol = expressions[1].substring(0, 1);
		String expressionss = expression + symbol + trigger.getThresold();
		trigger.setExpression(expressionss);
		trigger.setThresold(trigger.getThresold());
		return triggerService.updateTrigger(trigger);
	}
		
	@RequestMapping(value = "/remove", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Alarms> deleteTriggers(@RequestParam String triggerId, @RequestParam String actionId) throws Exception {
		Trigger persistTrigger = triggerService.find(triggerId);
		Action action = actionService.find(actionId);
		return triggerService.deleteTriggers(persistTrigger, action);
    }
}
