function slmlpService(dialogService,dateService,localStorageService,fileManagerConfig, webSocket, appConfig, promiseAjax, crudService, utilService) {

    var object = {};

    // DialogService service call goes here
    object.dialogService = dialogService;

    // Date service reference
    object.dateService = dateService;

    // LocalStorageService related functionalities goes here
    object.localStorageService = localStorageService;

    object.fileManagerConfig = fileManagerConfig;

    object.promiseAjax =  promiseAjax;

    object.crudService =  crudService;


    object.webSocket =  webSocket;

    object.appConfig =  appConfig;

    // UtilService service call goes here
    object.utilService = utilService

    return object;

};

/**
 * Pass function into module
 */
angular
.module('slmlp')
.factory('slmlpService', slmlpService)
