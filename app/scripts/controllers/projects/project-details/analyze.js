function analyzeCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.analyzeObject = {};
    $scope.listPerPage = "5";
    $scope.analyzeObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getAnalyzeList: function () {
            $http.get("views/json/analyze.json").success(function (result) {
                $scope.analyzeElement.analyzeLists = result;
            });
        },
        // ADD ANALYZE
        addAnalyze: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/analyze/create.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.analyzeObject.save = function (analyze) {
                                $scope.analyzeForm.submitted = true;
                                if (true) {
                                    $scope.analyzeForm.submitted = false;
                                    $scope.analyzeElement.analyzeLists.push(analyze); 
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        init: function () {
            $scope.project = slmlpService.localStorageService.get("projectRef");
            $scope.analyzeElement = {};
            $scope.analyzeObject.getAnalyzeList();
            $scope.analyze = {};
        }
    }

    $scope.analyzeObject.init();

}

angular
        .module('slmlp')
        .controller('analyzeCtrl', analyzeCtrl);
