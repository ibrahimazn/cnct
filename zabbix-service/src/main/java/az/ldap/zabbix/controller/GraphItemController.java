package az.ldap.zabbix.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.zabbix.entity.GraphItem;
import az.ldap.zabbix.service.GraphItemService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/graphitem")
public class GraphItemController extends CRUDController<GraphItem> implements ApiController {

	/** Item service references.*/
	@Autowired
	private GraphItemService graphItemService;
	
	@Override
	public GraphItem create(GraphItem graphItem) throws Exception {
		return graphItemService.save(graphItem);
	}

	@Override
	public GraphItem update(GraphItem graphItem, String id) throws Exception {
		return graphItemService.update(graphItem);
	}

	@Override
	public void delete(String id) throws Exception {
		graphItemService.delete(id);
	}

	@Override
	public GraphItem read(String id) throws Exception {
		return graphItemService.find(id);
	}

	@Override
	public List<GraphItem> list(String sortBy, String range, Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, GraphItem.class);
		Page<GraphItem> pageResponse = graphItemService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}

}
