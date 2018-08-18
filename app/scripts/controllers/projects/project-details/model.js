function modelDetailsCtrl($scope, slmlpService, $http, toastr,$state, fileNavigator) {
    /** Generic object for the controller. */
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'id';
    $scope.fileNavigator = new fileNavigator();
    $scope.modelDetailsObject = {};
//    $scope.listPerPage = "5";
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'id';
    slmlpService.appConfig.sort.sortBy = 'name';
    slmlpService.appConfig.sort.sortOrder = '+';
    var language = 'en';
    $scope.modelDetailsObject = {
        sort: function (tablename) {
            $scope.sortTrainingKey = tablename;
            $scope.sortTrainingreverse = !$scope.sortTrainingreverse;
        },
        sortTraining: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        optionToggled: function (list) {
            $scope.checkAllModel = $scope.modelDetailsElement.modelLists.every(function (itm) {
                return itm.checked == true;
            })
        },
        checkAll: function (isCheckedAll) {
            angular.forEach($scope.modelDetailsElement.modelLists, function (obj, key) {
                obj.checked = isCheckedAll;
            });
        },
        getModelSummaryList: function () {
            $http.get("views/json/model-summary.json").success(function (result) {
                $scope.modelDetailsElement.modelSummaryLists = result;
            });
        },
        getModelDatasetList: function () {
            $http.get("views/json/model-dataset.json").success(function (result) {
                $scope.modelDetailsElement.modelDatasetLists = result;
            });
        },
        getInputSchemaList: function () {
            $http.get("views/json/input-schema.json").success(function (result) {
                $scope.modelDetailsElement.inputSchemaLists = result;
            });
        },
        getVersionsList: function (model) {
            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/modelversions/listbymodel", "&modelId="+model.id);
            var results = [];
            hasList.then(function (result) {
                $scope.modelDetailsElement.versionLists = result;
            });
        },
        selectModel: function(model){
            slmlpService.localStorageService.set("selectModel", model)
            $scope.selectedModel = slmlpService.localStorageService.get("selectModel");
            $scope.modelDetailsObject.getTrainingJobsListByModel(1, model);
            $scope.modelDetailsObject.getSource(model);
            $scope.modelDetailsObject.getVersionsList(model);
        },
        // SELECT SOURCE or SCRIPT
        selectSourceScript: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/model/details/select-source.html", size, $scope,
            ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                },
                $scope.selectSource = function() {
                    $scope.model = $scope.selectedModel;
                    $scope.model.modelFileSrc = slmlpService.appConfig.CURRENT_FILE;
                    var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/model/updatemodel", $scope.model);
                    hasResult.then(function (result) {  // this is only run after $http completes
                        toastr.success('Source updated Successfully.', {
                            closeButton: true
                        });
                        $uibModalInstance.dismiss('cancel');
                    });
                }
            }]);
        },
        selectModelFile: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/model/details/select-source.html", size, $scope,
            ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                },
                $scope.selectSource = function() {
                    $scope.model = $scope.selectedModel;
                    $scope.model.modelFile = slmlpService.appConfig.CURRENT_FILE;
                    var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/model/savemodel", $scope.model);
                    hasResult.then(function (result) {  // this is only run after $http completes
                        toastr.success('Model saved successfully.', {
                            closeButton: true
                        });
                        $scope.modelDetailsObject.selectModel(result);
                        $uibModalInstance.dismiss('cancel');
                    });
                }
            }]);
        },
        getSource : function(model){
            $scope.fileNavigator.listByPath(model.modelFileSrc);
            //$scope.fileNavigator.refresh();
        },
        publishDataset: function (size) {
            $scope.selected = [];
            angular.forEach($scope.modelDetailsElement.modelLists, function (obj, key) {
                if (obj.checked) {
//                    console.log(obj)
                    $scope.selected.push(obj);
                }
            });

            if($scope.selected.length == 1) {
                slmlpService.dialogService.openDialog("views/projects/project-details/model/publish.html", size, $scope,
                        ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                                $scope.publish.modelPath = 'root/dataSet/dataSet/az-dataset.csv';
                                $scope.publish.destination = 'root/dataSet/dataSet-1/az-dataset.csv'
                                $scope.publish.path = 'root/dataSet/dataSet-2/az-dataset.csv'
                                $scope.modelDetailsObject.save = function (publish) {
                                    $scope.publishForm.submitted = true;
                                    if (true) {
                                        $scope.publishForm.submitted = false;
                                        $uibModalInstance.dismiss('cancel');
                                    }
                                }
                                $scope.cancel = function () {
                                    $uibModalInstance.dismiss('cancel');
                                };

                            }]);
            }
            if ($scope.selected.length == 0) {
                toastr.error('Please choose atleast one model', {closeButton: true});
            } else if($scope.selected.length > 1){
                 toastr.error('Only one model can be publish at a time', {closeButton: true});
            }
        },
         trainingDataset: function () {
            $scope.selected = [];

            angular.forEach($scope.modelDetailsElement.modelLists, function (obj, key) {
                if (obj.checked) {
                    var currentObj = {
                        id: obj.id,
                        name: obj.name,
                        modelFile: obj.modelFileSrc
                    }

                    $scope.selected.push(currentObj);

                }
            });
            slmlpService.localStorageService.set("selectedModel", $scope.selected);

            if($scope.selected.length > 0) {
                var allLinked = false;
                angular.forEach($scope.selected, function (obj, key) {
                    if(!obj.modelFile || obj.modelFile == null || obj.modelFile == '') {
                        toastr.error("selected models doesn't have script please link it.", {closeButton: true});
                        allLinked = false;
                    } else {
                        allLinked = true;
                    }
                });
                if(allLinked) {
                     $state.go('model.train')
                }

            }
            if ($scope.selected.length == 0) {
                toastr.error('Please choose atleast one model', {closeButton: true});
            }

        },
        list: function (pageNumber) {
            $scope.modelDetailsElement.listLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var hasactionServer = slmlpService.crudService.listAllByGatewayWithPagination("containers/container/api/model/listbyisactive", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit}, "&projectId="+$scope.projectId);
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasactionServer = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_GATEWAY_URL + "containers/container/api/model/findbysearchtext?=" + "lang=" + language + encodeURI($scope.filter) + "&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
                    "limit": limit
                });
            }
            hasactionServer.then(function (result) {  // this is only run after $http completes
                $scope.modelDetailsElement.listLoader = false;
                $scope.modelDetailsElement.modelLists = result;
                // For pagination
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.modelDetailsObject.list;
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
        delete: function (size, item) {
            slmlpService.dialogService.openDialog("views/delete-confirm.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {
                                $scope.confirmDelete = false;
                                setTimeout(function () {
                                    $scope.$apply(function () {
                                        var hasResult = slmlpService.crudService.softDeleteByGateway("containers/container/api/model", item);
                                        hasResult.then(function (result) {
                                            $state.reload();
                                            $scope.modelDetailsObject.list(1);
                                            toastr.success('Deleted Successfully.', {
                                                closeButton: true
                                            });
                                        })
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },



        getTrainingJobsList: function (pageNumber) {
            $scope.modelDetailsElement.trainingJobsLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            slmlpService.appConfig.sort.sortBy = "id";
            var hasList = slmlpService.crudService.listAllByGatewayWithPagination("containers/container/api/trainingjobs/list",
            slmlpService.appConfig.paginationHeaders(pageNumber, limit), {"limit": limit, "sortBy": "id"});
            var results = [];
            hasList.then(function (result) {
                $scope.modelDetailsElement.trainingJobsLoader = false;
                $scope.modelDetailsElement.trainingJobsLists = result;
            });

            $http.get("views/json/training-jobs.json").success(function (result) {
                //$scope.modelDetailsElement.trainingJobsLists = result;
            });
        },
        getTrainingJobsListByModel: function (pageNumber, model) {
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            slmlpService.appConfig.sort.sortBy = "id";
            var hasList = slmlpService.crudService.listAllByGatewayWithPagination("containers/container/api/trainingjobs/listbymodel",
            slmlpService.appConfig.paginationHeaders(pageNumber, limit), {"limit": limit, "sortBy": "id"}, "&modelId="+model.id);
            var results = [];
            hasList.then(function (result) {
                $scope.modelDetailsElement.evaluationLists = result;
            });

            $http.get("views/json/training-jobs.json").success(function (result) {
                //$scope.modelDetailsElement.trainingJobsLists = result;
            });
        },
        // ADD MODEL
        addModel: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/model/create/create.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.modelDetailsObject.save = function (analyze) {
                                $scope.createModelForm.submitted = true;
                                if (true) {
                                    $scope.createModelForm.submitted = false;
                                    $scope.modelDetailsElement.modelLists.push(model);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        init: function () {
            $scope.selectedModel = slmlpService.localStorageService.get("selectModel");
            $scope.projectId = slmlpService.localStorageService.get("projectId");
            $scope.project = slmlpService.localStorageService.get("projectRef");
            $scope.loginUser = slmlpService.localStorageService.get("user_name");
            $scope.modelDetailsElement = {
                listLoader: false,
                trainingJobsLoader: false
            };
            $scope.model = {
                projectId : $scope.projectId,
                createdBy: $scope.loginUser
            };
            $scope.type = "";
            $scope.modelDetailsElement.versionLists = [];
            $scope.modelDetailsObject.list(1);
            $scope.modelDetailsObject.getTrainingJobsList(1);
            $scope.dataset = {};
            $scope.publish = {};
            slmlpService.localStorageService.set("selectedModel", null);
            $scope.modelDetailsObject.getModelSummaryList();
            $scope.modelDetailsObject.getModelDatasetList();
            $scope.modelDetailsObject.getInputSchemaList();
        }
    }
    $scope.modelDetailsObject.init();
}

angular
        .module('slmlp')
        .controller('modelDetailsCtrl', modelDetailsCtrl);
