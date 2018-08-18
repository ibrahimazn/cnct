package com.appfiss.account.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Token Service.
 */
@Component
public class TokenService {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    /** REST API auth token constant. */
    private static final Cache REST_API_AUTH_TOKEN = CacheManager.getInstance().getCache("restApiAuthTokenCache");

    /**
     * Evicting expired tokens.
     *
     */
    public void evictExpiredTokens() {
        LOGGER.info("Evicting expired tokens");
        REST_API_AUTH_TOKEN.evictExpiredElements();
    }

    /**
     * Store token.
     *
     * @param token to set
     * @param authentication to set
     */
    public void store(String token, String key) {
        REST_API_AUTH_TOKEN.put(new Element(token, key));
    }

    /**
     * Check token already exists.
     *
     * @param token to set
     * @return true/false.
     */
    public boolean contains(String token) {
        return REST_API_AUTH_TOKEN.get(token) != null;
    }

    /**
     * Get the authentication token.
     *
     * @param token to set
     * @return Authentication status object
     * @throws Exception unhandled exceptions.
     */
    public String retrieve(String token) throws Exception {
        return (String) REST_API_AUTH_TOKEN.get(token).getObjectValue();
    }
}
