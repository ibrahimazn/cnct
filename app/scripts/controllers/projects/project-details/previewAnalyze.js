function previewAnalyzeCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.previewAnalyzeObject = {};
    $scope.powerOff = false;
    $scope.toggleFilter = function(step, name) {
        step.toggle = !step.toggle;
    };
    // set up sortable options
    $scope.sortableOptions = {
        items: '.sort-drag',
        placeholder: "item",
    };
     $scope.showPrivilege = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };
    $scope.previewAnalyzeObject = {
        getPreviewAnalyzeSteps: function () {
            $http.get("views/json/preview-analyze-steps.json").success(function (result) {
                $scope.previewAnalyzeElement.previewAnalyzeSteps = result;
            });
        },
        getPreviewAnalyzeLists: function () {
            $http.get("views/json/preview-analyze-data.json").success(function (result) {
                $scope.previewAnalyzeElement.previewAnalyzeLists = result;
            });
        },
        // ADD NEW STEP
        addNewStep: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/analyze/add-new-step.html", size, $scope,
            ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                    $scope.previewAnalyzeObject.save = function (processor) {
                        $scope.addNewStepForm.submitted = true;
                        if (true) {
                            $scope.addNewStepForm.submitted = false;
                            $scope.previewAnalyzeElement.previewAnalyzeSteps.push(processor); 
                            $uibModalInstance.dismiss('cancel');
                        }
                    }
                    $scope.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                    };

                }]);
        },
        // DELETE PROCESSOR STEP
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
                                $scope.previewAnalyzeElement.previewAnalyzeSteps.splice($scope.previewAnalyzeElement.previewAnalyzeSteps.indexOf(item), 1);
                                $uibModalInstance.dismiss('cancel');
                            })
                        }, 1000);
                    };

                }]);
        },
        init: function () {
            $scope.previewAnalyzeElement = {
                steps: {
                    id: 1,
                    name: "ADMIN",
                    groups: [
                        {
                            id: 1,
                            name: "Filter data",
                            processors: [
                                {
                                    name: "Filter rows/cells on value",
                                    type:"Filter"
                                    
                                },
                                {
                                    name: "Flag rows on value",
                                    type:"Flag"
                                    
                                },
                                {
                                    name: "Delete / Keep columns by name",
                                    type:"Delete"
                                    
                                },
                                {
                                    name: "Filter rows/cells on numerical range",
                                    type:"Filter"
                                    
                                },
                                {
                                    name: "Flag rows on numerical range",
                                    type:"Flag"
                                    
                                },
                                {
                                    name: "Filter rows/cells on date range",
                                    type:"Filter"
                                    
                                },
                                {
                                    name: "Flag rows/cells on date range",
                                    type:"Flag"
                                    
                                },
                                {
                                    name: "Filter rows/cells with formula",
                                    type:"Filter"
                                    
                                },
                                {
                                    name: "Flag rows with formula",
                                    type:"Flag"
                                    
                                }
                            ]
                        },
                        {
                            id: 2,
                            name: "Data cleansing",
                            processors: [
                                {
                                    name: "Negate boolean value",
                                    type:"Negate"
                                },
                                {
                                    name: "Normalize measure",
                                    type:"Negate"
                                },
                                {
                                    name: "Filter invalid rows/cells",
                                    type:"Filter"
                                },
                                {
                                    name: "Flag invalid rows",
                                    type:"Flag"
                                }
                            ]
                        },
                        {
                            id: 3,
                            name: " Split / Extract ",
                            processors: [
                                {
                                    name: "Split column",
                                    type:"Split"
                                },
                                {
                                    name: "Extract with regular expression",
                                    type:"Extract"
                                },
                                {
                                    name: "Split currencies in column",
                                    type:"Split"
                                },
                                {
                                    name: "Split invalid cells into another column",
                                    type:"Split"
                                },
                                {
                                    name: "Split e-mail addresses",
                                    type:"Split"
                                },
                                {
                                    name: "Unnest object (flatten JSON)",
                                    type:"Unnest"
                                },
                                {
                                    name: "Extract with JSONPath",
                                    type:"Extract"
                                }
                            ]
                        },
                        {
                            id: 4,
                            name: "Code",
                            processors: [
                                {
                                    name: "Formula",
                                    type:"Formula"
                                },
                                {
                                    name: "Filter rows/cells with formula",
                                    type:"Filter"
                                },
                                {
                                    name: "Flag rows with formula",
                                    type:"Flag"
                                },
                                {
                                    name: "Python function",
                                    type:"Formula"
                                }
                            ]
                        }
                    ],

                },
                optionsList:[]
            };
            $scope.previewAnalyze = {
                "customerId": "customer_id",
                "ip": "ip",
                "ipCountry": "ip_country",
                "pagesVisited": "pages_visited",
                "campain": "campain",
                "revenue": "revenue"
            };
            $scope.showPrivilege(0);
            $scope.previewAnalyzeObject.getPreviewAnalyzeLists();
            $scope.previewAnalyzeObject.getPreviewAnalyzeSteps();
            $scope.previewAnalyzeElement.previewAnalyzeSteps = [];
        }

    }

    $scope.previewAnalyzeObject.init();

}

angular
        .module('slmlp')
        .controller('previewAnalyzeCtrl', previewAnalyzeCtrl);