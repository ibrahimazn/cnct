/*
 * 
 */
package com.appfiss.account.constants;

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

  /** Creation started. */
  public static final String STARTED = "STARTED";

  /** Creation succeeded. */
  public static final String SUCCEEDED = "SUCCEEDED";

  /** Creation failed. */
  public static final String FAILED = "FAILED";

public static final Object SIMP_SESSION_ID = "sessionid";

public static final Object HEADER_X_AUTH_LOGIN_TOKEN = null;

public static final Object HEADER_X_TYPE = null;

public static final Object HEADER_X_AUTH_DEPARTMENT_ID = null;

public static final Object HEADER_X_AUTH_DOMAIN_ID = null;

public static final Object HEADER_X_AUTH_USER_ID = null;

public static final Object SIMP_MESSAGE_TYPE = null;

public static final Object NATIVE_HEADERS = null;

}
