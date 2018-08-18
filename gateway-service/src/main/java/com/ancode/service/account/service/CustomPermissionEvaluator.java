/*
 *
 */
package com.ancode.service.account.service;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * A role permission implementation that uses a Map to check whether a domain
 * Object and access level exists for a particular user.
 *
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

  /** Logger attribute. */
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

  /** Constant for role id. */
  public static final String ROLE_ID = "roleId";

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    try {
      String permissionData = permission.toString();

      if (authentication == null) {
        return false;
      }
      if (targetDomainObject != null) {
        Integer projectId = (Integer) targetDomainObject;
        if (projectId != null) {
          permissionData = permissionData + projectId.toString();
        }
      }

      for (GrantedAuthority grantedAuth : authentication.getAuthorities()) {
        if (grantedAuth.getAuthority().equals(permissionData)) {
          return true;
        }
      }

    } catch (NumberFormatException e) {
      LOGGER.error("ERROR AT PERMISSION CHECK", e);
    } catch (Exception e) {
      LOGGER.error("ERROR AT PERMISSION CHECK", e);
    }
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
      Object permission) {
    throw new UnsupportedOperationException();
  }
}
