package az.openfaas.connector;

/**
 * Zabbix exception handler.
 *
 */
@SuppressWarnings("serial")
public class OpenFaasException extends Exception {

    /**
     * Default constructor for Zabbix exception.
     */
    public OpenFaasException() {
        super();
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param message error message
     */
    public OpenFaasException(String message) {
       super(message);
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param message error message
     * @param cause object
     */
    public OpenFaasException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param cause object
     */
    public OpenFaasException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message error message
     * @param cause object
     * @param enableSuppression enable suppression
     * @param writableStackTrace writable stack trace
     */
    protected OpenFaasException(String message, Throwable cause,
           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
