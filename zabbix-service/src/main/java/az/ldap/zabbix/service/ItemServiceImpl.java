package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.DefaultItems;
import az.ldap.zabbix.entity.DefaultTriggers;
import az.ldap.zabbix.entity.DiscoveryRule;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.Trigger;
import az.ldap.zabbix.repository.DefaultItemRepository;
import az.ldap.zabbix.repository.DefaultTriggersRepository;
import az.ldap.zabbix.repository.DiscoveryRuleRepository;
import az.ldap.zabbix.repository.ItemRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.item.FilterRequest;
import az.zabbix.connector.item.ItemCUDResponse;
import az.zabbix.connector.item.ItemCUDResult;
import az.zabbix.connector.item.ItemCreateRequest;
import az.zabbix.connector.item.ItemDeleteRequest;
import az.zabbix.connector.item.ItemListRequest;
import az.zabbix.connector.item.ItemListResponse;
import az.zabbix.connector.item.ItemParams;
import az.zabbix.connector.item.ItemProtoTypeCreateRequest;
import az.zabbix.connector.item.ItemProtoTypeParams;
import az.zabbix.connector.item.ItemRequest;
import az.zabbix.connector.item.ItemResponse;
import az.zabbix.connector.item.ItemUpdateRequest;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Value(value = "${zabbix.proxy}")
	private String hostUrl;
	
	@Value(value = "${zabbix.delay}")
	private Integer delay;
		
	@Autowired
	private DiscoveryRuleRepository discoveryRepo;
	
	@Autowired
	private GraphService graphService;
	
	@Autowired
	private GraphItemService graphItemService;
	
	@Autowired
	private DefaultItemRepository defaultItemRepo;
	
	@Autowired
	private DefaultTriggersRepository defaultTriggerRepo;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private HostService hostService;

	/** Item creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<ItemProtoTypeCreateRequest, ItemCUDResult> itemPrototypeCreateReq;

	@Autowired
	private RestTemplateCall<ItemCreateRequest, ItemCUDResult> itemCreateReq;
	
	/** Item update Connection processor reference. */
	@Autowired
	private RestTemplateCall<ItemUpdateRequest, ItemCUDResult> itemUpdateReq;

	/** Item delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<ItemDeleteRequest, ItemCUDResult> itemDeleteReq;

	/** Item lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<ItemListRequest, ItemListResponse> itemListReq;

	@Override
	public Item save(Item item) throws Exception {
		if (item.getKey_().contains("#")) {
			ItemCUDResult itemResponse = itemPrototypeCreateReq.restCall(hostUrl, getCreateProtoTypeRequest(GenericConstants.AUTH, item),
					ItemCUDResult.class);
			if (itemResponse.getError() != null) {
				ItemProtoTypeCreateRequest param = getCreateProtoTypeRequest(GenericConstants.AUTH, item);
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemResponse.getError().get("data"));
			}
			ItemCUDResponse itemRes = itemResponse.getResult();
			item.setItemId(itemRes.getItemids().get(0));
			item = itemRepository.save(item);
			Thread.sleep(delay*1000);
			findAllFromZabbixServer(item.getHostId());
			return item;
		} else {
			ItemCUDResult itemResponse = itemCreateReq.restCall(hostUrl, getCreateItemRequest(GenericConstants.AUTH, item),
					ItemCUDResult.class);
			if (itemResponse.getError() != null) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemResponse.getError().get("data"));
			}
			ItemCUDResponse itemRes = itemResponse.getResult();
			item.setItemId(itemRes.getItemids().get(0));
			findAllFromZabbixServer(item.getHostId());
			Item items = find(item.getItemId());
			if (items == null) {
				return itemRepository.save(items);
			} else {
				return items;
			}
		}
	}

	@Override
	public Item update(Item item) throws Exception {
		ItemCUDResult itemResponse = itemUpdateReq.restCall(hostUrl, getUpdateItemRequest(GenericConstants.AUTH, item),
				ItemCUDResult.class);
		if (itemResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemResponse.getError().get("data"));
		}
		ItemCUDResponse itemRes = itemResponse.getResult();
		item.setItemId(itemRes.getItemids().get(0));
		return itemRepository.save(item);
	}

	@Override
	public void delete(Item item) throws Exception {
		ItemCUDResult itemResponse = itemDeleteReq.restCall(hostUrl, getDeleteItemRequest(GenericConstants.AUTH, item.getItemId()),
				ItemCUDResult.class);
		if (itemResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemResponse.getError().get("data"));
		}
		ItemCUDResponse itemRes = itemResponse.getResult();
		item.setItemId(itemRes.getItemids().get(0));
		itemRepository.delete(item);
		List<Item> items = itemRepository.findByHost(item.getHostId());
		String itemids = "";
		int i  = 0;
		for(Item itemx: items){
			if ((items.size()-1) == i) {
				itemids += itemx.getItemId();
			} else {
				itemids += itemx.getItemId() + ",";
			}
			i++;
		}
		graphItemService.findAllFromZabbixServer(itemids);
		graphService.findAllFromZabbixServer(item.getHostId());
		triggerService.findAllFromZabbixServer(item.getHostId());
	}

	@Override
	public void delete(String id) throws Exception {
		ItemCUDResult itemResponse = itemDeleteReq.restCall(hostUrl, getDeleteItemRequest(GenericConstants.AUTH, id),
				ItemCUDResult.class);
		if (itemResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemResponse.getError().get("data"));
		}
		ItemCUDResponse itemRes = itemResponse.getResult();
		Item item = find(itemRes.getItemids().get(0));
		item.setItemId(itemRes.getItemids().get(0));
		itemRepository.delete(id);
		graphService.findAllFromZabbixServer(item.getHostId());
		List<Item> items = itemRepository.findByHost(item.getHostId());
		String itemids = "";
		int i  = 0;
		for(Item itemx: items){
			if ((items.size()-1) == i) {
				itemids += itemx.getItemId();
			} else {
				itemids += itemx.getItemId() + ",";
			}
			i++;
		}
		graphItemService.findAllFromZabbixServer(itemids);
		triggerService.findAllFromZabbixServer(item.getHostId());
	}

	@Override
	public Item find(String id) throws Exception {
		Item item = itemRepository.findByItemId(id);
		return item;
	}

	@Override
	public List<Item> findAll() throws Exception {
		return itemRepository.findAll();
	}

	@Override
	public Page<Item> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return itemRepository.findAll(pagingAndSorting.toPageRequest());
	}

	public Item getItem(String itemId) throws Exception {
		ItemListResponse itemsResponse = itemListReq.restCall(hostUrl, getItemRequest(GenericConstants.AUTH, itemId),
				ItemListResponse.class);
		if (itemsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemsResponse.getError().get("data"));
		}
		List<ItemResponse> itemRes = itemsResponse.getResult();
		if (itemRes.size() > 0) {
			if (find(itemRes.get(0).getItemid()) == null) {
				Item item = itemEntityMapperUtil(itemRes.get(0));
				return item;
			}
			return find(itemRes.get(0).getItemid());
		}
		return null;
	}

	private ItemCreateRequest getCreateItemRequest(String authCode, Item item) {
		ItemCreateRequest request = new ItemCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("item.create");
		ItemParams itemParams = new ItemParams();
		itemParams.setName(item.getName());
		itemParams.setKey_(item.getKey_());
		itemParams.setHostid(item.getHostId());
		itemParams.setInterfaceid(item.getInterfaceId());
		itemParams.setDelay(item.getDelay());
		itemParams.setType(item.getType());
		itemParams.setValue_type(item.getValueType());
		itemParams.setStatus(item.getStatus());
		if (item.getUnits() != null && !item.getUnits().equals("val")) {
			itemParams.setUnits(item.getUnits());
		}
		request.setParams(itemParams);
		return request;
	}
	
	private ItemProtoTypeCreateRequest getCreateProtoTypeRequest(String authCode, Item item) {
		ItemProtoTypeCreateRequest request = new ItemProtoTypeCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("itemprototype.create");
		ItemProtoTypeParams itemParams = new ItemProtoTypeParams();
		itemParams.setName(item.getName());
		List<DiscoveryRule> discs = discoveryRepo.findByHostId(item.getHostId());
		for (DiscoveryRule disc : discs) {
			if (disc.getCategory().equals("Disk") && item.getCategory().equals("Disk") && item.getOsType().contains("linux")) {
				itemParams.setRuleid(disc.getRuleId());
			}
			if (disc.getCategory().equals("Networks") && item.getCategory().equals("Networks")) {
				itemParams.setRuleid(disc.getRuleId());
			}
			if (disc.getCategory().equals("FileSystem") && item.getCategory().equals("FileSystem")) {
				itemParams.setRuleid(disc.getRuleId());
			}
		}
		itemParams.setKey_(item.getKey_());
		itemParams.setHostid(item.getHostId());
		itemParams.setInterfaceid(item.getInterfaceId());
		itemParams.setDelay(item.getDelay());
		itemParams.setType(item.getType());
		itemParams.setValue_type(item.getValueType());
		itemParams.setStatus(item.getStatus());
		if (item.getUnits() != null && !item.getUnits().equals("val")) {
			itemParams.setUnits(item.getUnits());
		}
		request.setParams(itemParams);
		return request;
	}

	private ItemDeleteRequest getDeleteItemRequest(String authCode, String itemId) {
		ItemDeleteRequest request = new ItemDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("item.delete");
		List<String> items = new ArrayList<String>();
		items.add(itemId);
		request.setParams(items);
		return request;
	}

	private ItemListRequest getAllItemRequest(String authCode, String hostId) {
		ItemListRequest request = new ItemListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("item.get");
		ItemRequest user = new ItemRequest();
		user.setOutput("extend");
		user.setSortfield("itemid");
		user.setSortorder("ASC");
		user.setHostids(hostId);
		request.setParams(user);
		return request;
	}

	private ItemListRequest getItemRequest(String authCode, String itemId) {
		ItemListRequest request = new ItemListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("item.get");
		ItemRequest item = new ItemRequest();
		item.setOutput("extend");
		item.setSortfield("itemid");
		item.setSortorder("ASC");
		FilterRequest filter = new FilterRequest();
		filter.setItemid(itemId);
		item.setSearch(filter);
		request.setParams(item);
		return request;
	}

	private ItemUpdateRequest getUpdateItemRequest(String authCode, Item item) {
		ItemUpdateRequest request = new ItemUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("item.update");
		ItemParams itemParams = new ItemParams();
		itemParams.setDelay(item.getDelay());
		itemParams.setStatus(item.getStatus());
		itemParams.setItemid(item.getItemId());
		request.setParams(itemParams);
		return request;
	}

	public Item itemEntityMapperUtil(ItemResponse itemResponse) throws Exception {
		Item item = null;
		if (itemResponse != null) {
			item = new Item();
			String name = itemResponse.getName();
			if (itemResponse.getKey_().contains(",") && name.contains("$")) {
				name = name.replace("$1", itemResponse.getKey_().substring(itemResponse.getKey_().indexOf("[") + 1,
						itemResponse.getKey_().indexOf(",")));
				item.setName(name);
			} else if (name.contains("$")) {
				name = name.replace("$1", itemResponse.getKey_().substring(itemResponse.getKey_().indexOf("[") + 1,
						itemResponse.getKey_().indexOf("]")));
				item.setName(name);
			} else {
				item.setName(name);
			}
			item.setDelay(Integer.parseInt(itemResponse.getDelay()));
			item.setHostId(itemResponse.getHostid());
			Host host = hostService.find(itemResponse.getHostid());
			if (host != null) {
				item.setUserid_(host.getUserid_());
				item.setDepartmentid_(host.getDepartmentid_());
				item.setDomainid_(host.getDomainid_());
			}
			if (host.getOsType().contains("Windows") || host.getOsType().contains("windows")) {
				item.setOsType("windows");
			} else {
				item.setOsType("linux");
			}
			item.setInterfaceId(itemResponse.getInterfaceid());
			item.setItemId(itemResponse.getItemid());
			item.setKey_(itemResponse.getKey_());
			if (itemResponse.getKey_().contains("cpu") || itemResponse.getKey_().contains("Processor(_Total)")) {
				item.setCategory("CPU");
				DefaultItems resultItems = defaultItemRepo.findAllByKey_(itemResponse.getKey_(), item.getOsType());
				if (resultItems != null) {
					item.setGraph(resultItems.getGraph());
					item.setUsage(resultItems.getUsage());
					item.setUnits(resultItems.getUnits());
					item.setTriggerStatus(resultItems.getTriggerStatus());
				}
			} else if (itemResponse.getKey_().contains("memory") || itemResponse.getKey_().contains("swap")) {
				item.setCategory("Memory");
				DefaultItems resultItems = defaultItemRepo.findAllByKey_(itemResponse.getKey_(), item.getOsType());
				if (resultItems != null) {
					item.setGraph(resultItems.getGraph());
					item.setUsage(resultItems.getUsage());
					item.setUnits(resultItems.getUnits());
					item.setTriggerStatus(resultItems.getTriggerStatus());
				}
			} else if (itemResponse.getKey_().contains("fs") && !itemResponse.getKey_().contains("custom")) {
				item.setCategory("FileSystem");
				item.setGraph(true);
				item.setUsage(0);
				if (itemResponse.getKey_().contains("pfree") || itemResponse.getKey_().contains("pused")) {
					if (item.getKey_().contains("[")) {
						String[] keys = item.getKey_().split("\\[");
						String key = keys[0];
						DefaultItems resultItems = defaultItemRepo.findItemByLikeKeyAndOsType(key, item.getOsType());
						if (resultItems != null) {
							item.setTriggerStatus(resultItems.getTriggerStatus());
							item.setUnits(resultItems.getUnits());
						}
					}
					item.setUnits("%");
					
				} else {
					if (item.getKey_().contains("[")) {
						String[] keys = item.getKey_().split("\\[");
						String key = keys[0];
						DefaultItems resultItems = defaultItemRepo.findItemByLikeKeyAndOsType(key, item.getOsType());
						if (resultItems != null) {
							item.setTriggerStatus(resultItems.getTriggerStatus());
							item.setUnits(resultItems.getUnits());
						}
					}
					
				}
			} else if (itemResponse.getKey_().contains("net")) {
				item.setCategory("Networks");
				item.setGraph(true);
				item.setUsage(0);
				if (item.getKey_().contains("[")) {
					String[] keys = item.getKey_().split("\\[");
					String key = keys[0];
					DefaultItems resultItems = defaultItemRepo.findItemByLikeKeyAndOsType(key, item.getOsType());
					if (resultItems != null) {
						item.setTriggerStatus(resultItems.getTriggerStatus());
						item.setUnits(resultItems.getUnits());
					}
				}
				if (itemResponse.getKey_().contains("status")) {
					item.setUnits("val");
				} 
			} else if ((itemResponse.getKey_().contains("perf_") || itemResponse.getKey_().contains("custom.vfs.dev") ||itemResponse.getKey_().contains("custom")) && !itemResponse.getKey_().contains("Processor(_Total)") && !itemResponse.getName().contains("Number of threads")) {
				item.setCategory("Disks");
				item.setGraph(true);
				item.setUsage(0);
				if (item.getKey_().contains("[")) {
					String[] keys = item.getKey_().split("\\[");
					String key = keys[0];
					DefaultItems resultItems = defaultItemRepo.findItemByLikeKeyAndOsType(key, item.getOsType());
					if (resultItems != null) {
						item.setTriggerStatus(resultItems.getTriggerStatus());
						item.setUnits(resultItems.getUnits());
					}
				} else {
					DefaultItems resultItems = defaultItemRepo.findAllByKey_(itemResponse.getKey_(), item.getOsType());
					if (resultItems != null) {
						item.setTriggerStatus(resultItems.getTriggerStatus());
						item.setUnits(resultItems.getUnits());
					}
				}
				if (itemResponse.getName().contains("Bytes/")) {
					item.setUnits("Bps");
				} else if(itemResponse.getName().contains("Ops")) {
					item.setUnits("ops/s");
				} else if(itemResponse.getName().contains("ms")) {
					item.setUnits("ms");
				} else if(itemResponse.getName().contains("io")) {
					item.setUnits("io");
				} 
			} else {
				item.setCategory("Dashboard");
				DefaultItems resultItems = defaultItemRepo.findAllByKey_(itemResponse.getKey_(), item.getOsType());
				if (resultItems != null) {
					item.setGraph(resultItems.getGraph());
					item.setUsage(resultItems.getUsage());
					item.setUnits(resultItems.getUnits());
					item.setTriggerStatus(resultItems.getTriggerStatus());
				}
			}
			item.setStatus(Integer.valueOf(itemResponse.getStatus()));
			item.setType(Integer.valueOf(itemResponse.getType()));
			item.setValueType(Integer.valueOf(itemResponse.getValue_type()));
		}
		return item;
	}
	public Map<String, Item> convert(List<Item> itemList) {
		Map<String, Item> itemMap = new HashMap<String, Item>();
		for (Item item : itemList) {
			itemMap.put(item.getItemId(), item);
		}
		return itemMap;
	}
	
	@Override
	public List<Item> findAllFromZabbixServer(String hostId) throws Exception {
		ItemListResponse itemsResponse = itemListReq.restCall(hostUrl, getAllItemRequest(GenericConstants.AUTH, hostId), ItemListResponse.class);
		if (itemsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemsResponse.getError().get("data"));
		}
		List<ItemResponse> itemRes = itemsResponse.getResult();
		List<Item> items = new ArrayList<Item>();
		for (ItemResponse item : itemRes) {
			items.add(itemEntityMapperUtil(item));
		}
		HashMap<String, Item> itemMap = (HashMap<String, Item>) convert(items);
		List<Item> appItemList = itemRepository.findByHost(hostId);
		for (Item item : appItemList) {
			if (item.getItemId() != null) {
				if (itemMap.containsKey(item.getItemId())) {
					Item csItem = itemMap.get(item.getItemId());
					item.setName(csItem.getName());
					item.setHostId(csItem.getHostId());
					item.setDelay(csItem.getDelay());
					item.setKey_(csItem.getKey_());
					item.setInterfaceId(csItem.getInterfaceId());
					item.setItemId(csItem.getItemId());
					item.setStatus(csItem.getStatus());
					item.setUnits(csItem.getUnits());
					item.setType(csItem.getType());
					item.setValueType(csItem.getValueType());
					item.setCategory(csItem.getCategory());
					item.setUserid_(csItem.getUserid_());
					item.setDomainid_(csItem.getDomainid_());
					item.setDepartmentid_(csItem.getDepartmentid_());
					item.setGraph(csItem.getGraph());
					item.setTriggerStatus(csItem.getTriggerStatus());
					item.setUsage(csItem.getUsage());
					itemRepository.save(item);
					itemMap.remove(item.getItemId());
				} else {
					if (item.getItemId() != null) {
						itemRepository.delete(item);
					}
				}
			}
		}
		for (String key : itemMap.keySet()) {
			itemRepository.save(itemMap.get(key));
		}
		return itemRepository.findByHost(hostId);
	}

	@Override
	public List<Item> findAllByHostandOsType(String hostId, String osType) throws Exception {
		List<DefaultItems> resultItems = defaultItemRepo.findAllByOsType(osType);
		List<Item> items = new ArrayList<Item>();
		for (DefaultItems defItem : resultItems) {
			Item item = new Item();
			Item tempItem = null;
			Item existItem = null;
			if (defItem.getKey_().contains("#")) {
				List<String> keyValues = new ArrayList<String>();
				String[] keys = defItem.getKey_().split("\\[");
				String key = keys[0];
				String key1 = "";
				if(defItem.getKey_().contains(",")){
					String[] keysd = defItem.getKey_().split(",");
					String keyB = keysd[1].split("\\]")[0];
					key1 = keyB;
					keyValues.add(key1);
				}
				List<Item> existItems = itemRepository.findItemByLikeKeyAndHost(hostId, key);
				for (Item itm : existItems) {
					if (!key1.equals("") && defItem.getCategory().equals("FileSystem")) {
						String[] keyVal = defItem.getKey_().split("\\[");
						key1 = "";
						if (itm.getKey_().contains(",")) {
							String[] keyss = itm.getKey_().split(",");
							String keyC = keyss[1].split("\\]")[0];
							key1 = keyC;
						}
						if (keyValues.contains(key1)) {
							tempItem = new Item();
							tempItem.setId(defItem.getId());
							tempItem.setCategory(defItem.getCategory());
							tempItem.setDelay(defItem.getDelay());
							tempItem.setKey_(defItem.getKey_());
							tempItem.setName(defItem.getName());
							tempItem.setOsType(defItem.getOsType());
							tempItem.setType(defItem.getType());
							tempItem.setUnits(defItem.getUnits());
							tempItem.setValueType(defItem.getValueType());
							tempItem.setDataType(defItem.getDataType());
							tempItem.setGraph(defItem.getGraph());
							tempItem.setUsage(defItem.getUsage());
							tempItem.setTriggerStatus(defItem.getTriggerStatus());
							existItem = tempItem;
						} 
					} else {
							tempItem = new Item();
							tempItem.setId(defItem.getId());
							tempItem.setCategory(defItem.getCategory());
							tempItem.setDelay(defItem.getDelay());
							tempItem.setKey_(defItem.getKey_());
							tempItem.setName(defItem.getName());
							tempItem.setOsType(defItem.getOsType());
							tempItem.setType(defItem.getType());
							tempItem.setUnits(defItem.getUnits());
							tempItem.setValueType(defItem.getValueType());
							tempItem.setDataType(defItem.getDataType());
							tempItem.setGraph(defItem.getGraph());
							tempItem.setUsage(defItem.getUsage());
							tempItem.setTriggerStatus(defItem.getTriggerStatus());
							existItem = tempItem;
					}
				}
			} else {
				existItem = itemRepository.findItemByKeyAndHost(hostId, defItem.getKey_());
			}
			if (existItem == null && tempItem == null) {
				item.setId(defItem.getId());
				item.setCategory(defItem.getCategory());
				item.setDelay(defItem.getDelay());
				item.setKey_(defItem.getKey_());
				item.setName(defItem.getName());
				item.setOsType(defItem.getOsType());
				item.setType(defItem.getType());
				item.setUnits(defItem.getUnits());
				item.setValueType(defItem.getValueType());
				item.setDataType(defItem.getDataType());
				item.setGraph(defItem.getGraph());
				item.setUsage(defItem.getUsage());
				item.setTriggerStatus(defItem.getTriggerStatus());
				items.add(item);
			}
		}
		return items;
	}

	@Override
	public List<Item> findAllByHost(String hostId) throws Exception {
		List<Item> itemList =  itemRepository.findByHost(hostId);
		for (Item item : itemList) {
			if ( item.getDelayUnits() != null && item.getDelayUnits().equals("mins")) {
				item.setDelay(item.getDelay() / 60);
			}
		}
		return itemList;
	}

	@Override
	public Item deleteItem(Item item) throws Exception {
		ItemCUDResult itemResponse = itemDeleteReq.restCall(hostUrl, getDeleteItemRequest(GenericConstants.AUTH, item.getItemId()),
				ItemCUDResult.class);
		if (itemResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemResponse.getError().get("data"));
		}
		ItemCUDResponse itemRes = itemResponse.getResult();
		item.setItemId(itemRes.getItemids().get(0));
		itemRepository.delete(item);
		graphService.findAllFromZabbixServer(item.getHostId());
		List<Item> items = itemRepository.findByHost(item.getHostId());
		String itemids = "";
		int i  = 0;
		for(Item itemx: items){
			if ((items.size()-1) == i) {
				itemids += itemx.getItemId();
			} else {
				itemids += itemx.getItemId() + ",";
			}
			i++;
		}
		graphItemService.findAllFromZabbixServer(itemids);
		triggerService.findAllFromZabbixServer(item.getHostId());
		return item;
	}
	
	@Override
	public void deleteAllItem(List<Item> item) throws Exception {
		itemRepository.delete(item);
	}

	@Override
	public List<Item> loadDefaultItems(Host host) throws Exception {
		List<Item> itemList = new ArrayList<Item>();
		if(host.getOsType().contains("Windows") || host.getOsType().contains("windows")){
			host.setOsType("windows");
		} else {
			host.setOsType("linux");
		}
		List<DefaultItems> resultItems = defaultItemRepo.findAllByDefaultAndOstype(true, host.getOsType());
		for (DefaultItems defItem : resultItems) {
			Item item = new Item();
			item.setCategory(defItem.getCategory());
			item.setDelay(defItem.getDelay());
			item.setKey_(defItem.getKey_());
			item.setName(defItem.getName());
			item.setOsType(defItem.getOsType());
			item.setType(defItem.getType());
			item.setUnits(defItem.getUnits());
			item.setValueType(defItem.getValueType());
			item.setDataType(defItem.getDataType());
			item.setGraph(defItem.getGraph());
			item.setUsage(defItem.getUsage());
			item.setTriggerStatus(defItem.getTriggerStatus());
			item.setHostId(host.getHostId());
			item.setInterfaceId(host.getHostInterfaceList().get(0).getInterfaceId());
			item.setId(null);
			item.setDepartmentid_(host.getDepartmentid_());
			item.setUserid_(host.getUserid_());
			item.setDomainid_(host.getDomainid_());
			save(item);
		}
		return findAllFromZabbixServer(host.getHostId());
	}

	@Override
	public Integer zabbixItemCount(String hostId) throws Exception {
		ItemListResponse itemsResponse = itemListReq.restCall(hostUrl, getAllItemRequest(GenericConstants.AUTH, hostId), ItemListResponse.class);
		if (itemsResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, itemsResponse.getError().get("data"));
		}
		List<ItemResponse> itemRes = itemsResponse.getResult();
		return itemRes.size();
	}

	@Override
	public List<Item> getItemsForTrigger(String hostId, String osType) throws Exception {
		List<Item> itemsForTriggers = new ArrayList<Item>();
		List<DefaultTriggers> triggers = defaultTriggerRepo.findAllByDefaultAndHost(osType);
		for (Item item : findAllByHost(hostId)) {
			for (DefaultTriggers trigger : triggers) {
				if (trigger.getDescription().contains("#")) {
					if (item.getKey_().contains("[") && item.getKey_().split("\\[")[0].equalsIgnoreCase((trigger.getExpression().split(":")[1]).split("\\[")[0])) {
						List<Trigger> exTrigger = triggerService.findAllByItem(item.getItemId(), item.getHostId());
						if (exTrigger.size()  == 0) {
							itemsForTriggers.add(item);
						}
					}
				} else {
					if (item.getKey_().equalsIgnoreCase(trigger.getExpression().split(":")[1])) {
						List<Trigger> exTrigger = triggerService.findAllByItem(item.getItemId(), item.getHostId());
						if (exTrigger.size() == 0) {
							itemsForTriggers.add(item);
						}
					}
				}
			}
		}
		return itemsForTriggers;
	}

	@Override
	public List<Item> findAllByUsage(String hostId) throws Exception {
		return itemRepository.findAllByUsage(1, hostId);
	}

	@Override
	public Item findAllByKeyAndHost(String hostId, String key) throws Exception {
		return itemRepository.findItemByKeyAndHost(hostId, key);
	}

	@Override
	public void syncItemFromZabbixServer(List<String> hostIds) throws Exception {
		List<Item> items = itemRepository.findItemByNonHost(hostIds);
		itemRepository.delete(items);
	}

	@Override
	public List<Item> findAllByUser(Integer id, String type) throws Exception {
		if(type.equalsIgnoreCase("ROOT_ADMIN")){
			return itemRepository.findAll();
		} else if(type.equalsIgnoreCase("DOMAIN_ADMIN")){
			return itemRepository.findByDomain(id);
		} else {
			return itemRepository.findByUser(id);
		}
	}
}
