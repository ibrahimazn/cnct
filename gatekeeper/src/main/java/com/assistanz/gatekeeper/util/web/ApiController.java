package com.assistanz.gatekeeper.util.web;

/**
 * API controller for reusing constants, defining path etc...
 *
 */
public interface ApiController {

    // Generic constants
    /** Create. */
    String PATH_ID = "id";

    // Swagger constants
    /** Create. */
    String SW_METHOD_CREATE = "create";

    /** Read. */
    String SW_METHOD_READ = "read";

    /** Update. */
    String SW_METHOD_UPDATE = "update";

    /** Delete. */
    String SW_METHOD_DELETE = "delete";

    /** List. */
    String SW_METHOD_LIST = "/";

    // Paging constants
    /** Create. */
    String RANGE = "Range";

    // Paging Limit constants
    /** Pagination limit for number of rows. */
    String CONTENT_LIMIT = "Content-Limit";

    // Application paths
    /** API path. */
    String API_PATH = "/api";

    /** Authentication URL path. */
    String AUTHENTICATE_URL = API_PATH + "/authenticate";

    /** Authentication URL path. */
    String WHMCS_AUTHENTICATE_URL = API_PATH + "/whmcs/authenticate";

    // Spring Boot Actuator services path
    /** Auto config endpoint. */
    String AUTOCONFIG_ENDPOINT = "/autoconfig";
    /** Beans endpoint. */
    String BEANS_ENDPOINT = "/beans";
    /** Config groups endpoint. */
    String CONFIGPROPS_ENDPOINT = "/configprops";
    /** Environment endpoint. */
    String ENV_ENDPOINT = "/env";
    /** Mappings endpoint. */
    String MAPPINGS_ENDPOINT = "/mappings";
    /** Metrics endpoint. */
    String METRICS_ENDPOINT = "/metrics";
    /** Shutdown endpoint. */
    String SHUTDOWN_ENDPOINT = "/shutdown";
}
