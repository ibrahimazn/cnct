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
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.service.HostService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/host")
public class HostController extends CRUDController<Host> implements ApiController {

	@Autowired
	private HostService hostService;

	@Override
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public Host create(@RequestBody Host host) throws Exception {
		return hostService.save(host);
	}

	@Override
	@RequestMapping(value = "/{id}",method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public Host update(@RequestBody Host host, @PathVariable(PATH_ID) String id) throws Exception {
		return hostService.update(host);
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Host updateHost(@RequestBody Host host) throws Exception {
		return hostService.updateHost(host);
	}
	
	@RequestMapping(value = "/update/state/{id}",method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Host updateHostState(@RequestBody Host host, @PathVariable(PATH_ID) String id) throws Exception {
		return hostService.updateStateHost(host);
	}

	@Override
	public void delete(@PathVariable(PATH_ID) String id) throws Exception {
		hostService.delete(id);
	}

	@Override
	public Host read(@PathVariable(PATH_ID) String id) throws Exception {
		return hostService.find(id);
	}

	@Override
	public List<Host> list(@RequestParam String sortBy, @RequestHeader String range, @RequestParam Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Host.class);
		Page<Host> pageResponse = hostService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Host> getHosts(@RequestParam String id, @RequestParam String type) throws Exception {
        return hostService.findAllByUser(Integer.valueOf(id), type);
    }
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Host deleteHost(@PathVariable(PATH_ID) String id) throws Exception {
        return hostService.deleteHost(hostService.find(id));
    }
	
	@RequestMapping(value = "/removebyuuid/{uuid}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Host deleteHostByUuid(@PathVariable("uuid") String uuid) throws Exception {
		Host host = hostService.findByHostUuid(uuid);
		if (host != null) {
			return hostService.deleteHost(host);
		}
		return host;
    }

}
