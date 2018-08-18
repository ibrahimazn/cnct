/**
 * 
 */
package com.assistanz.gatekeeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author azn0084
 *
 */
@RestController
public class TokenController {
	
	@Autowired
    private TokenStore tokenStore;

	@RequestMapping(value = "/oauth/token/revoke", method = RequestMethod.POST)
	@ResponseBody
	public void create(@RequestParam("token") String tokenValue) throws Exception {
		 OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
         tokenStore.removeAccessToken(accessToken);
	}
}
