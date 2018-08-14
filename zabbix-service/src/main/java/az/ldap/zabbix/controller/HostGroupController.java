package az.ldap.zabbix.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.zabbix.entity.HostGroup;
import az.ldap.zabbix.service.HostGroupService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/hostgroup")
public class HostGroupController extends CRUDController<HostGroup> implements ApiController {

	@Autowired
	private HostGroupService hostGroupService;

	@Override
	public HostGroup create(HostGroup hostGroup) throws Exception {
		return hostGroupService.save(hostGroup);
	}

	@Override
	public HostGroup update(HostGroup hostGroup, String id) throws Exception {
		return hostGroupService.update(hostGroup);
	}

	@Override
	public void delete(String id) throws Exception {
		hostGroupService.delete(id);
	}

	@Override
	public HostGroup read(String id) throws Exception {
		return hostGroupService.find(id);
	}

	@Override
	public List<HostGroup> list(String sortBy, String range, Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, HostGroup.class);
		Page<HostGroup> pageResponse = hostGroupService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}

}
