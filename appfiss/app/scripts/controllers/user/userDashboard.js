function userDashboardCtrl($scope, appfissService, $http, toastr, $sce) {
    /** Generic object for the controller. */
    $scope.initialPod = '';
    $scope.panelId = 2;
    $scope.namespaceName = '';
    $scope.namespace = $scope.namespaceName;
    $scope.graph = '';
    $scope.userDashboardObject = {};
    $scope.projectsIds = [];

    $scope.userDashboardObject = {

        // Get Grafana Url.
        getGrafanaUrl: function () {
            var hasResult = appfissService.crudService.listAll("user/findactiveuser");
            hasResult.then(function (result) {
                $scope.grafanaUrl = result.grafanaUrl;
                $scope.userDashboardObject.getActiveProjects();

            });
        },

        /**Graphical representation of usage details like CPU, Memory and Network.*/
        showGraph: function (type) {
            if (type === 'graph' || type === 'usage') {
                if (!angular.isUndefined($scope.pod) && $scope.pod != null) {
                    $scope.selectedPodName = $scope.pod.name.trim();
                } else {
                    $scope.selectedPodName = $scope.initialPod;
                }
                if (!angular.isUndefined($scope.usageType) && $scope.usageType != null) {
                    $scope.panelId = parseInt($scope.usageType);
                } else {
                    $scope.usageType = 2;
                    $scope.panelId = 2;
                }
                if ($scope.pod.name.trim() === 'Over-All-Usage') {
                    $scope.graph = $sce.trustAsResourceUrl($scope.grafanaUrl + '/dashboard-solo/db/overall-user-usage?refresh=10s&orgId=1&var-ns=' + $scope.namespace + '&var-name=&panelId=' + $scope.panelId + '&from=now-1h&to=now');
                } else {
                    if ($scope.panelId == 2) {
                        $scope.panelId = 6;
                    } else if ($scope.panelId == 3) {
                        $scope.panelId = 7;
                    } else if ($scope.panelId == 5) {
                        $scope.panelId = 8;
                    }
                    $scope.graph = $sce.trustAsResourceUrl($scope.grafanaUrl +'/dashboard-solo/db/pods?orgId=1&var-namespace=' + $scope.namespace + '&var-podname=' + $scope.selectedPodName + '&panelId=' + $scope.panelId + '&from=now-1h&to=now');
                }

            }
            if (type === undefined || type == "logs") {
                if (!angular.isUndefined($scope.podName) && $scope.podName != null) {
                    $scope.selectedPod = $scope.podName.name.trim();
                } else {
                    $scope.selectedPod = $scope.initialPod;
                }
                $scope.logs = $sce.trustAsResourceUrl($scope.grafanaUrl + '/dashboard-solo/db/log-monitoringnew1?orgId=1&panelId=1&from=now-1000m&to=now&var-user=' + $scope.namespace + '&var-podname=' + $scope.selectedPod + '');
            }
        },

        /**Get project details of logged in user.*/
        getUserProject: function () {
            var hasProjects = appfissService.crudService.listAll("project/listbyisactive");
            hasProjects.then(function (result) {
                $scope.userDashboardElement.userProjectLists = result;
            });
        },

        /**Get all active projects.*/
        getActiveProjects: function () {
            var hasResult = appfissService.crudService.listAll("project/listbyisactive");
            hasResult.then(function (result) {
                $scope.projectsList = result;
                var projectsIds = [];
                angular.forEach($scope.projectsList, function (obj, key) {
                    projectsIds.push(obj.id);
                });
                $scope.projectsIds = projectsIds;
                $scope.userDashboardObject.getLaunchersList();
            });
        },

        /**Get all launchers which was launched by logged in user.*/
        getLaunchersList: function () {
            $scope.podList = [];
            $scope.monitoringPodList = [];
            $scope.userDashboardElement.graphLoader = true;
            var hasLaunchers = appfissService.crudService.listAllByGateway("containers/container/api/deployment/findalllaunchersbyuserandtype");
            hasLaunchers.then(function (result) {
                $scope.launchersList = result;
                $scope.launchersCount = $scope.launchersList.length;
                if ($scope.launchersList.length > 0) {
                    $scope.namespace = $scope.launchersList[0].namespace.name;
                }
//                $scope.graph = $sce.trustAsResourceUrl($scope.grafanaUrl + '/dashboard-solo/db/overall-user-usage?refresh=10s&orgId=1&var-ns=' + $scope.namespace + '&var-name=&panelId=' + $scope.panelId + '&from=now-1h&to=now');
//                setTimeout(function() {
//                    $scope.$apply(function() {
//                        $scope.userDashboardElement.graphLoader = false;
//                    })
//                    
//                }, 5000);
                
                $scope.userDashboardObject.getPodsList();
            });
        },

        //Get Pods List
        getPodsList: function () {
            $scope.userDashboardElement.logLoader = true;
            var hasDeployments = appfissService.crudService.listAllByGateway("containers/container/api/deployment/findalllaunchersbyuser");
            hasDeployments.then(function (result) {
                $scope.deploymentList = result;
                if ($scope.deploymentList.length > 0) {
                    $scope.namespace = $scope.deploymentList[0].namespace.name;
                }
                $scope.monitoringPodList.push({"name": "Over-All-Usage"});
                angular.forEach($scope.deploymentList, function (obj, key) {
                    if (!angular.isUndefined((obj.pod)) && obj.pod != null) {
                        $scope.podList.push(obj.pod);
                        $scope.monitoringPodList.push(obj.pod);
                    }
                });
                if ($scope.podList.length > 0) {
                    $scope.initialPod = $scope.podList[0].name.trim();
                    $scope.pod = $scope.monitoringPodList[0];
                    $scope.podName = $scope.podList[0];
                    $scope.graph = $sce.trustAsResourceUrl($scope.grafanaUrl + '/dashboard-solo/db/overall-user-usage?refresh=10s&orgId=1&var-ns=' + $scope.namespace + '&var-name=&panelId=2' + '&from=now-1h&to=now');
                    setTimeout(function() {
                    $scope.$apply(function() {
                        $scope.userDashboardElement.graphLoader = false;
                    })
                    
                }, 5000);
                    $scope.logs = $sce.trustAsResourceUrl($scope.grafanaUrl + '/dashboard-solo/db/log-monitoringnew1?orgId=1&panelId=1&from=now-1000m&to=now&var-user=' + $scope.namespace + '&var-podname=' + $scope.initialPod + '');
                    setTimeout(function() {
                    $scope.$apply(function() {
                        $scope.userDashboardElement.logLoader = false;
                    })
                    
                }, 5000);
                } else {
                    $scope.userDashboardElement.graphLoader = false;
                    $scope.userDashboardElement.logLoader = false;
                }
            });
        },

        delete: function (size, item) {
            appfissService.dialogService.openDialog("views/delete-confirm.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {
                                $scope.confirmDelete = false;
                                setTimeout(function () {
                                    $scope.$apply(function () {
                                        $scope.userDashboardElement.userProjectLists.splice(item, 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        init: function () {

            var accessToken = appfissService.localStorageService.get("access_token");
            if (accessToken === null || angular.isUndefined(accessToken)) {
               // window.location.href = appfissService.appConfig.AUTH_APP_URL + "auth/oauth/authorize?&response_type=code&client_id=client1&redirect_uri=" + appfissService.appConfig.UI_APP_URL + "authentication.html";
            }
            $scope.userDashboardObject.getGrafanaUrl();
            $scope.userDashboardObject.getUserProject();

            $scope.userDashboardElement = {
                userProjectLists: [],
                graphLoader: false,
                logLoader:false
            };
        }
    };

    $scope.userDashboardObject.init();

}

angular
        .module('appfiss')
        .controller('userDashboardCtrl', userDashboardCtrl);
