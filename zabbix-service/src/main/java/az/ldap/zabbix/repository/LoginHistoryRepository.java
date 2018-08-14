package az.ldap.zabbix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import az.ldap.zabbix.entity.LoginHistory;


/**
 * Login history repository
 */
public interface LoginHistoryRepository extends MongoRepository<LoginHistory, String>{

    /**
     * Get Login History by User id and already login.
     *
     * @param userId user id
     * @param isAlreadyLogin already login
     * @return LoginHistory
     */
    @Query("{userId : ?0, isAlreadyLogin : ?1 }")
    List<LoginHistory> findByUserIdAndAlreadyLogin(@Param("userId") String userId, @Param("isAlreadyLogin") Boolean isAlreadyLogin);

    /**
     * find by login token.
     *
     * @param loginToken id
     * @return Login history
     */
    @Query("{loginToken : ?0, hostId : ?1, sessionId : ?2}")
    LoginHistory findByLoginToken(@Param("loginToken") String loginToken, @Param("hostId") String hostId, @Param("sessionId") String sessionId);

    /**
     * find by session id.
     *
     * @param sessionId session id
     * @return Login history
     */
    @Query("{sessionId : ?0}")
    List<LoginHistory> findBySessionId(@Param("sessionId") String sessionId);
    
    /**
     * find by session id.
     *
     * @param sessionId session id
     * @return Login history
     */
    @Query("{sessionId : ?0, userId : ?1}")
    List<LoginHistory> findBySessionIdAndUser(@Param("sessionId") String sessionId, @Param("userId") String userId);

    /**
     * Get Login History by User id.
     *
     * @param userId user id
     * @return LoginHistory
     */
    @Query("{userId : ?0}")
    List<LoginHistory> findByUserId(@Param("userId") String userId);
    
    /**
     * Get Login History by host id.
     *
     * @param hostId user id
     * @return LoginHistory
     */
    @Query("{ 'hostId' : {'$exists':'true'} }")
    List<LoginHistory> findByHost();
    
    
    /**
     * Get Login History by User id.
     *
     * @param userId user id
     * @return LoginHistory
     */
    @Query("{ 'userId' : {'$exists':'true'}, 'sessionId': {'$exists':'true'} }")
    List<LoginHistory> findByUserAndSessionId();
    
    /**
     * Get Login History by User id.
     *
     * @param userId user id
     * @return LoginHistory
     */
    @Query("{userId : ?0, hostId : ?1}")
    LoginHistory findByUserIdAndHost(@Param("userId") String userId, @Param("hostId") String hostId);

}
