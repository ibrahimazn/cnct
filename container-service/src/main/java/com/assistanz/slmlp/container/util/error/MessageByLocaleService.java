/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.util.error;

/**
 * i18n key will be handled here to return message.
 *
 */
public interface MessageByLocaleService {

    /**
     * Get the message by key.
     *
     * @param id message key.
     * @return message value.
     */
    String getMessage(String id);

}
