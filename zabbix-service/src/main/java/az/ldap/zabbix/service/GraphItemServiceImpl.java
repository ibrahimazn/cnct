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
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.repository.GraphItemsRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.graph.GraphItemListRequest;
import az.zabbix.connector.graph.GraphItemListResponse;
import az.zabbix.connector.graph.GraphItemRequest;
import az.zabbix.connector.graph.GraphItemResponse;

/** GraphItem Items service implementation class. */
@Service
public class GraphItemServiceImpl implements GraphItemService {

	/** Host repository reference. */
	@Autowired
	private GraphItemsRepository graphItemRepo;
	
	@Autowired
	private ItemService itemService;

	@Value(value = "${zabbix.proxy}")
	private String graphUrl;

	/** Graph lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<GraphItemListRequest, GraphItemListResponse> graphItemListReq;

	@Override
	public GraphItem save(GraphItem GraphItem) throws Exception {
		return graphItemRepo.save(GraphItem);
	}

	@Override
	public GraphItem update(GraphItem GraphItem) throws Exception {
		return graphItemRepo.save(GraphItem);
	}

	@Override
	public void delete(GraphItem GraphItem) throws Exception {
		graphItemRepo.delete(GraphItem);
	}

	@Override
	public void delete(String id) throws Exception {
		graphItemRepo.delete(id);
	}

	@Override
	public GraphItem find(String id) throws Exception {
		return graphItemRepo.findByGraphItemId(id);
	}

	@Override
	public List<GraphItem> findAll() throws Exception {
		return graphItemRepo.findAll();
	}

	@Override
	public Page<GraphItem> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return graphItemRepo.findAll(pagingAndSorting.toPageRequest());
	}

	private GraphItemListRequest getGraphRequest(String authCode, String itemids) {
		GraphItemListRequest request = new GraphItemListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graphitem.get");
		GraphItemRequest graph = new GraphItemRequest();
		graph.setOutput("extend");
		graph.setSortfield("gitemid");
		graph.setSortorder("ASC");
		List<String> items = new ArrayList<String>(Arrays.asList(itemids.split(",")));
		graph.setItemids(items);
		request.setParams(graph);
		return request;
	}

	public GraphItem graphItemEntityMapperUtil(GraphItemResponse graphResponse) throws Exception {
		GraphItem graph = null;
		if (graphResponse != null) {
			graph = new GraphItem();
			graph.setColor(graphResponse.getColor());
			graph.setItemId(graphResponse.getItemid());
			Item item = itemService.find(graphResponse.getItemid());
			if (item != null) {
				graph.setHostId(item.getHostId());
			}
			graph.setGitemId(graphResponse.getGitemid());
			graph.setGraphId(graphResponse.getGraphid());
			
		}
		return graph;
	}

	private GraphItemListRequest getAllGraphItemRequest(String authCode, String itemids) {
		GraphItemListRequest request = new GraphItemListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("graphitem.get");
		GraphItemRequest graph = new GraphItemRequest();
		graph.setOutput("extend");
		graph.setSortfield("gitemid");
		List<String> items = new ArrayList<String>(Arrays.asList(itemids.split(",")));
		graph.setItemids(items);
		graph.setSortorder("ASC");
		request.setParams(graph);
		return request;
	}

	public Map<String, GraphItem> convert(List<GraphItem> graphList) {
		Map<String, GraphItem> graphMap = new HashMap<String, GraphItem>();
		for (GraphItem graph : graphList) {
			graphMap.put(graph.getGitemId(), graph);
		}
		return graphMap;
	}

	@Override
	public List<GraphItem> findAllFromZabbixServer(String itemids) throws Exception {
		GraphItemListResponse graphItemsResponse = graphItemListReq.restCall(graphUrl, getAllGraphItemRequest(GenericConstants.AUTH, itemids),
				GraphItemListResponse.class);
		if (graphItemsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
					graphItemsResponse.getError().get("data"));
		}
		List<GraphItemResponse> graphItemRes = graphItemsResponse.getResult();
		List<GraphItem> graphItems = new ArrayList<GraphItem>();
		for (GraphItemResponse graphItem : graphItemRes) {
			graphItems.add(graphItemEntityMapperUtil(graphItem));
		}
		HashMap<String, GraphItem> graphItemMap = (HashMap<String, GraphItem>) convert(graphItems);
		List<GraphItem> appGraphItemList = graphItemRepo.findAll();
		if(graphItems.size() > 0){
			appGraphItemList = graphItemRepo.findByHostId(graphItems.get(0).getHostId());
		}
		for (GraphItem graphItem : appGraphItemList) {
			if (graphItemMap.containsKey(graphItem.getGitemId())) {
				GraphItem csGraphItem = graphItemMap.get(graphItem.getGitemId());
				graphItem.setColor(csGraphItem.getColor());
				graphItem.setItemId(csGraphItem.getItemId());
				graphItem.setGitemId(csGraphItem.getGitemId());
				graphItem.setGraphId(csGraphItem.getGraphId());
				graphItem.setHostId(csGraphItem.getHostId());
				graphItemRepo.save(graphItem);
				graphItemMap.remove(graphItem.getGitemId());
			} else {
				graphItemRepo.delete(graphItem);
			}
		}
		for (String key : graphItemMap.keySet()) {
			graphItemRepo.save(graphItemMap.get(key));
		}
		return findAll();
	}

	@Override
	public GraphItem findByItem(String id) throws Exception {
		return graphItemRepo.findByItemId(id);
	}

	@Override
	public List<GraphItem> findAllByHost(String hostId) throws Exception {
		return graphItemRepo.findByHostId(hostId);
	}

	@Override
	public void deleteItem(GraphItem items) throws Exception {
		graphItemRepo.delete(items);
	}

	@Override
	public void deleteAllItem(List<GraphItem> items) throws Exception {
		graphItemRepo.delete(items);
	}

	@Override
	public List<GraphItem> findAllByGraph(String graphId) throws Exception {
		return graphItemRepo.findByGraphId(graphId);
	}

	@Override
	public void syncGraphItemFromZabbixServer(List<String> hostIds) throws Exception {
		List<GraphItem> graphs = graphItemRepo.findGraphItemByNonHost(hostIds);
		graphItemRepo.delete(graphs);
	}

	@Override
	public void deleteAllByItems(String graphIds) throws Exception {
		String[] items = graphIds.split(",");
		List<GraphItem> graphs = graphItemRepo.findGraphItemByNonItems(Arrays.asList(items));
		graphItemRepo.delete(graphs);
	}

}
