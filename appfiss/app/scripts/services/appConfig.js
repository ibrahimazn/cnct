/**
 * AppConfig factory used for generic configurations.
 *
 */

function appConfig() {
    var appConstant = {
        APP_NAME: "appfiss",
        APP_VERSION: 1.0,
        AUTHOR_NAME: "Jamseer",
        HTTP_GET: "GET",
        HTTP_POST: "POST",
        HTTP_PUT: "PUT",
        HTTP_PATCH: "PATCH",
        HTTP_DELETE: "DELETE",
        APP_URL: "http://localhost:8080/api/",
        APP_GATEWAY_URL: "http://localhost:8080/",
        APP_ROOT_URL: "http://localhost:8091/",
        GRAFANA_URL: "http://35.200.232.220:32399/dashboard-solo/db/",
        UI_APP_URL: window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '') + '/appfiss/app/',
        AUTH_APP_URL: "http://localhost:9090/",
        SOCKET_URL :  window.location.protocol + "//"+ window.location.hostname + ":8080/",
        CURRENT_PROJECT : '/',
        K8S_IP: "k8smaster.assistanz.com",
        CONTENT_LIMIT: '10',
        TOKEN_BEARER: 'Bearer',
        CURRENT_PATH: '/',
        CONTENT_MIN_LIMITS: 10,
        CURRENT_FILE:"",
        IMAGE_TYPE: {
          MODEL_PREDICTION: "MODEL_PREDICTION",
          TENSORFLOW: "Tensorflow",
          LAUNCHERS: "Launchers",
        },
        sort: {
            column: '',
            descending: false,
            sortBy: 'id',
            sortOrder: '+'
        },
        Math : window.Math,
        paginationHeaders: function (pageNumber, limit) {
            var headers = {};
            var rangeStart = (pageNumber - 1) * limit;
            var rangeEnd = (pageNumber - 1) * limit + (limit - 1);
            headers.Range = "items=" + rangeStart + "-" + rangeEnd;
            return headers;
        }
    }
    return appConstant;
}
;

angular
        .module('appfiss')
        .factory('appConfig', appConfig);
