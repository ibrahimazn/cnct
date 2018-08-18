function featuresCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.featuresObject = {};
    $scope.listPerPage = "5";
    $scope.featuresObject = {
        tabSet:function(showHide,index){
            $scope['opentab'+index]=showHide;
        },
        getFeaturesList: function () {
            $http.get("views/json/features.json").success(function (result) {
                $scope.featuresElement.featuresLists = result;
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
                                        $scope.featuresElement.featuresLists.splice($scope.featuresElement.featuresLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        init: function () {
            $scope.featuresElement = {};
            $scope.featuresObject.getFeaturesList();
            $scope.algorithms = {};
        }
    }

    $scope.featuresObject.init();

}

angular
        .module('slmlp')
        .controller('featuresCtrl', featuresCtrl);
