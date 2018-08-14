package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.domain.entity.Account;
import az.ldap.sync.domain.entity.User;
import az.ldap.sync.service.LdapSyncService;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.UserGroup;
import az.ldap.zabbix.repository.UserGroupRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.usergroup.FilterRequest;
import az.zabbix.connector.usergroup.UserGroupCUDResponse;
import az.zabbix.connector.usergroup.UserGroupCUDResult;
import az.zabbix.connector.usergroup.UserGroupCreateRequest;
import az.zabbix.connector.usergroup.UserGroupDeleteRequest;
import az.zabbix.connector.usergroup.UserGroupListRequest;
import az.zabbix.connector.usergroup.UserGroupListResponse;
import az.zabbix.connector.usergroup.UserGroupParams;
import az.zabbix.connector.usergroup.UserGroupRequest;
import az.zabbix.connector.usergroup.UserGroupResponse;
import az.zabbix.connector.usergroup.UserGroupRights;
import az.zabbix.connector.usergroup.UserGroupUpdateRequest;

@Service
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	private UserGroupRepository userGroupRepo;
	
	/** Logger attribute. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupServiceImpl.class);

	@Value(value = "${zabbix.proxy}")
	private String hostUrl;
	
	@Autowired
	private LdapSyncService ldapSync;
	
	@Autowired
	private UserService userService;

	/** UserGroup group creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserGroupCreateRequest, UserGroupCUDResult> userGroupCreateReq;

	/** UserGroup group update Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserGroupUpdateRequest, UserGroupCUDResult> userGroupUpdateReq;

	/** UserGroup group delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserGroupDeleteRequest, UserGroupCUDResult> userGroupDeleteReq;

	/** UserGroup group lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserGroupListRequest, UserGroupListResponse> userGroupListReq;

	@Override
	public UserGroup save(UserGroup userGroup) throws Exception {
		UserGroupCUDResult userResponse = userGroupCreateReq.restCall(hostUrl, getCreateUserRequest(GenericConstants.AUTH, userGroup),
				UserGroupCUDResult.class);
		if (userResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, userResponse.getError().get("data"));
		}
		UserGroupCUDResponse userRes = userResponse.getResult();
		userGroup.setUsrgrpId(userRes.getUsrgrpids().get(0));
		return userGroupRepo.save(userGroup);
	}

	@Override
	public UserGroup update(UserGroup userGroup) throws Exception {
		if (userGroup.getUserIds().size() > 0) {
			List<az.ldap.zabbix.entity.User> users = userService.findByGroupId(userGroup.getUsrgrpId());
			if (users.size() > 0) {
				for (az.ldap.zabbix.entity.User user : users) {
					if (!userGroup.getUserIds().contains(user.getUserId())) {
						userService.delete(user.getUserId());
					}
				}
				UserGroupCUDResult userResponse = userGroupUpdateReq.restCall(hostUrl,
						getUpdateUserGroupRequest(GenericConstants.AUTH, userGroup), UserGroupCUDResult.class);
				if (userResponse.getError() != null) {
					throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
							userResponse.getError().get("data"));
				}
				UserGroupCUDResponse userRes = userResponse.getResult();
				userGroup.setUsrgrpId(userRes.getUsrgrpids().get(0));
				return userGroupRepo.save(userGroup);
			}
		}
		return userGroup;
	}

	@Override
	public void delete(UserGroup userGroup) throws Exception {
		UserGroupCUDResult userResponse = userGroupDeleteReq.restCall(hostUrl,
				getDeleteUserRequest(GenericConstants.AUTH, userGroup.getUsrgrpId()), UserGroupCUDResult.class);
		if (userResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, userResponse.getError().get("data"));
		}
		UserGroupCUDResponse userRes = userResponse.getResult();
		userGroup.setUsrgrpId(userRes.getUsrgrpids().get(0));
		userGroupRepo.delete(userGroup);
	}

	@Override
	public void delete(String id) throws Exception {
		UserGroupCUDResult userResponse = userGroupDeleteReq.restCall(hostUrl, getDeleteUserRequest(GenericConstants.AUTH, id),
				UserGroupCUDResult.class);
		if (userResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, userResponse.getError().get("data"));
		}
		UserGroupCUDResponse userRes = userResponse.getResult();
		UserGroup user = find(userRes.getUsrgrpids().get(0));
		user.setUsrgrpId(userRes.getUsrgrpids().get(0));
		userGroupRepo.delete(user);
	}

	@Override
	public UserGroup find(String id) throws Exception {
		UserGroup user = userGroupRepo.findByUserGroupId(id);
		return user;
	}

	@Override
	public List<UserGroup> findAll() throws Exception {
		return userGroupRepo.findAll();
	}

	@Override
	public Page<UserGroup> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return userGroupRepo.findAll(pagingAndSorting.toPageRequest());
	}

	public UserGroup getUserGroup(String usergrpId) throws Exception {
		UserGroupListResponse usersResponse = userGroupListReq.restCall(hostUrl, getUserGroupRequest(GenericConstants.AUTH, usergrpId),
				UserGroupListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserGroupResponse> userRes = usersResponse.getResult();
		if (userRes.size() > 0) {
			if (find(userRes.get(0).getUsrgrpid()) == null) {
				UserGroup user = userGroupEntityMapperUtil(userRes.get(0));
				return user;
			}
			return find(userRes.get(0).getUsrgrpid());
		}
		return null;
	}

	private UserGroupCreateRequest getCreateUserRequest(String authCode, UserGroup user) {
		UserGroupCreateRequest request = new UserGroupCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("usergroup.create");
		UserGroupParams userparams = new UserGroupParams();
		userparams.setName(user.getName());
		request.setParams(userparams);
		return request;
	}

	private UserGroupDeleteRequest getDeleteUserRequest(String authCode, String userGrpId) {
		UserGroupDeleteRequest request = new UserGroupDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("usergroup.delete");
		List<String> userGroups = new ArrayList<String>();
		userGroups.add(userGrpId);
		request.setParams(userGroups);
		return request;
	}

	private UserGroupListRequest getAllUserGroupRequest(String authCode) {
		UserGroupListRequest request = new UserGroupListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("usergroup.get");
		UserGroupRequest user = new UserGroupRequest();
		user.setOutput("extend");
		user.setSortfield("usrgrpid");
		user.setSortorder("ASC");
		request.setParams(user);
		return request;
	}

	private UserGroupListRequest getUserGroupRequest(String authCode, String usergroupId) {
		UserGroupListRequest request = new UserGroupListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("usergroup.get");
		UserGroupRequest user = new UserGroupRequest();
		user.setOutput("extend");
		user.setSortfield("usrgrpid");
		user.setSortorder("ASC");
		FilterRequest filter = new FilterRequest();
		List<String> groups = new ArrayList<String>();
		groups.add(usergroupId);
		filter.setUsergrpid(groups);
		user.setFilter(filter);
		request.setParams(user);
		return request;
	}
	
	private UserGroupListRequest getUserGroupRequestByName(String authCode, String name) {
		UserGroupListRequest request = new UserGroupListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("usergroup.get");
		UserGroupRequest user = new UserGroupRequest();
		user.setOutput("extend");
		user.setSortfield("usrgrpid");
		user.setSortorder("ASC");
		FilterRequest filter = new FilterRequest();
		List<String> names = new ArrayList<String>();
		names.add(name);
		filter.setName(names);
		user.setFilter(filter);
		request.setParams(user);
		return request;
	}

	private UserGroupUpdateRequest getUpdateUserGroupRequest(String authCode, UserGroup user) {
		UserGroupUpdateRequest request = new UserGroupUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("usergroup.update");
		UserGroupParams userparams = new UserGroupParams();
		userparams.setName(user.getName());
		userparams.setUsrgrpid(user.getUsrgrpId());
		userparams.setUserids(user.getUserIds());
		UserGroupRights rights = new UserGroupRights();
		rights.setId(user.getHostId());
		rights.setPermission(3);
		userparams.setRights(rights);
		request.setParams(userparams);
		return request;
	}

	public UserGroup userGroupEntityMapperUtil(UserGroupResponse usergroupResponse) throws Exception {
		UserGroup userGroup = null;
		if (usergroupResponse != null) {
			userGroup = new UserGroup();
			userGroup.setName(usergroupResponse.getName());
			userGroup.setUsrgrpId(usergroupResponse.getUsrgrpid());
		}
		return userGroup;
	}
	
	public UserGroup userGroupMapperUtil(Account account) throws Exception {
		UserGroup userGroup = null;
		if (account != null) {
			userGroup = new UserGroup();
			userGroup.setName(account.getName());
		}
		return userGroup;
	}

	public Map<String, UserGroup> convert(List<UserGroup> userList) {
		Map<String, UserGroup> userGrpMap = new HashMap<String, UserGroup>();
		for (UserGroup userGroup : userList) {
			userGrpMap.put(userGroup.getUsrgrpId(), userGroup);
		}
		return userGrpMap;
	}
	
	public Map<String, UserGroup> convertUser(List<UserGroup> userList) {
		Map<String, UserGroup> userGrpMap = new HashMap<String, UserGroup>();
		for (UserGroup userGroup : userList) {
			userGrpMap.put(userGroup.getName(), userGroup);
		}
		return userGrpMap;
	}

	@Override
	public List<UserGroup> findAllFromZabbixServer() throws Exception {
		UserGroupListResponse usersResponse = userGroupListReq.restCall(hostUrl, getAllUserGroupRequest(GenericConstants.AUTH),
				UserGroupListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserGroupResponse> userRes = usersResponse.getResult();
		List<UserGroup> users = new ArrayList<UserGroup>();
		for (UserGroupResponse user : userRes) {
			users.add(userGroupEntityMapperUtil(user));
		}
		HashMap<String, UserGroup> csUserMap = (HashMap<String, UserGroup>) convert(users);
		List<UserGroup> appUserList = userGroupRepo.findAll();
		for (UserGroup user : appUserList) {
			if (csUserMap.containsKey(user.getUsrgrpId())) {
				UserGroup csUser = csUserMap.get(user.getUsrgrpId());
				user.setName(csUser.getName());
				user.setUsrgrpId(csUser.getUsrgrpId());
				userGroupRepo.save(user);
				csUserMap.remove(user.getUsrgrpId());
			} else {
				userGroupRepo.delete(user);
			}
		}
		for (String key : csUserMap.keySet()) {
			userGroupRepo.save(csUserMap.get(key));
		}
		return findAll();
	}

	@Override
	public void deleteUserGroup(UserGroup userGroup) throws Exception {
		userGroupRepo.delete(userGroup);
	}

	@Override
	public void deleteAllUserGroup(List<UserGroup> userGroup) throws Exception {
		userGroupRepo.delete(userGroup);
	}

	@Override
	public List<UserGroup> SyncUserGroup() throws Exception {
		UserGroupListResponse usersResponse = userGroupListReq.restCall(hostUrl, getAllUserGroupRequest(GenericConstants.AUTH),
				UserGroupListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserGroupResponse> userRes = usersResponse.getResult();
		List<UserGroup> userLists = new ArrayList<UserGroup>();
		for (UserGroupResponse user : userRes) {
			userLists.add(userGroupEntityMapperUtil(user));
		}
		List<UserGroup> users = new ArrayList<UserGroup>();
		List<Account>  userList = ldapSync.getAccountList();
		for (Account account : userList) {
			users.add(userGroupMapperUtil(account));
		}
		HashMap<String, UserGroup> csUserMap = (HashMap<String, UserGroup>) convertUser(users);
		for (UserGroup user : userLists) {
			if (csUserMap.containsKey(user.getName())) {
				update(user);
				csUserMap.remove(user.getName());
			} else {
				delete(user);
			}
		}
		for (String key : csUserMap.keySet()) {
			save(csUserMap.get(key));
		}
		return findAllFromZabbixServer();
	}

	@Override
	public UserGroup getByName(String name) throws Exception {
		return userGroupRepo.findByUserGroupName(name);
	}

	@Override
	public UserGroup findByHost(String hostId) throws Exception {
		return userGroupRepo.findByHostId(hostId);
	}

	@Override
	public UserGroup syncSave(UserGroup userGroup) throws Exception {
		UserGroupListResponse usersResponse = userGroupListReq.restCall(hostUrl, getUserGroupRequestByName(GenericConstants.AUTH,userGroup.getName()),
				UserGroupListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserGroupResponse> userRes = usersResponse.getResult();
		List<UserGroup> userLists = new ArrayList<UserGroup>();
		for (UserGroupResponse user : userRes) {
			UserGroup usergrp = userGroupEntityMapperUtil(user);
			usergrp.setType(userGroup.getType());
			userLists.add(usergrp);
		}
		if (userLists.size() == 0) {
			return save(userGroup);
		}
		if (find(userLists.get(0).getUsrgrpId()) != null) {
			return find(userLists.get(0).getUsrgrpId());
		}
		return userGroupRepo.save(userLists).get(0);
	}

	@Override
	public void removeUserGroup(String name) throws Exception {
		LOGGER.debug("user group remove"+ name);
		LOGGER.debug("user group auth"+ GenericConstants.AUTH);
		UserGroupListResponse usersResponse = userGroupListReq.restCall(hostUrl, getUserGroupRequestByName(GenericConstants.AUTH, name),
				UserGroupListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserGroupResponse> userRes = usersResponse.getResult();
		if (userRes.size() > 0) {
			delete(userRes.get(0).getUsrgrpid());
		} else {
			UserGroup usr = getByName(name);
			if (usr != null) {
				userGroupRepo.delete(usr);
			}
		}
	}

	@Override
	public List<UserGroup> findAllByType(String type) throws Exception {
		return userGroupRepo.findByType(type);
	}
  }
