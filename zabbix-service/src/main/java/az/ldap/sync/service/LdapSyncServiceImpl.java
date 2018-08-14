package az.ldap.sync.service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;
import az.ldap.sync.constants.CloudStackConstants;
import az.ldap.sync.domain.entity.Account;
import az.ldap.sync.domain.entity.Domain;
import az.ldap.sync.domain.entity.Group;
import az.ldap.sync.domain.entity.LdapDomainGroup;
import az.ldap.sync.domain.entity.LdapUser;
import az.ldap.sync.domain.entity.User;
import az.ldap.sync.util.CloudStackAccountService;
import az.ldap.sync.util.CloudStackDomainService;
import az.ldap.sync.util.CloudStackServer;
import az.ldap.sync.util.CloudStackUserService;
import az.ldap.zabbix.entity.Host;
import az.ldap.zabbix.entity.HostGroup;
import az.ldap.zabbix.entity.Item;
import az.ldap.zabbix.entity.Trigger;
import az.ldap.zabbix.entity.UserGroup;
import az.ldap.zabbix.service.ActionService;
import az.ldap.zabbix.service.GraphItemService;
import az.ldap.zabbix.service.GraphService;
import az.ldap.zabbix.service.HistoryService;
import az.ldap.zabbix.service.HostGroupService;
import az.ldap.zabbix.service.HostInterfaceService;
import az.ldap.zabbix.service.HostService;
import az.ldap.zabbix.service.ItemService;
import az.ldap.zabbix.service.TriggerService;
import az.ldap.zabbix.service.UserGroupService;
import az.ldap.zabbix.service.UserService;

/**
 * We have to sync up with cloudstack server for the following data
 *
 * 1. User 2. Domain 3. Account
 *
 * Get the corresponding data from cloud stack server, if the ldap server does
 * not have the data add it. If the ldap server has the data update it, if the
 * ldap server has data which the cloud stack server does not have, then delete
 * it.
 */
@Component
public class LdapSyncServiceImpl implements LdapSyncService {

	/** Logger attribute. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapSyncServiceImpl.class);

	/** CloudStack connector reference for user. */
	@Autowired
	private CloudStackUserService cloudStackUserService;

	/** CloudStack connector reference for account. */
	@Autowired
	private CloudStackAccountService csAccountService;

	@Autowired
	private ActionService actionService;

	/** The Ldap Base Dn. */
	@Value(value = "${ldap.base}")
	private String ldapBase;

	@Value(value = "${ldap.userDn}")
	private String ldapUser;
	
	@Value("#{'${zabbix.hosts}'.split(',')}")
	private List<String> hostIds;

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private UserService userService;

	@Autowired
	private HostService hostService;

	@Autowired
	private HostInterfaceService hostInterfaceService;

	@Autowired
	private GraphService graphService;

	@Autowired
	private GraphItemService graphItemService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private TriggerService triggerService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private HostGroupService hostGroupService;

	/** Reference of cloudStack Constants. */
	private CloudStackConstants cloudStackConstants;

	/** Secret key value is append. */
	@Value(value = "${aes.salt.secretKey}")
	private String aesSecretKey;

	/** Cloudstack API key value is append. */
	@Value(value = "${cloudstack.apiKey}")
	private String apiKey;

	/** Cloudstack API URL value is append. */
	@Value(value = "${cloudstack.apiUrl}")
	private String apiUrl;

	/** Cloudstack Secret key value is append. */
	@Value(value = "${cloudstack.secretKey}")
	private String secretKey;

	/** Cloudstack server integration. */
	@Autowired
	private CloudStackServer server;

	/** CloudStack Domain service for connectivity with cloudstack. */
	@Autowired
	private CloudStackDomainService domainService;

	/** CloudStack Domain service for connectivity with cloudstack. */
	@Autowired
	private CloudStackUserService csUserService;

	// Maximum number of objects to return in testing
	private static final long COUNT_LIMIT = 200;

	private Long vaue = 0L;

	// Maximum time to wait for results in testing (ms)
	private static final int TIME_LIMIT = 60000;

	private static SearchControls searchControls = new SearchControls(SearchControls.OBJECT_SCOPE, COUNT_LIMIT,
			TIME_LIMIT, null, true, false);

	@Override
	public void init(CloudStackServer server) throws Exception {
		this.server = server;
		this.server.setServer(apiUrl, secretKey, apiKey);
	}

	@Override
	public JSONObject verifyDuplicateUserEmail() throws Exception {
		// Verify Duplicate user before sync
		JSONObject userResponseObject = new JSONObject();
		cloudStackUserService.setServer(server);
		HashMap<String, String> userMap = new HashMap<String, String>();
		// userMap.put("domainid", domainUuid);
		userMap.put(cloudStackConstants.CS_LIST_ALL, "true");
		String response = cloudStackUserService.listUsers(userMap, cloudStackConstants.JSON);
		JSONArray userListJSON = null;
		JSONArray duplicateUserListJSON = new JSONArray();
		JSONArray emptyEmailListJSON = new JSONArray();
		JSONObject responseObject = new JSONObject(response).getJSONObject("listusersresponse");
		if (responseObject.has(cloudStackConstants.CS_USER)) {
			userListJSON = responseObject.getJSONArray(cloudStackConstants.CS_USER);
			// 2. Iterate the json list, convert the single json entity to user
			// Record encountered Strings in HashSet.
			HashSet<String> userEmail = new HashSet<>();
			HashMap<String, Integer> userEmailMap = new HashMap<>();
			int j = 0;
			for (int i = 0, size = userListJSON.length(); i < size; i++) {
				if (!userListJSON.getJSONObject(i).getString("username").equalsIgnoreCase("baremetal-system-account")) {
					if (userListJSON.getJSONObject(i).has("email")) {
						j++;
						Integer currentCount = userEmailMap.get(userListJSON.getJSONObject(i).getString("email"));
						if (currentCount == null || currentCount == 0) {
							userEmailMap.put(userListJSON.getJSONObject(i).getString("email"), 1);
						} else {
							currentCount++;
							userEmailMap.put(userListJSON.getJSONObject(i).getString("email"), currentCount);
						}
					} else {
						emptyEmailListJSON.put(userListJSON.getJSONObject(i));
					}
				}
			}

			for (int i = 0, size = userListJSON.length(); i < size; i++) {
				if (!userListJSON.getJSONObject(i).getString("username").equalsIgnoreCase("baremetal-system-account")) {
					if (userListJSON.getJSONObject(i).has("email")) {
						if (userEmailMap.containsKey(userListJSON.getJSONObject(i).getString("email"))
								&& userEmailMap.get(userListJSON.getJSONObject(i).getString("email")) > 1) {
							duplicateUserListJSON.put(userListJSON.getJSONObject(i));
						}
					}
				}
			}
		}

		userResponseObject.put("duplicate", duplicateUserListJSON);
		userResponseObject.put("emptyEmail", emptyEmailListJSON);
		return userResponseObject;
	}

	/**
	 * Sync call for synchronization list of user with ldap server.
	 *
	 * @throws Exception
	 *             unhandled errors.
	 */
	@Override
	public void sync() throws Exception {
		try {
			// 1. Sync manual sync items
			this.verifyDuplicateUserEmail();
		} catch (Exception e) {
			LOGGER.error("ERROR AT Finding User email duplicate", e);
		}
		try {
			// 2. Sync Domain OU.
			LOGGER.debug("Sync domain started 1");
			this.syncDomain();
		} catch (Exception e) {
			LOGGER.error("ERROR AT synch Domaim", e);
		}
		try {
			// 9. Sync Account OU.
			LOGGER.debug("Sync account started 1");
			this.syncAccount();
		} catch (Exception e) {
			LOGGER.error("ERROR AT synch Account", e);
		}
		try {
			// 10. Sync User Uid
			LOGGER.debug("Sync user started 1");
			this.syncUser();
		} catch (Exception e) {
			LOGGER.error("ERROR AT synch User", e);
		}

	}

	/**
	 * Sync with CloudStack server Domain.
	 *
	 * @throws Exception
	 *             cloudstack unhandled errors
	 */
	@Override
	public void syncDomain() throws Exception {
		LOGGER.debug("Sync domain started");
		List<LdapDomainGroup> domainList = new ArrayList<LdapDomainGroup>();
		HashMap<String, String> domainMap = new HashMap<String, String>();
		domainMap.put("listall", "true");
		server.setServer(apiUrl, secretKey, apiKey);
		domainService.setServer(server);
		String response = domainService.listDomains("json", domainMap);
		if (new JSONObject(response).getJSONObject("listdomainsresponse").has("domain")) {
			JSONArray domainListJSON = new JSONObject(response).getJSONObject("listdomainsresponse")
					.getJSONArray("domain");
			for (int i = 0, size = domainListJSON.length(); i < size; i++) {
				LdapDomainGroup domain = LdapDomainGroup.convert(domainListJSON.getJSONObject(i));
				if (domain.getOu() != null) {
					domain.setDn(buildDomainDn(domain));
					domainList.add(domain);
				}
			}
		}
		HashMap<Name, LdapDomainGroup> csDomainMap = (HashMap<Name, LdapDomainGroup>) LdapDomainGroup
				.convert(domainList);
		List<LdapDomainGroup> appDomainList = ldapTemplate.findAll(LdapDomainGroup.class);
		LOGGER.debug("Total rows updated : " + (appDomainList.size()));
		for (LdapDomainGroup domain : appDomainList) {
			if (csDomainMap.containsKey(domain.getDn())) {
				LdapDomainGroup csDomain = csDomainMap.get(domain.getDn());
				domain.setDn(csDomain.getDn());
				domain.setObjectClass(csDomain.getObjectClass());
				ldapTemplate.update(domain);
				csDomainMap.remove(domain.getDn());
			} else {
				LOGGER.debug("Sync user started 2");
				syncUser();
				LOGGER.debug("Sync account started 2");
				syncAccount();
				ldapTemplate.delete(domain);
			}
		}
		for (Name key : csDomainMap.keySet()) {
			LOGGER.debug("Syncservice domain uuid:");
			ldapTemplate.create(csDomainMap.get(key));
			LOGGER.debug("Total rows added", (csDomainMap.size()));
		}
	}

	public List getPersonNames() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "organizationalUnit"));
		filter.and(new EqualsFilter("objectclass", "container"));
		return ldapTemplate.search("dc=az-rnd", filter.encode(), new AttributesMapper() {
			public Object mapFromAttributes(Attributes attrs) throws NamingException {
				return attrs.get("ou").get();
			}
		});
	}

	/**
	 * Sync with Cloud Server Account.
	 *
	 * @throws Exception
	 *             cloudstack unhandled errors.
	 */
	@Override
	public void syncUser() throws Exception {
		LOGGER.debug("Sync user started");
		List<LdapUser> userList = new ArrayList<LdapUser>();
		HashMap<String, String> userMap = new HashMap<String, String>();
		// userMap.put("domainid", domainUuid);
		userMap.put(CloudStackConstants.CS_LIST_ALL, "true");
		// 1. Get the list of users from CS server using CS connector
		server.setServer(apiUrl, secretKey, apiKey);
		csUserService.setServer(server);
		String response = csUserService.listUsers(userMap, cloudStackConstants.JSON);
		LOGGER.debug("List user" + response);
		JSONArray userListJSON = null;
		JSONObject responseObject = new JSONObject(response).getJSONObject("listusersresponse");
		if (responseObject.has(cloudStackConstants.CS_USER)) {
			userListJSON = responseObject.getJSONArray(cloudStackConstants.CS_USER);
			// 2. Iterate the json list, convert the single json entity to user
			for (int i = 0, size = userListJSON.length(); i < size; i++) {
				// 2.1 Call convert by passing JSONObject to User entity and Add
				// the converted User entity to list.
				LdapUser user = LdapUser.convert(userListJSON.getJSONObject(i));
				if (!user.getUserName().equalsIgnoreCase("baremetal-system-account")) {
					if (user.getAccount() != null && user.getEmail() != null) {
						user.setDn(buildUserDn(user));
						userList.add(user);
					}
				}
			}
		}
		HashMap<Name, LdapUser> csUserMap = (HashMap<Name, LdapUser>) LdapUser.convert(userList);
		List<LdapUser> appUserList = ldapTemplate.findAll(LdapUser.class);
		LOGGER.debug("Total rows updateds : " + (appUserList.size()));
		for (LdapUser user : appUserList) {
			if (csUserMap.containsKey(user.getDn())) {
				ldapTemplate.update(user);
				csUserMap.remove(user.getDn());
			} else {
				ldapTemplate.delete(user);
				LdapQuery query = query().where("objectclass").is("groupOfNames").and("cn")
						.like(user.getDescription().split(",")[0]);
				List<Group> grpList = ldapTemplate.find(query, Group.class);
				for (Group group : grpList) {
					ldapTemplate.delete(group);
				}
				LOGGER.debug("Sync account started 3");
				syncAccount();
			}
		}
		for (Name key : csUserMap.keySet()) {
			LOGGER.debug("Syncservice domain uuid:");
			ldapTemplate.create(csUserMap.get(key));
			LdapQuery query = query().where("objectclass").is("groupOfNames").and("cn")
					.like(csUserMap.get(key).getDescription().split(",")[0]);
			List<Group> grpList = ldapTemplate.find(query, Group.class);
			for (Group group : grpList) {
				ldapTemplate.delete(group);
			}
			LOGGER.debug("Sync account started 4");
			syncAccount();
			LOGGER.debug("Total rows added", (csUserMap.size()));
		}
	}

	/**
	 * Sync with Cloud Server Account.
	 *
	 * @throws Exception
	 *             cloudstack unhandled errors.
	 */
	@Override
	public void syncAccount() throws Exception {
		LOGGER.debug("Sync account started");
		List<Group> accountList = new ArrayList<Group>();
		HashMap<String, String> departmentMap = new HashMap<String, String>();
		departmentMap.put(CloudStackConstants.CS_LIST_ALL, CloudStackConstants.STATUS_ACTIVE);
		server.setServer(apiUrl, secretKey, apiKey);
		csAccountService.setServer(server);
		// 1. Get the list of accounts from CS server using CS connector
		String response = csAccountService.listAccounts(CloudStackConstants.JSON, departmentMap);
		JSONArray accountListJSON = null;
		JSONObject responseObject = new JSONObject(response)
				.getJSONObject(CloudStackConstants.CS_LIST_ACCOUNT_RESPONSE);
		if (responseObject.has(CloudStackConstants.CS_ACCOUNT)) {
			accountListJSON = responseObject.getJSONArray(CloudStackConstants.CS_ACCOUNT);
			// 2. Iterate the json list, convert the single json entity to
			// department
			for (int i = 0, size = accountListJSON.length(); i < size; i++) {
				// 2.1 Call convert by passing JSONObject to department entity
				// and
				// Add the converted department entity to list
				Group group = Group.convert(accountListJSON.getJSONObject(i));
				if (!group.getName().equalsIgnoreCase("baremetal-system-account-ROOT")) {
					if (group.getOu() != null && group.getName() != null) {
						group.setDn(buildAccountDn(group));
						LdapQuery query = query().where("objectclass").is("inetOrgPerson").and("description")
								.like(group.getName() + ",*");
						List<LdapUser> appUserList = ldapTemplate.find(query, LdapUser.class);
						for (LdapUser user : appUserList) {
							group.addMember(LdapNameBuilder.newInstance(ldapBase).add(user.getDn()).build());
						}
						if (appUserList.size() == 0) {
							group.addMember(LdapNameBuilder.newInstance().add("uid", group.getName()).build());
						}
						accountList.add(group);
					}
				}
			}
		}

		HashMap<Name, Group> csGroupMap = (HashMap<Name, Group>) Group.convert(accountList);
		List<Group> appGroupList = ldapTemplate.findAll(Group.class);
		LOGGER.debug("Total rows updated : " + (appGroupList.size()));
		for (Group group : appGroupList) {
			if (csGroupMap.containsKey(group.getDn())) {
				csGroupMap.remove(group.getDn());
			} else {
				LOGGER.debug("Sync user started 3");
				syncUser();
				ldapTemplate.delete(group);
			}
		}
		for (Name key : csGroupMap.keySet()) {
			LOGGER.debug("Sync domain started 2");
			syncDomain();
			LOGGER.debug("Sync user started 4");
			syncUser();
			Group group = csGroupMap.get(key);
			LdapQuery query = query().where("objectclass").is("inetOrgPerson").and("description")
					.like(group.getName() + ",*");
			List<LdapUser> appUserList = ldapTemplate.find(query, LdapUser.class);
			for (LdapUser user : appUserList) {
				group.addMember(LdapNameBuilder.newInstance(ldapBase).add(user.getDn()).build());
			}
			if (appUserList.size() == 0) {
				group.addMember(LdapNameBuilder.newInstance().add("uid", group.getName()).build());
			}
			ldapTemplate.create(group);
			LOGGER.debug("Total rows added", (csGroupMap.size()));
		}
	}

	private Name buildDomainDn(LdapDomainGroup domain) {
		return LdapNameBuilder.newInstance().add("ou", domain.getOu()).build();
	}

	private Name buildUserDn(LdapUser user) {
		return LdapNameBuilder.newInstance().add("ou", user.getAccount()).add("uid", user.getEmail()).build();
	}

	private Name buildAccountDn(Group group) {
		return LdapNameBuilder.newInstance().add("ou", group.getOu()).add("cn", group.getName()).build();
	}

	@Override
	public List<User> getUserList() throws Exception {
		LOGGER.debug("Sync user started 5");
		syncUser();
		List<LdapUser> appUserList = ldapTemplate.findAll(LdapUser.class);
		List<User> result = new ArrayList<User>();
		for (LdapUser lUser : appUserList) {
			User responseReference = new User();
			responseReference.setAccount(lUser.getAccount());
			responseReference.setDescription(lUser.getDescription());
			responseReference.setEmail(lUser.getEmail());
			responseReference.setFirstName(lUser.getFirstName());
			responseReference.setLastName(lUser.getLastName());
			responseReference.setMail(lUser.getMail());
			responseReference.setUserName(lUser.getUserName());
			result.add(responseReference);
		}

		return result;
	}

	@Override
	public List<Account> getAccountList() throws Exception {
		LOGGER.debug("Sync account started 5");
		syncAccount();
		List<Group> appGroupList = ldapTemplate.findAll(Group.class);
		List<Account> result = new ArrayList<Account>();
		for (Group group : appGroupList) {
			Account responseReference = new Account();
			responseReference.setName(group.getName());
			responseReference.setDescription(group.getDescription());
			responseReference.setOu(group.getOu());
			Iterator iterator = group.getMembers().iterator();
			Set<String> members = new HashSet<String>();
			while (iterator.hasNext()) {
				String user = iterator.next().toString();
				members.add(user);
			}
			responseReference.setMembers(members);
			result.add(responseReference);
		}
		return result;
	}

	@Override
	public List<Domain> getDomainList() throws Exception {
		LOGGER.debug("Sync domain started 3");
		syncDomain();
		List<LdapDomainGroup> appGroupList = ldapTemplate.findAll(LdapDomainGroup.class);
		List<Domain> result = new ArrayList<Domain>();
		for (LdapDomainGroup group : appGroupList) {
			Domain responseReference = new Domain();
			responseReference.setOu(group.getOu());
			result.add(responseReference);
		}
		return result;
	}

	@Override
	public void zabbixSync() throws Exception {
		try {
			LOGGER.debug("Zabbix sync started");
			List<Domain> domains = getDomainList();
			LOGGER.debug("Domains sync completed : "+ domains.toString());
			List<User> users = getUserList();
			LOGGER.debug("Users sync completed : "+ users.toString());
			List<HostGroup> groups = hostGroupService.findAllByType("Domain");
			List<HostGroup> result = groups.stream().filter(
					c -> !(domains.stream().map(Domain::getOu).collect(Collectors.toList())).contains(c.getName()))
					.collect(Collectors.toList());
			if (result.size() > 0) {
				userService.SyncUser();
			}
			LOGGER.debug("Host group loop started");
			for (HostGroup hostGrp : result) {
				LOGGER.debug("host group remove"+ hostGrp.getName().toString());
				userGroupService.removeUserGroup(hostGrp.getName());
				hostGroupService.removeHostGroup(hostGrp.getName());
			}
			LOGGER.debug("Domain group loop started");
			for (Domain domain : domains) {
				HostGroup hostGroup = hostGroupService.findByName(domain.getOu());
				UserGroup grps = userGroupService.getByName(domain.getOu());
				if (grps == null) {
					grps = new UserGroup();
					grps.setName(domain.getOu());
					grps.setType("Domain");
					grps = userGroupService.syncSave(grps);
					LOGGER.debug("domain group remove"+ grps.getName().toString());
				} else {
					grps = userGroupService.syncSave(grps);
				}
				if (hostGroup == null) {
					hostGroup = new HostGroup();
					hostGroup.setName(domain.getOu());
					hostGroup.setType("Domain");
					hostGroup = hostGroupService.syncSave(hostGroup);
					LOGGER.debug("host group save"+ hostGroup.getName().toString());
				} else {
					hostGroup = hostGroupService.syncSave(hostGroup);
					LOGGER.debug("host group update"+ hostGroup.getName().toString());
				}
			}
			LOGGER.debug("Host and user group sync completed : "+ result.toString());
			List<Account> accounts = getAccountList();
			LOGGER.debug("Account sync completed : "+ result.toString());
			List<HostGroup> accGroups = hostGroupService.findAllByType("Department");
			List<HostGroup> results = accGroups.stream().filter(
					c -> !(accounts.stream().map(Account::getName).collect(Collectors.toList())).contains(c.getName()))
					.collect(Collectors.toList());
			if (results.size() > 0) {
				userService.SyncUser();
			}
			for (HostGroup hostGrp : results) {
				userGroupService.removeUserGroup(hostGrp.getName());
				hostGroupService.removeHostGroup(hostGrp.getName());
			}
			LOGGER.debug("Account loop started");
			for (Account account : accounts) {
				HostGroup hostGroup = hostGroupService.findByName(account.getName());
				UserGroup grps = userGroupService.getByName(account.getName());
				if (grps == null) {
					grps = new UserGroup();
					grps.setName(account.getName());
					grps.setType("Department");
					grps = userGroupService.syncSave(grps);
				} else {
					grps = userGroupService.syncSave(grps);
				}
				if (hostGroup == null) {
					hostGroup = new HostGroup();
					hostGroup.setName(account.getName());
					hostGroup.setType("Department");
					hostGroup = hostGroupService.syncSave(hostGroup);
				} else {
					hostGroup = hostGroupService.syncSave(hostGroup);
				}
				grps.setHostId(hostGroup.getGroupId());
				Iterator iterator = account.getMembers().iterator();
				Set<String> members = new HashSet<String>();
				List<String> userIds = new ArrayList<String>();
				while (iterator.hasNext()) {
					String user = iterator.next().toString();
					members.add(user);
					String[] uIds = user.split(",");
					if (uIds.length > 0) {
						String[] uId = uIds[0].split("uid=");
						if (uId.length > 0) {
							List<az.ldap.zabbix.entity.User> persistUser = userService.findByEmail(uId[1]);
							if (persistUser != null && persistUser.size() == 1) {
								userIds.add(persistUser.get(0).getUserId());
							}
						}
					}
				}
				grps.setUserIds(userIds);
				userGroupService.update(grps);
			}
			LOGGER.debug("User loop started");
			for (User lUser : users) {
				List<az.ldap.zabbix.entity.User> userList = userService.findByEmail(lUser.getEmail());
				if (userList.size() == 0) {
					az.ldap.zabbix.entity.User user = new az.ldap.zabbix.entity.User();
					user.setAlias(lUser.getEmail());
					user.setEmail(lUser.getEmail());
					user.setMediaType("1");
					user.setName(lUser.getFirstName());
					user.setSurname(lUser.getLastName());
					if (lUser.getDescription().contains("ROOT_ADMIN")) {
						user.setType(3);
					} else {
						user.setType(2);
					}
					UserGroup grps = userGroupService.getByName(lUser.getDescription().split(",")[0]);
					List<UserGroup> userGroups = new ArrayList<UserGroup>();
					userGroups.add(grps);
					user.setUsergroupsList(userGroups);
					userService.syncSave(user);
				} else if (userList.size() > 1) {
					List<az.ldap.zabbix.entity.User> userLst = new ArrayList<az.ldap.zabbix.entity.User>();
					userLst = userList.subList(1, userList.size());
					userService.deleteAllUser(userLst);
				}
			}
			LOGGER.debug("user,host,group sync done");
			hostInterfaceService.findAllFromZabbixServer();
			List<Host> hosts = hostService.findAllFromZabbixServer();
			List<String> hostList = new ArrayList<String>();
			LOGGER.debug("Host sync started");
			for (Host host : hosts) {
				hostList.add(host.getHostId());
				List<Item> items = itemService.findAllFromZabbixServer(host.getHostId());
				String itemids = "";
				int i = 0;
				for (Item item : items) {
					if ((items.size() - 1) == i) {
						itemids += item.getItemId();
					} else {
						itemids += item.getItemId() + ",";
					}
					i++;
				}
				LOGGER.debug("Group item sync started : "+itemids);
				graphItemService.findAllFromZabbixServer(itemids);
				LOGGER.debug("Group sync started : "+host.getHostId());
				graphService.findAllFromZabbixServer(host.getHostId());
				LOGGER.debug("Host trigger sync started : "+host.getHostId());
				triggerService.findAllFromZabbixServer(host.getHostId());
				LOGGER.debug("History sync started : "+host.getHostId());
				historyService.syncEventService(host.getHostId());
			}
			LOGGER.debug("Host sync done");
			itemService.syncItemFromZabbixServer(hostList);
			triggerService.syncTriggerFromZabbixServer(hostList);
			List<Trigger> triggers = triggerService.findAll();
			List<String> triggerStatus = new ArrayList<String>();
			for (Trigger trigger : triggers) {
				triggerStatus.add(trigger.getTriggerid());
			}
			historyService.syncEventTrigger(triggerStatus);
			actionService.removeAllByTriggers(triggerStatus);
			graphItemService.syncGraphItemFromZabbixServer(hostList);
			graphService.syncGraphFromZabbixServer(hostList);
			historyService.syncEvent(hostList);
			if (hosts.size() == 0) {
				historyService.removeAllEvent("0");
			}
		} catch (Exception e) {
			LOGGER.debug("Zabbix sync error : " + e.getMessage());
			e.printStackTrace();
		}
		LOGGER.debug("Zabbix sync successfully completed");

	}

	@Override
	public LdapUser updatePassword(String name, String password) throws Exception {
		List<LdapUser> ldapUsers = ldapTemplate.findAll(LdapUser.class);
		for (LdapUser user : ldapUsers) {
			String email = user.getEmail();
			if (email.equals(name)) {
				user.setUserPassword(password.getBytes(Charset.forName("UTF-8")));
				ldapTemplate.update(user);
				return user;
			}
		}
		return new LdapUser();
	}

}