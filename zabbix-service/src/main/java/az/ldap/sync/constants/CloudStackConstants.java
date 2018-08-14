package az.ldap.sync.constants;

/**
 * All the common cloudStack constants for the application will go here.
 *
 */
public class CloudStackConstants {

    /**
     * Makes sure that utility classes (classes that contain only static methods or fields in their API) do not have a
     * public constructor.
     */
    protected CloudStackConstants() {
        throw new UnsupportedOperationException();
    }

    /** Constant for upload types. */
    public static final String CS_UPLOAD_NOT_STARTED = "UploadNotStarted", CS_UPLOAD_OPERATION = "UploadOp",
            CS_UPLOAD_ABANDONED = "UploadAbandoned", CS_UPLOAD_ERROR = "UploadError";

    /** Constant for job types. */
    public static final String CS_JOB_ID = "jobid", CS_JOB_RESULT = "jobresult", CS_JOB_STATUS = "jobstatus";

    /** Constant for job types. */
    public static final String CS_JOB_INSTANCE_ID = "jobinstanceid";

    /** Constant for job types. */
    public static final String CS_JOB_VM_ID = "virtualMachineId";

    /** Constant for configuration. */
    public static final String GLOBAL_CONF = "configuration";

    /** Constant for vlan config. */
    public static final String VLAN_CONF = "vlaniprange";

    /** Constant for configuration response. */
    public static final String GLOBAL_CONF_RESPONSE = "listconfigurationsresponse";

    /** Constant for vlan response. */
    public static final String VLAN_CONF_RESPONSE = "listvlaniprangesresponse";

    /** Constant for configuration response. */
    public static final String GLOBAL_CONF_IPS = "use.system.public.ips";

    /** Constant for asynchronous job result response. */
    public static final String QUERY_ASYNC_JOB_RESULT_RESPONSE = "queryasyncjobresultresponse";

    /** Constant for finger print. */
    public static final String CS_FINGER_PRINT = "fingerprint";

    /** Constant for finger print. */
    public static final String CS_TEMPLATE_NAME = "templatename";

    /** Constant for created date and time. */
    public static final String CS_CREATED = "created";

    /** Constant for project account role. */
    public static final String CS_ACCOUNT_ROLE = "Regular";

    /** Constant for CS response type. */
    public static final String CS_RESPONSE = "response";

    /** Constant for CS response project id. */
    public static final String CS_PROJECT_RESPONSE_ID = "projectId";

    /** Constant for CS role response. */
    public static final String CS_PROJECT_ROLE = "role";

    /** Constant for project  add account. */
    public static final String CS_PROJECT_ADD_ACCOUNT = "addAccountToProject";

    /** Constant for project delete account. */
    public static final String CS_PROJECT_REMOVE_ACCOUNT = "deleteAccountFromProject";

    /** Constant for project update. */
    public static final String CS_PROJECT_UPDATE = "updateProject";

    /** Constant for project update response. */
    public static final String CS_PROJECT_UPDATE_RESPONSE = "updateprojectresponse";

    /** Constant for project account list. */
    public static final String CS_PROJECT_ACCOUNT_LIST = "listProjectAccounts";

    /** Constant for custom offering. */
    public static final String CS_CUSTOM_STATUS = "iscustomized";

    /** Constant for custom offering iops. */
    public static final String CS_CUSTOM_IOPS_STATUS = "iscustomizediops";

    /** Constant for generic id. */
    public static final String CS_ID = "id";

    /** Constant for virtual machine id. */
    public static final String CS_VIRTUAL_MACHINE_ID = "virtualmachineid";

    /** Constant for iops. */
    public static final String CS_MAX_IOPS = "maxiops", CS_MIN_IOPS = "miniops";

    /** Constant for resource's state. */
    public static final String CS_STATE = "state";

    /** Constant for uploaded volume. */
    public static final String CS_UPLOADED = "Uploaded";

    /** Constant for offerings id. */
    public static final String CS_DISK_OFFERING_ID = "diskofferingid", CS_SERVICE_OFFERING_ID = "serviceofferingid",
        CS_NETWORK_OFFERING_ID = "networkofferingid", CS_VPC_OFFERING_ID = "vpcofferingid";

    /** Constant for resource's tags. */
    public static final String CS_TAGS = "tags";

    /** Constant for offerings. */
    public static final String CS_DISK_OFFERING = "diskoffering", CS_STORAGE_OFFERING = "storageOffering";

    /** Constant for disk bytes rate. */
    public static final String CS_BYTES_READ = "bytesreadrate", CS_BYTES_WRITE = "byteswriterate";

    /** Constant for disk iops rate. */
    public static final String CS_IOPS_READ = "iopsreadrate", CS_IOPS_WRITE = "iopswriterate";

    /** Constant for disk size. */
    public static final String CS_DISK_SIZE = "disksize";

    /** Constant for custom offering iops. */
    public static final String CS_CUSTOM_IOPS = "customizediops";

    /** Constant for storage offering type. */
    public static final String CS_STORAGE_TYPE = "storagetype";

    /** Constant for custom disk offering. */
    public static final String CS_CUSTOM_OFFER = "customized";

    /** Constant for resource visibility. */
    public static final String CS_PUBLIC = "public";

    /** Constant for resource's description. */
    public static final String CS_DISPLAY_TEXT = "displaytext";

    /** Constant for password. */
    public static final String CS_PASSWORD = "password";

    /** Constant value for json response. */
    public static final String JSON = "json";

    /** Constant for domain id. */
    public static final String CS_DOMAIN_ID = "domainid";

    /** Constant for department id. */
    public static final String CS_DEPARTMENT_ID = "departmentid";

    /** Constant for guest network id. */
    public static final String CS_GUEST_NETWORK_ID = "guestnetworkid";

    /** Constant for department. */
    public static final String CS_DEPARTMENT = "department";

    /** Constant for project id. */
    public static final String CS_PROJECT_ID = "projectid";

    /** Constant for zone id. */
    public static final String CS_ZONE_ID = "zoneid";

    /** Constant for api key. */
    public static final String CS_API_KEY = "apikey";

    /** Constant for secret key. */
    public static final String CS_SECRET_KEY = "secretkey";

    /** Constant for status. */
    public static final String STATUS_ACTIVE = "true", STATUS_INACTIVE = "false";

    /** Constant for list all. */
    public static final String CS_LIST_ALL = "listall";

    /** Constant for account name. */
    public static final String CS_ACCOUNT = "account";

    /** Constant for accounts name. */
    public static final String CS_ACCOUNTS = "accounts";

    /** Constant for template update permission response. */
    public static final String CS_UPDATE_TEMPLATE_PERMISSION_RESPONSE = "updatetemplatepermissionsresponse";

    /** Constant for delete account response. */
    public static final String CS_DELETE_ACCOUNT_RESPONSE = "deleteaccountresponse";

    /** Constant for list account response. */
    public static final String CS_LIST_ACCOUNT_RESPONSE = "listaccountsresponse";

    /** Constant for disk size. */
    public static final String CS_SIZE = "size";

    /** Constant for generic name. */
    public static final String CS_NAME = "name";

    /** Constant for generic url. */
    public static final String CS_URL = "url";

    /** Constant for cloudStack error response. */
    public static final String CS_ERROR_CODE = "errorcode", CS_ERROR_TEXT = "errortext";

    /** Constant for login response. */
    public static final String CS_LOGIN_RESPONSE = "loginresponse";

    /** Constant for list user response. */
    public static final String CS_LIST_USER_RESPONSE = "listusersresponse";

    /** Constant for register user key response. */
    public static final String CS_REGISTER_KEY_RESPONSE = "registeruserkeysresponse";

    /** Constant for gateway of the network. */
    public static final String CS_GATEWAY = "gateway";

    /** Constant for account type. */
    public static final String CS_ACCOUNT_TYPE = "accounttype";

    /** Constant for netMask of the network. */
    public static final String CS_NETMASK = "netmask";

    /** Constant for nic id. */
    public static final String CS_NIC_ID = "nicid";

     /** Constant for create account response. */
    public static final String CS_ACCOUNT_RESPONSE = "createaccountresponse";

    /** Constants for jobs status. */
    public static final String ERROR_JOB_STATUS = "2", PROGRESS_JOB_STATUS = "0",  SUCCEEDED_JOB_STATUS = "1";

    /** Constant for CIDR. */
    public static final String CS_CIDR = "cidr";

    /** Constant for domain. */
    public static final String CS_DOMAIN = "domain";

    /** Constant for network domain. */
    public static final String CS_NETWORK_DOMAIN = "networkdomain";

    /** Constant for user name. */
    public static final String CS_USER_NAME = "username";

    /** Constant for user . */
    public static final String CS_USER = "user";

    /** Constant for user as user type . */
    public static final String CS_USER_TYPE = "0";

    /** Constant for user as domain admin . */
    public static final String CS_DOMAIN_TYPE = "2";

    /** Constant for user as root admin . */
    public static final String CS_ROOT_TYPE = "1";

    /** Constant for first name of the user. */
    public static final String CS_FIRST_NAME = "firstname";

    /** Constant for last name of the user. */
    public static final String CS_LAST_NAME = "lastname";

    /** Constant for email of the user. */
    public static final String CS_EMAIL = "email";

    /** Constant for host id. */
    public static final String CS_HOST_ID = "hostid";

    /** Constant for template id. */
    public static final String CS_TEMPLATE_ID = "templateid";

    /** Constant for cpu details. */
    public static final String CS_CPU_NUMBER = "cpunumber", CS_CPU_SPEED = "cpuspeed", CS_CPU_USED = "cpuused";

    /** Constant for storage details. */
    public static final String CS_DISK_IO_READ = "diskioread", CS_DISK_IO_WRITE = "diskiowrite",
            CS_DISK_KBS_READ = "diskkbsread", CS_DISK_KBS_WRITE = "diskkbswrite";

    /** Constant for network details. */
    public static final String CS_NETWORK_KBS_READ = "networkkbsread", CS_NETWORK_KBS_WRITE = "networkkbswrite";

    /** Constant for password status. */
    public static final String CS_PASSWORD_STATUS = "passwordenabled";

    /** Constant for iso id. */
    public static final String CS_ISO_ID = "isoid";

    /** Constant for iso name. */
    public static final String CS_ISO_NAME = "isoname";

    /** Constant for nic. */
    public static final String CS_NIC = "nic";

    /** Constant for network id. */
    public static final String CS_NETWORK_ID = "networkid";

    /** Constant for instance name. */
    public static final String CS_INSTANCE_NAME = "instancename";

    /** Constant for user id. */
    public static final String CS_USER_ID = "userid";

    /** Constant for ip address. */
    public static final String CS_IP_ADDRESS = "ipaddress";

    /** Constant for primary memory. */
    public static final String CS_MEMORY = "memory";

    /** Constant for resource name. */
    public static final String CS_DISPLAY_NAME = "displayname";

    /** Constant for keyboard type. */
    public static final String CS_DISPLAY_VM = "displayvm";

    /** Constant for keyboard type. */
    public static final String CS_ACTIVE_VM = "true";

    /** Constant for keyboard type. */
    public static final String CS_KEYBOARD_TYPE = "keyboard";

    /** Constant for hypervisor type. */
    public static final String CS_HYPERVISOR_TYPE = "hypervisor";

    /** Constant for custom offering cpu number. */
    public static final String CS_CUSTOM_CORE = ".cpuNumber";

    /** Constant for custom offering cpu memory. */
    public static final String CS_CUSTOM_MEMORY = ".memory";

    /** Constant for custom offering minimum iops. */
    public static final String CS_MIN_IOPS_REQUEST = ".minIops";

    /** Constant for custom offering maximum iops. */
    public static final String CS_MAX_IOPS_REQUEST = ".maxIops";

    /** Constant for custom offering cpu speed. */
    public static final String CS_CUSTOM_CPU = ".cpuSpeed";

    /** Constant for response for cpu vm deploy. */
    public static final String CS_VM_DEPLOY = "deployvirtualmachineresponse";

    /** Constant for response for cpu vm deploy. */
    public static final String CS_DEDICATE_HOST = "dedicatehostresponse";

    /** Constant for custom details. */
    public static final String CS_CUSTOM_DETAILS = "details[0]";

    /** Constant for resource count. */
    public static final String CS_RESOURCE_COUNT = "resourcecount";

    /** Constant for response vm array. */
    public static final String CS_VM = "virtualmachine";

    /** Constant used to contact cloud admin. */
    public static final String CONTACT_CLOUD_ADMIN = "error.contact.cloud.admin";

    /** Constant used to resource limit check in puplic pool. */
    public static final String RESOURCE_CHECK = "resource.check";

    /** Constant used for entity validation. */
    public static final String ENTITY_VMINSTANCE = "vmInstance";

    /** Constant used for entity validation. */
    public static final String ENTITY_HOST = "host";

    /** Constant used for network offering ids . */
    public static final String CS_NETWORK_IDS = "networkids";

    /** Constant used for keyboard type. */
    public static final String KEYBOARD_VALUE = "us";

    /** Constant used for vm stop response. */
    public static final String CS_VM_STOP_RESPONSE = "stopvirtualmachineresponse";

    /** Constant used for vm reboot response. */
    public static final String CS_VM_REBOOT_RESPONSE = "rebootvirtualmachineresponse";

    /** Constant used for vm restore response. */
    public static final String CS_VM_RESTORE_RESPONSE = "restorevmresponse";

    /** Constant used for vm destroy response. */
    public static final String CS_VM_DESTROY_RESPONSE = "destroyvirtualmachineresponse";

    /** Constant used for vm expunge request. */
    public static final String CS_VM_ENPUNGE = "expunge";

    /** Constant used for vm recover response. */
    public static final String CS_VM_RECOVER_RESPONSE = "recovervirtualmachineresponse";

    /** Constant used for vm start response. */
    public static final String CS_VM_START_RESPONSE = "startvirtualmachineresponse";

    /** Constant used for vm migrate response. */
    public static final String CS_VM_MIGRATE_RESPONSE = "migratevirtualmachineresponse";

    /** Constant used for vm attach iso response. */
    public static final String CS_VM_ATTACHISO_RESPONSE = "attachisoresponse";

    /** Constant used for vm detach iso response. */
    public static final String CS_VM_DETACHISO_RESPONSE = "detachisoresponse";

    /** Constant used for vm reset password response. */
    public static final String CS_VM_RESET_PASSWORD_RESPONSE = "resetpasswordforvirtualmachineresponse";

    /** Constant used for vm display name. */
    public static final String CS_VM_DISPLAYNAME = "displayName";

    /** Constant used for list capacity response. */
    public static final String CS_CAPACITY_LIST_RESPONSE = "listcapacityresponse";

    /** Constant used for resource capacity. */
    public static final String CS_CAPACITY = "capacity";

    /** Constant used for capacity type. */
    public static final String CAPACITY_TYPE =  "type";

    /** Constant used for capacity total. */
    public static final String CS_CAPACITY_TOTAL = "capacitytotal";

    /** Constant used for capacity total. */
    public static final String CS_CAPACITY_USED = "capacityused";

    /** Constant used for list of public ipaddress response. */
    public static final String CS_PUBLIC_IPADDRESS_RESPONSE = "listpublicipaddressesresponse";

    /** Constant used for capacity count. */
    public static final String CS_CAPACITY_COUNT = "count";

    /** Constant used for update resource count response. */
    public static final String CS_UPDATE_RESOURCE_COUNT = "updateresourcecountresponse";

    /** Constant used for capacity total. */
    public static final String CS_RESOURCE_TYPE = "resourcetype";

    /** Constant used for capacity total. */
    public static final String CS_LIST_VM_RESPONSE = "listvirtualmachinesresponse";

    /** Constant used for associate network id. */
    public static final String CS_ASSOCIATE_NETWORK = "associatednetworkid";

    /** Constant used virtual network. */
    public static final String CS_FOR_VM_NETWORK = "forvirtualnetwork";

    /** Constant used scale vm response. */
    public static final String SCALE_VM_RESPONSE = "scalevirtualmachineresponse";

    /** Constant for command information. */
    public static final String CS_CMD_INFO = "cmdInfo";

    /** Constant for is ready state. */
    public static final String CS_READY_STATE = "isready";

    /** Constant for template action. */
    public static final String CS_VISIBILITY = "ispublic", CS_FEATURED = "isfeatured", CS_EXTRACTABLE = "isextractable",
            CS_DYNAMIC_SCALABLE = "isdynamicallyscalable", CS_BOOTABLE = "bootable", CS_ROUTING = "isrouting",
            CS_REQUIRES_HVM = "requireshvm";

    /** Constant for generic OS type id. */
    public static final String CS_OS_TYPEID = "ostypeid";

    /** Constant for routing key. */
    public static final String CS_ROUTING_KEY = "Routing Key";

    /** Constant for generic hypervisor. */
    public static final String CS_HYPERVISOR = "hypervisor";

    /** Constant for generic format. */
    public static final String CS_FORMAT = "format";

    /** Constant for generic templatetype. */
    public static final String CS_TEMPLATE_TYPE = "templatetype";

    /** Constant for generic system. */
    public static final String CS_SYSTEM = "system";

    /** Constant for template list response. */
    public static final String CS_LIST_TEMPLATE_RESPONSE = "listtemplatesresponse";

    /** Constant for ISO list response. */
    public static final String CS_LIST_ISO_RESPONSE = "listisosresponse";

    /** Constant for template response. */
    public static final String CS_PREPARE_TEMPLATE_RESPONSE = "preparetemplateresponse";

    /** Constant for register ISO response. */
    public static final String CS_REGISTER_ISO_RESPONSE = "registerisoresponse";

    /** Constant for register template response. */
    public static final String CS_REGISTER_TEMPLATE_RESPONSE = "registertemplateresponse";

    /** Constant for delete ISO response. */
    public static final String CS_DELETE_ISO_RESPONSE = "deleteisoresponse";

    /** Constant for delete template response. */
    public static final String CS_DELETE_TEMPLATE_RESPONSE = "deletetemplateresponse";

    /** Constant for template name. */
    public static final String TEMPLATE_NAME = "template";

    /** Constant for template ISO name. */
    public static final String ISO_TEMPLATE_NAME = "iso";

    /** Constant for asynchronous event type. */
    public static final String CS_COMMAND_EVENT_TYPE = "commandEventType";

    /** Constant for action event status. */
    public static final String CS_EVENT_STATUS = "status";

    /** Constant for action event status as completed. */
    public static final String CS_EVENT_COMPLETE = "Completed";

    /** Constant for action event name. */
    public static final String CS_EVENT_NAME = "event";

    /** Constant for volume type. */
    public static final String CS_VOLUME_TYPE = "type";

    /** Constant for network type. */
    public static final String CS_TYPE = "type";

    /** Constant for CS VM forced stop. */
    public static final String CS_FORCED_STOP = "forced";

    /** Constant for network offering traffic type. */
    public static final String CS_TRAFFIC_TYPE = "traffictype";

    /** Constant for network offering guest Ip type. */
    public static final String CS_GUEST_IP_TYPE = "guestiptype";

    /** Constant for network offering availability. */
    public static final String CS_AVAILABILITY = "availability";

    /** Constant for stickiness policies. */
    public static final String CS_STICKY_POLICIES = "stickinesspolicies";

    /** Constant for sticky policy. */
    public static final String CS_STICKY_POLICY = "stickinesspolicy";

    /** Constant for load balancer rule id. */
    public static final String CS_LB_RULE_ID = "lbruleid";

    /** Constant for method name. */
    public static final String CS_METHOD_NAME = "methodname";

     /** Constant for table size. */
    public static final String CS_TABLE_SIZE = "tablesize";


    /** Constant for VM snapshot id. */
    public static final String CS_VM_SNAPSHOT_ID = "vmsnapshotid";

    /** Constant for VM snapshot. */
    public static final String CS_VM_SNAPSHOT = "vmsnapshot";

    /** Constant for description. */
    public static final String CS_DESCRIPTION = "description";

    /** Constant for current snapshot. */
    public static final String CS_CURRENT = "current";

    /** Constant for parent ID of the vm snapshot. */
    public static final String CS_PARENT = "parent";

    /** Constant for keypair of the vm. */
    public static final String CS_KEYPAIR = "keypair";

    /** Constant for reset ssh key pair response. */
    public static final String CS_RESET_KEYPAIR_RESPONSE = "resetSSHKeyforvirtualmachineresponse";

    /** Constant for static NAT IP addresses. */
    public static final String CS_IS_STATIC_NAT = "isstaticnat";

    /** Constant for source NAT IP addresses. */
    public static final String CS_IS_SOURCE_NAT = "issourcenat";


    /** Constant for VLAN associated with the IP address. */
    public static final String CS_VLAN_NAME = "vlanname";

    /** Constant for network associated IP addresses. */
    public static final String CS_ASSOCIATED_NETWORK_ID = "associatednetworkid";

    /** Constant for display flag. */
    public static final String CS_FOR_DISPLAY = "fordisplay";

    /** Constant for create remote access VPN response. */
    public static final String CS_CREATE_REMOTE_ACCESS_VPN = "createremoteaccessvpnresponse";

    /** Constant for remote access VPN. */
    public static final String CS_REMOTE_ACCESS_VPN = "remoteaccessvpn";

    /** Constant for vpc. */
    public static final String CS_VPC = "vpc";

    /** Constant for VPN ipsec preshared key. */
    public static final String CS_PRESHARED_KEY = "presharedkey";

    /** Constant for add VPN user response. */
    public static final String CS_ADD_VPN_USER_RESPONSE = "addvpnuserresponse";

    /** Constant for VPN user. */
    public static final String CS_VPN_USER = "vpnuser";

    /** Constant for remote access VPN response. */
    public static final String CS_REMOTE_ACCESS_VPN_RESPONSE = "listremoteaccessvpnsresponse";

    /** Constant for list VPN user response. */
    public static final String CS_LIST_VPN_USERS_RESPONSE = "listvpnusersresponse";

    /** Constant for public ip address. */
    public static final String CS_PUBLIC_IP_ADDRESS = "publicipaddress";

    /** Constant for delete remote access VPN response. */
    public static final String CS_DELETE_REMOTE_ACCESS_VPN = "deleteremoteaccessvpnresponse";

    /** Constant for remove VPN user response. */
    public static final String CS_REMOVE_VPN_USER_RESPONSE = "removevpnuserresponse";

    /** Constant for remove VPN public ip. */
    public static final String CS_PUBLIC_IP_ID = "publicipid";

     /** Constant for cookie name. */
    public static final String CS_COOKIE_NAME = "cookie-name";

    /** Constant for length. */
    public static final String CS_LENGTH = "length";

    /** Constant for expires. */
    public static final String CS_EXPIRE = "expire";

    /** Constant for mode. */
    public static final String CS_MODE = "mode";

    /** Constant for request learn. */
    public static final String CS_REQUEST_LEARN = "request-learn";

    /** Constant for prefix. */
    public static final String CS_PREFIX = "prefix";

    /** Constant for indirect. */
    public static final String CS_INDIRECT = "indirect";

    /** Constant for nocache. */
    public static final String CS_NO_CACHE = "nocahche";

    /** Constant for post only. */
    public static final String CS_POST_ONLY = "postonly";

    /** Constant for hold time. */
    public static final String CS_HOLD_TIME = "holdtime";

    /** Constant for parameters. */
    public static final String CS_PARAMS = "params";

    /** Constant for parameters. */
    public static final String CS_COOKIE = "cookie-name";

    /** Constant for revert VM snapshot response. */
    public static final String CS_REVERT_VM_SNAPSHOT_RESPONSE = "reverttovmsnapshotresponse";

    /** Constant for create VM snapshot response. */
    public static final String CS_CREATE_VM_SNAPSHOT_RESPONSE = "createvmsnapshotresponse";

    /** Constant for delete VM snapshot response. */
    public static final String CS_DELETE_VM_SNAPSHOT_RESPONSE = "deletevmsnapshotresponse";

    /** Constant for list VM snapshot response. */
    public static final String CS_LIST_VM_SNAPSHOT_RESPONSE = "listvmsnapshotresponse";

    /** Constant for list router response. */
    public static final String CS_LIST_ROUTER_RESPONSE = "listroutersresponse";

    /** Constant for router. */
    public static final String CS_ROUTER = "router";

    /** Constant for status of failed. */
    public static final String CS_STATUS = "status";

    /** Constant for status of alert usage. */
    public static final String CS_ALERT_USAGE = "ALERT-USAGE";

    /** Constant for status of alert message. */
    public static final String CS_ALERT_MESSAGE = "body";

    /** Constant for status of alert subject. */
    public static final String CS_ALERT_SUBJECT = "subject";

    /** Constant for status of failed. */
    public static final String CS_STATUS_FAILED = "FAILED";

    /** Constant for status of Error. */
    public static final String CS_STATUS_ERROR = "Error";

    /** Constant for status of succeeded. */
    public static final String CS_STATUS_SUCCEEDED = "SUCCEEDED";

    /** Constant for id of event. */
    public static final String CS_EVENT_ID = "ctxStartEventId";

    /** Constant for CS resource state status. */
    public static final String CS_RESOURCE_STATE_STATUS = "OperationSucceeded";

    /** Constant for CS event date and time. */
    public static final String CS_EVENT_DATE_TIME = "eventDateTime";

    /** Constant for action websocket publish. */
    public static final String CS_ACTION_MAP = "/topic/action.event/";

    /** Constant for async websocket publish. */
    public static final String CS_ASYNC_MAP = "/topic/async.event/";

    /** Constant for error websocket publish. */
    public static final String CS_ERROR_MAP = "/topic/error.event/";

    /** Constant for resource websocket publish. */
    public static final String CS_RESOURCE_MAP = "/topic/resource.event/";

    /** Constant for alert websocket publish. */
    public static final String CS_ALERT_MAP = "/topic/alert.event/";

    /** Constant for alert websocket publish. */
    public static final String CS_DASH_MAP = "/topic/dashboard/";

    /** Constant for seperator. */
    public static final String CS_SEPERATOR = "/";

    /** Constant for action event status as scheduled. */
    public static final String CS_EVENT_SCHEDULED = "Scheduled";

    /** Constant for CS async job id. */
    public static final String CS_ASYNC_JOB_ID = "jobId";

    /** Constant for uuid. */
    public static final String CS_UUID = "uuid";

    /** Constant for uuid of instance. */
    public static final String CS_INSTANCE_UUID = "instanceUuid";

    /** Constant for account id. */
    public static final String CS_ACCOUNT_ID = "accountid";

    /** Constant for volume upload. */
    public static final String CS_VOLUME_UPLOAD = "VOLUME.UPLOAD";

    /** Constant for capacity percentage used. */
    public static final String CS_CAPACITY_PERCENT = "percentused";

    /** Constant for capacity max used. */
    public static final Integer CS_CAPACITY_MAX = 80;

    /** Constant for admin. */
    public static final String CS_ADMIN = "admin";

    /** Constant for project. */
    public static final String CS_PROJECT = "project";

    /** Constant for virtual machine ids. */
    public static final String CS_VIRTUAL_MACHINE_IDS = "virtualmachineIds";

    /** Constant for affinity group ids. */
    public static final String CS_AFFINITY_GROUP_IDS = "affinitygroupids";

    /** Constant for update virtual machine response. */
    public static final String CS_UPDATE_VM_RESPONSE = "updatevirtualmachineresponse";

    /** Constant for project. */
    public static final String PROJECT = "project";

    /** Constant for distributed VPC router. */
    public static final String CS_DISTRIBUTED_VPC_ROUTER = "distributedvpcrouter";

    /** Constant for is default. */
    public static final String CS_IS_DEFAULT = "isdefault";

    /** Constant for supports region level VPC. */
    public static final String CS_SUPPORTS_REGION_LEVEL_VPC = "supportsregionLevelvpc";

    /** Constant for VPC. */
    public static final String CS_FOR_VPC = "forvpc";

    /** Constant for VPC id. */
    public static final String CS_VPC_ID = "vpcid";

    /** Constant for list VPC response. */
    public static final String CS_LIST_VPC_RESPONSE = "listvpcsresponse";

    /** Constant for redundant VPC router */
    public static final String CS_REDUNDANT_VPC_ROUTER = "redundantvpcrouter";

    /** Constant for restart required. */
    public static final String CS_RESTART_REQUIRED = "restartrequired";

    /** Constant for ACL id. */
    public static final String CS_ACL_ID = "aclid";

    /** Constant for scope. */
    public static final String CS_SCOPE = "scope";

    /** Constant for physical network id. */
    public static final String CS_PHYSICAL_NETWORK_ID = "physicalnetworkid";

    /** Constant for shared network. */
    public static final String CS_SHARED = "Shared";

    /** Constant for disk Bytes Read Rate. */
    public static final String CS_DISK_BYTES_READ = "diskBytesReadRate";

    /** Constant for disk Bytes Write Rate. */
    public static final String CS_DISK_BYTES_WRITE = "diskBytesWriteRate";

    /** Constant for disk Iops Read Rate. */
    public static final String CS_DISK_IOPS_READ = "diskIopsReadRate";

    /** Constant for disk Iops Write Rate. */
    public static final String CS_DISK_IOPS_WRITE = "diskIopsWriteRate";

    /** Constant for provisioning type. */
    public static final String CS_PROVISIONING_TYPE = "provisioningtype";

    /** Allocated state ip address. */
    public static final String ALLOCATED = "Allocated";

    /** Source NAT ip address. */
    public static final String CS_SOURCENAT = "issourcenat";

    /** Vlan id. */
    public static final String CS_VLAN_ID = "vlanid";

    /** Value . */
    public static final String CS_VALUE = "value";

    /** FREE Ips . */
    public static final String FREE = "FREE";

    /** Constant for sourcenatsupported. */
    public static final String CS_SOURCE_NAT_SUPPORTED = "sourcenatsupported";;

    /** Constant for vlan. */
    public static final String CS_VLAN = "vlan";

     /** Constant used for assign vm response. */
    public static final String CS_ASSIGN_VM_RESPONSE = "assignvirtualmachineresponse";

     /** Constant for vpn gatway.*/
    public static final String CS_VPN_GATEWAY = "vpngateway";

    /** Constant for user login.*/
    public static final String CS_USER_LOGIN = "USER.LOGIN";

     /** Constant for vpc private gatway.*/
    public static final String CS_PRIVATE_GATEWAY = "privategateway";

     /** Constant for vpn cusomter gatway.*/
     public static final String CS_VPN_CUSTOMER_GATEWAY = "vpncustomergateway";

     /** Constant for vpc private gatway.*/
     public static final String CS_VPN_CONNECTION = "vpnconnection";

     /** Constant for chinese name.*/
     public static final String CS_CHINESE_NAME = "chineseName";

     /** Constant for all.*/
     public static final String CS_ALL_LIST = "all";

     /** Constant for show removed.*/
     public static final String CS_SHOW_REMOVED = "showremoved";

     /** Constant for show allocated only.*/
     public static final String CS_ALLOCATED_ONLY = "allocatedonly";

     /** Constant for volume id.*/
     public static final String CS_VOLUME_ID = "volumeid";

     /** Constant for LB algorithm. */
     public static final String CS_LB_ALGORITHM = "algorithm";

     /** Constant for LB source ip address. */
     public static final String CS_SOURCE_IP_ADDRESS = "sourceipaddress";

     /** Constant for list internal LB response. */
     public static final String CS_LIST_INTERNAL_RESPONSE = "listloadbalancersresponse";

     /** Constant for list LB. */
     public static final String LOAD_BALANCER = "loadbalancer";

     /** Constant for list LB rule. */
     public static final String LOAD_BALANCER_RULE = "loadbalancerrule";

     /** Constant for list internal LB delete response. */
     public static final String CS_DELETE_LB_RESPONSE = "deleteloadbalancerresponse";
}
