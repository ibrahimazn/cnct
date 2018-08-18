/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.util;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import com.assistanz.slmlp.container.error.exception.ApplicationException;
import com.assistanz.slmlp.container.error.exception.CustomGenericException;
import com.assistanz.slmlp.container.util.error.Errors;
import com.assistanz.slmlp.container.util.error.MessageByLocaleService;
import com.assistanz.slmlp.container.util.error.MessageByLocaleServiceImpl;

import org.springframework.http.HttpStatus;

/**
 * Generic Exception handling controller.
 *
 */
public class ExceptionHandlingController {

    /** Message source attribute. */
    @Autowired
    private MessageSource messageSource;

    /** Message local service. */
    @Autowired
    private MessageByLocaleService messageByLocaleService;

    /**
     * Handle if entity is not found.
     *
     * @param ex exception to handle
     * @return errors
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    Errors handleException(EntityNotFoundException ex) {
        Errors errors = new Errors(messageSource);
        messageByLocaleService = new MessageByLocaleServiceImpl();
        errors.addGlobalError(ex.getMessage());
        return errors;
    }

    /**
     * Custom generic exception handler.
     *
     * @param ex the exception to be handle
     * @return errors
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    Errors handleException(CustomGenericException ex) {
        Errors errors = new Errors(messageSource);
        errors.addGlobalError(messageByLocaleService.getMessage(ex.getErrorMsg()));
        return errors;
    }

    /**
     * Handle any precondition.
     *
     * @param ex exception to handle
     * @return errors
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    Errors handleException(ApplicationException ex) {
        return ex.getErrors();
    }

    /**
     * Handle transaction related exception.
     *
     * @param ex exception to handle
     * @return errors
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    Errors handleException(TransactionSystemException ex) {
        Errors errors = new Errors(messageSource);
        errors.addGlobalError(ex.getRootCause().getMessage());
        return errors;
    }

    /**
     * Generic checked exception handler.
     *
     * @param ex
     *          the exception to handle
     * @return errors
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    Errors handleException(Exception ex) {
      ex.printStackTrace();
      Errors errors = new Errors(messageSource);
      messageByLocaleService = new MessageByLocaleServiceImpl();
      errors.addGlobalError(messageByLocaleService.getMessage(ex.getMessage()));
      return errors;
    }

    /**
     * Generic checked exception handler.
     *
     * @param ex the exception to handle
     * @return errors
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    Errors handleException(RestClientException ex) {
        ex.printStackTrace();
        Errors errors = new Errors(messageSource);
        messageByLocaleService = new MessageByLocaleServiceImpl();
        errors.addGlobalError(messageByLocaleService.getMessage(ex.getMessage()));
        return errors;
    }

    /**
     * Handler for optimistic lock exception.
     *
     * @param ex the exception to handle
     * @return errors
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_FAILURE)
    @ResponseBody
    Errors handleException(OptimisticLockException ex) {
        Errors errors = new Errors(messageSource);
        errors.addGlobalError("error.javax.optimistic.Exception");
        return errors;
    }

}
