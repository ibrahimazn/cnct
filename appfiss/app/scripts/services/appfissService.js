// AppFiss Service for configure all components in one place.
function appfissService(dialogService, dateService, localStorageService, fileManagerConfig, webSocket, appConfig, promiseAjax, crudService, utilService) {
    
    var object = {};
    // DialogService service call goes here
    object.dialogService = dialogService;
    // Date service reference
    object.dateService = dateService;
    // LocalStorageService related functionalities goes here
    object.localStorageService = localStorageService;
    // filemanager
    object.fileManagerConfig = fileManagerConfig;
    //ajax call
    object.promiseAjax = promiseAjax;
    //Crud service 
    object.crudService = crudService;
    //Websocket object
    object.webSocket = webSocket;
    // general configuration and constants.
    object.appConfig = appConfig;
    // UtilService service call goes here
    object.utilService = utilService;

    return object;

}

/**
 * Register service in appfiss module.
 */
angular
        .module('appfiss')
        .factory('appfissService', appfissService)
