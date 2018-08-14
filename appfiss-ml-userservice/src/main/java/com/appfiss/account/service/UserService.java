/*
 *
 */
package com.appfiss.account.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.appfiss.account.entity.User;
import com.appfiss.account.util.PagingAndSorting;

/**
 * Service Interface for the User related actions like create user, delete user,
 * edit user, search user, etc.,.
 */
public interface UserService extends CRUDService<User> {

	/**
	 * Find all Active User lists with Pagination.
	 *
	 * @param pagingAndSorting
	 *            pagination and sorting
	 * @param isActive
	 *            isActive status
	 * @return active user list
	 * @throws Exception
	 *             if error occurs
	 */
	Page<User> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception;

	/**
	 * Find all user list by user and search text.
	 *
	 * @param pagingAndSorting
	 *            paging and sorting
	 * @param searchText
	 *            search text
	 * @param userType
	 *            the user type
	 * @return user list
	 * @throws Exception
	 *             if error occurs
	 */
	Page<User> findAllBySearchTextWithUserType(PagingAndSorting pagingAndSorting, String searchText, String userType)
			throws Exception;

	/**
	 * Find all user list by user and search text.
	 *
	 * @param pagingAndSorting
	 *            paging and sorting
	 * @param searchText
	 *            search text
	 * @return user list
	 * @throws Exception
	 *             if error occurs
	 */
	Page<User> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception;

	/**
	 * Soft deleting by selected user.
	 *
	 * @param user
	 *            user.
	 * @return deleted user.
	 * @throws Exception
	 *             if error occurs.
	 */
	User softDelete(User user) throws Exception;

	/**
	 * Method tolist all the availvle active Users.
	 *
	 * @return users list
	 */
	List<User> findAllByIsActive();

	/**
	 * Find by user name and is active.
	 *
	 * @param userName
	 *            the user name
	 * @param isActive
	 *            the is active
	 * @return the user
	 */
	User findByUserNameAndIsActive(String userName, int isActive);

	/**
	 * Gets the current logged in user.
	 *
	 * @return the current logged in user
	 * @throws Exception
	 *             the exception
	 */
	User getCurrentLoggedInUser() throws Exception;

	/**
	 * Creates the function.
	 *
	 * @return the user
	 * @throws Exception
	 *             the exception
	 */
	User createFunction(Long userId, String namespace) throws Exception;
}
