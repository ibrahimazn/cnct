package com.appfiss.connector.k8s.exception;

/**
 * Kubectl exception handler.
 *
 */
@SuppressWarnings("serial")
public class KubeCtlException extends Exception {

    /**
     * Default constructor for Zabbix exception.
     */
    public KubeCtlException() {
        super();
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param message error message
     */
    public KubeCtlException(String message) {
       super(message);
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param message error message
     * @param cause object
     */
    public KubeCtlException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param cause object
     */
    public KubeCtlException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message error message
     * @param cause object
     * @param enableSuppression enable suppression
     * @param writableStackTrace writable stack trace
     */
    protected KubeCtlException(String message, Throwable cause,
           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
