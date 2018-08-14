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
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.service.HostService;
import az.ldap.zabbix.service.ItemService;
import az.ldap.zabbix.service.PagingAndSorting;

@RestController
@RequestMapping("/zabbix/api/item")
public class ItemController extends CRUDController<Item> implements ApiController {
	
	/** Item service references.*/
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private HostService hostService;

	@Override
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public Item create(@RequestBody Item item) throws Exception {
		return itemService.save(item);
	}

	@Override
	@RequestMapping(value = "/{id}",method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
	public Item update(@RequestBody Item item, @PathVariable(PATH_ID) String id) throws Exception {
		if (item.getDelayUnits().equals("mins")) {
        	item.setDelay(item.getDelay() * 60);
        }
		return itemService.update(item);
	}

	@Override
	public void delete(@PathVariable(PATH_ID) String id) throws Exception {
		itemService.delete(id);
	}

	@Override
	public Item read(@PathVariable(PATH_ID) String id) throws Exception {
		return itemService.find(id);
	}

	@Override
	public List<Item> list(@RequestParam String sortBy,  @RequestHeader String range,  @RequestParam Integer limit, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, Item.class);
		Page<Item> pageResponse = itemService.findAll(page);
		response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
		return pageResponse.getContent();
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getItems(@RequestParam String host, @RequestParam String ostype) throws Exception {
        if(ostype.contains("Windows") || ostype.contains("windows")){
        	return itemService.findAllByHostandOsType(host, "windows");	
        } else {
        	return itemService.findAllByHostandOsType(host, "linux");
        }
    }
	
	@RequestMapping(value = "/host", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getItemsByHost(@RequestParam String host) throws Exception {
        return itemService.findAllByHost(host);
    }
	
	@RequestMapping(value = "/trigger/host", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
	public List<Item> getItemsByHostAndTrigger(@RequestParam String host) throws Exception {
		Host existHost = hostService.find(host);
		String osType = "";
		if (existHost.getOsType().contains("Windows") || existHost.getOsType().contains("windows")) {
			osType = "windows";
		} else {
			osType = "linux";
		}
		return itemService.getItemsForTrigger(host, osType);
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Item deleteHost(@PathVariable(PATH_ID) String id) throws Exception {
        return itemService.deleteItem(itemService.find(id));
    }
}
