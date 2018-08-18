/*
 *
 */
package com.appfiss.account.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.appfiss.account.util.PagingAndSorting;
import com.appfiss.account.dao.UserRepository;
import com.appfiss.account.entity.User;

/**
 * User actions and business related things will be handled by this
 * implementation class, each user related action will have definition.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	/** Inject user repository to access data object. */
	@Autowired
	private UserRepository userrepo;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User save(User user) throws Exception {
		LOGGER.info("record user save function initiated");
		
		LOGGER.debug("record user business related things");

		return userrepo.save(user);
	}

	@Override
	public User update(User user) throws Exception {
		return userrepo.save(user);
	}

	@Override
	public void delete(User user) throws Exception {
		userrepo.delete(user);
	}

	@Override
	public void delete(Long id) throws Exception {
		userrepo.delete(id);
	}

	@Override
	public User find(Long id) throws Exception {
		return userrepo.findOne(id);
	}

	@Override
	public Page<User> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return userrepo.findAll(pagingAndSorting.toPageRequest());
	}

	@Override
	public List<User> findAll() throws Exception {
		return (List<User>) userrepo.findAll();
	}

	@Override
	public Page<User> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception {
		return userrepo.findAllByIsActive(pagingAndSorting.toPageRequest(), isActive);
	}

	@Override
	public Page<User> findAllBySearchTextWithUserType(PagingAndSorting pagingAndSorting, String searchText,
			String userType) throws Exception {
		return userrepo.findBySearchText(pagingAndSorting.toPageRequest(), searchText, true, User.UserType.ADMIN);
	}

	@Override
	public Page<User> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception {
		return userrepo.findBySearchText(pagingAndSorting.toPageRequest(), searchText, true);
	}

	@Override
	public User softDelete(User user) throws Exception {
		return null;
	}

	@Override
	public List<User> findAllByIsActive() {
		return userrepo.findUserByActive(1);
	}

	@Override
	public User findByUserNameAndIsActive(String userName, int isActive) {
		return userrepo.findByNameAndIsActive(userName, 1);
	}

	@Override
	public User getCurrentLoggedInUser() throws Exception {
		return null;
	}

	@Override
	public User createFunction(Long userId, String namespace) throws Exception {
		return null;
	}

}
