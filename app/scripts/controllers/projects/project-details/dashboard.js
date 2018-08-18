function dashboardCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.dashboardObject = {};
    $scope.listPerPage = "5";
    $scope.dashboardObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getDashboardsList: function () {
            $http.get("views/json/dashboard.json").success(function (result) {
                $scope.dashboardElement.dashboardsLists = result;
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
                                        $scope.dashboardElement.dashboardsLists.splice($scope.dashboardElement.dashboardsLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        init: function () {
            $scope.dashboardElement = {};
            $scope.dashboardObject.getDashboardsList();
            $scope.dashboard = {};
        }
    }

    $scope.dashboardObject.init();

}

angular
        .module('slmlp')
        .controller('dashboardCtrl', dashboardCtrl);
