package com.appfiss.account.service;

import java.util.List;

import com.appfiss.account.entity.LoginHistory;
import com.appfiss.account.entity.User;

/**
 * Login history service.
 *
 */
public interface LoginHistoryService extends CRUDService<LoginHistory> {
	/**
	 * Set the login details history to database.
	 *
	 * @param userName
	 *            user name of user
	 * @param password
	 *            password of user
	 * @param domain
	 *            domain of user
	 * @param rememberMe
	 *            true of false
	 * @param loginToken
	 *            login token
	 * @return LoginHistory
	 * @throws Exception
	 *             unhandled exception
	 */
	LoginHistory saveLoginDetails(String userName, String password, String loginToken) throws Exception;

	/**
	 * find by login token and is already login.
	 *
	 * @param userId
	 *            user id
	 * @param isAlreadyLogin
	 *            is already login
	 * @return Login history
	 */
	List<LoginHistory> findByUserIdAndAlreadyLogin(String userId, Boolean isAlreadyLogin);

	/**
	 * find by login token and is already login.
	 *
	 * @param userId
	 *            user id
	 * @return Login history
	 */
	List<LoginHistory> findByUserId(String userId);


	/**
	 * find by session id.
	 *
	 * @param sessionId
	 *            session id
	 * @return Login history
	 */
	List<LoginHistory> findBySessionId(String sessionId);

	/**
	 * find by session id.
	 *
	 * @param sessionId
	 *            session id
	 * @return Login history
	 */
	List<LoginHistory> findBySessionIdAndUser(String sessionId, String userId);

}
