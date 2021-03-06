package com.appfiss.connector.k8s.exception;

/**
 * An Exception class used to handle for global error send from CS server.
 *
 */
public class CustomGenericException extends RuntimeException {

    /** Serial version id. */
    private static final long serialVersionUID = 1L;
    /** Error code. */
    private String errorCode;
    /** Error message. */
    private String errorMsg;

    /**
     * Get the error code.
     *
     * @return error code.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Set the error code.
     *
     * @param errorCode code for error.
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Get the error message.
     *
     * @return errorMsg message.
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Set the error message.
     *
     * @param errorMsg error message.
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * Constructor to display error message and error code for CustomGenericException.
     *
     * @param errorCode code for error.
     * @param errorMsg error message.
     */
    public CustomGenericException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
