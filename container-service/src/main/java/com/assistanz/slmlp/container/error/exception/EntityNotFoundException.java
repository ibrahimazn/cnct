/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.error.exception;

/**
 * An Exception class used to handle Entity Not Found Exception.
 *
 */
public class EntityNotFoundException extends Exception {

    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to display error message for Entity Not Found Exception.
     *
     * @param message to be displayed as exception message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
