function datasetCtrl($scope, $stateParams, $rootScope, $state, localStorageService, toastr,$window, slmlpService, $http, fileManagerConfig, item, fileNavigator, apiMiddleware, $location, $state) {
    /** Generic object for the controller. */
    var $storage = $window.localStorage;
    $scope.config = fileManagerConfig;
    slmlpService.appConfig.CURRENT_PATH = $scope.config.basePath;
    $scope.reverse = false;
    $scope.predicate = ['model.type', 'model.name'];
    $scope.order = function (predicate) {
        $scope.reverse = ($scope.predicate[1] === predicate) ? !$scope.reverse : false;
        $scope.predicate[1] = predicate;
    };
    $scope.query = '';
    $scope.fileNavigator = new fileNavigator();
    $scope.apiMiddleware = new apiMiddleware();
    $scope.uploadFileList = [];
    $scope.viewTemplate = $storage.getItem('viewTemplate') || 'main-icons.html';
    $scope.fileList = [];
    $scope.temps = [];
    $scope.unCommitedChanges = [];
    $scope.datasetObject = {};
    $scope.form = {};
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'id';
    slmlpService.appConfig.sort.sortBy = 'name';
    slmlpService.appConfig.sort.sortOrder = '+';
    var language = 'en';
    $scope.filerManagerDetailsObject = {};
    $scope.gitObject = {
        node: "192.168.4.250",
        podUuid: "fc27227b-0f3f-11e8-be1f-b60b3383439f",
        pvc: "pvc-3f5980e7-081f-11e8-be1f-b60b3383439f",
        path: "/",
        branch: "master",
        commitMessage: "",
        projectName:slmlpService.localStorageService.get("projectRef").name,
        datasetName:"",
        remoteUrl: "",
        srcUrl: slmlpService.localStorageService.get("projectRef").sourceRepo.url,
        projectId : slmlpService.localStorageService.get("projectId"),
        patternFiles : [],
        rmpatternFiles : []
    };

    $scope.filerManagerDetailsObject = {
        isLoading: false,
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        getBranchList: function (dataset) {
            var gitObject = angular.copy($scope.gitObject);
            if(dataset && dataset != null) {
                gitObject.srcUrl = null;
                gitObject.datasetName = dataset.name;
                gitObject.remoteUrl = dataset.dataSetPublishUrl;
            }
            var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/branches", gitObject);
            var results = [];
            hasList.then(function (result) {
                $scope.branchList = result;
                $scope.gitObject.branch = $scope.branchList[0];
            });
        },
        gitPull : function (size, dataset) {
            var gitObject = angular.copy($scope.gitObject);
            if(dataset && dataset != null) {
                gitObject.srcUrl = null;
                gitObject.datasetName = dataset.name;
                gitObject.remoteUrl = dataset.dataSetPublishUrl;
            }
            var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/pull", gitObject);
            hasList.then(function (result) {
                $scope.pullStatus = result.message;
                slmlpService.dialogService.openDialog("views/projects/project-details/file-manager/pull-status.html", size, $scope,
                        ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                                $scope.cancel = function () {
                                    $uibModalInstance.dismiss('cancel');
                                };
                                $scope.confirmDelete = function () {
                                    $uibModalInstance.dismiss('cancel');
                                };

                            }]);
            });
        },
        lfspush: function (commitMessage, dataset) {
            $scope.isValidFile = true;
            $scope.selectedFiles = [];
            angular.forEach($scope.filerManagerElement.fileStatusLists, function (obj, key) {
                if (obj.checked == true && obj.status != 'Missing') {
                    $scope.selectedFiles.push(obj.file);
                }
                if (obj.checked == true && obj.status == 'Missing') {
                    $scope.gitObject.rmpatternFiles.push(obj.file);
                }
            });
            $scope.gitObject.patternFiles = $scope.selectedFiles;
            if ($scope.selectedFiles.length == 0) {
                $scope.isValidFile = false;
                toastr.error('Please add files before push', {closeButton: true});
            } else if (commitMessage == '') {
                $scope.isValidFile = false;
                toastr.error('Please enter the commit message', {closeButton: true});
            }
            if ($scope.isValidFile) {
                //$scope.gitObject.srcUrl = "";
                var gitObject = angular.copy($scope.gitObject);
                if(dataset && dataset != null) {
                    gitObject.srcUrl = null;
                    gitObject.datasetName = dataset.name;
                    gitObject.remoteUrl = dataset.dataSetPublishUrl;
                }
                var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/lfs", gitObject);
                var results = [];
                hasList.then(function (result) {
                    $scope.commitView = false;
                });
            }
        },
        branchSelected: function () {
            $scope.gitObject.branch = $scope.selectedBranch;
        },
        checkAll: function (isCheckedAll, tabName) {
            angular.forEach($scope.filerManagerElement.fileStatusLists, function (obj, key) {
                obj.checked = isCheckedAll;
            });
        },
        optionToggled: function (list, tabName) {
            if (tabName == 'fileManager') {
                $scope.filesCheckAll = list.every(function (itm) {
                    return itm.checked == true;
                })
            } else {
                $scope.project.trainingEngines.checkAll = list.every(function (itm) {
                    return itm.checked == true;
                })
            }
        },
        getFileManagerStatusList: function (dataset) {
            $scope.filerManagerDetailsObject.getBranchList(dataset);
            $scope.commitView = true;
            console.log(dataset)
            var gitObject = angular.copy($scope.gitObject);
            if(dataset && dataset != null) {
                gitObject.srcUrl = null;
                gitObject.datasetName = dataset.name;
                gitObject.remoteUrl = dataset.dataSetPublishUrl;
            }

            var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/status", gitObject);
            var results = [];
            hasList.then(function (result) {
                if (result.UntrackedFiles.length > 0) {
                    for (var i = 0; i < result.UntrackedFiles.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.UntrackedFiles[i], 'status': 'UntrackedFiles'});
                    }
                }
                if (result.Missing.length > 0) {
                    for (var i = 0; i < result.Missing.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.Missing[i], 'status': 'Missing'});
                    }
                }
                if (result.UntrackedFolders.length > 0) {
                    for (var i = 0; i < result.UntrackedFolders.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.UntrackedFolders[i], 'status': 'UntrackedFolders'});
                    }
                }
                if (result.Added.length > 0) {
                    for (var i = 0; i < result.Added.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.Added[i], 'status': 'Added'});
                    }
                }
                if (result.Modified.length > 0) {
                    for (var i = 0; i < result.Modified.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.Modified[i], 'status': 'Modified'});
                    }
                }
                if (result.Changed.length > 0) {
                    for (var i = 0; i < result.Changed.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.Changed[i], 'status': 'Changed'});
                    }
                }
                if (result.ConflictList.length > 0) {
                    for (var i = 0; i < result.ConflictList.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.ConflictList[i], 'status': 'ConflictList'});
                    }
                }
                if (result.Removed.length > 0) {
                    for (var i = 0; i < result.Removed.length; i++) {
                        $scope.filerManagerElement.fileStatusLists.push({'file': result.Removed[i], 'status': 'Removed'});
                    }
                }
            });
        },

        init: function () {
            $scope.filerManagerElement = {};
            $scope.filerManagerElement.fileStatusLists = [];
            $scope.branchList = [];
            $scope.selectedFiles = [];
            $scope.selectedBranch = "";
            $scope.sourceRepo = {};
            $scope.fileManager = {};
            $scope.filesElement = {};
            $scope.files = {};
        }
    }

    $scope.getHome = function () {
        var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/files/home", "&node=192.168.4.250&podUuid=40e3cfe4-0721-11e8-be1f-b60b3383439f&pvc=pvc-f6d37825-0720-11e8-be1f-b60b3383439f&path=/");
        hasList.then(function (result) {
            ///$scope.config.basePath = result.path;
            ///slmlpService.appConfig.CURRENT_PATH = $scope.config.basePath;
        });
    }

    $scope.gitLFSTrack = function(){
        var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/lfs", $scope.gitObject);
        var results = [];
        hasList.then(function (result) {
            console.log(result);
            $scope.unCommitedChanges = result;
        });
    };

    $scope.uploadFile = function (event) {
        var filename = event.target.files[0].name;
        if (event.target.files.length == 0) {
            toastr.error('Please choose atleast one file', {closeButton: true});
        } else {
            console.log(slmlpService.appConfig.CURRENT_PATH)
            var hasUploaded = slmlpService.promiseAjax.uploadFile(event.target.files[0], "40e3cfe4-0721-11e8-be1f-b60b3383439f", "pvc-f6d37825-0720-11e8-be1f-b60b3383439f", "192.168.4.250", slmlpService.appConfig.CURRENT_PATH, slmlpService.localStorageService, "containers/container/api/files/uploadFile")
            hasUploaded.then(function (result) {
               if(result.data.result == "No such file or directory") {
                   toastr.error(result.data.result, {closeButton: true});
               } else {
                toastr.success(result.data.result, {closeButton: true});
                $scope.fileNavigator.list();
                $scope.fileNavigator.refresh();
                }
            }).catch(function (result) {
                if (result.data.message.indexOf("exceeds") > -1) {
                    toastr.error("File size should be in below < 100MB", {closeButton: true});
                }
                $scope.fileNavigator.refresh();
            });
        }

    };


    $scope.importFile = function () {
        var filename = event.target.files[0].name;
        var hasUploaded = slmlpService.promiseAjax.uploadFile(event.target.files[0], $scope.gitObject.podUuid, $scope.gitObject.pvc, $scope.gitObject.node, slmlpService.appConfig.CURRENT_PATH, slmlpService.localStorageService, "containers/container/api/files/importFile")
        hasUploaded.then(function (result) {
            if (result.data.result == "No such file or directory") {
                toastr.error(result.data.result, {closeButton: true});
            } else {
                toastr.success(result.data.result, {closeButton: true});
                $scope.fileNavigator.list();
                $scope.fileNavigator.refresh();
            }
        }).catch(function (result) {
            if (result.data.message.indexOf("exceeds") > -1) {
                toastr.error("File size should be in below < 100MB", {closeButton: true});
            }
            $scope.fileNavigator.refresh();
        });
    };

    $scope.$watch('temps', function () {
        if ($scope.singleSelection()) {
            $scope.temp = $scope.singleSelection();
        } else {
            $scope.temp = new item({rights: 644});
            $scope.temp.multiple = true;
        }
        $scope.temp.revert();
    });

    $scope.fileNavigator.onRefresh = function () {
        $scope.temps = [];
        $scope.query = '';
        $rootScope.selectedModalPath = $scope.fileNavigator.currentPath;
    };

    $scope.setTemplate = function (name) {
        $storage.setItem('viewTemplate', name);
        $scope.viewTemplate = name;
    };

    $scope.isSelected = function (item) {
        return $scope.temps.indexOf(item) !== -1;
    };

    $scope.selectOrUnselect = function (item, $event) {
        var indexInTemp = $scope.temps.indexOf(item);
        var isRightClick = $event && $event.which == 3;

        if ($event && $event.target.hasAttribute('prevent')) {
            $scope.temps = [];
            return;
        }
        if (!item || (isRightClick && $scope.isSelected(item))) {
            return;
        }
        if ($event && $event.shiftKey && !isRightClick) {
            var list = $scope.fileList;
            var indexInList = list.indexOf(item);
            var lastSelected = $scope.temps[0];
            var i = list.indexOf(lastSelected);
            var current = undefined;
            if (lastSelected && list.indexOf(lastSelected) < indexInList) {
                $scope.temps = [];
                while (i <= indexInList) {
                    current = list[i];
                    !$scope.isSelected(current) && $scope.temps.push(current);
                    i++;
                }
                return;
            }
            if (lastSelected && list.indexOf(lastSelected) > indexInList) {
                $scope.temps = [];
                while (i >= indexInList) {
                    current = list[i];
                    !$scope.isSelected(current) && $scope.temps.push(current);
                    i--;
                }
                return;
            }
        }
        if ($event && !isRightClick && ($event.ctrlKey || $event.metaKey)) {
            $scope.isSelected(item) ? $scope.temps.splice(indexInTemp, 1) : $scope.temps.push(item);
            return;
        }
        $scope.temps = [item];
    };

    $scope.singleSelection = function () {
        return $scope.temps.length === 1 && $scope.temps[0];
    };

    $scope.totalSelecteds = function () {
        return {
            total: $scope.temps.length
        };
    };

    $scope.selectionHas = function (type) {
        return $scope.temps.find(function (item) {
            return item && item.model.type === type;
        });
    };

    $scope.prepareNewFolder = function () {
        var item = new item(null, $scope.fileNavigator.currentPath);
        $scope.temps = [item];
        return item;
    };

    $scope.smartClick = function (item) {
        var pick = $scope.config.allowedActions.pickFiles;
        if (item.isFolder()) {
            var obj = $scope.fileNavigator.folderClick(item);
            console.log(obj);
            return obj;
        }

        if (typeof $scope.config.pickCallback === 'function' && pick) {
            var callbackSuccess = $scope.config.pickCallback(item.model);
            if (callbackSuccess === true) {
                return;
            }
        }


    };

    $scope.modal = function (id, hide, returnElement) {
        var element = angular.element('#' + id);
        element.modal(hide ? 'hide' : 'show');
        $scope.apiMiddleware.apiHandler.error = '';
        $scope.apiMiddleware.apiHandler.asyncSuccess = false;
        return returnElement ? element : true;
    };

    $scope.modalWithPathSelector = function (id) {
        $rootScope.selectedModalPath = $scope.fileNavigator.currentPath;
        return $scope.modal(id);
    };

    $scope.isInThisPath = function (path) {
        var currentPath = $scope.fileNavigator.currentPath.join('/') + '/';
        return currentPath.indexOf(path + '/') !== -1;
    };

    $scope.edit = function () {
        $scope.apiMiddleware.edit($scope.singleSelection()).then(function () {
            $scope.modal('edit', true);
        });
    };

    $scope.getUrl = function (_item) {
        return $scope.apiMiddleware.getUrl(_item);
    };

    var validateSamePath = function (item) {
        var selectedPath = $rootScope.selectedModalPath.join('');
        var selectedItemsPath = item && item.model.path.join('');
        return selectedItemsPath === selectedPath;
    };

    var getQueryParam = function (param) {
        var found = $window.location.search.substr(1).split('&').filter(function (item) {
            return param === item.split('=')[0];
        });
        return found[0] && found[0].split('=')[1] || undefined;
    };
    //
    $scope.datasetObject = {
        addMoreInputAdditionalParameters: function () {
            var newItemNo = $scope.inputAdditionalParameters.length + 1;
            $scope.inputAdditionalParameters.push({id: newItemNo, notifyActions: []});
            angular.forEach($scope.userNotifications, function (val, key) {
                var notify = {};
                notify.id = val.id;
                notify.notificationType = val.notificationType;
                notify.notificationTypeId = val.notificationTypeId;
                notify.userNotificationsId = val.userNotificationsId;
                notify.selected = false;
                $scope.inputAdditionalParameters[newItemNo - 1].notifyActions.push(notify);
            });
        },

        deleteOneInputAdditionalParameters: function (id) {
            var i = $scope.inputAdditionalParameters.indexOf(id);
            return i > -1 ? $scope.inputAdditionalParameters.splice(i, 1) : [];
        },
        addMoreOutputAdditionalParameters: function () {
            var newItemNo = $scope.outputAdditionalParameters.length + 1;
            $scope.outputAdditionalParameters.push({id: newItemNo, notifyActions: []});
            angular.forEach($scope.userNotifications, function (val, key) {
                var notify = {};
                notify.id = val.id;
                notify.notificationType = val.notificationType;
                notify.notificationTypeId = val.notificationTypeId;
                notify.userNotificationsId = val.userNotificationsId;
                notify.selected = false;
                $scope.outputAdditionalParameters[newItemNo - 1].notifyActions.push(notify);
            });
        },

        deleteOneOutputAdditionalParameters: function (id) {
            var i = $scope.outputAdditionalParameters.indexOf(id);
            return i > -1 ? $scope.outputAdditionalParameters.splice(i, 1) : [];
        },
        // DELETE DEPARTMENT
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
                                        $scope.datasetElement.datasetLists.splice($scope.datasetElement.datasetLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };
                        }]);
        },
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
                $scope.datasetObject.directories = [
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

                $scope.datasetObject.fileList = [];
                angular.forEach($scope.datasetObject.directories[0].sub, function (directories) {
                    $scope.datasetObject.fileList.push(directories);
                });
            }
            if (file.path == 'dataSet') {
                $scope.datasetObject.directories = [
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
                                        path: "sample.csv",
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

                $scope.datasetObject.fileList = [];
                angular.forEach($scope.datasetObject.directories[0].sub[0].sub, function (directories) {
                    $scope.datasetObject.fileList.push(directories)
                });
            }
            if (file.path == 'dataSet-1') {
                $scope.datasetObject.directories = [
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
                                                path: "sample.csv",
                                                size: "22 Mb",
                                                updatedOn: "14.7.17 12:20:12",
                                                commit: "easa232sdsfcsd34345"
                                            }
                                        ]
                                    },
                                    {
                                        type: "file",
                                        path: "sample.scv"
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
                $scope.datasetObject.fileList = [];
                angular.forEach($scope.datasetObject.directories[0].sub[0].sub[0].sub, function (directories) {
                    $scope.datasetObject.fileList.push(directories)
                });
            }
            if (file.path == 'ver1') {
                $scope.datasetObject.directories = [
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
                                        path: "sample1.csv",
                                        size: "32 Mb",
                                        updatedOn: "14.7.17 12:20:12",
                                        commit: "easa232sdsfcsd34345"
                                    },
                                    {
                                        type: "file",
                                        path: "sample2.csv",
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
                $scope.datasetObject.fileList = [{
                        type: "file",
                        path: "sample1.csv",
                        size: "32 Mb",
                        updatedOn: "14.7.17 12:20:12",
                        commit: "easa232sdsfcsd34345"
                    },
                    {
                        type: "file",
                        path: "sample2.csv",
                        size: "2 GB",
                        updatedOn: "14.7.17 12:20:12",
                        commit: "easa232sdsfcsd34345"
                    }];
            }
            if (file.path == 'ver2') {
                $scope.datasetObject.directories = [
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
                                        path: "sample1.csv",
                                        size: "22 Mb",
                                        updatedOn: "14.7.17 12:20:12",
                                        commit: "easa232sdsfcsd34345"
                                    },
                                    {
                                        type: "file",
                                        path: "sample2.csv",
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
                $scope.datasetObject.fileList = [{
                        type: "file",
                        path: "sample1.csv",
                        size: "22 Mb",
                        updatedOn: "14.7.17 12:20:12",
                        commit: "easa232sdsfcsd34345"
                    },
                    {
                        type: "file",
                        path: "sample2.csv",
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
        getProject: function () {
            if ($stateParams.id > 0) {
                $scope.projectId = $stateParams.id;
                slmlpService.localStorageService.set("projectId", $stateParams.id);
                var hasServer = slmlpService.crudService.read("project", $stateParams.id);
                hasServer.then(function (result) {
                    $scope.project = result;
                    slmlpService.localStorageService.set("projectRef", $scope.project);
                    //slmlpService.appConfig.CURRENT_PROJECT = "/"+$scope.project.name+"/";
                    $scope.dataSetList = $scope.project.dataSets;
                });
            };
        },
        getDatasetList: function () {
            $http.get("views/json/dataset.json").success(function (result) {
//                $scope.datasetElement.datasetLists = result;
            });
        },
        // CREATE DATASET
        createDataset: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/dataset/create.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.datasetObject.save = function (dataset) {
                                $scope.publishForm.submitted = true;
                                if (true) {
                                    $scope.publishForm.submitted = false;
                                    $scope.datasetElement.datasetLists.push(dataset);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        // PUBLISH
        publishDataset: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/dataset/publish.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.datasetObject.save = function (dataset) {
                                $scope.publishForm.submitted = true;
                                if (true) {
                                    $scope.publishForm.submitted = false;
                                    $scope.datasetElement.datasetLists.push(dataset);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        selectDataset : function(dataset){
            $scope.datasetView = true;
            $scope.selectedDataset = dataset;
            $scope.importObj.from = dataset.dataSetConfig.dataSourceUrl;
            $scope.fileNavigator.currentPath = [];
            slmlpService.localStorageService.set("dataset", dataset.name);
        },
        importDataset: function (size) {
            slmlpService.dialogService.openDialog("views/projects/project-details/dataset/import.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.datasetObject.save = function (dataset,isValid) {

                                    var path = "/";
                                    var filePath = "";
                                    angular.forEach($rootScope.selectedModalPath, function(currentFolder ,key) {
                                        filePath = filePath + path + currentFolder;

                                    })

                                    if(filePath == "") {
                                        filePath = "/";
                                    }
                                    $scope.importFormSubmitted = true;
                                    if ($scope.importForm.$valid) {
                                        var uploadObject = {
                                            fileUrl: dataset.from,
                                            podUuid: $scope.gitObject.podUuid,
                                            pvc: $scope.gitObject.pvc,
                                            path: filePath,
                                            node: $scope.gitObject.node,
                                        };
                                        $scope.importLoader = true;
//                                    var extn = dataset.from.split(".").pop();
//                                    if (extn === 'csv') {
                                        var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/importFile", uploadObject);
                                        var results = [];
                                        hasList.then(function (result) {
                                            $scope.importLoader = false;
                                            toastr.success("Imported successfully", {closeButton: true});
                                            $scope.importFormSubmitted = false;
                                            $scope.fileNavigator.list();
                                            $scope.fileNavigator.refresh();
                                            $uibModalInstance.dismiss('cancel');
                                            $state.reload();

                                        }).catch(function (result) {
                                            $scope.importLoader = false;
                                            toastr.error("Import failed due to :" + result.data, {closeButton: true});
                                            $scope.fileNavigator.refresh();
                                        });
//                                    }
//                                     else {
//                                        toastr.error("Choose CSV file to import", {closeButton: true});
//                                    }
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        // DATASOURCE DETAILS
        datasourceDetails: function (size, dataSetId) {
            slmlpService.dialogService.openDialog("views/projects/project-details/dataset/datasource-details.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            var hasResult = slmlpService.crudService.read("dataset", dataSetId);
                            hasResult.then(function (result) {
                                $scope.publish = result.dataSetConfig;
                                $scope.dataSourceAdditionalParams = $scope.publish.dataSourceAdditionalParam;
                            });
//                            $scope.publish.datasetPath = 'root/dataSet/dataSet-1/az-dataset.csv';
//                            $scope.publish.keyname = 'root/dataSet/dataSet-1/az-dataset.csv';
//                            $scope.publish.values = 'root/dataSet/ver1';
                            $scope.datasetObject.clipboard = function (id) {
                                var copyText = document.getElementById(id);
                                copyText.select();
                                document.execCommand("Copy");
                            }

                            $scope.datasetObject.save = function (dataset) {
                                $scope.publishForm.submitted = true;
                                if (true) {
                                    $scope.publishForm.submitted = false;
                                    $scope.datasetElement.datasetLists.push(dataset);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
         // DATASET DETAILS
        datasetDetails: function (size, dataSetId) {
            slmlpService.dialogService.openDialog("views/projects/project-details/dataset/dataset-details.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            var hasResult = slmlpService.crudService.read("dataset", dataSetId);
                            hasResult.then(function (result) {
                                $scope.publish = result.dataSetConfig;
                                $scope.dataSourceAdditionalParams = $scope.publish.dataSourceAdditionalParam;
                                $scope.dataSetAdditionalParams = $scope.publish.dataSetAdditionalParam;
                                $scope.dataSetAdditionalParams = $scope.publish.dataSetAdditionalParam;

                            });

                            $scope.datasetObject.clipboard = function (id) {
                                var copyText = document.getElementById(id);
                                copyText.select();
                                document.execCommand("Copy");
                            }

                            $scope.datasetObject.save = function (dataset) {
                                $scope.publishForm.submitted = true;
                                if (true) {
                                    $scope.publishForm.submitted = false;
                                    $scope.datasetElement.datasetLists.push(dataset);
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        changeSort: function (sortBy, pageNumber, sort) {
            var sort = slmlpService.utilService.getSortingOrderBySortBy(sortBy);
            $scope.sort = sort;
            sort.sortOrder = (sort.sortOrder == "+") ? "-" : "+";
            $scope.sort = sort;
            slmlpService.appConfig.sort.sortOrder = sort.sortOrder;
            slmlpService.appConfig.sort.sortBy = sortBy;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hasResult = {};
            var hasResult = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "dataset/list?=" + "lang=" + language + "&projectId=" + $scope.projectId + "&sortBy=" + slmlpService.appConfig.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
            hasResult.then(function (result) { // this is only run after $http
                // completes0
                $scope.datasetElement.datasetLists = result;
                // For pagination
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
            });
        },
        list: function (pageNumber) {
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var additionalParam = "&projectId=" + $scope.projectId;
                var hasactionServer = slmlpService.crudService.listAllWithPagination("dataset/list", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit}, additionalParam);
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasactionServer = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "dataset/findbysearchtext?=" + "lang=" + language + encodeURI($scope.filter) + "&projectId=" + $scope.projectId + "&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
                    "limit": limit
                });
            }
            hasactionServer.then(function (result) {  // this is only run after $http completes
                $scope.datasetElement.datasetLists = result;
//                $scope.dataSetConfig = result.dataSetConfig;
                // For pagination
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.datasetObject.list;
            }).catch(function (result) {
                if (!angular.isUndefined(result.data)) {
                    if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                        var msg = result.data.globalError[0];
                        toastr.error(
                                msg,
                                {
                                    closeButton: true
                                }
                        );
                    }
                }
            });
        },
        chooseView : function(view){
            if(view == 'data') {
                $scope.datasetView = false;
            }
            if(view == 'commit') {
                $scope.commitView = false;
                $scope.filerManagerElement.fileStatusLists = [];
            }
        },
        searchList: function (quicksearch) {
            $scope.quicksearch = quicksearch;
            $scope.datasetObject.list(1);
        },
        init: function () {
            $scope.getHome();
            $scope.projectId = slmlpService.localStorageService.get("projectId");
            $scope.project = slmlpService.localStorageService.get("projectRef");
            $scope.datasetObject.getProject();
            $scope.datasetObject.list(1);
            $scope.datasetView = false;
            $scope.commitView = false;
            $scope.selectedDataset = {};
            $scope.datasetElement = {};
            $scope.inputAdditionalParameters = [];
            $scope.datasetObject.addMoreInputAdditionalParameters();
            $scope.outputAdditionalParameters = [];
            $scope.datasetObject.addMoreOutputAdditionalParameters();
            $scope.datasetObject.getDatasetList();
            $scope.dataset = {};
            $scope.publish = {};
            $scope.fileManager = {};
            $scope.filesElement = {};
            $scope.files = {};
            $scope.importObj = {};
        }
    }
    
//    $scope.fileManager.selectedItems = [];
    $scope.datasetObject.init();
    $scope.filerManagerDetailsObject.init();
    $scope.$watch('temps', function () {
        if ($scope.singleSelection()) {
            $scope.temp = $scope.singleSelection();
            slmlpService.appConfig.CURRENT_FILE = $scope.temp.model.fullPath();
        } else {
            $scope.temp = new item({rights: 644});
            $scope.temp.multiple = true;
        }
        $scope.temp.revert();
    });

    $scope.fileNavigator.onRefresh = function () {
        $scope.temps = [];
        $scope.query = '';
        $rootScope.selectedModalPath = $scope.fileNavigator.currentPath;
    };
    $scope.fileNavigator.currentPath.push($scope.project.name);
    $scope.fileNavigator.currentPath.push("datasets");
    $scope.fileNavigator.currentPath.push(slmlpService.localStorageService.get("dataset"));
    $scope.fileNavigator.showCurrentPath($scope.fileNavigator.currentPath);
    
    $scope.moveToCsvConversion = function () {
        var extn = slmlpService.appConfig.CURRENT_FILE.split(".").pop();
        if (extn === 'csv') {
            $scope.buttonDisable = false;
            $scope.selectedCsvFile = slmlpService.appConfig.CURRENT_FILE;
            localStorageService.set("pathVariable", $scope.selectedCsvFile);
            $state.go('file-manager.explore-csv-to-table')
        } else {
            $scope.buttonDisable = true;
            toastr.error("Choose CSV file to explore table format", {closeButton: true});
        }

    }
}

angular
        .module('slmlp')
        .controller('datasetCtrl', datasetCtrl);
