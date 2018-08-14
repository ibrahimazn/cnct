package az.ldap.sync.constants;


/**
 * Event Types from Cloudstack Server.
 */
public class EventTypes {
    /**
     * Makes sure that utility classes (classes that contain only static methods or fields in their API) do not have a
     * public constructor.
     */
    protected EventTypes() {
        throw new UnsupportedOperationException();
    }   
    /** Account events. */
    public static final String EVENT_ACCOUNT = "ACCOUNT.";
    public static final String EVENT_ACCOUNT_ENABLE = "ACCOUNT.ENABLE";
    public static final String EVENT_ACCOUNT_DISABLE = "ACCOUNT.DISABLE";
    public static final String EVENT_ACCOUNT_CREATE = "ACCOUNT.CREATE";
    public static final String EVENT_ACCOUNT_DELETE = "ACCOUNT.DELETE";
    public static final String EVENT_ACCOUNT_UPDATE = "ACCOUNT.UPDATE";
    public static final String EVENT_ACCOUNT_MARK_DEFAULT_ZONE = "ACCOUNT.MARK.DEFAULT.ZONE";

    /** UserVO Events. */
    public static final String EVENT_USER = "USER.";
    public static final String EVENT_USER_LOGIN = "USER.LOGIN";
    public static final String EVENT_USER_LOGOUT = "USER.LOGOUT";
    public static final String EVENT_USER_CREATE = "USER.CREATE";
    public static final String EVENT_USER_DELETE = "USER.DELETE";
    public static final String EVENT_USER_DISABLE = "USER.DISABLE";
    public static final String EVENT_USER_UPDATE = "USER.UPDATE";
    public static final String EVENT_USER_ENABLE = "USER.ENABLE";
    public static final String EVENT_USER_LOCK = "USER.LOCK";

    // register for user API and secret keys
    /** register for user API and secret keys. */
    public static final String EVENT_REGISTER_SECRET = "REGISTER.";
    /** register for user API and secret keys. */
    public static final String EVENT_REGISTER_FOR_SECRET_API_KEY = "REGISTER.USER.KEY";

    /** Domains. */
    public static final String EVENT_DOMAIN = "DOMAIN.";
    public static final String EVENT_DOMAIN_CREATE = "DOMAIN.CREATE";
    public static final String EVENT_DOMAIN_DELETE = "DOMAIN.DELETE";
    public static final String EVENT_DOMAIN_UPDATE = "DOMAIN.UPDATE";
    
    /** unknown type. */
    public static final String EVENT_UNKNOWN = "unknown";    

}
