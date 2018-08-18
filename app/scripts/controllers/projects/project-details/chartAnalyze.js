function chartAnalyzeCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.chartAnalyzeObject = {};
    $scope.showPrivilege = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };

    $scope.steps = ["Filter rows/cells on value (invalid - no desc)", "Remove Columns", "Delete/Keep  columns by name (invalid)", "Split  column (invalid - no desc)", "Flag  invalid rows (invalid)"];
    // set up sortable options
    $scope.sortableOptions = {
        items: '.sort-drag',
        placeholder: "item",

//        stop: function(e, ui) {
//            // do something here
//        }
    };
    $scope.chartAnalyzeObject = {
        getChartAnalyzeLists: function () {
            $http.get("views/json/chart-analyze-data.json").success(function (result) {
                $scope.chartAnalyzeElement.chartAnalyzeLists = result;
            });
        },
        generateChart: function () {
            setTimeout(function () {
                var chart = c3.generate({
                    bindto: '#chart',
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
                    }
                });
//               chart.resize({width:610});
                $(window).trigger('resize');
            }, 1000);
        },
        // ADD NEW STEP
        addChartOption: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/analyze/add-chart-option.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.chartAnalyzeObject.save = function (step) {
                                $scope.addNewStepForm.submitted = true;
                                if (true) {
                                    $scope.addNewStepForm.submitted = false;
                                    $scope.previewAnalyzeElement.previewAnalyzeSteps.push(step);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        delete: function (item, deleteItemAxis) {
            if (deleteItemAxis == 'xaxis') {
                $scope.selectedGroupBars = [];
                $scope.selectedXaxis.splice($scope.selectedXaxis.indexOf(item), 1);
            } else if (deleteItemAxis == 'yaxis') {
                $scope.selectedYaxis.splice($scope.selectedYaxis.indexOf(item), 1);
            } else if (deleteItemAxis == 'groupBars') {
                $scope.selectedGroupBars.splice($scope.selectedGroupBars.indexOf(item), 1);
            } else if (deleteItemAxis == 'filter') {
                $scope.selectedFilter.splice($scope.selectedFilter.indexOf(item), 1);
            } else if (deleteItemAxis == 'animation') {
                $scope.selectedAnimation.splice($scope.selectedAnimation.indexOf(item), 1);
            } else if (deleteItemAxis == 'subcharts') {
                $scope.selectedSubcharts.splice($scope.selectedSubcharts.indexOf(item), 1);
            } else if (deleteItemAxis == 'tooltip') {
                $scope.selectedTooltip.splice($scope.selectedTooltip.indexOf(item), 1);
            }

        },
        refresh: function () {
            $scope.chartAnalyzeElement.chartLoader = false;
        },
        init: function () {
            $scope.chartAnalyzeElement = {
                isCollapsedFilter: true
            };
            $scope.chartAnalyze = {};
            $scope.sampleEngine = {};
            $scope.selectedXaxis = [];
            $scope.selectedYaxis = [];
            $scope.selectedFilter = [];
            $scope.selectedGroupBars = [];
            $scope.selectedAnimation = [];
            $scope.selectedSubcharts = [];
            $scope.selectedTooltip = [];
            $scope.chartAnalyzeObject.getChartAnalyzeLists();
//            $scope.chartAnalyzeObject.generateChart();
            $scope.showPrivilege(1);
            $scope.chartAnalyzeLists = [
                {
                    "name": "count of records",
                    "type": "symbol",
                    "id": "0"
                },
                {
                    "name": "col_0",
                    "type": "char",
                    "id": "1"
                },
                {
                    "name": "col_1",
                    "type": "char",
                    "id": "2"
                },
                {
                    "name": "col_2",
                    "type": "char",
                    "id": "3"
                },
                {
                    "name": "col_3",
                    "type": "char",
                    "id": "4"
                }
            ];

        }

    }

    $scope.sortableOptions = {
        connectWith: ".connected-apps-container",
        start: function (e, ui) {
            $scope.sourceModelClone = ui.item.sortable.sourceModel.slice();
        },
        stop: function (e, ui) {
            // if the element is removed from the first container
            if (
                    $(e.target).hasClass("first") &&
                    ui.item.sortable.droptarget &&
                    e.target != ui.item.sortable.droptarget[0]
                    ) {
                var selectedXaxis = $scope.selectedXaxis;
                selectedXaxis = selectedXaxis.slice(-1);
                $scope.selectedXaxis = selectedXaxis;

                var selectedGroupBars = $scope.selectedGroupBars;
                selectedGroupBars = selectedGroupBars.slice(-1);
                $scope.selectedGroupBars = selectedGroupBars;

                var selectedAnimation = $scope.selectedAnimation;
                selectedAnimation = selectedAnimation.slice(-1);
                $scope.selectedAnimation = selectedAnimation;

                var selectedSubcharts = $scope.selectedSubcharts;
                selectedSubcharts = selectedSubcharts.slice(-1);
                $scope.selectedSubcharts = selectedSubcharts;

                ui.item.sortable.sourceModel.length = 0;
                // clone the original model to restore the removed item
                Array.prototype.push.apply(
                        ui.item.sortable.sourceModel,
                        $scope.sourceModelClone
                        );
            }
        }
    };

    $scope.chartAnalyzeObject.init();
}

angular
        .module('slmlp')
        .controller('chartAnalyzeCtrl', chartAnalyzeCtrl);