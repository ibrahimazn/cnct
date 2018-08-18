function projectsCtrl($scope, $stateParams, slmlpService, $http, $state, toastr, $stateParams, localStorageService) {
    /** Generic object for the controller. */
    $scope.projectsObject = {};
    $scope.projectGeneralForm = {};
    $scope.form = {};
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.sort = slmlpService.appConfig.sort;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'name';
    slmlpService.appConfig.sort.sortBy = 'name';
    slmlpService.appConfig.sort.sortOrder = '+';
    var language = 'en';
    $scope.projectGeneralForm = {};
    $scope.projectSourceRepoForm = {};
    $scope.projectDatasetForm = {};
    $scope.datasetTab = 0;
    $scope.users = [];
    $scope.dataSetList = [];
    $scope.dataSetConfigList = [];
    $scope.tabActive = 0;
    $scope.roles = [];
    $scope.defaultUser = true;

    $scope.projectsObject = {
        optionToggled: function (list, tabName) {
            if (tabName == 'launchers') {
                $scope.project.launchers.checkAll = list.every(function (itm) {
                    return itm.checked == true;
                })
            } else {
                $scope.project.trainingEngines.checkAll = list.every(function (itm) {
                    return itm.checked == true;
                })
            }
        },
        checkAll: function (isCheckedAll, tabName) {
            if (tabName == 'launchers') {
                $scope.checkList = $scope.launchersLists;
            } else {
                $scope.checkList = $scope.trainingEnginesLists;
            }
            angular.forEach($scope.checkList, function (obj, key) {
                obj.checked = isCheckedAll;
            });
        },
        checkValidation: function (form, tab) {
            var validLauncher = false;
            var validTrainingEngine = false;
            if (tab == 1) {
                $scope.projectGeneralFormSubmitted = true;
            } else if (tab == 2) {
                $scope.projectSourceRepoFormSubmitted = true;
            } else if (tab == 6) {
                $scope.projectScoringFormSubmitted = true;
            }

            angular.forEach($scope.launchersLists, function (obj, key) {
                if (obj.checked) {
                    validLauncher = true;
                }
            })
//            angular.forEach($scope.trainingEnginesLists, function (obj, key) {
//                if (obj.checked) {
//                    validTrainingEngine = true;
//                }
//            })
            if (form.$valid) {
                if (form.$name === 'projectLaunchersForm') {
                    if (validLauncher) {
                        $scope.projectsObject.setActiveTab(tab);
                        $scope.projectSourceRepoFormSubmitted = false;
                        $scope.projectScoringFormSubmitted = false;
                        $scope.projectGeneralFormSubmitted = false;
                    } else {
                        toastr.error('Please choose atleast one launcher', {closeButton: true});
                    }
                }
                if (form.$name === 'projectTrainingEnginesForm') {
//                    if (validTrainingEngine) {
//                        $scope.projectsObject.setActiveTab(tab);
//                        $scope.projectSourceRepoFormSubmitted = false;
//                        $scope.projectScoringFormSubmitted = false;
//                        $scope.projectGeneralFormSubmitted = false;
//                    } else {
//                        toastr.error('Please choose atleast one training ', {closeButton: true});
//                    }
                }
//                if (form.$name != 'projectLaunchersForm' && form.$name != 'projectTrainingEnginesForm') {
                if (form.$name != 'projectLaunchersForm') {
                    $scope.projectsObject.setActiveTab(tab);
                    $scope.projectSourceRepoFormSubmitted = false;
                    $scope.projectScoringFormSubmitted = false;
                    $scope.projectGeneralFormSubmitted = false;
                }
            }
        },
        cancelTemMembers: function () {
            $scope.teamMembers = {};
            $scope.teamMembersFormSubmitted = false;
            $scope.updateTeamMemberBtn = false;
        },

        // ADD PROJECT
        save: function (project) {
//            $scope.form.submitted = true;
            if (project.launchers != '' && !angular.isUndefined(project.launchers)) {
                var checkAllLaunchers = project.launchers.checkAll;
            }
//            if (project.trainingEngines != '' && !angular.isUndefined(project.trainingEngines)) {
//                var checkAllTrainingEngines = project.trainingEngines.checkAll;
//            }
            //Source Repository
            $scope.project.sourceRepo = {};
            $scope.project.sourceRepo.repository = project.repository;
            $scope.project.sourceRepo.url = project.url;
            //Launchers
            $scope.project.launchers = [];

            angular.forEach($scope.launchersLists, function (obj, key) {
                if (checkAllLaunchers) {
                    $scope.project.launchers.push(obj);
                } else {
                    if (obj.checked) {
                        $scope.project.launchers.push(obj);
                    }
                }
            })
            //TrainingEngines
            $scope.project.trainingEngines = [];
//            angular.forEach($scope.trainingEnginesLists, function (obj, key) {
//                if (checkAllTrainingEngines) {
//                    $scope.project.trainingEngines.push(obj);
//                } else {
//                    if (obj.checked) {
//                        $scope.project.trainingEngines.push(obj);
//                    }
//                }
//            })

            $scope.tools = [];

            $scope.environmentVariables = [];
            var tools = [];
            var environmentVariables = [];
            angular.forEach($scope.additionalParameters, function (val, key) {
                environmentVariables.push({'name': val.name, 'path': val.path});
            });
            $scope.environmentVariables = environmentVariables;
            $scope.tools = tools;
            if (angular.isUndefined($scope.project.offlineDeployment)) {
                $scope.project.offlineDeployment = {};
            }

            angular.forEach($scope.platformTools, function (obj, key) {
                if (obj.checked) {
                    $scope.tools.push(obj);
                }
            })

            $scope.project.offlineDeployment.platformTools = $scope.tools;
            $scope.project.offlineDeployment.environmentVariables = $scope.environmentVariables;
            $scope.projectValid = true;
            $scope.projectGeneralFormSubmitted = false;
            $scope.projectSourceRepoFormSubmitted = false;
            $scope.projectScoringFormSubmitted = false;
            if (project.name == '' || angular.isUndefined(project.name)) {
                $scope.projectsObject.setActiveTab(0);
                $scope.projectGeneralFormSubmitted = true;
                $scope.projectValid = false;
            } else if (project.repository == '' || angular.isUndefined(project.repository) || angular.isUndefined(project.url) || project.url == '') {
                $scope.projectsObject.setActiveTab(1);
                $scope.projectSourceRepoFormSubmitted = true;
                $scope.projectValid = false;
            } else if (project.launchers.length <= 0) {
                $scope.projectsObject.setActiveTab(2);
                toastr.error('Please choose atleast one launcher', {closeButton: true});
                $scope.projectValid = false;
            } else if ($scope.projectsElement.dataSetList <= 0 || project.dataSets == 0) {
                $scope.projectsObject.setActiveTab(3);
                toastr.error('Please add atleast one dataset', {closeButton: true});
                $scope.projectValid = false;
//            } else if (project.trainingEngines <= 0) {
//                $scope.projectsObject.setActiveTab(4);
//                toastr.error('Please choose atleast one training ', {closeButton: true});
//                $scope.projectValid = false;
            } else if (project.modelPath == "" || project.applicationPath == "" || angular.isUndefined(project.modelPath) || angular.isUndefined(project.applicationPath)) {
                $scope.projectsObject.setActiveTab(5);
                $scope.projectScoringFormSubmitted = true;
                $scope.projectValid = false;
            } else if (project.projectUsers <= 0) {
                $scope.projectsObject.setActiveTab(7);
            }

            if ($scope.defaultUser) {
                $scope.projectsElement.teamMembersList = [{"userId": $scope.adminUserId,"roleId": $scope.adminRoleId,"userName": $scope.loginUser,"roleName": "ADMIN" }]
                $scope.defaultUser = false;
                $scope.project.projectUsers = $scope.projectsElement.teamMembersList;
            }
            if ($scope.projectValid) {
                $scope.projectsElement.createLoader = true;
                var hasResult = slmlpService.crudService.add("project", $scope.project);
                hasResult.then(function (result) {  // this is only run after $http completes
                    $scope.projectsElement.createLoader = false;
                    slmlpService.localStorageService.set("projectId", result.id);
                    $state.reload();
                    $state.go('projects.list')
                    toastr.success('Created Successfully.', {
                        closeButton: true
                    });
                }).catch(function (result) {
                    $scope.projectsElement.createLoader = false;
                    if (!angular.isUndefined(result.data)) {
                        if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                            var msg = result.data.globalError[0];
                            toastr.error(
                                    msg,
                                    {
                                        closeButton: true
                                    }
                            );
                        } else if (result.data.fieldErrors !== null) {
                            angular.forEach(result.data.fieldErrors, function (errorMessage, key) {
                                $scope.showLoader = false;
                                var msg = errorMessage;
                                toastr.error(
                                        msg,
                                        {
                                            closeButton: true
                                        }
                                );
                            });
                        }
                    }
                });
            }
        },
        edit: function (dataSetObject, $index) {
            $scope.dataSetId = $index;
            $scope.updateDatasetBtn = true;
            var datasets = {};
            var datasetsObj = dataSetObject;
            datasets.dataSourceUrl = datasetsObj.dataSetConfig.dataSourceUrl;
            datasets.dataSourceAuthUrl = datasetsObj.dataSetConfig.dataSourceAuthUrl;
            datasets.dataSetPublishUrl = datasetsObj.dataSetConfig.dataSetPublishUrl;
            datasets.dataSetAuthUrl = datasetsObj.dataSetConfig.dataSetAuthUrl;
            datasets.name = datasetsObj.name;
            datasets.dataSource = datasetsObj.dataSource;
            datasets.storageType = datasetsObj.dataSetConfig.dataSetStorageType;
            datasets.datasetVerLimit = datasetsObj.dataSetConfig.dataSetVersionLimit;
            $scope.inputAdditionalParameters = datasetsObj.dataSetConfig.dataSourceAdditionalParam;
            $scope.outputAdditionalParameters = datasetsObj.dataSetConfig.dataSetAdditionalParam;
            $scope.datasets = angular.copy(datasets);
        },

        setActiveTab: function (index) {
            $scope.activeTab = index;
            if ($scope.tabActive <= index) {
                $scope.tabActive = index;
            }
        },
        openDatepicker: function (field) {
            $scope.projectsElement[field + 'Opened'] = true;
        },
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },

        addMoreInputAdditionalParameters: function () {
            var newItemNo = $scope.inputAdditionalParameters.length + 1;
            $scope.inputAdditionalParameters.push({id: newItemNo});
        },

        deleteOneInputAdditionalParameters: function (id) {
            var i = $scope.inputAdditionalParameters.indexOf(id);
            return i > -1 ? $scope.inputAdditionalParameters.splice(i, 1) : [];
        },
        addMoreOutputAdditionalParameters: function () {
            var newItemNo = $scope.outputAdditionalParameters.length + 1;
            $scope.outputAdditionalParameters.push({id: newItemNo});
        },

        deleteOneOutputAdditionalParameters: function (id) {
            var i = $scope.outputAdditionalParameters.indexOf(id);
            return i > -1 ? $scope.outputAdditionalParameters.splice(i, 1) : [];
        },

        addMoreAdditionalParameters: function () {
            var newItemNo = $scope.additionalParameters.length + 1;
            $scope.additionalParameters.push({id: newItemNo});
        },

        deleteOneAdditionalParameters: function (id) {
            var i = $scope.additionalParameters.indexOf(id);
            return i > -1 ? $scope.additionalParameters.splice(i, 1) : [];
        },
        onlineAdditionalParameter: function () {
            var newItemNo = $scope.onlineAdditionalParameters.length + 1;
            $scope.onlineAdditionalParameters.push({id: newItemNo});
        },

        deleteOneOnlineAdditionalParameters: function (id) {
            var i = $scope.onlineAdditionalParameters.indexOf(id);
            return i > -1 ? $scope.onlineAdditionalParameters.splice(i, 1) : [];
        },
        deleteDataSet: function (size, item) {
            slmlpService.dialogService.openDialog("views/delete-confirm.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {
                                $scope.confirmDelete = false;
                                setTimeout(function () {
                                    $scope.$apply(function () {
                                        $scope.projectsElement.dataSetList.splice($scope.projectsElement.dataSetList.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },

        checkDuplication: function (keyVal, additionalParam) {
            if (additionalParam == 'datasetAdditionalParameters') {
                var additionalParameterList = $scope.inputAdditionalParameters;
            } else if (additionalParam == 'dataSourceAdditionalParameters') {
                var additionalParameterList = $scope.outputAdditionalParameters
            } else if (additionalParam == 'offline') {
                var additionalParameterList = $scope.additionalParameters;
            } else if (additionalParam == 'online') {
                var additionalParameterList = $scope.onlineAdditionalParameters;
            }
            angular.forEach(additionalParameterList, function (data, key) {
                if (data.keyParam == keyVal.keyParam && keyVal.id != data.id && keyVal.keyParam != "") {
                    toastr.error("Key value Already Added");
                    keyVal.keyParam = "";
                }
            });
        },
        checkDuplicationDeployment: function (nameValue, additionalParam) {
            if (additionalParam == 'offline') {
                var additionalParameterList = $scope.additionalParameters;
                angular.forEach(additionalParameterList, function (data, key) {
                    if (data.name == nameValue.name && nameValue.id != data.id && nameValue.name != "") {
                        toastr.error("Key value Already Added");
                        nameValue.name = "";
                    }
                });
            } else if (additionalParam == 'online') {
                var additionalParameterList = $scope.onlineAdditionalParameters;
                angular.forEach(additionalParameterList, function (data, key) {
                    if (data.keyName == nameValue.keyName && nameValue.id != data.id && nameValue.keyName != "") {
                        toastr.error("Key value Already Added");
                        nameValue.keyName = "";
                    }
                });
            }

        },
        cancelDataSet: function () {
            $scope.updateDatasetBtn = false;
            $scope.dataSetId = "";
            $scope.datasets = {};
            $scope.outputAdditionalParameters = [];
            $scope.inputAdditionalParameters = [];
            $scope.projectsObject.addMoreInputAdditionalParameters();
            $scope.projectsObject.addMoreOutputAdditionalParameters();
            $scope.projectDatasetForm.submitted = false;
            $scope.datasetTab = 0;
        },
        saveDataset: function (form, datasetsObj) {
            form.submitted = true;
            var dataSourceAdditionalParams = [];
            var dataSetAdditionalParams = [];
            angular.forEach($scope.inputAdditionalParameters, function (val, key) {
                dataSourceAdditionalParams.push({'keyParam': val.keyParam, 'valueParam': val.valueParam});
            });
            $scope.dataSourceAdditionalParam = dataSourceAdditionalParams;
            angular.forEach($scope.dataSourceAdditionalParam, function (val, key) {
                $scope.paramValidation = true;
                if ((!angular.isUndefined(val.keyParam) && val.keyParam != "")) {
                    if (angular.isUndefined(val.valueParam) || val.valueParam == "") {
                        $scope.paramValidation = false;
                    }
                }
                if ((!angular.isUndefined(val.valueParam) && val.valueParam != "")) {
                    if (angular.isUndefined(val.keyParam) || val.keyParam == "") {
                        $scope.paramValidation = false;
                    }
                }
            });
            $scope.datasetTab = 0;
            if ($scope.datasetTab == 0 && !form.sourceAuthUrl.$invalid && $scope.paramValidation && datasetsObj.dataSourceUrl != '' && !angular.isUndefined(datasetsObj.dataSourceUrl)) {
                $scope.datasetTab = 1;
            }
            angular.forEach($scope.outputAdditionalParameters, function (val, key) {
                dataSetAdditionalParams.push({'keyParam': val.keyParam, 'valueParam': val.valueParam});
            });
            $scope.dataSetAdditionalParam = dataSetAdditionalParams;
            $scope.listDataset = true;
            $scope.dataSetNameValid = true;

            if (form.$valid && $scope.paramValidation) {
                if ($scope.updateDatasetBtn) {
                    angular.forEach($scope.projectsElement.dataSetList, function (data, key) {
                        if (data.name == datasetsObj.name && key != $scope.dataSetId) {
                            toastr.error("Dataset Name Already Added");
                            $scope.dataSetNameValid = false;
                        }
                    });
                } else {
                    angular.forEach($scope.projectsElement.dataSetList, function (data, key) {
                        if (data.name == datasetsObj.name) {
                            toastr.error("Dataset Name Already Added");
                            $scope.dataSetNameValid = false;
                        }
                    });
                }
                if ($scope.dataSetNameValid) {
                    var dataset = {};
                    dataset.name = datasetsObj.name;
                    dataset.dataSource = datasetsObj.dataSource;
                    dataset.dataSetConfig = {
                        dataSetStorageTypes : []
                    };
                   var datasetStorage = [];
                    datasetStorage.push(datasetsObj.storageType);
                    dataset.dataSetConfig.dataSourceUrl = datasetsObj.dataSourceUrl;
                    dataset.dataSetConfig.dataSourceAuthUrl = datasetsObj.dataSourceAuthUrl;
                    dataset.dataSetConfig.dataSetPublishUrl = datasetsObj.dataSetPublishUrl;
                    dataset.dataSetConfig.dataSetAuthUrl = datasetsObj.dataSetAuthUrl;
                    dataset.dataSetConfig.dataSetStorageTypes = datasetStorage;
                    dataset.dataSetConfig.dataSetVersionLimit = datasetsObj.datasetVerLimit;
                    dataset.dataSetConfig.dataSourceAdditionalParam = $scope.dataSourceAdditionalParam;
                    dataset.dataSetConfig.dataSetAdditionalParam = $scope.dataSetAdditionalParam;
                    if ($scope.updateDatasetBtn) {
                        $scope.projectsElement.dataSetList[$scope.dataSetId] = dataset;
                        $scope.dataSetId = "";
                        $scope.updateDatasetBtn = false;
                    } else {
                        $scope.projectsElement.dataSetList.push(dataset);
                    }
                    form.submitted = false;

                    $scope.datasets = {};
                    $scope.datasetTab = 0;
                    $scope.outputAdditionalParameters = [];
                    $scope.inputAdditionalParameters = [];
                    $scope.projectsObject.addMoreInputAdditionalParameters();
                    $scope.projectsObject.addMoreOutputAdditionalParameters();
                }
            }
        },

        saveTeamMembers: function (form, teamObj) {
            $scope.teamMembersFormSubmitted = true;
            $scope.teamMemberNameValid = true;
            $scope.inValidUser = 0;
            angular.forEach($scope.users, function (value, key) {
                if (teamObj.userName === value.userName || teamObj.userName === $scope.loginUser) {
                    $scope.inValidUser = 1;
                }
            });
            if ($scope.inValidUser === 0) {
                toastr.error("Team Member is not available... Invalid User Name");
            }
            angular.forEach($scope.projectsElement.teamMembersList, function (data, key) {
                if (data.userName == teamObj.userName) {
                    toastr.error("Team Member Name Already Added");
                    $scope.teamMemberNameValid = false;
                }
            });
            if (form.$valid && $scope.teamMemberNameValid && $scope.inValidUser == 1) {
                $scope.teamMembersFormSubmitted = false;
                if ($scope.defaultUser) {
                    $scope.projectsElement.teamMembersList = [{ "userId": $scope.adminUserId, "roleId": $scope.adminRoleId,"userName": $scope.loginUser,"roleName": "ADMIN"}]
                    $scope.defaultUser = false;
                }
                var teamMembers = {};
                angular.forEach($scope.users, function (value, key) {
                    if (teamObj.userName === value.userName) {
                        userObj = value;
                    }
                });
                angular.forEach($scope.roleList, function (value, key) {
                    if (teamObj.roleName === value.role) {
                        $scope.roleObj = value;
                    }
                });
                teamMembers.userId = userObj.id;
                teamMembers.roleId = $scope.roleObj.id;
                teamMembers.userName = teamObj.userName;
                teamMembers.roleName = teamObj.roleName;
                $scope.projectsElement.teamMembersList.push(teamMembers)
                form.submitted = false;
                $scope.teamMembers = {};
            }
        },
        editTeamMembers: function (teamMembersObject, $index) {
            $scope.teamMemberId = $index;
            $scope.updateTeamMemberBtn = true;
            $scope.teamMembers = angular.copy(teamMembersObject);
        },
        updateTeamMembers: function (form, teamObj) {
            $scope.teamMembersFormSubmitted = true;
            $scope.teamMemberNameValid = true;
            angular.forEach($scope.projectsElement.teamMembersList, function (data, key) {
                if (data.name == teamObj.name && key != $scope.teamMemberId) {
                    toastr.error("Team Member Name Already Added");
                    $scope.teamMemberNameValid = false;
                }
            });
            if (form.$valid && $scope.teamMemberNameValid) {
                $scope.teamMembersFormSubmitted = false;
                $scope.projectsElement.teamMembersList[$scope.teamMemberId] = teamObj;
                $scope.updateTeamMemberBtn = false;
                $scope.teamMemberId = "";
                $scope.teamMembers = {};
            }
        },
        saveDeploymentConfig: function (isValid, platformTools) {
            $scope.deploymentConfigFormSubmitted = true;
            if (isValid) {
                $scope.projectsObject.setActiveTab(7);
            }
        },
        tabSelected: function (index) {
            $scope.datasetTab = index;
        },
        addDataset: function () {
            $scope.listDataset = true;
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
                    $scope.projectRef = slmlpService.localStorageService.get("projectRef");
                });
            };
        },

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
                                        var hasResult = slmlpService.crudService.softDelete("project", item);
                                        hasResult.then(function (result) {
                                            $state.reload();
                                            $scope.projectsObject.list(1);
                                            toastr.success('Deleted Successfully.', {
                                                closeButton: true
                                            });
                                        })
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },
        deleteTeamMember: function (teamObj) {
            $scope.projectsElement.teamMembersList.splice($scope.projectsElement.teamMembersList.indexOf(teamObj), 1);
        },
        list: function (pageNumber) {
            $scope.projectsElement.listLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var hasactionServer = slmlpService.crudService.listAllWithPagination("project/list", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit});
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasactionServer = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "project/findbysearchtext?=" + "lang=" + language + encodeURI($scope.filter) + "&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
                    "limit": limit
                });
            }
            hasactionServer.then(function (result) {  // this is only run after $http completes
                $scope.projectsElement.listLoader = false;
                $scope.projectsElement.projectLists = result;
                // For pagination
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.projectsObject.list;
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

        searchList: function (quicksearch) {
            $scope.quicksearch = quicksearch;
            $scope.projectsObject.list(1);
        },

        changeSort: function (sortBy, pageNumber, sort) {
            $scope.projectsElement.listLoader = true;
           var sort = slmlpService.utilService.getSortingOrderBySortBy(sortBy);
            $scope.sort = sort;
            slmlpService.appConfig.sort.sortBy = 'id';
            sort.sortOrder = (sort.sortOrder == "+") ? "-" : "+";
            $scope.sort = sort;
            slmlpService.appConfig.sort.sortOrder = sort.sortOrder;
            slmlpService.appConfig.sort.sortBy = sortBy;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hasResult = {};
            var hasResult = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "project/list?=" + "lang=" + language + "&sortBy=" + slmlpService.appConfig.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
            hasResult.then(function (result) { // this is only run after $http
                $scope.projectsElement.listLoader = false;
                // completes0
                $scope.projectsElement.projectLists = result;
                // For pagination
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
            });
        },

        // GET LAUNCHERS
        getLaunchersList: function () {
            var additionalParams = "&type=Launchers"
            var hasResult = slmlpService.crudService.listAll("launcher/listbytype", additionalParams);
            hasResult.then(function (result) {
                $scope.launchersLists = result;
            });
        },

        // GET TRAINING_ENGINES
        gettrainingEnginesList: function () {
            var additionalParams = "&type=TrainingEngine"
            var hasResult = slmlpService.crudService.listAll("trainingengine/listbytype",additionalParams);
            hasResult.then(function (result) {
                $scope.trainingEnginesLists = result;
            });
        },

        // GET DEPLOYMENT PLATFORM TOOLS
        getPlatformToolsList: function () {
            var hasResult = slmlpService.crudService.listAll("platformtools/listbyisactive");
            hasResult.then(function (result) {
                $scope.platformTools = result;
            });
        },

//        getPlatformToolsList: function() {
//            var hasList = slmlpService.crudService.listAllByGateway("containers/container/api/launchers/listbytype",
//            "&type="+slmlpService.appConfig.IMAGE_TYPE.MODEL_PREDICTION);
//            var results = [];
//            hasList.then(function (result) {
//                $scope.platformTools = result;
//            });
//        },

        getDataSourceList: function () {
            var hasResult = slmlpService.crudService.listAll("dataset/listdatasourcebyisactive");
            hasResult.then(function (result) {
                $scope.datasourceList = result;
            });
        },
        getDataSetStorageTypeList: function () {
            var hasResult = slmlpService.crudService.listAll("datasetconfig/listdatasetstoragetypebyisactive");
            hasResult.then(function (result) {
                $scope.datasetStorageTypeList = result;
            });
        },
        getUsers: function (searchText) {
            var users = []
            $scope.users = [];
            var hasUsers = slmlpService.crudService.listAll("user/list");
            hasUsers.then(function (result) { // this is only run after $http
                // completes0
                angular.forEach(result, function (obj, key) {
                    if ($scope.loginUser === obj.userName) {
                        $scope.adminUserId = obj.id;
                    } else {
                        users.push(obj)
                    }
                })
                $scope.users = users;
            });
        },

        //GET ROLE DETAILS
        getRoles: function () {

            var hasResult = slmlpService.crudService.listAll("role/listbyisactive");
            hasResult.then(function (result) {
                $scope.roleList = result;
                angular.forEach(result, function (value, key) {
                    if (value.role === "ADMIN") {
                        $scope.adminRoleId = value.id;
                    }
                    $scope.roles.push(value.role);
                });
            });
        },

        //Cancel the creating project
        cancel: function () {
            $state.reload();
            $state.go('projects.list');
        },

        init: function () {
            $scope.projectsElement = {
                checkInOpened: false,
                checkOutOpened: false,
                dateOptions: slmlpService.dateService.getDateOptions(),
                dataSetList: [],
                teamMembersList: [],
                createLoader: false,
                listLoader: false

            };
            $scope.loginUser = slmlpService.localStorageService.get("user_name")
            $scope.projectsObject.getProject();
            
            $scope.projectsObject.list(1);
            $scope.projectsObject.getLaunchersList();
            $scope.projectsObject.gettrainingEnginesList();
            $scope.projectsObject.getPlatformToolsList();
            $scope.projectsObject.getDataSourceList();
            $scope.projectsObject.getDataSetStorageTypeList();


            $scope.inputAdditionalParameters = [];
            $scope.projectsObject.addMoreInputAdditionalParameters();
            $scope.outputAdditionalParameters = [];
            $scope.projectsObject.addMoreOutputAdditionalParameters();

            $scope.additionalParameters = [];
            $scope.projectsObject.addMoreAdditionalParameters();
            $scope.onlineAdditionalParameters = [];
            $scope.projectsObject.onlineAdditionalParameter();

            
            $scope.projectsElement.dateOptions.minDate = new Date();
            $scope.projects = {};
            $scope.datasets = {};
            $scope.teamMembers = {};
            $scope.project = {
                dataSets: [],
                projectUsers: [],
                launchers: {
                    checkAll: ""
                },
                trainingEngines: {
                    checkAll: ""
                }
            };

            $scope.nextButton = true;

            $scope.projectsObject.getUsers();
            $scope.projectsObject.getRoles();

        }

    }

    $scope.projectsObject.init();

}

angular
        .module('slmlp')
        .controller('projectsCtrl', projectsCtrl);
