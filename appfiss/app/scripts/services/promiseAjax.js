/**
 * Generic ajax request throgh promise for handling common.
 *
 */

function promiseAjax($http, appConfig, localStorageService) {

    var global = appConfig;

    // Http token request with authentication
    var httpTokenRequest = function (method, url, headers, data) {
        if (angular.isUndefined(data)) {
            var data = {};
            data.limit = global.CONTENT_LIMIT;
        }
        ;
//        if ((angular.isUndefined(localStorageService.get('userToken'))
//                || localStorageService.get('userToken') === null)) {
//            //Todo: Need to change the logout call
//            utilService.logout();
//    	} else {
        var config = {
            "method": method,
            "data": data,
            "url": url,
            "headers": {
                'x-requested-with': 'USER_REQUEST',
                //  'x-auth-token': localStorageService.get('userToken'),
                'Content-Type': 'application/json',
                'Range': "items=0-9",
                'Authorized': "true",
                'Authorization': appConfig.TOKEN_BEARER 
//                        + localStorageService.get("access_token"),
                // 'x-auth-remember': localStorageService.get('userRememberMe')
            }
        };

        if (headers != null && !angular.isUndefined(headers) && headers != '') {
            angular.forEach(headers, (function (value, key) {
                config.headers[key] = '';
                config.headers[key] = value;
            }));
        }

        return $http(config).then(function (res) {
            var data = res.data;
            // For Pagination
            if (res.headers('Content-Range') && typeof (res.headers('Content-Range')) != 'undefined') {
                var contentRange = res.headers('Content-Range');
                if (!isNaN(contentRange.split("/")[1])) {
                    data.totalItems = contentRange.split("/")[1];
                    data.itemsPerPage = global.CONTENT_LIMIT;
                }
            }
            return data;
        }).catch(function (result) {
            if (!angular.isUndefined(result) && (result.status === -1 || result.status === 401)) {
                //TODO: Handle Exception
                if(localStorageService.get("access_token") != null) {
                    localStorageService.set("access_token", null);
                    alert("Your session has been expired");
                }
                //window.location.href = appConfig.AUTH_APP_URL  + "auth/oauth/authorize?&response_type=code&client_id=client1&redirect_uri=" + appConfig.UI_APP_URL + "authentication.html";
            }
            throw result;
        });
        //}
    };

    //Default http request without authentication
    var httpRequest = function (method, moduleName, data, queryParams) {
        if (typeof (queryParams) != "undefined") {
            queryParams = "&" + queryParams;
        } else {
            queryParams = "";
        }
        var config = {
            "method": method,
            "data": data,
            "url": appConfig.APP_URL + moduleName + "?lang=en&sortBy=-id" + queryParams,
            "headers": {'Content-Type': 'application/json', 'Range': "items=0-9", 'Authorized': "false"}
        };
        return $http(config).then(function (result) {
            return result.data;
        }).catch(function (result) {
            if (!angular.isUndefined(result) && (result.status === -1 || result.status === 401)) {
                //TODO: Handle Exception
            }
            throw result;
        });
    };
    
    var httpResourceRequest = function (moduleName) {
         return $http.get(moduleName).then(function (result) {
            return result;
        }).catch(function (result) {
            
            throw result;
        });
    };

    var uploadFile = function(files, uuid, pvc, node, path, localStorageService, url) {
            var fd = new FormData();
            if(!angular.isUndefined(files)) {
                    fd.append("file",files);
            }
            fd.append("pvc",pvc);
            fd.append("node",node);
            fd.append('path',path);
            fd.append('uuid',uuid);
            return $http.post(appConfig.APP_GATEWAY_URL + url, fd, {
                transformRequest : angular.identity,
                "headers": {
                     'x-requested-with': 'USER_REQUEST',
                     //'x-auth-token': localStorageService.get('adminToken'),
                     'Content-Type': undefined,
                     'Range': "items=0-9",
                     'Authorized': "true",
                     "Authorization": appConfig.TOKEN_BEARER 
//                             + localStorageService.get("access_token")
                     //'x-auth-remember': localStorageService.get('adminRememberMe')
                 }
            });

    }
// File upload http request
    var httpFileuploadRequest = function(formData, url, $cookies, localStorageService) {

        return $http.post(appConfig.APP_URL + url, formData, {
            transformRequest : angular.identity,
           "headers": {
                'x-requested-with': 'USER_REQUEST',
                //'x-auth-token': localStorageService.get('adminToken'),
                'Content-Type': undefined,
                'Range': "items=0-9",
                'Authorized': "true",
                "Authorization": appConfig.TOKEN_BEARER 
//                        + localStorageService.get("access_token")
                //'x-auth-remember': localStorageService.get('adminRememberMe')
            }
        });
    };

    var requestObject = {
        httpTokenRequest: httpTokenRequest,
        httpRequest: httpRequest,
        httpFileuploadRequest: httpFileuploadRequest,
        uploadFile: uploadFile,
        httpResourceRequest : httpResourceRequest
    };
    return requestObject;
}

/**
 * Pass function into module
 */
angular
        .module('appfiss')
        .factory('promiseAjax', promiseAjax)
