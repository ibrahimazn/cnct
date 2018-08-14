package az.ldap.zabbix.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.zabbix.entity.HostTemplate;
import az.ldap.zabbix.service.HostTemplateService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/hosttemplate")
public class HostTemplateController extends CRUDController<HostTemplate> implements ApiController {

	@Autowired
	private HostTemplateService hostTemplateService;
	
	@Override
	public HostTemplate create(HostTemplate hostTemplate) throws Exception {
		return hostTemplateService.save(hostTemplate);
	}

	@Override
	public HostTemplate update(HostTemplate hostTemplate, String id) throws Exception {
		return hostTemplateService.update(hostTemplate);
	}

	@Override
	public void delete(String id) throws Exception {
		hostTemplateService.delete(id);
	}

	@Override
	public HostTemplate read(String id) throws Exception {
		return hostTemplateService.find(id);
	}

	@Override
	public List<HostTemplate> list(String sortBy, String range, Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, HostTemplate.class);
		Page<HostTemplate> pageResponse = hostTemplateService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}

}
