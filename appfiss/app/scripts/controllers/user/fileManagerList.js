function fileManagerListCtrl($scope, toastr, appfissService, $http, $timeout, $rootScope, $window, $translate, fileManagerConfig, item, fileNavigator, apiMiddleware, $location, $state) {
    /** Generic object for the controller. */
    var $storage = $window.localStorage;
    $scope.config = fileManagerConfig;
    appfissService.appConfig.CURRENT_PATH = $scope.config.basePath;
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
    $scope.gitObject = {
        node: "192.168.4.251",
        podUuid: "fc27227b-0f3f-11e8-be1f-b60b3383439f",
        pvc: "pvc-3f5980e7-081f-11e8-be1f-b60b3383439f",
        path: "/",
        branch: "master",
        commitMessage: "",
            patternFiles : [],
            rmpatternFiles : []
    };
    $scope.cloneRepo = function () {
        var hasList = appfissService.crudService.listAllByGateway("containers/container/api/files/actions", "&path=/");
        var results = [];
        hasList.then(function (result) {
            $scope.unCommitedChanges = result;
        });
    }

    $scope.getUnCommittedChanges = function () {
        var hasList = appfissService.crudService.PostViaGateway("containers/container/api/files/commitfiles", $scope.gitObject);
        var results = [];
        hasList.then(function (result) {
            $scope.unCommitedChanges = result;
        });
    }

    $scope.gitPull = function (size) {
        var hasList = appfissService.crudService.PostViaGateway("containers/container/api/files/pull", $scope.gitObject);
//        var results = [];
        hasList.then(function (result) {
            $scope.pullStatus=result.message;
//            $scope.pullStatus="Auto Merge cannot be done. Resolve Conflict";
            appfissService.dialogService.openDialog("views/projects/project-details/file-manager/pull-status.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {$uibModalInstance.dismiss('cancel');
//                                $scope.confirmDelete = false;
//                                setTimeout(function () {
//                                    $scope.$apply(function () {
//                                        $uibModalInstance.dismiss('cancel');
//                                    })
//                                }, 1000);
                            };

                        }]);
            console.log(result);
            //toastr.success(result.message, {closeButton: true});
        });
    }

    $scope.gitStatus = function () {
        var hasList = appfissService.crudService.PostViaGateway("containers/container/api/files/status", $scope.gitObject);
        var results = [];
        hasList.then(function (result) {
            console.log(result);
            $scope.unCommitedChanges = result;
        });
    }

    $scope.gitPush = function () {
        var hasList = appfissService.crudService.PostViaGateway("containers/container/api/files/push", $scope.gitObject);
        var results = [];
        hasList.then(function (result) {
            console.log(result);
            $scope.unCommitedChanges = result;
        });
    }

    $scope.gitLFSTrack = function () {
        var hasList = appfissService.crudService.PostViaGateway("containers/container/api/files/lfs", $scope.gitObject);
        var results = [];
        hasList.then(function (result) {
            console.log(result);
            $scope.unCommitedChanges = result;
        });
    }

    $scope.uploadFile = function () {
        var filename = event.target.files[0].name;
        if (event.target.files.length == 0) {
            toastr.error('Please choose atleast one file', {closeButton: true});
        } else {
            console.log(appfissService.appConfig.CURRENT_PATH)
            var hasUploaded = appfissService.promiseAjax.uploadFile(event.target.files[0], $scope.gitObject.podUuid, $scope.gitObject.pvc, $scope.gitObject.node, appfissService.appConfig.CURRENT_PATH, appfissService.localStorageService, "containers/container/api/files/uploadFile")
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
        }

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

    $scope.changeLanguage = function (locale) {
        if (locale) {
            $storage.setItem('language', locale);
            return $translate.use(locale);
        }
        $translate.use($storage.getItem('language') || fileManagerConfig.defaultLang);
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

    $scope.openImagePreview = function () {
        var item = $scope.singleSelection();
        $scope.apiMiddleware.apiHandler.inprocess = true;
        $scope.modal('imagepreview', null, true)
                .find('#imagepreview-target')
                .attr('src', $scope.getUrl(item))
                .unbind('load error')
                .on('load error', function () {
                    $scope.apiMiddleware.apiHandler.inprocess = false;
                    $scope.$apply();
                });
    };

    $scope.openEditItem = function () {
        var item = $scope.singleSelection();
        $scope.apiMiddleware.getContent(item).then(function (data) {
            item.tempModel.content = item.model.content = data.result;
        });
        $scope.modal('edit');
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

    $scope.changePermissions = function () {
        $scope.apiMiddleware.changePermissions($scope.temps, $scope.temp).then(function () {
            $scope.fileNavigator.refresh();
            $scope.modal('changepermissions', true);
        });
    };

    $scope.download = function () {
        var item = $scope.singleSelection();
        if ($scope.selectionHas('dir')) {
            return;
        }
        if (item) {
            return $scope.apiMiddleware.download(item);
        }
        return $scope.apiMiddleware.downloadMultiple($scope.temps);
    };

    $scope.copy = function () {
        var item = $scope.singleSelection();
        if (item) {
            var name = item.tempModel.name.trim();
            var nameExists = $scope.fileNavigator.fileNameExists(name);
            if (nameExists && validateSamePath(item)) {
                $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
                return false;
            }
            if (!name) {
                $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
                return false;
            }
        }
        $scope.apiMiddleware.copy($scope.temps, $rootScope.selectedModalPath).then(function () {
            $scope.fileNavigator.refresh();
            $scope.modal('copy', true);
        });
    };

    $scope.compress = function () {
        var name = $scope.temp.tempModel.name.trim();
        var nameExists = $scope.fileNavigator.fileNameExists(name);

        if (nameExists && validateSamePath($scope.temp)) {
            $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
            return false;
        }
        if (!name) {
            $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
            return false;
        }

        $scope.apiMiddleware.compress($scope.temps, name, $rootScope.selectedModalPath).then(function () {
            $scope.fileNavigator.refresh();
            if (!$scope.config.compressAsync) {
                return $scope.modal('compress', true);
            }
            $scope.apiMiddleware.apiHandler.asyncSuccess = true;
        }, function () {
            $scope.apiMiddleware.apiHandler.asyncSuccess = false;
        });
    };

    $scope.extract = function () {
        var item = $scope.temp;
        var name = $scope.temp.tempModel.name.trim();
        var nameExists = $scope.fileNavigator.fileNameExists(name);

        if (nameExists && validateSamePath($scope.temp)) {
            $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
            return false;
        }
        if (!name) {
            $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
            return false;
        }

        $scope.apiMiddleware.extract(item, name, $rootScope.selectedModalPath).then(function () {
            $scope.fileNavigator.refresh();
            if (!$scope.config.extractAsync) {
                return $scope.modal('extract', true);
            }
            $scope.apiMiddleware.apiHandler.asyncSuccess = true;
        }, function () {
            $scope.apiMiddleware.apiHandler.asyncSuccess = false;
        });
    };

    $scope.remove = function () {
        $scope.apiMiddleware.remove($scope.temps).then(function () {
            $scope.fileNavigator.refresh();
            $scope.modal('remove', true);
        });
    };

    $scope.move = function () {
        var anyItem = $scope.singleSelection() || $scope.temps[0];
        if (anyItem && validateSamePath(anyItem)) {
            $scope.apiMiddleware.apiHandler.error = $translate.instant('error_cannot_move_same_path');
            return false;
        }
        $scope.apiMiddleware.move($scope.temps, $rootScope.selectedModalPath).then(function () {
            $scope.fileNavigator.refresh();
            $scope.modal('move', true);
        });
    };

    $scope.rename = function () {
        var item = $scope.singleSelection();
        var name = item.tempModel.name;
        var samePath = item.tempModel.path.join('') === item.model.path.join('');
        if (!name || (samePath && $scope.fileNavigator.fileNameExists(name))) {
            $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
            return false;
            $scope.filerManagerDetailsObject.getLaunchersList();
        }
        $scope.apiMiddleware.rename(item).then(function () {
            $scope.fileNavigator.refresh();
            $scope.modal('rename', true);
        });
    };

    $scope.createFolder = function () {
        var item = $scope.singleSelection();
        var name = item.tempModel.name;
        if (!name || $scope.fileNavigator.fileNameExists(name)) {
            return $scope.apiMiddleware.apiHandler.error = $translate.instant('error_invalid_filename');
        }
        $scope.apiMiddleware.createFolder(item).then(function () {
            $scope.fileNavigator.refresh();
            $scope.modal('newfolder', true);
        });
    };

    $scope.addForUpload = function ($files) {
        $scope.uploadFileList = $scope.uploadFileList.concat($files);
        $scope.modal('uploadfile');
    };

    $scope.removeFromUpload = function (index) {
        $scope.uploadFileList.splice(index, 1);
    };

    $scope.uploadFiles = function () {
        $scope.apiMiddleware.upload($scope.uploadFileList, $scope.fileNavigator.currentPath).then(function () {
            $scope.fileNavigator.refresh();
            $scope.uploadFileList = [];
            $scope.modal('uploadfile', true);
        }, function (data) {
            var errorMsg = data.result && data.result.error || $translate.instant('error_uploading_files');
            $scope.apiMiddleware.apiHandler.error = errorMsg;
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

    // GET LAUNCHERS

    $scope.filerManagerDetailsObject = {
        getLaunchersList: function () {
            var hasResult = appfissService.crudService.listAll("launcher/listbyisactive");
            hasResult.then(function (result) {
                $scope.filerManagerElement.launchersLists = result;
            });
        },
        launchFile : function(){
            var hasResult = appfissService.crudService.listAll("launcher/file/launch");
            hasResult.then(function (result) {
            });
        },
        getFile : function(){
            var hasResult = appfissService.crudService.listAll("launcher/file-manager/launch?type=File");
            hasResult.then(function (result) {
            });
        },
        launchEditor: function (launcher) {
            $scope.launchStatus = "Started";
            launcher.projectId = $scope.projectId;
            var hasResult = appfissService.crudService.add("launcher/launch", launcher);
            hasResult.then(function (result) {

                appfissService.dialogService.openDialog("views/projects/project-details/file-manager/start-launcher.html", "md", $scope,
                        ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                                setTimeout(function () {
                                    $uibModalInstance.dismiss('cancel');
                                    $state.go('launchers.list');
                                }, 25000);
                                $scope.cancel = function () {

                                };


                            }]);
//                if(result.id) {
//                    setTimeout(function() {
//                        $scope.filerManagerDetailsObject.openInNewTab('http://'+ appfissService.appConfig.K8S_IP + ':' + result.nodePort);
//                    },3000);
//
//                }
                //$scope.filerManagerElement.launchersLists = result;
            });
        },

        webSocketEvents: function () {
            $scope.$on("Deployment_ScalingReplicaSet", function (event, args) {
                console.log("Event", event);
                console.log("args", args);
                $scope.launchStatus = "Launching";
                $scope.launcherUrl = 'http://' + appfissService.appConfig.K8S_IP + ':' + args.metadata.selfLink;

            });
            $scope.$on("Pod_Started", function (event, args) {
                console.log("PodEvent", event);
                console.log("Podargs", args);
                $scope.launchStatus = true;
                $scope.launchStatus = "Launched";
                $scope.filerManagerDetailsObject.openInNewTab($scope.launcherUrl);
            });
        },

        // Start Launcher
        startLauncher: function (size) {
            appfissService.dialogService.openDialog("views/projects/project-details/file-manager/start-launcher.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },

        openInNewTab: function (url) {
            var win = window.open(url, '_blank');
            win.focus();
        },

        init: function () {
            $scope.projectId = appfissService.localStorageService.get("projectId");
            $scope.filerManagerElement = [];
            $scope.filerManagerDetailsObject.getLaunchersList();
            $scope.filerManagerDetailsObject.webSocketEvents();
        }
    }

    $scope.filerManagerDetailsObject.init();


    $scope.changeLanguage(getQueryParam('lang'));
    $scope.isWindows = getQueryParam('server') === 'Windows';
    $scope.fileNavigator.refresh();


}

angular
        .module('appfiss')
        .controller('fileManagerListCtrl', fileManagerListCtrl);
