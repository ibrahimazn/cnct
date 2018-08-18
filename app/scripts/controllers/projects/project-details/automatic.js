function automaticCtrl($scope, slmlpService, $http, localStorageService, $rootScope, toastr, $window, $translate, $location, $state) {
    /** Generic object for the controller. */
    $scope.automaticObject = {};
    var $storage = $window.localStorage;
    $scope.listPerPage = "5";
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'id';
    slmlpService.appConfig.sort.sortBy = 'name';
    slmlpService.appConfig.sort.sortOrder = '+';
    var language = 'en';

    $scope.setActiveTab = function (automaticTab) {
        slmlpService.localStorageService.set("automaticTab", automaticTab);
    },

    $scope.goToTab = function (index) {
        $scope.automaticTab = index;
            if ($scope.automaticTabActive <= index) {
                $scope.automaticTabActive = index;
            }
    },
    $scope.datasetList = function (pageNumber) {
        var additionalParam = "&projectId=" + $scope.projectId;
        var hasactionServer = slmlpService.crudService.listAll("dataset/list", additionalParam);
        hasactionServer.then(function (result) {  // this is only run after $http completes
            $scope.datasetLists = result;
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
    $scope.changeDataset = function(dataset){
        slmlpService.localStorageService.set("dataset", dataset.name);
    },
    $scope.launchNoteBook = function(size){
        slmlpService.dialogService.openDialog("views/projects/project-details/model/notebook-launcher.html", size, $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                        $scope.startLauncher = function(launcher){
                            launcher.projectId = $scope.projectId;
                            if($scope.launchLists.length == 0){
                                $uibModalInstance.dismiss('cancel');
                                $scope.launch(launcher);
                            } else {
                                console.log(launcher);
                                $scope.openLauncher(launcher);
                                $state.reload();
                                $state.go('model.list');
                                toastr.success('Model created Successfully.', {
                                    closeButton: true
                                });
                            }
                        },
                        $scope.cancel = function () {
                            $uibModalInstance.dismiss('cancel');
                            toastr.success('Created Successfully.', {
                                closeButton: true
                            });
                            $state.go('model.list');
                        };
                    }]);
    },
    $scope.getLaunchesList = function(){
        var additionalParams = "&type=Launchers"
        var hasResult = slmlpService.crudService.listAll("launcher/listbytype", additionalParams);
        hasResult.then(function (result) {
            $scope.launchersLists = result;
        });
    },
    $scope.saveModel =  function (form) {
        $scope.modelFormSubmitted = true;
        if (form.$valid) {
            $scope.importModelLoader = true;
            $scope.model.modelType = "CUSTOM";
            $scope.model.dataSetId = $scope.model.dataSet.id;
            var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/model", $scope.model);
            hasResult.then(function (result) {  // this is only run after $http completes
                $scope.importModelLoader = false;
                $scope.launchNoteBook('md');
            }).catch(function (result) {
                $scope.importModelLoader = false;
            if (!angular.isUndefined(result.data)) {
                if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                    var msg = result.data.globalError[0];
                    toastr.error(
                            msg,
                            {
                                closeButton: true
                            }
                    );
                } else if (result.data.fieldErrors !== null) {
                        angular.forEach(result.data.fieldErrors, function (errorMessage, key) {
                            $scope.showLoader = false;
                            var msg = errorMessage;
                            toastr.error(
                                msg,
                                {
                                    closeButton: true
                                }
                            );
                        });
                    }
                }
            });
        }
    },
    $scope.checkValidation =  function (form, tab) {
        var validLauncher = false;
        if (tab == 1) {
            $scope.modelFormSubmitted = true;
        }
        if (form.$valid) {

            $scope.model.modelType = $scope.model.modelTypes;
            if ($scope.model.modelType == 'PREDEFINED') {
                $scope.goToTab(tab);
            } else if(tab == 0) {
                $scope.modelCreateLoader = true;
                $scope.model.dataSetId = $scope.model.dataSet.id;
                var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/model", $scope.model);
                hasResult.then(function (result) {  // this is only run after $http completes
                    toastr.success('Created Successfully.', {
                        closeButton: true
                    });
                    $scope.modelCreateLoader = false;
                    if($scope.launchLists.length == 0){
                        $scope.launch($scope.model.launcher);
                    } else {
                        $scope.openLauncher($scope.model.launcher);
                        $state.reload();
                        $state.go('model.list')
                    }
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
                        } else if (result.data.fieldErrors !== null) {
                            angular.forEach(result.data.fieldErrors, function (errorMessage, key) {
                                $scope.showLoader = false;
                                var msg = errorMessage;
                                toastr.error(
                                        msg,
                                        {
                                            closeButton: true
                                        }
                                );
                            });
                        }
                    }
                });
            } else {
                $scope.goToTab(tab+1);
            }
        }
    },
    $scope.launch = function(launcher){
        $scope.launchStatus = "Started";
        launcher.projectId = $scope.projectId;
        $scope.launcherLoader = true;
        var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/launchers/launch", launcher);
        hasResult.then(function (result) {
            slmlpService.dialogService.openDialog("views/projects/project-details/file-manager/start-launcher.html", "md", $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            setTimeout(function () {
                                $scope.launcherLoader = false;
                                $uibModalInstance.dismiss('cancel');
                                toastr.success('Model created Successfully.', {
                                    closeButton: true
                                });
                                $state.go('launchers.list');
                            }, 8000);
                            $scope.cancel = function () {

                            };


                        }]);
//                if(result.id) {
//                    setTimeout(function() {
//                        $scope.filerManagerDetailsObject.openInNewTab('http://'+ slmlpService.appConfig.K8S_IP + ':' + result.nodePort);
//                    },3000);
//
//                }
            //$scope.filerManagerElement.launchersLists = result;
        });
    }
    // Get active tab from localStorage
    $scope.getActiveTab = function () {
        return slmlpService.localStorageService.get("automaticTab");
    },

    $scope.setLancher = function(form,launcher){
        $scope.model.launcher = launcher;
        $scope.checkValidation(form, 0);
    },
    $scope.chooseDataset = function(type) {
        $scope.type = type;
        $scope.model.chooseFiles = true;
    },
    $scope.chooseSource = function(type) {
        $scope.type = type;
        $scope.model.chooseFiles = true;
    },
    $scope.chooseModel = function(type) {
        $scope.type = type;
        $scope.model.chooseFiles = true;
    },
    $scope.fileChoosed = function(){
        console.log($scope.type);
        if($scope.type == 'dataset') {
            if ( /\.(csv)$/i.test(slmlpService.appConfig.CURRENT_FILE) ) {
                $scope.model.chooseFiles = false;
                $scope.model.datasetFile = slmlpService.appConfig.CURRENT_FILE;
            } else {
                var msg = "Choose valid dataset file format";
                toastr.error(msg, { closeButton: true});
                $scope.model.chooseFiles = true;
            }

        } else if($scope.type == 'source') {
            if ( /\.(py|R)$/i.test(slmlpService.appConfig.CURRENT_FILE) ) {
                $scope.model.chooseFiles = false;
                $scope.model.modelFileSrc = slmlpService.appConfig.CURRENT_FILE;
            } else {
                var msg = "Choose valid script file format";
                toastr.error(msg, { closeButton: true});
                $scope.model.chooseFiles = true;
            }
        } else if($scope.type == 'model') {
            if ( /\.(sav|rds|pkl)$/i.test(slmlpService.appConfig.CURRENT_FILE) ) {
                $scope.model.chooseFiles = false;
                $scope.model.modelFile = slmlpService.appConfig.CURRENT_FILE;
                var arr =  (/[^/]+(?=(?:\.[^.]+)?$)/gi).exec($scope.model.modelFile);
                var name = arr[0].split(".")[0];
                $scope.model.name = "custom-" + name;
            } else {
                var msg = "Choose valid model file format";
                toastr.error(msg, { closeButton: true});
                $scope.model.chooseFiles = true;
            }
        }
    },
    $scope.openLauncher = function(containerObj) {
        console.log("node", containerObj)
        var url = 'http://'+ slmlpService.appConfig.K8S_IP + ':' + containerObj.service.nodePort
        var win = window.open(url, '_blank');
        //win.focus();
    },
    $scope.containerList = function(pageNumber) {
        slmlpService.appConfig.sort.sortBy = "id";
        var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
        var hasResult = slmlpService.crudService.listAllWithPagination("launcher/list", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                "limit": limit}, "&projectId="+ parseInt($scope.projectId));
        hasResult.then(function (result) {  // this is only run after $http completes
            $scope.launchersLists = result;
            console.log(result);
            $scope.launchLists = result;
            if($scope.launchLists.length == 0){
                $scope.getLaunchesList();
            }
            // For pagination
            $scope.paginationObject.limit = limit;
            $scope.paginationObject.currentPage = pageNumber;
            $scope.paginationObject.totalItems = result.totalItems;
            $scope.paginationObject.pagingMethod = $scope.containerList;
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
    // Check if current tab is active
    $scope.isActiveTab = function (tabName, index) {
        $scope.automaticTab = $scope.getActiveTab();
    },
    $scope.automaticObject = {
        init: function () {
            $scope.quicksearch == null;
            $scope.automaticElement = {};
            $scope.automatic = {};
            $scope.datasetLists = [];
            $scope.projectId = slmlpService.localStorageService.get("projectId");
            $scope.loginUser = slmlpService.localStorageService.get("user_name");
            $scope.model = {
                projectId : $scope.projectId,
                createdBy: $scope.loginUser
            };
            $scope.type = "";
            $scope.model.chooseFiles = false;
            $scope.datasetList(1);
            $scope.launchersLists = [];
            $scope.launchLists = [];
            $scope.model.chooseFile = slmlpService.appConfig.CURRENT_FILE;
            $scope.containerList(1);

            //$scope.getLaunchesList();
        }
    }
    $scope.automaticObject.init();
}

angular
        .module('slmlp')
        .controller('automaticCtrl', automaticCtrl);
