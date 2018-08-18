function scheduledDeploymentsCtrl($scope, slmlpService,$http) {
    /** Generic object for the controller. */
    $scope.scheduledDeploymentsObject = {};
    $scope.listPerPage = "5";
    
    $scope.scheduledDeploymentsObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getScheduledDeploymentsList: function () {
            $http.get("views/json/scheduled-deployments.json").success(function (result) {
                $scope.scheduledDeploymentsElement.scheduledDeploymentsLists = result;
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
                                        $scope.scheduledDeploymentsElement.scheduledDeploymentsLists.splice($scope.scheduledDeploymentsElement.scheduledDeploymentsLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        // ADD NEW SCHEDULED DEPLOYMENTS
        newScheduledDeployments: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/scheduled-deployments/create.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.scheduledDeploymentsObject.save = function (analyze) {
                                $scope.createModelForm.submitted = true;
                                if (true) {
                                    $scope.createModelForm.submitted = false;
                                    $scope.scheduledDeploymentsElement.scheduledDeploymentsLists.push(scheduledDeployments); 
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        init: function () {
            $scope.scheduledDeploymentsElement = {};
            $scope.scheduledDeploymentsObject.getScheduledDeploymentsList();
            $scope.dataset = {};
            $scope.scheduledDeployments={};
            $scope.scheduledDeployments.time = new Date();
        }
    }
    $scope.scheduledDeploymentsObject.init();
}

angular
        .module('slmlp')
        .controller('scheduledDeploymentsCtrl', scheduledDeploymentsCtrl);