/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.constants;

/**
 * All the Generic constants for the application will go here.
 *
 */
public class GenericConstants {

    /**
     * Makes sure that constant classes (classes that contain only static methods
     * or fields in their API) do not have a public constructor.
     */
    protected GenericConstants() {
        throw new UnsupportedOperationException();
    }

    /** Constant for default limit. */
    public static final Integer DEFAULTLIMIT = 10;

    /** Constant used to set content range response for pagination. */
    public static final String CONTENT_RANGE_HEADER = "Content-Range";

    /** Constant used to strip range value from UI for pagination. */
    public static final String RANGE_PREFIX = "items=";

    /** Constant for AES encryption. */
    public static final String ENCRYPT_ALGORITHM = "AES";

    /** Constant for character encoding. */
    public static final String CHARACTER_ENCODING = "UTF-8";

    /** Constant for content type. */
    public static final String CONTENT_TYPE = "Content-Type";

    /** Constant for authentication date format. */
    public static final String AUTH_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /** Constant for user login URL path. */
    public static final String LOGIN_URL = "/login";

    /** Constant for user logout URL path. */
    public static final String LOGOUT_URL = "/logout";

    /** Constant for user logout redirect URL path. */
    public static final String LOGIN_OUT_URL = "/login?out=1";

    /** Constant for cookie name. */
    public static final String COOKIES_NAME = "JSESSIONID";

    /** Constant for OPTIONS. */
    public static final String OPTIONS = "OPTIONS";

    /** Constant for not implemented. */
    public static final String NOT_IMPLEMENTED = "501";

    /** Constant for token separator. */
    public static final String TOKEN_SEPARATOR = "@@";

    /** Constant for Name . */
    public static final String NAME = "name";

    /** Template architecture constant values. */
    public static final String[] TEMPLATE_ARCHITECTURE = {"32", "64"};

    /** Creation started. */
    public static final String STARTED = "STARTED";

    /** Creation succeeded. */
    public static final String SUCCEEDED = "SUCCEEDED";

    /** Creation failed. */
    public static final String FAILED = "FAILED";
    
    /** Creation succeeded. */
    public static final String SUCCESS = "success";
    
    /** Creation succeeded. */
    public static final String ERROR = "error";
    
    /** Constant for POD. */
    public static final String POD = "POD";

    /** Constant for DEPLOYMENT. */
    public static final String DEPLOYMENT = "DEPLOYMENT";

    /** Constant for REPLICASET. */
    public static final String REPLICASET = "REPLICASET";

    /** Constant for PERSISTENTVOLUMECLAIM. */
    public static final String PERSISTENTVOLUMECLAIM = "PERSISTENTVOLUMECLAIM";
    
    /** The Constant SCRIPT_PYTHON. */
    public static final String SCRIPT_PYTHON = "python3 ";
    
    /** The Constant SCRIPT_R. */
    public static final String SCRIPT_R = "Rscript ";
    
    /** The Constant PLATFORM_PYTHON. */
    public static final String PLATFORM_PYTHON  = "Python";
    
    /** The Constant PLATFORM_R. */
    public static final String PLATFORM_R = "R";
    
    /** The Constant PYTHON_EXT. */
    public static final String PYTHON_EXT = ".py";
    
    /** The Constant R_EXT. */
    public static final String R_EXT = ".R";
    
    /** The Constant OPENFAAS_FUNCTION_CREATE. */
    public static final String OPENFAAS_FUNCTION_CREATE = "system/functions";
    
    /** The Constant INVOKE_FUNCTION. */
    public static final String INVOKE_FUNCTION = "function/";
    
    /** The Constant METHOD_POST. */
    public static final String METHOD_POST = "post";
    
    /** The Constant METHOD_DELETE. */
    public static final String METHOD_DELETE =  "delete";
    
    /** The Constant HOME_PATH. */
    public static final String HOME_PATH = "/home/";
    
    /** The Constant VOLUME. */
    public static final String VOLUME = "volume";
    
    /** The Constant HYPHEN. */
    public static final String HYPHEN = "-";
    
    /** The Constant CUSTOM_ENGINE. */
    public static final String CUSTOM_ENGINE =  "CustomEngine";
    
    /** The Constant NAMESPACE_REQUEST. */
    public static final String NAMESPACE_REQUEST = "?namespace=";
    
    /** The Constant X_USER. */
    public static final String X_USER = "X_User";
    
    /** The Constant VERSION. */
    public static final String VERSION = "version";
    
    /** The Constant NOT_IMPLEMENTED_EXCEPTION. */
    public static final String NOT_IMPLEMENTED_EXCEPTION = "501";
    
    /** The Constant CUSTOM_MODEL. */
    public static final String CUSTOM_MODEL = "Custom Python/R";
    
    /** The Constant PYTHON_MODEL. */
    public static final String PYTHON_MODEL = ".sav";
    
    /** The Constant R_MODEL. */
    public static final String R_MODEL = ".rds";

}
