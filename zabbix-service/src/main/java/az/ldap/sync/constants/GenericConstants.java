package az.ldap.sync.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * All the common constants for the application will go here.
 *
 */
public class GenericConstants {

    /**
     * Makes sure that utility classes (classes that contain only static methods or fields in their API) do not have a
     * public constructor.
     */
    protected GenericConstants() {
        throw new UnsupportedOperationException();
    }

    /** Constant used to strip range value from UI for pagination. */
    public static final String RANGE_PREFIX = "items=";

    /** Constant used to set content range response for pagination. */
    public static final String CONTENT_RANGE_HEADER = "Content-Range";

    /** Constant for default limit. */
    public static final Integer DEFAULTLIMIT = 10;

    /** Constant for default job status. */
    public static final String DEFAULT_JOB_STATUS = "10";

    /** Constants for job status. */
    public static final String ERROR_JOB_STATUS = "2", PROGRESS_JOB_STATUS = "0",  SUCCEEDED_JOB_STATUS = "1";

    /** Constant for generic exception status code. */
    public static final String NOT_IMPLEMENTED = "501";

    /** Resource type constants values. */
    public static final String RESOURCE_MEMORY = "0", RESOURCE_CPU = "1", RESOURCE_SECONDARY_STORAGE = "6",
            RESOURCE_PRIMARY_STORAGE = "3", RESOURCE_IP_ADDRESS = "4";

    /** Constant map for default resource types. */
    public static final Map<String, String> RESOURCE_CAPACITY = Arrays
            .stream(new String[][] {{"0", "9"},
                {"1", "8"},
                {"6", "11"},
                {"3", "10"},
                {"4", "1"}})
            .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

    /** Template architecture constant values. */
    public static final String[] TEMPLATE_ARCHITECTURE = {"32", "64"};

    /** Constant for generic name. */
    public static final String NAME = "name";
    
    /** Constant for generic name. */
    public static String AUTH = "3d84866c6d4f212a82257739e84689bes";

    /** Constant for AES encryption. */
    public static final String ENCRYPT_ALGORITHM = "AES";

    /** Constant for character encoding. */
    public static final String CHARACTER_ENCODING = "UTF-8";

    /** Constant for token separator. */
    public static final String TOKEN_SEPARATOR = "@@";
    
    /** Constant for VM Start separator. */
    public static final String VM_ACTION_START = "VM OFFLINE ACTION CODE";
    
    /** Constant for VM Stop separator. */
    public static final String VM_ACTION_STOP = "VM STOP CODE";
    
    /** Constant for VM Destroy separator. */
    public static final String VM_ACTION_DESTROY = "VM DESTROY/EXPUNGE CODE";
    
    /** Constant for VM Recover separator. */
    public static final String VM_ACTION_RECOVER = "VM RECOVER CODE";
    
    /** Constant for VM Resize separator. */
    public static final String VM_ACTION_RESIZE = "VM RESIZE CODE";
    
    /** Constant for VM Re-install separator. */
    public static final String VM_ACTION_REINSTALL = "VM RE-INSTALL CODE";
    
    /** Constant for VM Re-start separator. */
    public static final String VM_ACTION_REBOOT = "VM RESTART CODE";
    
    /** Constant for VM Resize separator. */
    public static final String VM_ACTION_RESET = "VM RESETPASSWORD CODE";

    /** Constant for content type. */
    public static final String CONTENT_TYPE = "Content-Type";

    /** Constant for authentication date format. */
    public static final String AUTH_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /** Constant for user login URL path. */
    public static final String LOGIN_URL = "/login";

    /** Constant for user logout redirect URL path. */
    public static final String LOGIN_OUT_URL = "/login?out=1";

    /** Constant for user logout URL path. */
    public static final String LOGOUT_URL = "/logout";

    /** Constant for cookie name. */
    public static final String COOKIES_NAME = "JSESSIONID";

    /** Page error seperator constant. */
    public static final String PAGE_ERROR_SEPARATOR = "PAGE_ERROR";

    /** Full permission role constant. */
    public static final String FULL_PERMISSION = "FULL_PERMISSION";

    /** All permission role constant. */
    public static final String NO_PERMISSION = "NO_PERMISSION";

    /** Constant for instance. */
    public static final String INSTANCE = "Instance";

    /** Constant for volume. */
    public static final String VOLUME = "Volume";

    /** Constant for upload volume. */
    public static final String UPLOAD_VOLUME = "UploadVolume";

    /** Constant for network. */
    public static final String NETWORK = "Network";

    /** Constant for IP. */
    public static final String IP = "IP";

    /** Constant for VPC. */
    public static final String VPC = "VPC";

    /** Constant for restore instance. */
    public static final String RESTORE_INSTANCE = "RestoreInstance";

    /** Constant for destroy status. */
    public static final String DESTROY = "Destroy";

    /** Constant for expunging status. */
    public static final String EXPUNGING = "Expunging";

    /** Constant for project. */
    public static final String PROJECT = "Project";

    /** Constant for department. */
    public static final String DEPARTMENT = "Department";

    /** Constant for update status. */
    public static final String UPDATE = "update";

    /** Constant for delete status. */
    public static final String DELETE = "delete";

    /** Constant for memory. */
    public static final String MEMORY = "Memory";

    /** Constant for resize volume. */
    public static final String RESIZEVOLUME = "RESIZEVOLUME";

    /** Constant for VM migrate. */
    public static final String MIGRATE = "Migrate";
    
	/** Constant for key generation type. */
    public static final String RANDOM_KEY_GENERATION_TYPE = "random", MANUAL_KEY_GENERATION_TYPE = "manual";

    // === Security ===
    public static final String SECURITY_TOKEN_SECURITY_CHANNEL_INTERCEPTOR = "tokenSecurityChannelInterceptor";
    public static final String ROOT_ADMIN = "rootadmin";

    // === Identifiers ===
    public static final String HEADER_X_AUTH_TOKEN = "x-auth-token";
    public static final String HEADER_X_AUTH_LOGIN_TOKEN = "x-auth-login-token";
    public static final String SERVICE_TOKEN_AUTH = "tokenAuth";
    public static final String HEADER_X_UUID = "x-uuid";
    public static final String HEADER_X_AUTH_USER_ID = "x-auth-user-id";
    public static final String HEADER_X_AUTH_DOMAIN_ID = "x-auth-domain-id";
    public static final String HEADER_X_AUTH_DEPARTMENT_ID = "x-auth-department-id";
    public static final String SIMP_MESSAGE_TYPE = "simpMessageType";
    public static final String SIMP_SESSION_ID = "simpSessionId";
    public static final String NATIVE_HEADERS = "nativeHeaders";
    public static final String HEADER_X_REQUESTED_WITH = "x-requested-with";
	public static final String HEADER_X_TYPE = "x-auth-type";

}


