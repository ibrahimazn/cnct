function launchersDetailsCtrl($scope, slmlpService, toastr, $http, $stateParams) {
    /** Generic object for the controller. */
    $scope.launchersDetailsObject = {};
    $scope.listPerPage = "100";
    $scope.paginationObject = {};
    $scope.launchersDetailsObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        list: function (pageNumber) {
//            $http.get("views/json/projectLaunchers.json").success(function (result) {
//                $scope.launchersDetailsElement.launchersDetailsLists = result;
//            });
            
            slmlpService.appConfig.sort.sortBy = "id";
            $scope.launchersDetailsElement.listLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hasResult = slmlpService.crudService.listAllWithPagination("launcher/list", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit}, "&projectId="+ parseInt($scope.projectId));
            hasResult.then(function (result) {  // this is only run after $http completes
                $scope.launchersDetailsElement.listLoader = false;
                $scope.launchersDetailsElement.launchersDetailsLists = result;
                // For pagination
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.launchersDetailsObject.list;
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
        },
        delete: function (size, appContainer) {
            slmlpService.dialogService.openDialog("views/delete-confirm.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.appContainer = angular.copy(appContainer);
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {
                                $scope.confirmDelete = false;

                                var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/deployment/destroy", $scope.appContainer );
                                hasResult.then(function (result) {  // this is only run after $http completes
                                    $uibModalInstance.dismiss('cancel');
                                    toastr.success('Launcher destroyed successfully', {
                                        closeButton: true
                                    });
                                    $scope.launchersDetailsObject.list(1);
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
//                                setTimeout(function () {
//                                    $scope.$apply(function () {
//                                        $scope.launchersDetailsElement.launchersDetailsLists.splice($scope.launchersDetailsElement.launchersDetailsLists.indexOf(item), 1);
//                                        $uibModalInstance.dismiss('cancel');
//                                    })
//                                }, 1000);
                            };

                        }]);
        },
        
        openLauncher: function(containerObj) {
            console.log("node", containerObj)
            var url = 'http://'+ slmlpService.appConfig.K8S_IP + ':' + containerObj.service.nodePort
            var win = window.open(url, '_blank');
            win.focus();
        },
        init: function () {
            $scope.projectId = slmlpService.localStorageService.get("projectId");
            $scope.project = slmlpService.localStorageService.get("projectRef");
            $scope.launchersDetailsElement = {
                listLoader: false
            };
            $scope.launchersDetailsObject.list(1);
            $scope.launchersDetails = {};
            
            if($stateParams.containerId) {
                alert($stateParams.containerId);
            }
        }
    }

    $scope.launchersDetailsObject.init();

}

angular
        .module('slmlp')
        .controller('launchersDetailsCtrl', launchersDetailsCtrl);
