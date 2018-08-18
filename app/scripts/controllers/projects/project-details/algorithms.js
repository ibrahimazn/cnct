function algorithmsCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.algorithmsObject = {};
    $scope.listPerPage = "5";
    $scope.algorithmsObject = {
        optionToggled: function (list, tabName) {
            if (tabName == 'launchers') {
                $scope.project.launchers.checkAll = list.every(function (itm) {
                    return itm.checked == true;
                })
            } else {
                $scope.project.trainingEngines.checkAll = list.every(function (itm) {
                    return itm.checked == true;
                })
            }
        },
        addCustomPython: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/model/create/custom-python.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.algorithmsObject.save = function (analyze) {
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

        checkAll: function (isCheckedAll) {
            angular.forEach($scope.algorithmsElement.algorithmsLists, function (obj, key) {
                console.log(obj);
                obj.selected = isCheckedAll;
            });
        },
        getPreviewAnalyzeLists: function () {
            $http.get("views/json/preview-analyze-data.json").success(function (result) {
                $scope.algorithmsElement.algorithmsPreviewLists = result;
            });
        },
        featuresPredictionLists: function () {
            $http.get("views/json/features-prediction.json").success(function (result) {
                $scope.algorithmsElement.featuresPredictionLists = result;
            });
        },
        previewList: function (showList) {
            $scope.previewList = showList;
        },
        tabSet: function (showHide, index) {
            $scope['opentab' + index] = showHide;
        },
        getAlgorithmsList: function () {
            $http.get("views/json/algorithms.json").success(function (result) {
                $scope.algorithmsElement.algorithmsLists = result;
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
                                        $scope.algorithmsElement.algorithmsLists.splice($scope.algorithmsElement.algorithmsLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        getLaunchesList: function () {
            var hasResult = slmlpService.crudService.listAll("launcher/listbyisactive");
            hasResult.then(function (result) {
                $scope.algorithmsElement.launchersLists = result;
            });

        },
        launchEditor: function (launcher) {
            $scope.launchStatus = "Started";
            slmlpService.dialogService.openDialog("views/projects/project-details/file-manager/start-launcher.html", "md", $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            setTimeout(function () {
                                $uibModalInstance.dismiss('cancel');
                            }, 2000);
                            $scope.cancel = function () {

                            };
                        }]);

        },
        init: function () {
            $scope.algorithmsElement = {};
            $scope.algorithmsObject.getAlgorithmsList();
            $scope.algorithmsObject.getPreviewAnalyzeLists();
            $scope.algorithmsObject.featuresPredictionLists();
            $scope.algorithmsObject.getLaunchesList();
            $scope.algorithms = {};
        }
    }

    $scope.algorithmsObject.init();

}

angular
        .module('slmlp')
        .controller('algorithmsCtrl', algorithmsCtrl);
