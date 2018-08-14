function hostController($scope, $rootScope, $stateParams,$http, $window, $sce, appfissService, toastr, $state, localStorageService) {
    $scope.paginationObject = {};
    $scope.global = appfissService.crudService.appConfig;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'name';
    $scope.sort = appfissService.appConfig.sort;
    appfissService.appConfig.sort.sortBy = 'name';
    appfissService.appConfig.sort.sortOrder = '+';
    $scope.global = appfissService.crudService.appConfig;
    $scope.hostSearch = null;

    $scope.hostObject = {
        list: function (pageNumber) {
            //$scope.showLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.hostSearch === null) {
                var hasactionServer = appfissService.crudService.listAllWithPagination("host/listhostdetails", appfissService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit});  
             } else {
                $scope.filter = "&searchText=" + $scope.hostSearch;
                hasactionServer = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "host/listallbysearchtext" + "?lang=" + appfissService.localStorageService.cookie.get('language') + encodeURI($scope.filter) + "&sortBy=" + appfissService.appConfig.sort.sortOrder + appfissService.appConfig.sort.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
            }
            
                hasactionServer.then(function (result) {  // this is only run after $http completes
                $scope.hostList = result;
                // For pagination
                $scope.totalCounts = result.totalItems;
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.hostObject.list;
               // $scope.showLoader = false;
            });
           
        },
        
        listwithjson : function(){
            $http.get("views/json/hosts.json").success(function (result) {
                $scope.hostList = result;
            });
        },

        hostChangeSort: function (sortBy, pageNumber, sort) {
            var sort = appfissService.utilService.getSortingOrderBySortBy(sortBy);
            $scope.sort = sort;
            sort.sortOrder = (sort.sortOrder == "+") ? "-" : "+";
            $scope.sort = sort;
            appfissService.appConfig.sort.sortOrder = sort.sortOrder;
            appfissService.appConfig.sort.sortBy = sortBy;
            $scope.showLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hashostList = {};
            var hashostList = appfissService.crudService.listAllWithPagination("host/listhostdetails", appfissService.appConfig.paginationHeaders(pageNumber, limit), {
                "limit": limit});
            hashostList.then(function (result) { // this is only run after $http

                $scope.hostList = result;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.showLoader = false;
            });
        },

        hostSearchList: function (hostSearch) {
            $scope.hostSearch = hostSearch;
            $scope.hostObject.list(1);
        },
        disableHost: function (host) {
            $scope.host = host;
            appfissService.dialogService.openDialog("views/configuration/host/disablehost.html", 'sm', $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function (host) {
                                var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_POST,
                                        appfissService.appConfig.APP_URL + "host/disable" + "?lang=" + localStorageService.cookie.get('language'), {},
                                        host);
                                hasResult.then(function (result) {
                                    $state.reload();
                                    toastr.pop({
                                        type: 'success',
                                        body: 'Successfully Disabled.',
                                        showCloseButton: true
                                    });
                                });
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },

        enableHost: function (host) {
            $scope.host = host;
            appfissService.dialogService.openDialog("views/configuration/host/enablehost.html", 'sm', $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function (host) {
                                var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_POST,
                                        appfissService.appConfig.APP_URL + "host/enable" + "?lang=" + localStorageService.cookie.get('language'), {},
                                        host);
                                hasResult.then(function (result) {
                                    $state.reload();
                                    toastr.pop({
                                        type: 'success',
                                        body: 'Successfully  Enabled.',
                                        showCloseButton: true
                                    });
                                });
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },
        reconnectHost: function (host) {
            appfissService.dialogService.openDialog("views/configuration/host/reconnect.html", 'sm', $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.host = host;
                            $scope.ok = function (host) {
                                appfissService.appConfig.webSocketLoaders.hostLoader = true;
                                var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_POST,
                                        appfissService.appConfig.APP_URL + "host/reconnect" + "?lang=" + localStorageService.cookie.get('language'), {},
                                        host);
                                hasResult.then(function (result) {
                                    $uibModalInstance.dismiss('cancel');
                                }).catch(function (result) {
                                    appfissService.appConfig.webSocketLoaders.hostLoader = false;
                                    if (!angular.isUndefined(result.data)) {
                                        if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                                            var msg = result.data.globalError;
                                            toastr.pop({
                                                type: 'error',
                                                body: msg,
                                                showCloseButton: true
                                            });
                                        }
                                    }
                                });
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
       
        moreInformation : function(host){
            $scope.host = host;
            appfissService.dialogService.openDialog("views/configuration/host/moreinfo.html", 'sm', $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
              }]);
        },
        getHostCounts: function () {
            var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET,
                    appfissService.appConfig.APP_URL + "host/hostCounts" + "?lang=" + localStorageService.cookie.get('language'));
            hasResult.then(function (result) {
                $scope.runningHostCount = result.runningHostCount;
                $scope.downHostCount = result.downHostCount;
                $scope.hostCount = result.hostCount;
            });
        },

        init: function () {
            //$scope.hostObject.list(1);
            $scope.hostObject.listwithjson();
           // $scope.hostObject.getHostCounts();
           
        }
    }


    $scope.hostObject.init();

}
;
angular
        .module('appfiss')
        .controller('hostController', hostController);
        