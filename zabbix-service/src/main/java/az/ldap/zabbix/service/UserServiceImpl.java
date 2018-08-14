package az.ldap.zabbix.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import az.ldap.sync.constants.GenericConstants;
import az.ldap.sync.service.LdapSyncService;
import az.ldap.sync.service.LdapSyncServiceImpl;
import az.ldap.sync.util.CustomGenericException;
import az.ldap.zabbix.entity.User;
import az.ldap.zabbix.entity.UserGroup;
import az.ldap.zabbix.repository.UserRepository;
import az.zabbix.connector.RestTemplateCall;
import az.zabbix.connector.user.FilterRequest;
import az.zabbix.connector.user.Media;
import az.zabbix.connector.user.UserCUDResponse;
import az.zabbix.connector.user.UserCUDResult;
import az.zabbix.connector.user.UserCreateRequest;
import az.zabbix.connector.user.UserDeleteRequest;
import az.zabbix.connector.user.UserGroupRequest;
import az.zabbix.connector.user.UserListRequest;
import az.zabbix.connector.user.UserListResponse;
import az.zabbix.connector.user.UserParams;
import az.zabbix.connector.user.UserRequest;
import az.zabbix.connector.user.UserResponse;
import az.zabbix.connector.user.UserUpdateRequest;
import az.zabbix.connector.util.RandomPasswordGenerator;

@Service
public class UserServiceImpl implements UserService {

	@Value(value = "${zabbix.proxy}")
	private String hostUrl;
	
	/** Logger attribute. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private LdapSyncService ldapSync;

	/** User creation Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserCreateRequest, UserCUDResult> userCreateReq;

	/** User update Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserUpdateRequest, UserCUDResult> userUpdateReq;

	/** User delete Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserDeleteRequest, UserCUDResult> userDeleteReq;

	/** User lists Connection processor reference. */
	@Autowired
	private RestTemplateCall<UserListRequest, UserListResponse> userListReq;

	@Override
	public User save(User user) throws Exception {
		UserCUDResult userResponse = userCreateReq.restCall(hostUrl, getCreateUserRequest(GenericConstants.AUTH, user),
				UserCUDResult.class);
		if (userResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, userResponse.getError().get("data"));
		}
		UserCUDResponse userRes = userResponse.getResult();
		user.setUserId(userRes.getUserids().get(0));
		user.setIsActive(true);
		return userRepo.save(user);
	}

	@Override
	public User update(User user) throws Exception {
		if (find(user.getUserId()) != null) {
			UserCUDResult userResponse = userUpdateReq.restCall(hostUrl, getUpdateUserRequest(GenericConstants.AUTH, user),
					UserCUDResult.class);
			if (userResponse.getError() != null) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, userResponse.getError().get("data"));
			}
			UserCUDResponse userRes = userResponse.getResult();
			user = find(user.getUserId());
			user.setUserId(userRes.getUserids().get(0));
			return userRepo.save(user);
		} else {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "No users doesn't exist");

		}
	}

	@Override
	public void delete(User user) throws Exception {
		if (find(user.getUserId()) != null && !user.getAlias().equals("Admin") && !user.getAlias().equals("guest") && !user.getAlias().equals("basic@monitor")) {
			UserCUDResult userResponse = userDeleteReq.restCall(hostUrl, getDeleteUserRequest(GenericConstants.AUTH, user.getUserId()),
					UserCUDResult.class);
			if (userResponse.getError() != null) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, userResponse.getError().get("data"));
			}
			UserCUDResponse userRes = userResponse.getResult();
			user = find(user.getUserId());
			user.setUserId(userRes.getUserids().get(0));
			user.setIsActive(false);
			userRepo.delete(user);
		} else if(!user.getAlias().equals("Admin") && !user.getAlias().equals("guest") && !user.getAlias().equals("basic@monitor")) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "No users doesn't exist");
		}
	}

	@Override
	public void delete(String id) throws Exception {
		if (find(id) != null) {
			UserCUDResult userResponse = userDeleteReq.restCall(hostUrl, getDeleteUserRequest(GenericConstants.AUTH, id),
					UserCUDResult.class);
			if (userResponse.getError() != null) {
				throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, userResponse.getError().get("data"));
			}
			UserCUDResponse userRes = userResponse.getResult();
			User user = find(userRes.getUserids().get(0));
			user.setUserId(userRes.getUserids().get(0));
			user.setIsActive(false);
			userRepo.delete(user);
		} else {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, "No users doesn't exist");
		}
	}

	@Override
	public User find(String id) throws Exception {
		User user = userRepo.findByUserId(id);
		return user;
	}

	public User getUser(String userId) throws Exception {
		UserListResponse usersResponse = userListReq.restCall(hostUrl, getUserRequest(GenericConstants.AUTH, userId),
				UserListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserResponse> userRes = usersResponse.getResult();
		if (userRes.size() > 0) {
			if (find(userRes.get(0).getUserid()) == null) {
				User user = userEntityMapperUtil(userRes.get(0));
				return user;
			}
			return find(userRes.get(0).getUserid());
		}
		return null;
	}

	@Override
	public List<User> findAll() throws Exception {
		return userRepo.findAll();
	}

	@Override
	public Page<User> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return userRepo.findAll(pagingAndSorting.toPageRequest());
	}

	private UserCreateRequest getCreateUserRequest(String authCode, User user) {
		UserCreateRequest request = new UserCreateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("user.create");
		UserParams userparams = new UserParams();
		userparams.setAlias(user.getAlias());
		userparams.setLang(user.getLanguage());
		userparams.setName(user.getName());
		userparams.setSurname(user.getSurname());
		userparams.setType(user.getType());
		userparams.setPasswd(new String(RandomPasswordGenerator.generatePswd(6, 10, 1, 1, 1)));
		List<UserGroupRequest> grps = new ArrayList<>();
		for(UserGroup usrGroup: user.getUsergroupsList()){
			UserGroupRequest userGroup = new UserGroupRequest();
			userGroup.setUsrgrpid(usrGroup.getUsrgrpId());
			grps.add(userGroup);
		}
		userparams.setUsrgrps(grps);
		Media media = new Media();
		media.setActive(0);
		media.setMediatypeid("1");
		media.setSendto(user.getEmail());
		media.setSeverity(63);
		media.setPeriod("1-7,00:00-24:00");
		List<Media> medias = new ArrayList<Media>();
		medias.add(media);
		userparams.setUser_medias(medias);
		request.setParams(userparams);
		return request;
	}

	private UserDeleteRequest getDeleteUserRequest(String authCode, String userId) {
		UserDeleteRequest request = new UserDeleteRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("user.delete");
		List<String> users = new ArrayList<String>();
		users.add(userId);
		request.setParams(users);
		return request;
	}

	private UserListRequest getAllUserRequest(String authCode) {
		UserListRequest request = new UserListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("user.get");
		UserRequest user = new UserRequest();
		user.setOutput("extend");
		user.setSortfield("userid");
		user.setSortorder("ASC");
		request.setParams(user);
		return request;
	}

	private UserListRequest getUserRequest(String authCode, String userId) {
		UserListRequest request = new UserListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("user.get");
		UserRequest user = new UserRequest();
		user.setOutput("extend");
		user.setSortfield("userid");
		user.setSortorder("ASC");
		FilterRequest filter = new FilterRequest();
		filter.setUserid(userId);
		user.setSearch(filter);
		request.setParams(user);
		return request;
	}
	
	private UserListRequest getUserRequestByName(String authCode, String name) {
		UserListRequest request = new UserListRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("user.get");
		UserRequest user = new UserRequest();
		user.setOutput("extend");
		user.setStartSearch(true);
		FilterRequest filter = new FilterRequest();
		filter.setAlias(name);
		user.setSearch(filter);
		request.setParams(user);
		return request;
	}

	private UserUpdateRequest getUpdateUserRequest(String authCode, User user) {
		UserUpdateRequest request = new UserUpdateRequest();
		request.setAuth(authCode);
		request.setId(1L);
		request.setJsonrpc("2.0");
		request.setMethod("user.update");
		UserParams userparams = new UserParams();
		userparams.setUserid(user.getUserId());
		userparams.setAlias(user.getAlias());
		userparams.setLang(user.getLanguage());
		userparams.setName(user.getName());
		userparams.setSurname(user.getSurname());
		userparams.setType(user.getType());
		request.setParams(userparams);
		return request;
	}

	public User userEntityMapperUtil(UserResponse userResponse) throws Exception {
		User user = null;
		if (userResponse != null) {
			user = new User();
			user.setAlias(userResponse.getAlias());
			user.setUserId(userResponse.getUserid());
			user.setName(userResponse.getName());
			user.setType(Integer.valueOf(userResponse.getType()));
			user.setSurname(userResponse.getSurname());
			user.setLanguage(userResponse.getLang());
			user.setEmail(userResponse.getAlias());
		}
		return user;
	}
	
	public User userMapperUtil(az.ldap.sync.domain.entity.User userResponse) throws Exception {
		User user = null;
		if (userResponse != null) {
			user = new User();
			user.setAlias(userResponse.getEmail());
			user.setName(userResponse.getUserName());
			user.setSurname(userResponse.getLastName());
			user.setEmail(userResponse.getEmail());
			user.setLanguage(userResponse.getLanguage());
			String[] accounts = userResponse.getDescription().split(",");
			List<UserGroup> userGroupList = new ArrayList<UserGroup>();
			UserGroup userGroup = userGroupService.getByName(accounts[0]);
			userGroupList.add(userGroup);
			UserGroup userGroups = userGroupService.getByName(userResponse.getAccount());
			userGroupList.add(userGroups);
			user.setUsergroupsList(userGroupList);
			if (accounts[1].equalsIgnoreCase("root_admin")) {
				user.setType(3);
			} else if (accounts[1].equalsIgnoreCase("domain_admin")) {
				user.setType(2);
			} else {
				user.setType(1);
			}
			user.setIsActive(true);
		}
		return user;
	}

	public Map<String, User> convert(List<User> userList) {
		Map<String, User> userMap = new HashMap<String, User>();
		for (User user : userList) {
			userMap.put(user.getUserId(), user);
		}
		return userMap;
	}
	
	public Map<String, User> convertUser(List<User> userList) {
		Map<String, User> userMap = new HashMap<String, User>();
		for (User user : userList) {
			userMap.put(user.getAlias(), user);
		}
		return userMap;
	}

	@Override
	public List<User> findAllFromZabbixServer() throws Exception {
		UserListResponse usersResponse = userListReq.restCall(hostUrl, getAllUserRequest(GenericConstants.AUTH), UserListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserResponse> userRes = usersResponse.getResult();
		List<User> users = new ArrayList<User>();
		for (UserResponse user : userRes) {
			users.add(userEntityMapperUtil(user));
		}
		HashMap<String, User> csUserMap = (HashMap<String, User>) convert(users);
		List<User> appUserList = userRepo.findAll();
		for (User user : appUserList) {
			if (csUserMap.containsKey(user.getUserId())) {
				User csUser = csUserMap.get(user.getUserId());
				user.setAlias(csUser.getAlias());
				user.setEmail(csUser.getEmail());
				user.setIsActive(true);
				user.setLanguage(csUser.getLanguage());
				user.setName(csUser.getName());
				user.setSurname(csUser.getSurname());
				user.setType(csUser.getType());
				user.setUserId(csUser.getUserId());
				userRepo.save(user);
				csUserMap.remove(user.getUserId());
			} else {
				userRepo.delete(user);
			}

		}
		for (String key : csUserMap.keySet()) {
			userRepo.save(csUserMap.get(key));
		}
		return findAll();
	}

	@Override
	public void deleteUser(User user) throws Exception {
		userRepo.delete(user);
	}

	@Override
	public void deleteAllUser(List<User> user) throws Exception {
		userRepo.delete(user);
	}

	@Override
	public List<User> SyncUser() throws Exception {
		LOGGER.info("User Sync"+ hostUrl);
		LOGGER.info("Auth Token"+ GenericConstants.AUTH);
		UserListResponse usersResponse = userListReq.restCall(hostUrl, getAllUserRequest(GenericConstants.AUTH), UserListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserResponse> userRes = usersResponse.getResult();
		List<User> userLists = new ArrayList<User>();
		for (UserResponse user : userRes) {
			userLists.add(userEntityMapperUtil(user));
		}
		List<User> users = new ArrayList<User>();
		List<User> existUsers = userRepo.findAll();
		List<az.ldap.sync.domain.entity.User>  userList = ldapSync.getUserList();
		for (az.ldap.sync.domain.entity.User usr : userList) {
			for (User usrs : existUsers) {
				if (usrs.getAlias().equals(usr.getEmail())) {
					users.add(userMapperUtil(usr));
				}
			}
		}
		HashMap<String, User> csUserMap = (HashMap<String, User>) convertUser(users);
		for (User user : userLists) {
			if (csUserMap.containsKey(user.getAlias())) {
				update(user);
				LOGGER.info("updated user "+ user.getEmail());
				csUserMap.remove(user.getAlias());
			} else if(csUserMap.size() == 0){
				LOGGER.info("added user "+ user.getEmail());
				save(user);
			} else if(csUserMap.size() > 0){
				LOGGER.info("deleted user "+ user.getEmail());
				delete(user);
			}
		}
		for (String key : csUserMap.keySet()) {
			LOGGER.info("added user "+ csUserMap.get(key).getEmail());
			save(csUserMap.get(key));
		}
		return findAllFromZabbixServer();
	}

	@Override
	public List<User> findByEmail(String email) throws Exception {
		return userRepo.findByUserEmail(email);
	}

	@Override
	public List<User> findByGroupId(String groupId) throws Exception {
		UserGroup group = userGroupService.find(groupId);
		Query query = new Query();
		if (group != null) {
			query.addCriteria(Criteria.where("usergroupsList.$id").is(new ObjectId(group.getId())));
		}
		List<User> users = mongoTemplate.find(query, User.class);
        return users;
	}

	@Override
	public User syncSave(User user) throws Exception {
		UserListResponse usersResponse = userListReq.restCall(hostUrl, getUserRequestByName(GenericConstants.AUTH, user.getAlias()), UserListResponse.class);
		if (usersResponse.getError() != null) {
			throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED, usersResponse.getError().get("data"));
		}
		List<UserResponse> userRes = usersResponse.getResult();
		List<User> userLists = new ArrayList<User>();
		for (UserResponse users : userRes) {
			userLists.add(userEntityMapperUtil(users));
		}
		if (userLists.size() == 0) {
			return save(user);
		}
		return userRepo.save(userLists).get(0);
	}
}
