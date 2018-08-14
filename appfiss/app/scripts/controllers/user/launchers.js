function launchersDetailsCtrl($scope, appfissService, toastr, $http, $stateParams) {
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
            appfissService.appConfig.sort.sortBy = "id";
            $scope.launchersDetailsElement.listLoader = false;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hasResult = appfissService.crudService.listAllWithPagination("launcher/list", appfissService.appConfig.paginationHeaders(pageNumber, limit), {
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
                    if (!angular.isUndefined(result.data.globalError) && result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
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
             $http.get("views/json/launchers.json").success(function(result){
                $scope.launchersDetailsElement.listLoader = false;
                $scope.launchersDetailsElement.launchersDetailsLists = result;
            });
        },
        delete: function (size, appContainer) {
            appfissService.dialogService.openDialog("views/delete-confirm.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.appContainer = angular.copy(appContainer);
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {
                                $scope.confirmDelete = false;

                                var hasResult = appfissService.crudService.PostViaGateway("containers/container/api/deployment/destroy", $scope.appContainer );
                                hasResult.then(function (result) {  // this is only run after $http completes
                                    $uibModalInstance.dismiss('cancel');
                                    toastr.success('Launcher destroyed successfully', {
                                        closeButton: true
                                    });
                                    $scope.launchersDetailsObject.list(1);
                                }).catch(function (result) {
                                    if (!angular.isUndefined(result.data)) {
                                        if (!angular.isUndefined(result.data.globalError) && result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
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
            var url = 'http://'+ appfissService.appConfig.K8S_IP + ':' + containerObj.service.nodePort
            var win = window.open(url, '_blank');
            win.focus();
        },
        launchFile: function () {
            var hasResult = appfissService.crudService.listAll("launcher/file/launch");
            hasResult.then(function (result) {
            });
        },
        getFile: function () {
            var hasResult = appfissService.crudService.listAll("launcher/file-manager/launch?type=File");
            hasResult.then(function (result) {
            });
        },
        launchEditor: function (launcher) {
            $scope.launchStatus = "Started";
            launcher.projectId = $scope.projectId;
            $scope.launcherLoader = true;
            var hasResult = appfissService.crudService.PostViaGateway("containers/container/api/launchers/launch", launcher);
            hasResult.then(function (result) {
                
                appfissService.dialogService.openDialog("views/projects/project-details/file-manager/start-launcher.html", "md", $scope,
                        ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                                setTimeout(function () {
                                    $scope.launcherLoader = false;
                                    $uibModalInstance.dismiss('cancel');
                                    $scope.launchersDetailsObject.list(1);
                                }, 7000);
                                $scope.cancel = function () {

                                };


                            }]);
//                if(result.id) {
//                    setTimeout(function() {
//                        $scope.filerManagerDetailsObject.openInNewTab('http://'+ appfissService.appConfig.K8S_IP + ':' + result.nodePort);
//                    },3000);
//
//                }
                //$scope.filerManagerElement.launchersLists = result;
            });
        },

        webSocketEvents: function () {
            $scope.$on("Deployment_ScalingReplicaSet", function (event, args) {
                console.log("Event", event);
                console.log("args", args);
                $scope.launchStatus = "Launching";
                $scope.launcherUrl = 'http://' + appfissService.appConfig.K8S_IP + ':' + args.metadata.selfLink;

            });
            $scope.$on("Pod_Started", function (event, args) {
                console.log("PodEvent", event);
                console.log("Podargs", args);
                $scope.launchStatus = true;
                $scope.launchStatus = "Launched";
                $scope.filerManagerDetailsObject.openInNewTab($scope.launcherUrl);
            });
        },

        // Start Launcher
        startLauncher: function (size) {
            appfissService.dialogService.openDialog("views/projects/project-details/file-manager/start-launcher.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            
                            $scope.cancel = function () {
                                
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },

        openInNewTab: function (url) {
            var win = window.open(url, '_blank');
            win.focus();
        },
        getLaunchers : function(){
            var hasResult = appfissService.crudService.listAll("launcher/listbyisactive");
            hasResult.then(function (result) {
                $scope.launchersDetailsElement.launchersLists = result;
            });
            $http.get("views/json/platforms.json").success(function(result){
                $scope.launchersDetailsElement.launchersLists = result;
            });
        },
        init: function () {
            $scope.projectId = appfissService.localStorageService.get("projectId");
            $scope.project = appfissService.localStorageService.get("projectRef");
            $scope.launchersDetailsElement = {
                listLoader: false
            };
            $scope.launchersDetailsObject.list(1);
            $scope.launchersDetails = {};
            $scope.launchersDetailsElement.launchersLists = [];
            $scope.launchersDetailsObject.getLaunchers();
            if($stateParams.containerId) {
                alert($stateParams.containerId);
            }
        }
    }

    $scope.launchersDetailsObject.init();
    $scope.$on("launchers.create", function(event,msg){

        console.log(msg)
        toastr.success(msg.content, {
            closeButton: true
        });
        $scope.launchersDetailsObject.list(1);
    });
}

angular
        .module('appfiss')
        .controller('launchersDetailsCtrl', launchersDetailsCtrl);
