function createDashboardCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.createDashboardObject = {};
    $scope.datasetInsightList = ["crm_and_web_history_enriched", "web_history_prepared", "crm_and_web_history", "AZDatasetOutput", "AZ_Dataset", "AZ_Employee_DatasetOutput", "AZ_Employee_Dataset"];
    $scope.chartInsightList = ["Count by 3.5 on iris_data_prepared", "Chart on iris_data_prepared", "Chart on iris_data"];
    $scope.commentsInsightList = ["Comments 1", "Comments 2", "Comments 3", "Comments 4", "Comments 5", "Comments 6", "Comments 7", "Comments 8"];
    $scope.launchersInsightList = ["RStudio_session", "Jupyter_session1", "Jupyter_session2", "Terminal", "RStudio_session 2"];
    $scope.metricInsightList = ["Metric 1", "Metric 2", "Metric 3", "Metric 4", "Metric 5"];
    $scope.staticInsightList = ["Static insight 1", "Static insight 2", "Static insight 3", "Static insight 4", "Static insight 5"];
    $scope.savedModelReportInsightList = ["Full report of model Prediction (DECISION_TREE_CLASSIFICATION) on iris_data_prepared", "Full report of model Prediction (SCIKIT_MODEL) on iris_data_prepared", "Full report of model Prediction (LARS) on iris_data_prepared_preparedj"];
    $scope.standardItems = [
        {sizeX: 2, sizeY: 2, row: 1, col: 2, name: "Item 2"},
        {sizeX: 2, sizeY: 2, row: 1, col: 4, name: "Item 3"},
        {sizeX: 2, sizeY: 2, row: 2, col: 0, name: "Item 4"},
        {sizeX: 3, sizeY: 3, row: 0, col: 0, name: "Item 5"},
        {sizeX: 3, sizeY: 3, row: 0, col: 4, name: "Item 6"}
    ];


    $scope.isHorizontal = true;
    var tmpList = [];

    for (var i = 1; i <= 6; i++) {
        tmpList.push({
            text: 'Item ' + i,
            value: i
        });
    }

    $scope.list = tmpList;

    $scope.gridsterOpts = {
        resizable: {
            enabled: true,
            handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
            stop: function (event, $element, widget) {
                var id = "#chart" + $element.find('li').prevObject[0].id;
                $scope.createDashboardObject.generateChart(id);
            } // optional callback fired when item is finished resizing
        },
    }
    $scope.createDashboardObject = {
        generateChart: function (id) {
            setTimeout(function () {
                var chart = c3.generate({
                    bindto: id,
                    color: {
                        pattern: ['#50B050','#C1E2EC','#FF7F0E']
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
                    axis: {
                        y: {
                            label: {
                                text: 'Y Label',
                                position: 'outer-middle'
                            },
                            tick: {
                                format: d3.format("$,") // ADD
                            }
                        },
                        y2: {
                            show: false,
                            label: {
                                text: 'Y2 Label',
                                position: 'outer-middle'
                            }
                        }
                    },
                });

            }, 1000);

        },
        duplicateItem: function (item, index) {
            var standardItemsLength = $scope.standardItems.length;
            var name = 'item' + standardItemsLength;
            $scope.standardItems.push({sizeX: 3, sizeY: 3, name: item.name});
            var id = "#chart" + standardItemsLength;
            $scope.createDashboardObject.generateChart(id);
        },
        delete: function (item) {
            $scope.standardItems.splice($scope.standardItems.indexOf(item), 1);
        },
        getDashboardTilesList: function () {
            $http.get("views/json/dashboard-tiles.json").success(function (result) {
                $scope.createDashboardElement.dashboardTilesLists = result;
            });
        },
        addTiles: function (size, item) {
            $scope.dashboardTile = item;
            $scope.existingDataset = false;
            slmlpService.dialogService.openDialog("views/projects/project-details/deployments/dashboard/add-tiles.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },
        publishDashboard: function (size, item) {
            $scope.dashboardTile = item;
            $scope.existingDataset = false;
            slmlpService.dialogService.openDialog("views/projects/project-details/deployments/dashboard/publish.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },
        deployToServer: function (size, item) {
            $scope.dashboardTile = item;
            $scope.existingDataset = false;
            slmlpService.dialogService.openDialog("views/projects/project-details/deployments/dashboard/deploy-to-server.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },
        toggle: function () {
            $scope.existingInsight = !$scope.existingInsight;
        },
        init: function () {
            $scope.createDashboardElement = {};
            $scope.createDashboardObject.getDashboardTilesList();
            $scope.createDashboardObject.generateChart('#chart0');
            $scope.createDashboardObject.generateChart('#chart1');
            $scope.createDashboardObject.generateChart('#chart2');
            $scope.createDashboardObject.generateChart('#chart3');
            $scope.createDashboardObject.generateChart('#chart4');
            $scope.jobs = {};
        }
    }

    $scope.createDashboardObject.init();

}

angular
        .module('slmlp')
        .controller('createDashboardCtrl', createDashboardCtrl);
