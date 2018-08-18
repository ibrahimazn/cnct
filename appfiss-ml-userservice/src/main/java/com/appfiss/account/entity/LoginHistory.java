package com.appfiss.account.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Login history tracking entity.
 *
 */
@Entity
@Table(name = "login_history")
@SuppressWarnings("serial")
@EntityListeners(AuditingEntityListener.class)
public class LoginHistory implements Serializable {

    /** Unique ID of the login user. */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /** User id of the login. */
    @Column(name = "user_id")
    private Long userId;
    
    /** Token details of the login user. */
    @Column(name = "login_token")
    private String loginToken;

    /** Already Login of the user. */
    @Column(name = "is_already_login")
    private Boolean isAlreadyLogin;
    
    /** Session id for websocket. */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * Get the id of LoginHistory.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of LoginHistory.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the userId of LoginHistory.
     *
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Set the userId of LoginHistory.
     *
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
	 * @return the loginToken
	 */
	public String getLoginToken() {
		return loginToken;
	}

	/**
	 * @param loginToken the loginToken to set
	 */
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	/**
     * Get the isAlreadyLogin of LoginHistory.
     *
     * @return the isAlreadyLogin
     */
    public Boolean getIsAlreadyLogin() {
        return isAlreadyLogin;
    }

    /**
     * Set the isAlreadyLogin of LoginHistory.
     *
     * @param isAlreadyLogin the isAlreadyLogin to set
     */
    public void setIsAlreadyLogin(Boolean isAlreadyLogin) {
        this.isAlreadyLogin = isAlreadyLogin;
    }

	/**
	 * Get the session id.
	 * 
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Set the session's id.
	 * 
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}  
}
