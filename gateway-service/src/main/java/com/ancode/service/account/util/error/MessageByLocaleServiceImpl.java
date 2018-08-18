/*
 * 
 */
package com.ancode.service.account.util.error;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Message source service for get message by key.
 *
 */
@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

  /** Message source attribute for internationalization support. */
  @Autowired
  private MessageSource messageSource;

  @Override
  public String getMessage(String id) {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      String message = "";
      if (id.contains(" ")) {
        String[] errMsg = id.split(" ");
        for (String errKey : errMsg) {
          message += messageSource.getMessage(errKey, null, locale) + " ";
        }
        return message.trim();
      }
      return messageSource.getMessage(id, null, locale);
    } catch (NoSuchMessageException ex) {
      return id;
    } catch (NullPointerException ex) {
      // Do nothing, the i18n key will be returned
    }
    return id;
  }
}
