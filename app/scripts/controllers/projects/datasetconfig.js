function datasetConfigCtrl($scope, slmlpService, $http, toastr) {
    $scope.datasetConfigObject = {};
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'id';
     slmlpService.appConfig.sort.sortBy = 'id';
    slmlpService.appConfig.sort.sortOrder = '+';
    var language = 'en';
    
    $scope.datasetConfigObject = {
        list: function (pageNumber) {
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var hasactionServer = slmlpService.crudService.listAllWithPagination("datasetconfig/list", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit});
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasactionServer = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "datasetconfig/findbysearchtext?=" + "lang=" + language + encodeURI($scope.filter) + "&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
                    "limit": limit
                });
            }
            hasactionServer.then(function (result) {  // this is only run after $http completes
                $scope.dataSetConfigList = result;
                // For pagination
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.datasetConfigObject.list;
            }).catch(function (result) {
                if (!angular.isUndefined(result.data)) {
                    if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                        var msg = result.data.globalError[0];
                        toastr.error(
                                msg,
                                {
                                    closeButton: true
                                }
                        );
                    }
                }
            });
            ;
        },
        
        searchList: function (quicksearch) {
            $scope.quicksearch = quicksearch;
            $scope.datasetConfigObject.list(1);
        },

        changeSort: function (sortBy, pageNumber, sort) {
            var sort = slmlpService.utilService.getSortingOrderBySortBy(sortBy);
            $scope.sort = sort;
            sort.sortOrder = (sort.sortOrder == "+") ? "-" : "+";
            $scope.sort = sort;
            slmlpService.appConfig.sort.sortOrder = sort.sortOrder;
            slmlpService.appConfig.sort.sortBy = sortBy;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hasResult = {};
            var hasResult = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "datasetconfig/list?=" + "lang=" + language + "&sortBy=" + slmlpService.appConfig.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
            hasResult.then(function (result) { // this is only run after $http
                // completes0
                $scope.dataSetConfigList = result;
                // For pagination
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
            });
        },

        init: function () {
           $scope.datasetConfigObject.list(1);
        }
    }

    $scope.datasetConfigObject.init();

}

angular
        .module('slmlp')
        .controller('datasetConfigCtrl', datasetConfigCtrl);
