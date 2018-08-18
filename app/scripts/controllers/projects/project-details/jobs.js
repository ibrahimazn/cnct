function jobsCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.jobsObject = {};
    $scope.listPerPage = "5";
    $scope.jobsObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getJobsList: function () {
            $http.get("views/json/jobs.json").success(function (result) {
                $scope.jobsElement.jobsLists = result;
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
                                        $scope.jobsElement.jobsLists.splice($scope.jobsElement.jobsLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        init: function () {
            $scope.jobsElement = {};
            $scope.jobsObject.getJobsList();
            $scope.jobs = {};
        }
    }

    $scope.jobsObject.init();

}

angular
        .module('slmlp')
        .controller('jobsCtrl', jobsCtrl);
