function continuousIntegrationCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.continuousIntegrationObject = {};
    $scope.continuousIntegrationObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getContinuousIntegrationList: function () {
            $http.get("views/json/continuous-integration.json").success(function (result) {
                $scope.continuousIntegrationElement.continuousIntegrationLists = result;
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
                                        $scope.continuousIntegrationElement.continuousIntegrationLists.splice($scope.continuousIntegrationElement.continuousIntegrationLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        init: function () {
            $scope.continuousIntegrationElement = {};
            $scope.continuousIntegrationObject.getContinuousIntegrationList();
            $scope.continuousIntegration = {};
        }
    }

    $scope.continuousIntegrationObject.init();

}

angular
        .module('slmlp')
        .controller('continuousIntegrationCtrl', continuousIntegrationCtrl);
