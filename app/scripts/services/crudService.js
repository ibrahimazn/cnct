/**
 * CRUD service is used to perform a CRUD operations in generic
 *
 */
function crudService(appConfig, promiseAjax) {

    var object = {};
    var language = "en";

    object.appConfig = appConfig;

//    object.pagingList = function(moduleName, headers, data) {
//        return promiseAjax.httpRequest(appConfig.HTTP_GET,
//        moduleName+"?lang="+language + "&sortBy="+appConfig.sort.sortOrder+appConfig.sort.sortBy+"&limit="+data.limit, headers, data);
//    };
//
//    object.create = function(moduleName, object) {
//        return promiseAjax.httpRequest(appConfig.HTTP_POST, moduleName+"?lang="+language, object, '');
//    };
//
//    object.read = function(moduleName, id) {
//        return promiseAjax.httpRequest(appConfig.HTTP_GET, moduleName+"/"+id+"?lang="+language, '');
//    };
//
//    object.update = function(moduleName, object) {
//        return promiseAjax.httpRequest(appConfig.HTTP_PUT, moduleName+"?lang="+language, '', object);
//    };
//
//    object.remove = function(moduleName, id) {
//        return promiseAjax.httpRequest(appConfig.HTTP_DELETE, moduleName+"/"+id+"?lang="+language, '');
//    };
//
//object.softDelete = function(moduleName, object) {
//        return promiseAjax.httpRequest(appConfig.HTTP_DELETE, moduleName  +"/delete/"+object.id+"?lang="+language, object,'');
//    };
//
    object.get = function(moduleName) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_URL+moduleName + "?lang="+language, '');
    };
//
//
//
// List all by pagination limit
    object.listAllWithPagination = function(moduleName, headers, data, additionalParams) {
        var queryParams = "lang="+language + "&sortBy="+appConfig.sort.sortOrder+appConfig.sort.sortBy+"&limit="+data.limit;
        if(!angular.isUndefined(additionalParams) && additionalParams != null) {
            queryParams = queryParams + additionalParams;
        }
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_URL+moduleName+"?" + queryParams, headers, data);
    };

    object.listAllByGatewayWithPagination = function(moduleName, headers, data, additionalParams) {
        var queryParams = "lang="+language + "&sortBy="+appConfig.sort.sortOrder+appConfig.sort.sortBy+"&limit="+data.limit;
        if(!angular.isUndefined(additionalParams) && additionalParams != null) {
            queryParams = queryParams + additionalParams;
        }
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_GATEWAY_URL+moduleName+"?" + queryParams, headers, data);
    };

    object.listAll = function(moduleName, additionalParams) {
        var queryParams = "lang="+language+"&sortBy=-id";
        if(!angular.isUndefined(additionalParams) && additionalParams != null) {
            queryParams = queryParams + additionalParams;
        }
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_URL+moduleName+"?" + queryParams);
    };

    object.listAllByGateway = function(moduleName, additionalParams) {
        var queryParams = "lang="+language+"&sortBy=-id";
        if(!angular.isUndefined(additionalParams) && additionalParams != null) {
            queryParams = queryParams + additionalParams;
        }
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_GATEWAY_URL+moduleName+"?" + queryParams);
    };

    object.listAllByGatewayWithPagination = function(moduleName, headers, data, additionalParams) {
        var queryParams = "lang="+language + "&sortBy="+appConfig.sort.sortOrder+appConfig.sort.sortBy+"&limit="+data.limit;
        if(!angular.isUndefined(additionalParams) && additionalParams != null) {
            queryParams = queryParams + additionalParams;
        }
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_GATEWAY_URL+moduleName+"?" + queryParams, headers, data);
    };

    object.PostViaGateway = function(moduleName, object) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_POST, appConfig.APP_GATEWAY_URL+moduleName+"?lang="+language, '', object);
    };

    object.UpdateViaGateway = function(moduleName, object) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_PUT, appConfig.APP_GATEWAY_URL+moduleName+"?lang="+language, '', object);
    };

    object.list = function(moduleName, headers, data) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET,
        appConfig.APP_URL+moduleName+"?lang="+language+ "&sortBy="+appConfig.sort.sortOrder+appConfig.sort.sortBy+"&limit="+data.limit, headers, data);
    };

    object.add = function(moduleName, object) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_POST, appConfig.APP_URL+moduleName+"?lang="+language, '', object);
    };

    object.read = function(moduleName, id) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_URL+moduleName+"/"+id+"?lang="+language, '');
    };
    object.readByGateway = function(moduleName, id) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_GET, appConfig.APP_GATEWAY_URL+moduleName+"/"+id+"?lang="+language, '');
    };

    object.update = function(moduleName, object) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_PUT, appConfig.APP_URL+moduleName+"?lang="+language, '', object);
    };

    object.delete = function(moduleName, id) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_DELETE, appConfig.APP_URL+moduleName+"/"+id+"?lang="+language, '');
    };

    object.softDelete = function(moduleName, object) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_DELETE, appConfig.APP_URL + moduleName  +"/delete/"+object.id+"?lang="+language, '', object);
    };

    object.softDeleteByGateway = function(moduleName, object) {
        return promiseAjax.httpTokenRequest(appConfig.HTTP_DELETE, appConfig.APP_GATEWAY_URL + moduleName  +"/delete/"+object.id+"?lang="+language, '', object);
    };

    object.listAllByFilter = function(moduleName, object) {
        return promiseAjax.httpTokenRequest( appConfig.HTTP_GET, appConfig.APP_URL + moduleName +"?dept=" +object.id+ "&lang=" + localStorageService.cookie.get('language'));
    };

    return object;
};


/**
 * Pass function into module
 */
angular
    .module('slmlp')
    .factory('crudService', crudService);
