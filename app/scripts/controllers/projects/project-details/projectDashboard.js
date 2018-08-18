function projectDashboardCtrl($scope, slmlpService, $http, $stateParams) {
    /** Generic object for the controller. */
    $scope.projectDashboardObject = {};
    $scope.createDashboardObject = {
        generateChart: function (id) {
            setTimeout(function () {
                var chart = c3.generate({
                    bindto: id,
                    color: {
                        pattern: ['#50B050', '#C1E2EC', '#FF7F0E']
                    },
                    data: {
                        columns: [
                            ['data1', 30, 200, 100, 400, 150, 250],
                            ['data2', 50, 20, 10, 40, 15, 25],
                            ['data3', 50, 20, 10, 40, 15, 25]
                        ],
                        axes: {
                            data2: 'y2'
                        },
                        types: {
                            data2: 'bar'
                        }
                    },
                });

            }, 1000);

        },
        getLaunchersList: function () {
            $http.get("views/json/launchers.json").success(function (result) {
                $scope.createDashboardElement.launchersLists = result;
            });
        },
        
        deploymentData: function() {
            $scope.createDashboardElement.listLoader = true;
            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/modelDeployment/predictedData", "&deploymentId="+ $scope.deploymentId)
            var results = [];
            hasList.then(function (result) {
                console.log("test", result)
                $scope.createDashboardElement.listLoader = false;
                $scope.createDashboardElement.deploymentList = result;
                $scope.predictedColumn = $scope.createDashboardElement.deploymentList.columns.indexOf($scope.createDashboardElement.deploymentList.predicted_column);
            });
        },
        
        
        init: function () {
            $scope.predictedColumn = 0;
            $scope.deploymentId = $stateParams.deploymentId ;
            $scope.createDashboardElement = {};
            $scope.createDashboardObject.getLaunchersList();
            $scope.createDashboardObject.generateChart('#chart');
            $scope.createDashboardObject.deploymentData();
            $scope.jobs = {};
        }
    }

    $scope.createDashboardObject.init();

}

angular
        .module('slmlp')
        .controller('projectDashboardCtrl', projectDashboardCtrl);
