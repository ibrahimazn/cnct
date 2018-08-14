/*
 * 
 */
package com.appfiss.account.util;

/**
 * i18n key will be handled here to return message.
 *
 */
public interface MessageByLocaleService {

  /**
   * Get the message by key.
   *
   * @param id
   *          message key.
   * @return message value.
   */
  String getMessage(String id);

}
