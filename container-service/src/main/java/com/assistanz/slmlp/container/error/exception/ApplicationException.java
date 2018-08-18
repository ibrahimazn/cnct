/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.error.exception;

import com.assistanz.slmlp.container.util.error.Errors;

/**
 * An Exception class used to handle Application Exception.
 *
 */
public class ApplicationException extends Exception {

    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /** Errors object to get all the errors. */
    private Errors errors;

    /**
     * Constructor to display error message for Application Exception.
     *
     * @param errors to be displayed as exception message
     */
    public ApplicationException(Errors errors) {
        this.errors = errors;
    }

    /**
     * Get the errors.
     *
     * @return errors object
     */
    public Errors getErrors() {
        return this.errors;
    }

}
