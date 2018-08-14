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
import az.ldap.zabbix.entity.Graph;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.service.GraphService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/graph")
public class GraphController extends CRUDController<Graph> implements ApiController {

	@Autowired
	private GraphService graphService;
	
	@Override
	public Graph create(@RequestBody Graph graph) throws Exception {
		return graphService.save(graph);
	}

	@Override
	public Graph update(@RequestBody Graph graph, @PathVariable(PATH_ID) String id) throws Exception {
		return graphService.save(graph);
	}

	@Override
	public void delete(@PathVariable(PATH_ID) String id) throws Exception {
		graphService.delete(id);
	}

	@Override
	public Graph read(@PathVariable(PATH_ID) String id) throws Exception {
		return graphService.find(id);
	}

	@Override
	public List<Graph> list(@RequestParam String sortBy, @RequestHeader(value = "Range") String range, @RequestParam Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Graph.class);
		Page<Graph> pageResponse = graphService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}
	
	@RequestMapping(value = "/host", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Graph> getItemsByHost(@RequestParam String host) throws Exception {
        return graphService.findAllByHost(host);
    }
	
	@RequestMapping(value = "/item", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Graph> getGraphByItem(@RequestParam String itemId, @RequestParam String hostId) throws Exception {
        return graphService.findAllByItem(itemId, hostId);
    }
	
	@RequestMapping(value = "/host/favorite", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Graph> getGraphByFavoriteAndHost(@RequestParam String hostId) throws Exception {
        return graphService.findAllByHostAndFavorite(hostId, true);
    }
	
	@RequestMapping(value = "/favorite/all", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Graph> getGraphByFavorite() throws Exception {
        return graphService.findAllByFavorite(true);
    }
	
	@RequestMapping(value = "/favorite", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Graph> setFavorite(@RequestParam String graphId, @RequestParam Boolean favorite) throws Exception {
        return graphService.setFavorite(graphId, favorite);
    }
}
