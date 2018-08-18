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
        APP_URL: "http://localhost:8086/api/",
        APP_GATEWAY_URL: "http://localhost:8086/",
        APP_ROOT_URL: "http://localhost:8091/",
        GRAFANA_URL: "http://localhost:32399/dashboard-solo/db/",
        UI_APP_URL: window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '') + '/appfiss/app/',
        AUTH_APP_URL: "http://localhost:9090/",
        SOCKET_URL :  window.location.protocol + "//"+ window.location.hostname + ":8086/",
        CURRENT_PROJECT : '/',
        K8S_IP: "k8smaster.assistanz.com",
        CONTENT_LIMIT: '10',
        TOKEN_BEARER: 'BearereyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzQ0Mzk2MDMsInVzZXJfbmFtZSI6InNsbWxwQGFzc2lzdGFuei5jb20iLCJhdXRob3JpdGllcyI6WyJST0xFX3VzZXIiLCJST0xFX0RFUEFSVE1FTlRfTElTVCJdLCJqdGkiOiIzMzZkYzU0NC0wMmZjLTQyN2YtODNjNi0yOTJhZTBlMTMxYTEiLCJjbGllbnRfaWQiOiJjbGllbnQxIiwic2NvcGUiOlsiZm9vIiwicmVhZCIsIndyaXRlIl19.D2U8fhZc1aH2NBwnehiTdtgkG1QUpyBybUBbKX0bLzvIckVjE10iKCQDIETxHjgSiDOzbBs8LZ8nu8VG7fKwOALUKSuMzaJjayeTAsglW3FsR0j0Z0dHUsy6-c80sB7NY0ipTIggzgsUJdTzaYQerxsg6hcsuN4XdTSzNc_BRSU3FzAbxb6pnrW5R-ltKWQTKDyyU1X7J2no3wEKro5UJQ8Qn0-RkBtYS7gp3I_koTVo7yPseqLlGDvDHcpqLvCnKzXdw4m7OB3ckayL4yIGNGPX4GRuFRNvnlAD5sH7a6qs48qbzOtLv84ijZwNRdZIxX3XGlgHGrPodbKu-TyPRQ',
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
