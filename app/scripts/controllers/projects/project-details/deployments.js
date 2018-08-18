function deploymentsDetailsCtrl($scope, $state, slmlpService, $http, toastr) {
    /** Generic object for the controller. */
    $scope.deploymentsObject = {};
    $scope.listPerPage = "5";
    $scope.paginationObject = {};
     $scope.sort = slmlpService.appConfig.sort
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'name';
    slmlpService.appConfig.sort.sortBy = 'name';
    slmlpService.appConfig.sort.sortOrder = '+';
    $scope.deploymentsObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getDeploymentsList: function () {
            $http.get("views/json/dashboard.json").success(function (result) {
                $scope.deploymentsElement.deploymentsLists = result;
            });
        },

        openChooseFileWindow: function(size, type) {
            slmlpService.dialogService.openDialog("views/projects/project-details/model/train/choose-dataset.html", size, $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {

                $scope.chooseFile = function() {

                    $scope.deployment[type] = angular.copy(slmlpService.appConfig.CURRENT_FILE);
                    $uibModalInstance.dismiss('cancel');
                }
                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

            }]);
        },

        // CREATE DEPLOYMENT
        createDeployment: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/deployments/create.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.deploymentsObject.save = function (analyze) {
                                $scope.createModelForm.submitted = true;
                                if (true) {
                                    $scope.createModelForm.submitted = false;
                                    $scope.deploymentsElement.deploymentsLists.push(deployments);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        // ADD MODEL
        scoreDataset: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/scoring/score.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.deploymentsObject.save = function (analyze) {
                                $scope.createModelForm.submitted = true;
                                if (true) {
                                    $scope.createModelForm.submitted = false;
                                    $scope.deploymentsElement.deploymentsLists.push(deployments);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        delete: function (size, modelDeployment) {
            slmlpService.dialogService.openDialog("views/delete-confirm.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.modelDeployment = modelDeployment;
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {
                                var modelDeployment = {
                                    id: $scope.modelDeployment.id
                                }
                                var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/modelDeployment/delete", modelDeployment);
                                hasResult.then(function (result) {
                                    toastr.success('Deleted Successfully.', {
                                        closeButton: true
                                    });
                                    $scope.deploymentsObject.deploymentList();
                                });
                                 $uibModalInstance.dismiss('cancel');
                                $scope.confirmDelete = false;
                            };

                        }]);
        },

        modelList: function() {
            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/model/findbyproject", "&projectId="+$scope.projectId);
            var results = [];
            hasList.then(function (result) {
                $scope.deploymentsElement.modelList = result;
            });
        },

        imageList: function() {
            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/launchers/listbytype",
            "&type="+slmlpService.appConfig.IMAGE_TYPE.MODEL_PREDICTION);
            var results = [];
            hasList.then(function (result) {
                $scope.deploymentsElement.imageList = result;
            });
        },

        getVersionList: function (model) {
            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/modelversions/listbymodel", "&modelId="+model.id);
            var results = [];
            hasList.then(function (result) {
                $scope.deploymentsElement.versionList = result;
            });
        },

        create: function() {
            $scope.deploymentForm.submitted = true;
            if($scope.deploymentForm.$valid) {
                $scope.deploymentsElement.createLoader = true;
                var modelDeployment = $scope.deployment;
                var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/modelDeployment/saveOffline", modelDeployment);
                hasResult.then(function (result) {
                    
//                    $state.reload();
                     setTimeout(function() {
                         $scope.deploymentsElement.createLoader = false;
                        $state.go('deployments.list');
                    }, 15000);
                }).catch(function (result) {
                        $scope.deploymentsElement.createLoader = false;
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

        deploymentList: function() {
            $scope.deploymentsElement.listLoader = true;
            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/modelDeployment/listByProject", "&projectId="+ $scope.projectId)
            var results = [];
            hasList.then(function (result) {
                $scope.deploymentsElement.listLoader = false;
                $scope.deploymentsElement.deploymentList = result;
            });
        },


        init: function () {
            $scope.projectId = slmlpService.localStorageService.get("projectId");
            $scope.project = slmlpService.localStorageService.get("projectRef");
            $scope.deploymentsElement = {
                modelList: [],
                versionList: [],
                listLoader: false,
                createLoader: false
            };

            $scope.deploymentsObject.getDeploymentsList();
            $scope.dataset = {};
            $scope.deployment = {};
            $scope.deploymentsObject.modelList();
            $scope.deploymentsObject.imageList();
            $scope.deploymentsObject.deploymentList(1);
        }
    }
    $scope.deploymentsObject.init();
}

angular
        .module('slmlp')
        .controller('deploymentsDetailsCtrl', deploymentsDetailsCtrl);
