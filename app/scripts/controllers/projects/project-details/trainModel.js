function trainModelCtrl($scope, slmlpService, $state, $http, toastr) {
    /** Generic object for the controller. */
    $scope.trainingJobObject = {};
    $scope.trainingJobObject = {
        fileList: [],

        directories: [
            {
                type: "folder",
                path: "/"
            }
        ],
        isLoading: false,

        getSubFolders: function (file, clientId, isRestore) {
            var fileManager = $scope.fileManager;

            if (file.path == '/') {
                $scope.trainingJobObject.directories = [
                    {
                        type: "folder-open",
                        path: "/",
                        sub: [
                            {
                                type: "folder",
                                path: "dataSet",
                                size: "1GB",
                                updatedOn: "10.7.17 12:20:12",
                                commit: "edsfcsd34345"
                            },
                            {
                                type: "folder",
                                path: "ver1",
                                size: "2GB",
                                updatedOn: "16.7.17 1:20:12",
                                commit: "eedgtd2124"

                            },
                            {
                                type: "folder",
                                path: "ver2",
                                size: "3GB",
                                updatedOn: "25.8.17 1:20:12",
                                commit: "eedgtd2124"
                            },
                            {
                                type: "file",
                                path: "ver3",
                                size: "5GB",
                                updatedOn: "30.8.17 1:20:12",
                                commit: "e65464edgtd2124"
                            }
                        ]
                    }
                ]

                $scope.trainingJobObject.fileList = [];
                angular.forEach($scope.trainingJobObject.directories[0].sub, function (directories) {
                    $scope.trainingJobObject.fileList.push(directories);
                });
            }
            if (file.path == 'dataSet') {
                $scope.trainingJobObject.directories = [
                    {
                        type: "folder-open",
                        path: "/",
                        sub: [
                            {
                                type: "folder-open",
                                path: "dataSet",
                                sub: [
                                    {
                                        type: "folder",
                                        path: "dataSet-1",
                                        size: "5GB",
                                        updatedOn: "30.8.17 1:20:12",
                                        commit: "e65464edgtd2124"
                                    },
                                    {
                                        type: "file",
                                        path: "sample.json",
                                        size: "5GB",
                                        updatedOn: "30.8.17 1:20:12",
                                        commit: "e65464edgtd2124"
                                    }
                                ]
                            },
                            {
                                type: "folder",
                                path: "ver1",
                            },
                            {
                                type: "folder",
                                path: "ver2",
                            },
                            {
                                type: "file",
                                path: "ver3",
                            }
                        ]
                    }
                ]

                $scope.trainingJobObject.fileList = [];
                angular.forEach($scope.trainingJobObject.directories[0].sub[0].sub, function (directories) {
                    $scope.trainingJobObject.fileList.push(directories)
                });
            }
            if (file.path == 'dataSet-1') {
                $scope.trainingJobObject.directories = [
                    {
                        type: "folder-open",
                        path: "/",
                        sub: [
                            {
                                type: "folder-open",
                                path: "dataSet",
                                sub: [
                                    {
                                        type: "folder-open",
                                        path: "dataSet-1",
                                        sub: [
                                            {
                                                type: "file",
                                                path: "sample.json",
                                                size: "22 Mb",
                                                updatedOn: "14.7.17 12:20:12",
                                                commit: "easa232sdsfcsd34345"
                                            }
                                        ]
                                    },
                                    {
                                        type: "file",
                                        path: "sample.json"
                                    }
                                ]
                            },
                            {
                                type: "folder",
                                path: "ver1",
                            },
                            {
                                type: "folder",
                                path: "ver2",
                            },
                            {
                                type: "file",
                                path: "ver3",
                            }
                        ]
                    }
                ]
                $scope.trainingJobObject.fileList = [];
                angular.forEach($scope.trainingJobObject.directories[0].sub[0].sub[0].sub, function (directories) {
                    $scope.trainingJobObject.fileList.push(directories)
                });
            }
            if (file.path == 'ver1') {
                $scope.trainingJobObject.directories = [
                    {
                        type: "folder-open",
                        path: "/",
                        sub: [
                            {
                                type: "folder",
                                path: "dataSet",
                            },
                            {
                                type: "folder-open",
                                path: "ver1",
                                sub: [
                                    {
                                        type: "file",
                                        path: "ver1sample1.json",
                                        size: "32 Mb",
                                        updatedOn: "14.7.17 12:20:12",
                                        commit: "easa232sdsfcsd34345"
                                    },
                                    {
                                        type: "file",
                                        path: "ver1sample2.json",
                                        size: "2 GB",
                                        updatedOn: "14.7.17 12:20:12",
                                        commit: "easa232sdsfcsd34345"
                                    }
                                ]
                            },
                            {
                                type: "folder",
                                path: "ver2",
                            },
                            {
                                type: "file",
                                path: "ver3",
                            }
                        ]
                    }
                ]
                $scope.trainingJobObject.fileList = [{
                        type: "file",
                        path: "ver1sample1.json",
                        size: "32 Mb",
                        updatedOn: "14.7.17 12:20:12",
                        commit: "easa232sdsfcsd34345"
                    },
                    {
                        type: "file",
                        path: "ver1sample2.json",
                        size: "2 GB",
                        updatedOn: "14.7.17 12:20:12",
                        commit: "easa232sdsfcsd34345"
                    }];
            }
            if (file.path == 'ver2') {
                $scope.trainingJobObject.directories = [
                    {
                        type: "folder-open",
                        path: "/",
                        sub: [
                            {
                                type: "folder",
                                path: "dataSet",
                            },
                            {
                                type: "folder",
                                path: "ver1",
                            },
                            {
                                type: "folder-open",
                                path: "ver2",
                                sub: [
                                    {
                                        type: "file",
                                        path: "ver2sample.json",
                                        size: "22 Mb",
                                        updatedOn: "14.7.17 12:20:12",
                                        commit: "easa232sdsfcsd34345"
                                    },
                                    {
                                        type: "file",
                                        path: "ver2sample2.json",
                                        size: "22 Mb",
                                        updatedOn: "14.7.17 12:20:12",
                                        commit: "easa232sdsfcsd34345"
                                    }
                                ]
                            },
                            {
                                type: "file",
                                path: "ver3",
                            }
                        ]
                    }
                ]
                $scope.trainingJobObject.fileList = [{
                        type: "file",
                        path: "ver2sample.json",
                        size: "22 Mb",
                        updatedOn: "14.7.17 12:20:12",
                        commit: "easa232sdsfcsd34345"
                    },
                    {
                        type: "file",
                        path: "ver2sample2.json",
                        size: "22 Mb",
                        updatedOn: "14.7.17 12:20:12",
                        commit: "easa232sdsfcsd34345"
                    }];
            }
        },
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getDatasetList: function() {
            var hasResult = slmlpService.crudService.listAll("dataset/list", "&projectId=" + $scope.projectId);
            hasResult.then(function (result) { // this is only run after $http
                $scope.trainModelElement.datasetLists = result;
            });
        },
        getTrainingEnginesList: function() {
            $scope.trainModelElement.trainingEngineLists = slmlpService.localStorageService.get("projectRef").trainingEngines;
        },

        openChooseFileWindow: function(size, type) {
            slmlpService.dialogService.openDialog("views/projects/project-details/model/train/choose-dataset.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {

                $scope.chooseFile = function() {
                    $scope.trainingJob.filePath = slmlpService.appConfig.CURRENT_FILE;
                    $scope.trainingJob[type] = slmlpService.appConfig.CURRENT_FILE;
                     $uibModalInstance.dismiss('cancel');
                }
                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

            }]);
        },

        startTraining: function() {
            $scope.trainingJobForm.submitted = true;
            console.log("$scope.trainingJobForm", $scope.trainingJobForm);
            if($scope.trainingJobForm.$valid) {
                $scope.trainModelElement.startTrainingLoader = true;
                var trainingJob = {
                    dataSetId: $scope.trainModelElement.modelLists[0].dataSetId,
                    filePath: $scope.trainModelElement.modelLists[0].datasetFile,
                    outputPath: $scope.trainingJob.outputPath,
                    models: $scope.trainModelElement.modelLists,
                    testRatio: $scope.trainingJob.testRatio
                };
                var hasResult = slmlpService.crudService.PostViaGateway("containers/container/api/trainingjobs/startTraining", trainingJob);
                hasResult.then(function (result) {
                    toastr.success('Added Successfully.', {
                        closeButton: true
                    });
                    $scope.trainModelElement.startTrainingLoader = false;
                    $state.reload();
                    $state.go('model.list')
                });

            }
        },

        getModelList: function () {
            $scope.trainModelElement.modelLists = slmlpService.localStorageService.get("selectedModel");
        },
        delete: function (item) {
            $scope.trainModelElement.modelLists.splice($scope.trainModelElement.modelLists.indexOf(item), 1);
        },

        init: function () {
            $scope.projectId = slmlpService.localStorageService.get("projectId");
            $scope.trainModelElement = {
                startTrainingLoader: false
            };
            $scope.sourceRepo = {};
            $scope.fileManager = {};
            $scope.trainingJob = {
                testRatio: 0
            };
            $scope.filesElement = {};
            $scope.trainingJobObject.getModelList();
            $scope.trainingJobObject.getDatasetList();
            $scope.trainingJobObject.getTrainingEnginesList();
            $scope.files = {};
        }
    }
//    $scope.fileManager.selectedItems = [];
    $scope.trainingJobObject.init();


}

angular
        .module('slmlp')
        .controller('trainModelCtrl', trainModelCtrl);
