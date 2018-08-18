/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.util.error;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores and exposes information about data-binding and validation errors of a specific object.
 *
 */
public class Errors {

    /** Message source attribute for internationalization support. */
    private MessageSource messageSource;

    /** Global error list attribute. */
    private List<String> globalError = new ArrayList<String>();

    /** Field errors attribute. */
    private HashMap<String, String> fieldErrors = new HashMap<String, String>();

    /**
     * Parameterized constructor.
     *
     * @param messageSource to set
     */
    public Errors(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get global error.
     *
     * @return error list
     */
    public List<String> getGlobalError() {
        return this.globalError;
    }

    /**
     * Add global error.
     *
     * @param errorMessage to set.
     */
    public void addGlobalError(String errorMessage) {
        try {
            errorMessage = messageSource.getMessage(errorMessage, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException ex) {
            this.globalError.add(errorMessage);
        } catch (NullPointerException ex) {
            // Do nothing, the i18n key will be returned
        }
        this.globalError.add(errorMessage);
    }

    /**
     * Get field errors.
     *
     * @return map of field errors
     */
    public HashMap<String, String> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * Add field errors.
     *
     * @param field to set
     * @param errorMessage to set
     */
    public void addFieldError(String field, String errorMessage) {
        try {
            errorMessage = messageSource.getMessage(errorMessage, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException ex) {
            this.fieldErrors.put(field, errorMessage);
        } catch (NullPointerException ex) {
            // Do nothing, the i18n key will be returned
        }
        this.fieldErrors.put(field, errorMessage);
    }

    /**
     * Set field errors.
     *
     * @param errorsMap to set
     */
    public void setFieldErrors(Map<String, String> errorsMap) {
        this.fieldErrors.putAll(errorsMap);
    }

    /**
     * Check we have global and field errors.
     *
     * @return true/false
     */
    public Boolean hasErrors() {
        return !(fieldErrors.isEmpty() && globalError.isEmpty());
    }

    @Override
    public String toString() {
      return "Errors [messageSource=" + messageSource + ", globalError=" + globalError + ", fieldErrors=" + fieldErrors
          + "]";
    }
}
