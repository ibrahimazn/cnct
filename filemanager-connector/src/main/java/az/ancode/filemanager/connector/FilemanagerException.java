package az.ancode.filemanager.connector;

/**
 * Zabbix exception handler.
 *
 */
@SuppressWarnings("serial")
public class FilemanagerException extends Exception {

    /**
     * Default constructor for Zabbix exception.
     */
    public FilemanagerException() {
        super();
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param message error message
     */
    public FilemanagerException(String message) {
       super(message);
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param message error message
     * @param cause object
     */
    public FilemanagerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized constructor for Zabbix exception.
     *
     * @param cause object
     */
    public FilemanagerException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message error message
     * @param cause object
     * @param enableSuppression enable suppression
     * @param writableStackTrace writable stack trace
     */
    protected FilemanagerException(String message, Throwable cause,
           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
