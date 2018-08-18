function statusCtrl($scope, slmlpService, $http, $timeout, toastr, $state, $location) {
    /** Generic object for the controller. */
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
        getBranchList: function () {
            var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/branches", $scope.gitObject);
            var results = [];
            hasList.then(function (result) {
                $scope.branchList = result;
                $scope.gitObject.branch = $scope.branchList[0];
            });
        },
        push: function (commitMessage) {
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
                toastr.error('Changes not staged for commit', {closeButton: true});
            } else if (commitMessage == '') {
                $scope.isValidFile = false;
                toastr.error('Please enter commit message for push files', {closeButton: true});
            }
            if ($scope.isValidFile) {
                var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/push", $scope.gitObject);
                var results = [];
                hasList.then(function (result) {
                    toastr.success("git push successfully completed", {
                        closeButton: true
                    });
                    $state.go('file-manager.list');
                });
            }
        },
        lfspush: function (commitMessage) {
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
                $scope.fileManager.isLoading = true;
                //$scope.gitObject.srcUrl = "";
                var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/lfs", $scope.gitObject);
                var results = [];
                hasList.then(function (result) {
                    $scope.fileManager.isLoading = false;
                    toastr.success('File(s) pushed successfully.', {
                        closeButton: true
                    });
                    $state.go('dataset.view');
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
        getFileManagerStatusList: function () {
            var hasList = slmlpService.crudService.PostViaGateway("containers/container/api/files/status", $scope.gitObject);
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
            $scope.filerManagerElement = {
                lfPushLoader:false
            };
            $scope.filerManagerElement.fileStatusLists = [];
            $scope.branchList = [];
            $scope.selectedFiles = [];
            $scope.selectedBranch = "";
            $scope.filerManagerDetailsObject.getBranchList();
            $scope.filerManagerDetailsObject.getFileManagerStatusList();
            $scope.sourceRepo = {};
            $scope.fileManager = {};
            $scope.filesElement = {};
            $scope.files = {};
        }
    }
    $scope.filerManagerDetailsObject.init();

}

angular
        .module('slmlp')
        .controller('statusCtrl', statusCtrl);
