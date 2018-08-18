

function scoringDetailsCtrl($scope,$stateParams, slmlpService,$http, $state, toastr) {
    /** Generic object for the controller. */
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.modelDetailsObject = {};
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'id';
    slmlpService.appConfig.sort.sortBy = 'name';
    slmlpService.appConfig.sort.sortOrder = '+';
    $scope.scoringObject = {};
    $scope.scoringObj = {};
    var language = 'en';
    $scope.tags = [
        { text: 'COLUMN1' },
        { text: 'COLUMN2' },
        { text: 'COLUMN3' }
    ];
//    $scope.listPerPage = "5";
    $scope.page = 1;
    $scope.items = [];
    $scope.fetching = false;

    // Fetch more items
    $scope.getMore = function() {
      $scope.page++;
      $scope.fetching = true;
      $http.get('views/json/scoring-summary.json', { page : $scope.page }).then(function(items) {
        $scope.fetching = false;
        // Append the items to the list
        $scope.items = $scope.items.concat(items);
      });
    };
    $scope.scoringObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getScoringList:function(pageNumber){
            $scope.scoringElement.listLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var hasScoringList = slmlpService.crudService.listAllByGatewayWithPagination("containers/container/api/scoring/listbyisactive", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit}, "&projectId="+$scope.projectId);
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasScoringList = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_GATEWAY_URL + "containers/container/api/scoring/findbysearchtext?=" + "lang=" + language + encodeURI($scope.filter) + "&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
                    "limit": limit
                });
            }
            hasScoringList.then(function (result) {
                $scope.scoringElement.listLoader = false;
                $scope.scoringElement.scoringLists  = result;
            });
        },
        getColumns: function () {
            $scope.columns = [{"columnName":"COLUMN1"},{"columnName":"COLUMN2"},{"columnName":"COLUMN3"},{"columnName":"COLUMN4"},{"columnName":"COLUMN5"}];
        },
        getScoringSummaryList: function () {
            $http.get("views/json/scoring-summary.json").success(function (result) {
                $scope.scoringElement.scoringSummaryLists = result;
            });
        },
        getArtifactList: function () {
            $http.get("views/json/scoring-artifact.json").success(function (result) {
                $scope.scoringElement.scoringArtifactLists = result;
            });
        },
        getModelList: function() {
            var hasModelList = slmlpService.crudService.listAllByGateway("containers/container/api/model/findbyproject", "&projectId="+$scope.projectId);
            hasModelList.then(function (result) {
                $scope.scoringElement.modelLists = result;
            });
        },
        selectModel: function(model){
            $scope.scoringObj.datasetFile = model.datasetFile;
            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/modelversions/listbymodel", "&modelId="+model.id);
            var results = [];
            hasList.then(function (result) {
                $scope.scoringElement.versionLists = result;
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
                                        var hasResult = slmlpService.crudService.softDeleteByGateway("containers/container/api/scoring", item);
                                        hasResult.then(function (result) {
                                            $state.reload();
                                            $scope.scoringObject.getScoringList(1);
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
        scoreModel: function (score) {
            $scope.scoringElement.scoreLoader = true;
            var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/scoring/runScore", score);
            hasResult.then(function (result) {  // this is only run after $http completes
                slmlpService.localStorageService.set("selectScore", result);
                $scope.selectedScore = slmlpService.localStorageService.get("selectScore");
                toastr.success(
                        "Model scored successfully",
                        {
                            closeButton: true
                        }
                );
                $scope.scoringElement.scoreLoader = false;
            }).catch(function (result) {
                $scope.scoringElement.scoreLoader = false;
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
        },
        getScore : function(id){
            var hasServer = slmlpService.crudService.readByGateway("containers/container/api/scoring", id);
            hasServer.then(function (result) {
                slmlpService.localStorageService.set("selectScore", result);
                $scope.selectedScore = result;
            }).catch(function (result) {
            });
        },
        // ADD MODEL
        scoreDataset: function (size) {
            $scope.type = "";
            slmlpService.dialogService.openDialog("views/projects/project-details/scoring/score.html", size, $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                    $scope.scoringObject.save = function (form) {
                        $scope.scoreFormSubmitted = true;
                        if (form.$valid) {
                            $scope.scoreFormSubmitted = false;
                            $scope.scoringElement.scoreLoader = true;
                            var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/scoring/saveScore", $scope.scoringObj);
                            hasResult.then(function (result) {  // this is only run after $http completes

                                    setTimeout(function() {
                                        toastr.success('Created Successfully.', {
                                            closeButton: true
                                        });
                                        $scope.scoringElement.scoreLoader = false;

                                        $scope.cancel();
                                        $scope.scoringObject.getScoringList(1);
                                        $state.reload();
                                    }, 15000);
                                }).catch(function (result) {
                                    $scope.scoringElement.scoreLoader = false;
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
                    $scope.cancel = function () {
                        delete $scope.scoringObj;
                        $uibModalInstance.dismiss('cancel');
                    },
                    $scope.chooseDataset = function(type){
                        $scope.type = type;
                        $scope.scoringElement.chooseFiles = true;
                    },
                    $scope.chooseFile = function(type){
                        $scope.type = type;
                        $scope.scoringElement.chooseFiles = true;
                    },
                    $scope.datasetToBeScored = function (size) {
                        $uibModalInstance.dismiss('cancel');
                        $scope.scoringObject.selectDatasetToBeScore(size);
                    },
                    $scope.fileChoosed = function(){
                        if($scope.type == 'data') {
                            $scope.scoringObj.datasetFile = slmlpService.appConfig.CURRENT_FILE;
                        }
                        if($scope.type == 'output') {
                            $scope.scoringObj.scoreFile = slmlpService.appConfig.CURRENT_FILE;
                        }
                        //$scope.model.datasetFile = "/dataset/data.csv"
                        $scope.scoringElement.chooseFiles = false;
                    },
                    $scope.selectDestination = function (size) {
                        $uibModalInstance.dismiss('cancel');
                        $scope.scoringObject.selectDestination(size);
                    };
                }]);
        },
        // SELECT DATASET TO BE SCORED
        selectDatasetToBeScore: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/scoring/dataset-tobe-scored.html", size, $scope,
            ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                $scope.cancel = function (size) {
                    $uibModalInstance.dismiss('cancel');
                    $scope.scoringObject.scoreDataset(size);
                };
                $scope.selected = function (size) {
                    $uibModalInstance.dismiss('cancel');
                    $scope.scoringObject.scoreDataset(size);
                };
            }]);
        },
        selectScore: function(score){
            slmlpService.localStorageService.set("selectScore", score)
            $scope.selectedScore = slmlpService.localStorageService.get("selectScore");
        },
        // SELECT DESTINATION
        selectDestination: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/scoring/select-destination.html", size, $scope,
            ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                $scope.cancel = function (size) {
                    $uibModalInstance.dismiss('cancel');
                    $scope.scoringObject.scoreDataset(size);
                };
                $scope.selected = function (size) {
                    $uibModalInstance.dismiss('cancel');
                    $scope.scoringObject.scoreDataset(size);
                };
            }]);
        },
        init: function () {
            $scope.selectedScore = slmlpService.localStorageService.get("selectScore");
            $scope.projectId = slmlpService.localStorageService.get("projectId");
            $scope.project = slmlpService.localStorageService.get("projectRef");
            $scope.loginUser = slmlpService.localStorageService.get("user_name");
            $scope.scoringElement = {
                listLoader: false,
                scoreLoader: false
            };
            $scope.scoringObj = {};
            $scope.scoringElement.chooseFiles = false;
            $scope.scoringObject.getScoringList(1);
            $scope.scoringObject.getColumns();
            $scope.scoringObject.getScoringSummaryList();
            $scope.scoringObject.getArtifactList();
            $scope.scoringObject.getModelList();
            if(!angular.isUndefined($stateParams.id1) && $stateParams.id1 > 0){
                $scope.scoringObject.getScore($stateParams.id1);
                $scope.scoringObject.scoreModel($scope.selectedScore);
            }
        }
    }
    $scope.scoringObject.init();
}

angular
        .module('slmlp')
        .controller('scoringDetailsCtrl', scoringDetailsCtrl);
