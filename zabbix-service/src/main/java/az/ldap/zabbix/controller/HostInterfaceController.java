package az.ldap.zabbix.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.zabbix.entity.HostInterface;
import az.ldap.zabbix.service.HostInterfaceService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/hostinterface")
public class HostInterfaceController extends CRUDController<HostInterface> implements ApiController {

	@Autowired
	private HostInterfaceService hostGroupService;
	
	@Override
	public HostInterface create(HostInterface hostInterface) throws Exception {
		return hostGroupService.save(hostInterface);
	}

	@Override
	public HostInterface update(HostInterface hostInterface, String id) throws Exception {
		return hostGroupService.update(hostInterface);
	}

	@Override
	public void delete(String id) throws Exception {
		hostGroupService.delete(id);
	}

	@Override
	public HostInterface read(String id) throws Exception {
		return hostGroupService.find(id);
	}

	@Override
	public List<HostInterface> list(String sortBy, String range, Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, HostInterface.class);
		Page<HostInterface> pageResponse = hostGroupService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}

}
