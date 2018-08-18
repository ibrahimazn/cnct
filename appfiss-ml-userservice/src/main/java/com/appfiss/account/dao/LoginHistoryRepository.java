package com.appfiss.account.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.appfiss.account.entity.LoginHistory;
import com.appfiss.account.entity.User;

/**
 * Login history repository
 */
public interface LoginHistoryRepository extends PagingAndSortingRepository<LoginHistory, Long> {

	/**
	 * find by login token.
	 *
	 * @param loginToken
	 *            id
	 * @return Login history
	 */
	@Query(value = "SELECT login FROM LoginHistory login WHERE login.loginToken = :loginToken")
	LoginHistory findByLoginToken(@Param("loginToken") String loginToken);

	/**
	 * find by session id.
	 *
	 * @param sessionId
	 *            session id
	 * @return Login history
	 */
	@Query(value = "SELECT login FROM LoginHistory login WHERE login.sessionId = :sessionId")
	List<LoginHistory> findBySessionId(@Param("sessionId") String sessionId);

	/**
	 * Get Login History by User id.
	 *
	 * @param userId
	 *            user id
	 * @return LoginHistory
	 */
	@Query(value = "SELECT login FROM LoginHistory login WHERE login.userId = :userId")
	LoginHistory findByUserId(@Param("userId") Long userId);

	/**
	 * Get Login History by User id and already login.
	 *
	 * @param userId
	 *            user id
	 * @param isAlreadyLogin
	 *            already login
	 * @return LoginHistory
	 */
	@Query("SELECT login FROM LoginHistory login WHERE login.userId = :userId AND login.isAlreadyLogin = :isAlreadyLogin")
	List<LoginHistory> findByUserIdAndAlreadyLogin(@Param("userId") String userId,
			@Param("isAlreadyLogin") Boolean isAlreadyLogin);

	/**
	 * find by session id.
	 *
	 * @param sessionId
	 *            session id
	 * @return Login history
	 */
	@Query("SELECT login FROM LoginHistory login WHERE login.sessionId = :sessionId AND login.userId = :userId")
	List<LoginHistory> findBySessionIdAndUser(@Param("sessionId") String sessionId, @Param("userId") String userId);

	/**
	 * Get Login History by User id.
	 *
	 * @param userId
	 *            user id
	 * @return LoginHistory
	 */
	@Query("SELECT login FROM LoginHistory login WHERE login.userId = :userId")
	List<LoginHistory> findByUserId(@Param("userId") String userId);

	/**
	 * Get Login History by User id.
	 *
	 * @param userId
	 *            user id
	 * @return LoginHistory
	 */
	@Query("SELECT login FROM LoginHistory login WHERE login.userId IS NOT NULL AND login.sessionId IS NOT NULL")
	List<LoginHistory> findByUserAndSessionId();

}
