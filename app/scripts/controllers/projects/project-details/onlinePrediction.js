function onlinePredictionCtrl($scope, slmlpService) {
    /** Generic object for the controller. */
    $scope.fileManager = {};
    //File Manager
    $scope.fileManager = {

        fileList: [],

//        fileSetCount: 0,

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
                $scope.fileManager.directories = [
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

                $scope.fileManager.fileList = [];
                angular.forEach($scope.fileManager.directories[0].sub, function (directories) {
                    $scope.fileManager.fileList.push(directories);
                });
            }
            if (file.path == 'dataSet') {
                $scope.fileManager.directories = [
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
                                        path: "dataSet-1"
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

                $scope.fileManager.fileList = [];
                angular.forEach($scope.fileManager.directories[0].sub[0].sub, function (directories) {
                    $scope.fileManager.fileList.push(directories)
                });
            }
            if (file.path == 'dataSet-1') {
                $scope.fileManager.directories = [
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
                $scope.fileManager.fileList = [];
                angular.forEach($scope.fileManager.directories[0].sub[0].sub[0].sub, function (directories) {
                    $scope.fileManager.fileList.push(directories)
                });
            }
            if (file.path == 'ver1') {
                $scope.fileManager.directories = [
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
                $scope.fileManager.fileList = [{
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
                $scope.fileManager.directories = [
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
                $scope.fileManager.fileList = [{
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
        init: function () {
            $scope.filesElement = {};
            $scope.files = {};
        }
    }
    $scope.fileManager.selectedItems = [];


    $scope.fileManager.init();

}

angular
        .module('slmlp')
        .controller('onlinePredictionCtrl', onlinePredictionCtrl);