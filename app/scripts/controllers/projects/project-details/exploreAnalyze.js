function exploreAnalyzeCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.exploreAnalyzeObject = {};
    $scope.exploreAnalyzeObject = {
        getDatasetExploreList: function () {
            $http.get("views/json/explore-data.json").success(function (result) {
                $scope.exploreAnalyzeElement.exploreDataLists = result;
            });
        },
        getTrainingJobsList: function () {
            $http.get("views/json/training-jobs.json").success(function (result) {
                $scope.exploreAnalyzeElement.trainingJobsLists = result;
            });
        },
        listAppeal: function () {
            $scope.listAppeal = true;
        },
        addColumn: function () {
            $scope.exploreAnalyzeElement.schemaLists.push({
                "id": "",
                "name": "",
                "type": "string",
                "autoDetect": ""
            });
        },
        process: function () {
            $scope.processList = true;
            $http.get("views/json/process.json").success(function (result) {
                $scope.exploreAnalyzeElement.processLists = result;
            });
        },
        previousTab: function (index) {
            $scope.exploreAnalyzeObject.showTabByIndex(index);
        },
        cancel: function () {
            $scope.exploreAnalyzeObject.showTabByIndex(0);
        },
        savePreProcess: function (index) {
            if (true) {
                $scope.exploreAnalyzeObject.showTabByIndex(index);
                $http.get("views/json/schema.json").success(function (result) {
                    $scope.exploreAnalyzeElement.schemaLists = result;
                });
            }
        },
        saveSchema:function (index) {
            if (true) {
                $scope.exploreAnalyzeObject.showTabByIndex(index);
                $http.get("views/json/explore-data.json").success(function (result) {
                    $scope.exploreAnalyzeElement.exploreDataLists = result;
                });
            }
        },
        save: function (index) {
            if (true) {
                $scope.exploreAnalyzeObject.showTabByIndex(index);
            }
        },
        showTabByIndex: function (index) {
            var formWizard = $('#custom_form_wizard');
            var total = 3;
            var current = index + 1;
            var $percent = (current / total) * 100;
            $('#custom_form_wizard').find('.progress-bar').css({
                width: $percent + '%'
            });

            for (var i = 0; i < total; i++) {
                $scope.exploreAnalyzeWizard['tab_' + i] = false;
            }
            $scope.exploreAnalyzeWizard['tab_' + index] = true;
            jQuery('li', $('#custom_form_wizard')).removeClass("done");
            var li_list = $('#custom_form_wizard').find('li');
            for (var i = 0; i < index; i++) {
                jQuery(li_list[i]).addClass("done");
            }
        },
          // PUBLISH         
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
                                        $scope.exploreAnalyzeElement.schemaLists.splice($scope.exploreAnalyzeElement.schemaLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        init: function () {
            $scope.exploreAnalyzeWizard = [];
            $scope.exploreAnalyzeElement = {
                processLists: [],
                schemaLists: []
            };
            $scope.exploreAnalyze = {
                "customerId": "customer_id",
                "ip": "ip",
                "ipCountry": "ip_country",
                "pagesVisited": "pages_visited",
                "campain": "campain",
                "revenue": "revenue"
            };
            $scope.publish={};
            $scope.exploreAnalyzeObject.getDatasetExploreList();
            $scope.exploreAnalyzeObject.getTrainingJobsList();
        }

    }

    $scope.exploreAnalyzeObject.init();

}

angular
        .module('slmlp')
        .controller('exploreAnalyzeCtrl', exploreAnalyzeCtrl);