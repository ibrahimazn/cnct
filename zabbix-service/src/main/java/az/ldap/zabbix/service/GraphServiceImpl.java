package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.Graph;
import az.ldap.zabbix.entity.GraphItem;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.Trigger;
import az.ldap.zabbix.repository.GraphRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.graph.GraphCUDResponse;
import az.zabbix.connector.graph.GraphCUDResult;
import az.zabbix.connector.graph.GraphCreateRequest;
import az.zabbix.connector.graph.GraphDeleteRequest;
import az.zabbix.connector.graph.GraphItemParams;
import az.zabbix.connector.graph.GraphListRequest;
import az.zabbix.connector.graph.GraphListResponse;
import az.zabbix.connector.graph.GraphParams;
import az.zabbix.connector.graph.GraphProtoCreateRequest;
import az.zabbix.connector.graph.GraphRequest;
import az.zabbix.connector.graph.GraphResponse;
import az.zabbix.connector.graph.GraphUpdateRequest;
import az.zabbix.connector.graph.FilterRequest;

/** Graph service implementation class. */
@Service
public class GraphServiceImpl implements GraphService {

	/** Host repository reference. */
	@Autowired
	private GraphRepository graphRepo;

	@Autowired
	private GraphItemService graphItemService;
	
	@Autowired
	private HostService hostService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private HistoryService historyService;

	@Value(value = "${zabbix.proxy}")
	private String graphUrl;

	/** Graph creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<GraphCreateRequest, GraphCUDResult> graphCreateReq;
	
	/** Graph creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<GraphProtoCreateRequest, GraphCUDResult> graphProtoCreateReq;

	/** Graph update Connection processor reference. */
	@Autowired
	private RestTemplateCall<GraphUpdateRequest, GraphCUDResult> graphUpdateReq;

	/** Graph delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<GraphDeleteRequest, GraphCUDResult> graphDeleteReq;

	/** Graph lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<GraphListRequest, GraphListResponse> graphListReq;

	public Graph save(Graph graph) throws Exception {
		if(graph.getName().contains("#")) {
			GraphCUDResult graphResponse = graphProtoCreateReq.restCall(graphUrl, getCreateProtoGraphRequest(GenericConstants.AUTH, graph),
					GraphCUDResult.class);
			if (graphResponse.getError() != null) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, graphResponse.getError().get("data"));
			}
			List<Item> items = itemService.findAllByHost(graph.getHostId());
			String itemids = "";
			int i  = 0;
			for(Item item: items){
				if ((items.size()-1) == i) {
					itemids += item.getItemId();
				} else {
					itemids += item.getItemId() + ",";
				}
				i++;
			}
			graphItemService.findAllFromZabbixServer(itemids);
			findAllFromZabbixServer(graph.getHostId());
			return graph;
		}
		GraphCUDResult graphResponse = graphCreateReq.restCall(graphUrl, getCreateGraphRequest(GenericConstants.AUTH, graph),
				GraphCUDResult.class);
		if (graphResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, graphResponse.getError().get("data"));
		}
		GraphCUDResponse graphRes = graphResponse.getResult();
		graph.setGraphId(graphRes.getGraphids().get(0));
		List<Item> items = itemService.findAllByHost(graph.getHostId());
		String itemids = "";
		int i  = 0;
		for(Item item: items){
			if ((items.size()-1) == i) {
				itemids += item.getItemId();
			} else {
				itemids += item.getItemId() + ",";
			}
			i++;
		}
		graphItemService.findAllFromZabbixServer(itemids);
		List<GraphItem> gitems = new ArrayList<GraphItem>();
		for (GraphItem item : graph.getGraphItemsList()) {
			GraphItem gitem = new GraphItem();
			gitem = graphItemService.findByItem(item.getItemId());
			gitems.add(gitem);
		}
		graph.setGraphItemsList(gitems);
		findAllFromZabbixServer(graph.getHostId());
		Graph graphs = find(graphRes.getGraphids().get(0));
		if (graphs != null) {
			return graphs;
		}
		return graphRepo.save(graph);
	}

	@Override
	public Graph update(Graph graph) throws Exception {
		GraphCUDResult graphResponse = graphUpdateReq.restCall(graphUrl, getUpdateGraphRequest(GenericConstants.AUTH, graph),
				GraphCUDResult.class);
		if (graphResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, graphResponse.getError().get("data"));
		}
		GraphCUDResponse graphRes = graphResponse.getResult();
		graph.setGraphId(graphRes.getGraphids().get(0));
		return graphRepo.save(graph);
	}

	@Override
	public void delete(Graph graph) throws Exception {
		GraphCUDResult graphResponse = graphDeleteReq.restCall(graphUrl,
				getDeleteGraphRequest(GenericConstants.AUTH, graph.getGraphId()), GraphCUDResult.class);
		if (graphResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, graphResponse.getError().get("data"));
		}
		GraphCUDResponse graphRes = graphResponse.getResult();
		graph.setGraphId(graphRes.getGraphids().get(0));
		graphRepo.delete(graph);
	}

	@Override
	public void delete(String id) throws Exception {
		GraphCUDResult graphResponse = graphDeleteReq.restCall(graphUrl, getDeleteGraphRequest(GenericConstants.AUTH, id),
				GraphCUDResult.class);
		if (graphResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, graphResponse.getError().get("data"));
		}
		GraphCUDResponse graphRes = graphResponse.getResult();
		Graph graph = find(graphRes.getGraphids().get(0));
		graph.setGraphId(graphRes.getGraphids().get(0));
		graphRepo.delete(id);
	}

	@Override
	public Graph find(String id) throws Exception {
		Graph graph = graphRepo.findByGraphId(id);
		return graph;
	}

	@Override
	public List<Graph> findAll() throws Exception {
		return graphRepo.findAll();
	}

	@Override
	public Page<Graph> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return graphRepo.findAll(pagingAndSorting.toPageRequest());
	}

	public Graph getGraph(String graphId, String hostId) throws Exception {
		GraphListResponse graphsResponse = graphListReq.restCall(graphUrl, getGraphRequest(GenericConstants.AUTH, graphId),
				GraphListResponse.class);
		if (graphsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, graphsResponse.getError().get("data"));
		}
		List<GraphResponse> graphRes = graphsResponse.getResult();
		if (graphRes.size() > 0) {
			if (find(graphRes.get(0).getGraphid()) == null) {
				Graph graph = graphEntityMapperUtil(graphRes.get(0), hostId);
				return graph;
			}
			return find(graphRes.get(0).getGraphid());
		}
		return null;
	}

	private GraphCreateRequest getCreateGraphRequest(String authCode, Graph graph) {
		GraphCreateRequest request = new GraphCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graph.create");
		GraphParams graphParams = new GraphParams();
		graphParams.setName(graph.getName());
		List<GraphItemParams> gItems = new ArrayList<GraphItemParams>();
		for (GraphItem item : graph.getGraphItemsList()) {
			GraphItemParams items = new GraphItemParams();
			items.setColor(item.getColor());
			items.setItemid(item.getItemId());
			gItems.add(items);
		}
		graphParams.setGitems(gItems);
		graphParams.setHeight(graph.getHeight());
		graphParams.setWidth(graph.getWidth());
		request.setParams(graphParams);
		return request;
	}
	
	private GraphProtoCreateRequest getCreateProtoGraphRequest(String authCode, Graph graph) {
		GraphProtoCreateRequest request = new GraphProtoCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graphprototype.create");
		GraphParams graphParams = new GraphParams();
		graphParams.setName(graph.getName());
		List<GraphItemParams> gItems = new ArrayList<GraphItemParams>();
		for (GraphItem item : graph.getGraphItemsList()) {
			GraphItemParams items = new GraphItemParams();
			items.setColor(item.getColor());
			items.setItemid(item.getItemId());
			gItems.add(items);
		}
		graphParams.setGitems(gItems);
		graphParams.setHeight(graph.getHeight());
		graphParams.setWidth(graph.getWidth());
		request.setParams(graphParams);
		return request;
	}

	private GraphDeleteRequest getDeleteGraphRequest(String authCode, String graphId) {
		GraphDeleteRequest request = new GraphDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graph.delete");
		List<String> graph = new ArrayList<String>();
		graph.add(graphId);
		request.setParams(graph);
		return request;
	}

	private GraphListRequest getAllGraphRequest(String authCode, String hostId) {
		GraphListRequest request = new GraphListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graph.get");
		GraphRequest graph = new GraphRequest();
		graph.setOutput("extend");
		graph.setSortfield("graphid");
		List<String> hosts = new ArrayList<String>(Arrays.asList(hostId.split(",")));
		graph.setHostids(hosts);
		graph.setSortorder("ASC");
		request.setParams(graph);
		return request;
	}

	private GraphListRequest getGraphRequest(String authCode, String graphId) {
		GraphListRequest request = new GraphListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graph.get");
		GraphRequest graph = new GraphRequest();
		graph.setOutput("extend");
		graph.setSortfield("graphid");
		graph.setSortorder("ASC");
		FilterRequest filter = new FilterRequest();
		filter.setGraphid(graphId);
		graph.setSearch(filter);
		request.setParams(graph);
		return request;
	}

	private GraphUpdateRequest getUpdateGraphRequest(String authCode, Graph graph) {
		GraphUpdateRequest request = new GraphUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graph.update");
		GraphParams graphParams = new GraphParams();
		graphParams.setGraphid(graph.getGraphId());
		graphParams.setName(graph.getName());
		request.setParams(graphParams);
		return request;
	}

	public Graph graphEntityMapperUtil(GraphResponse graphResponse, String hostId) throws Exception {
		Graph graph = null;
		if (graphResponse != null) {
			graph = new Graph();
			graph.setName(graphResponse.getName());
			graph.setGraphId(graphResponse.getGraphid());
			graph.setHeight(Integer.valueOf(graphResponse.getHeight()));
			graph.setWidth(Integer.valueOf(graphResponse.getWidth()));
			graph.setHostId(hostId);
			Host host = hostService.find(hostId);
			graph.setUserid_(host.getUserid_());
			graph.setDepartmentid_(host.getDepartmentid_());
			graph.setDomainid_(host.getDomainid_());
		}
		return graph;
	}

	public Map<String, Graph> convert(List<Graph> graphList) {
		Map<String, Graph> graphMap = new HashMap<String, Graph>();
		for (Graph graph : graphList) {
			graphMap.put(graph.getGraphId(), graph);
		}
		return graphMap;
	}

	@Override
	public List<Graph> findAllFromZabbixServer(String hostId) throws Exception {
		GraphListResponse graphsResponse = graphListReq.restCall(graphUrl, getAllGraphRequest(GenericConstants.AUTH, hostId),
				GraphListResponse.class);
		if (graphsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, graphsResponse.getError().get("data"));
		}
		List<GraphResponse> graphRes = graphsResponse.getResult();
		List<Graph> graphs = new ArrayList<Graph>();
		for (GraphResponse graph : graphRes) {
			graphs.add(graphEntityMapperUtil(graph, hostId));
		}
		HashMap<String, Graph> graphMap = (HashMap<String, Graph>) convert(graphs);
		List<Graph> appGraphList = graphRepo.findByHostId(hostId);
		for (Graph graph : appGraphList) {
			if (graphMap.containsKey(graph.getGraphId())) {
				Graph csGraph = graphMap.get(graph.getGraphId());
				graph.setName(csGraph.getName());
				graph.setHeight(csGraph.getHeight());
				graph.setWidth(csGraph.getWidth());
				graph.setGraphId(csGraph.getGraphId());
				graph.setHostId(csGraph.getHostId());
				graph.setUserid_(csGraph.getUserid_());
				graph.setDepartmentid_(csGraph.getDepartmentid_());
				graph.setDomainid_(csGraph.getDomainid_());
				List<GraphItem> items = graphItemService.findAllByGraph(csGraph.getGraphId());
				if(items.size() > 0){
					graph.setHostId(items.get(0).getHostId());
					Item item = itemService.find(items.get(0).getItemId());
					graph.setType(item.getCategory());
					graph.setHostId(item.getHostId());
					graph.setTriggerStatus(item.getTriggerStatus());
				}
				graph.setGraphItemsList(items);
				graphRepo.save(graph);
				graphMap.remove(graph.getGraphId());
			} else {
				graphRepo.delete(graph);
			}

		}
		for (String key : graphMap.keySet()) {
			Graph graph = graphMap.get(key);
			List<GraphItem> items = graphItemService.findAllByGraph(graph.getGraphId());
			if(items.size() > 0){
				graph.setHostId(items.get(0).getHostId());
			}
			graph.setGraphItemsList(items);
			graph.setHostId(hostId);
			if (items.size() > 0) {
				Item item = itemService.find(items.get(0).getItemId());
				graph.setType(item.getCategory());
				graph.setTriggerStatus(item.getTriggerStatus());
			}
			graphRepo.save(graph);
		}
		return findAll();
	}

	@Override
	public List<Graph> findAllByHost(String hostId) throws Exception {
		 List<Graph> graphList = new ArrayList<Graph>();
		 List<Graph> graphs = graphRepo.findByHostId(hostId);
		 for (Graph graph : graphs) {
			if (graph.getGraphItemsList().size() > 0 && graph.getGraphItemsList().get(0) != null) {
				List<Trigger> triggers = triggerService.findAllByItem(graph.getGraphItemsList().get(0).getItemId(),
						graph.getHostId());
				Item item = itemService.find(graph.getGraphItemsList().get(0).getItemId());
				graph.setTriggerCount(triggers.size());
				graph.setTriggerStatus(item.getTriggerStatus());
			}
			 graphList.add(graph);
		 }
		 return graphList;
	}

	@Override
	public void deleteGraph(Graph graph) throws Exception  {
		graphRepo.delete(graph);
	}

	@Override
	public void deleteAllGraph(List<Graph> graph) throws Exception  {
		graphRepo.delete(graph);
	}

	@Override
	public List<Graph> findAllByItem(String itemId, String hostId) throws Exception {
		List<Graph> graphs = new ArrayList<Graph>();
		GraphItem graphItme = graphItemService.findByItem(itemId);
		if (graphItme == null && hostId != null) {
			historyService.getVmDashboard(hostId);
			graphItme = graphItemService.findByItem(itemId);
		}
		if (graphItme != null) {
			graphs = graphRepo.findByGraph(graphItme.getGraphId());
		}
		List<Graph> graphList = new ArrayList<Graph>();
		for (Graph graph : graphs) {
			if (graph.getGraphItemsList().size() > 0  && graph.getGraphItemsList().get(0) != null ) {
				List<Trigger> triggers = triggerService.findAllByItem(graph.getGraphItemsList().get(0).getItemId(),
						graph.getHostId());
				Item item = itemService.find(graph.getGraphItemsList().get(0).getItemId());
				graph.setTriggerCount(triggers.size());
				graph.setTriggerStatus(item.getTriggerStatus());
			}
			graphList.add(graph);
		}
		return graphList;
	}

	@Override
	public List<Graph> findAllByHostAndFavorite(String hostId, Boolean isFavorite) throws Exception {
		List<Graph> graphList = new ArrayList<Graph>();
		List<Graph> graphs = graphRepo.findByHostIdAndFavorite(hostId, isFavorite);
		for (Graph graph : graphs) {
			if (graph.getGraphItemsList().size() > 0  && graph.getGraphItemsList().get(0) != null ) {
				List<Trigger> triggers = triggerService.findAllByItem(graph.getGraphItemsList().get(0).getItemId(),
						graph.getHostId());
				Item item = itemService.find(graph.getGraphItemsList().get(0).getItemId());
				graph.setTriggerCount(triggers.size());
				graph.setTriggerStatus(item.getTriggerStatus());
			}
			graphList.add(graph);
		}
		return graphList;
	}

	@Override
	public List<Graph> findAllByFavorite(Boolean isFavorite) throws Exception {
		List<Graph> graphList = new ArrayList<Graph>();
		List<Graph> graphs = graphRepo.findByFavorite(isFavorite);
		for (Graph graph : graphs) {
			if (graph.getGraphItemsList().size() > 0  && graph.getGraphItemsList().get(0) != null ) {
				List<Trigger> triggers = triggerService.findAllByItem(graph.getGraphItemsList().get(0).getItemId(),
						graph.getHostId());
				Item item = itemService.find(graph.getGraphItemsList().get(0).getItemId());
				graph.setTriggerCount(triggers.size());
				graph.setTriggerStatus(item.getTriggerStatus());
			}
			graphList.add(graph);
		}
		return graphList;
	}

	@Override
	public List<Graph> setFavorite(String graphId, Boolean isFavorite) throws Exception {
		List<Graph> graphs = new ArrayList<Graph>();
		Graph grph = graphRepo.findByGraphId(graphId);
		if (grph != null) {
			grph.setFavorite(isFavorite);
			graphRepo.save(grph);
			graphs = graphRepo.findByHostId(grph.getHostId());
		}
		List<Graph> graphList = new ArrayList<Graph>();
		for (Graph graph : graphs) {
			if (graph.getGraphItemsList().size() > 0  && graph.getGraphItemsList().get(0) != null ) {
				List<Trigger> triggers = triggerService.findAllByItem(graph.getGraphItemsList().get(0).getItemId(),
						graph.getHostId());
				Item item = itemService.find(graph.getGraphItemsList().get(0).getItemId());
				graph.setTriggerCount(triggers.size());
				graph.setTriggerStatus(item.getTriggerStatus());
			}
			graphList.add(graph);
		}
		return graphList;
	}

	@Override
	public void syncGraphFromZabbixServer(List<String> hostIds) throws Exception {
		List<Graph> graphs = graphRepo.findGraphByNonHost(hostIds);
		graphRepo.delete(graphs);
	}
}
