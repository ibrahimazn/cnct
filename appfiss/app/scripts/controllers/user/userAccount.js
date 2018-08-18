function userAccountCtrl($scope, appfissService, $http, localStorageService, $stateParams, $window, $state, toastr) {
    /** Generic object for the controller. */
    $scope.userAccountObject = {};
    $scope.global = appfissService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'userName';
    appfissService.appConfig.sort.sortBy = 'userName';
    appfissService.appConfig.sort.sortOrder = '+';
    var language = 'en';
    $scope.listPerPage = "5";
    $scope.confirmPassword = false;
    $scope.roles = [];
    $scope.user = {};
    $scope.sort = function (keyname) {
        $scope.sortKey = keyname;   //set the sortKey to the param passed
        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
    }
    $scope.userAccountObject = {
        getUser: function (index) {
            var hasResult = appfissService.crudService.read("user", index)
            hasResult.then(function (result) {
                $scope.user = result;
            }).catch(function (result) {

            });
            $http.get("views/json/userAccount.json").success(function (result) {
                $scope.user = result[index];
            });
        },
        // ADD USER
        addUserAccount: function (size) {
            $scope.imageError = false;
            appfissService.dialogService.openDialog("views/user-accounts/create.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {

                            $scope.userAccountObject.save = function (form, user) {

                                // Image width and height validation
                                $scope.userAccountForm.submitted = true;
                                if (!angular.isUndefined(user)) {
                                    var role = user.userType;
                                    if (user.profileImgFile != "" && !angular.isUndefined(user.profileImgFile) && user.profileImgFile != null) {//
                                        var img = document.getElementById('profile-image');
                                        var width, height;
                                        var d_width = img.width;
                                        var d_height = img.height;
                                        if (d_width > 120 || d_height > 120) {
                                            $scope.imageError = true;
                                            toastr.error('Profile image resolution should be smaller than or equal to 120 x 120', {closeButton: true});
                                            $state.reload();
                                        }
                                    }
                                    var saveMethod = appfissService.appConfig.HTTP_POST;
                                    var saveUrl = "user/save";

                                    if (angular.isUndefined(user.lastName) || user.lastName == '') {
                                        user.lastName = '';
                                    }
                                    var regularExpression = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[A-Z]).{8,}$/;
                                    if (!regularExpression.test(user.confirmPassword)) {
                                        $scope.passwordMsg = true;
                                    } else {
                                        if (user.password != user.confirmPassword) {
                                            alert("Password did not match with Confirm Password");
                                        } else {
                                            if ($scope.userAccountForm.$valid && !$scope.imageError) {
                                                $scope.userAccountElement.createLoader = true;
                                                var formData = new FormData();
                                                angular.forEach(user, function (obj, key) {
                                                    if (!angular.isUndefined(obj)) {
                                                        formData.append(key, obj);
                                                    } else {
                                                        formData.append(key, "");
                                                    }
                                                });
                                                var hasResult = appfissService.promiseAjax.httpFileuploadRequest(formData, saveUrl, user, localStorageService)
                                                hasResult.then(function (result) {
                                                    $scope.userAccountElement.createLoader = false;
                                                    $state.reload();
                                                    $scope.userAccountForm.submitted = false;
                                                    $uibModalInstance.dismiss('cancel');
                                                    toastr.success('Created Successfully.', {
                                                        closeButton: true
                                                    });
                                                }).catch(function (result) {
                                                    $scope.userAccountElement.createLoader = false;
                                                    user.userType = role;
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
                                                                form[key].$invalid = true;
                                                                form[key].errorMessage = errorMessage;
                                                                $scope.showLoader = false;
                                                                var msg = errorMessage;
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },
        // EDIT AND UPDATE USER
        edit: function (size, userAccountObj, index) {
            appfissService.dialogService.openDialog("views/user-accounts/edit.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.editDepartmentList = [];
                            if (!userAccountObj.phone) {
                            } else {
                                userAccountObj.mobilePhone = userAccountObj.phone;
                            }
                            if (!userAccountObj.email) {
                                userAccountObj.email = '';
                            } else {
                                userAccountObj.email = userAccountObj.email;
                            }
                            angular.forEach($scope.departmentList, function (value, key) {
                                if (userAccountObj.department.name != value.name) {
                                    $scope.editDepartmentList.push(value);
                                }
                            });
                            $scope.userAccount = angular.copy(userAccountObj);
                            $scope.userAccount.profImage = 'http://' + $window.location.hostname + ':8080/' + 'resources/' + $scope.userAccount.profileImgFile;
                            $scope.userAccountElement.updateButton = true;
                            $scope.userAccountElement.editedIndex = index;
                            $scope.userAccountObject.update = function (form, user) {
                                if (user.profileImgFile != "" && !angular.isUndefined(user.profileImgFile) && user.profileImgFile != null) {//
                                    var img = document.getElementById('profile-image');
                                    var width, height;
                                    var d_width = img.width;
                                    var d_height = img.height;
                                    if (d_width > 120 || d_height > 120) {
                                        $scope.imageError = true;
                                        toastr.error('Profile image resolution should be smaller than or equal to 120 x 120', {closeButton: true});
                                        $state.reload();
                                    }
                                }
                                var saveMethod = appfissService.appConfig.HTTP_POST;
                                var saveUrl = "user/update/" + user.id;
                                $scope.userAccountForm.submitted = true;
                                if ($scope.userAccountForm.$valid) {
                                    user.departmentId = user.department.id;
                                    $scope.userAccountForm.submitted = false;
                                    var formData = new FormData();
                                    user.departmentId = user.department.id;
                                    angular.forEach(user, function (obj, key) {
                                        if (!angular.isUndefined(obj)) {
                                            formData.append(key, obj);
                                        } else {
                                            formData.append(key, "");
                                        }
                                    });
                                    $scope.userAccountElement.editLoader = true;
                                    var hasResult = appfissService.promiseAjax.httpFileuploadRequest(formData, saveUrl, user, localStorageService)
                                    hasResult.then(function (result) {
                                        $scope.userAccountElement.editLoader = false;
                                        $uibModalInstance.dismiss('cancel');
                                        $state.reload();
                                        $scope.userAccountObject.list(1);
                                        $uibModalInstance.dismiss('cancel');
                                        toastr.success('Updated Successfully.', {
                                            closeButton: true
                                        });
                                    }).catch(function (result) {
                                        $scope.userAccountElement.editLoader = false;
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
                                                    form[key].$invalid = true;
                                                    form[key].errorMessage = errorMessage;
                                                    $scope.showLoader = false;
                                                    var msg = errorMessage;
                                                });
                                            }
                                        }
                                        $uibModalInstance.dismiss('cancel');
                                    });
                                    $scope.userAccountElement.editedIndex = "";
                                    $scope.userAccountElement.updateButton = false;
                                }
                            }
                            $scope.cancel = function () {
                                $scope.userAccountElement.updateButton = false;
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        launchFunction: function () {
            var hasResult = appfissService.crudService.listAll("user/launchfunction");
            hasResult.then(function (result) {
                $state.reload();
                toastr.success('Launched file function Successfully.', {
                    closeButton: true
                });
            })
        },
        // DELETE USER
        delete: function (size, item) {
            appfissService.dialogService.openDialog("views/delete-confirm.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.confirmDelete = function () {
                                $scope.confirmDelete = false;
                                setTimeout(function () {
                                    $scope.$apply(function () {
                                        var hasResult = appfissService.crudService.softDelete("user", item);
                                        hasResult.then(function (result) {
                                            $state.reload();
                                            $scope.userAccountObject.list(1);
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

        list: function (pageNumber) {

            $scope.userAccountElement.listLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var hasactionServer = appfissService.crudService.listAllWithPagination("user/list", appfissService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit});
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasactionServer = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "user/findbysearchtext?=" + "lang=" + language + "&userType=" + $scope.role + encodeURI($scope.filter) + "&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
                    "limit": limit
                });
            }
            hasactionServer.then(function (result) {  // this is only run after $http completes
                $scope.userAccountElement.userLists = result;
                $scope.userAccountElement.listLoader = false;
                // For pagination
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.userAccountObject.list;
            });

            $http.get("views/json/userAccount.json").success(function (result) {
                $scope.userAccountElement.userLists = result;
                $scope.userAccountElement.listLoader = false;
            });


        },

        searchList: function (quicksearch, type) {
            $scope.quicksearch = quicksearch;
            $scope.role = type;
            $scope.userAccountObject.list(1);
        },

        changeSort: function (sortBy, pageNumber, sort) {
            var sort = appfissService.utilService.getSortingOrderBySortBy(sortBy);
            $scope.sort = sort;
            appfissService.appConfig.sort.sortBy = 'id';
            sort.sortOrder = (sort.sortOrder == "+") ? "-" : "+";
            $scope.sort = sort;
            appfissService.appConfig.sort.sortOrder = sort.sortOrder;
            appfissService.appConfig.sort.sortBy = sortBy;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hasResult = {};
            var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "user/list?=" + "lang=" + language + "&sortBy=" + appfissService.appConfig.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
            hasResult.then(function (result) { // this is only run after $http
                // completes0
                $scope.userAccountElement.userLists = result;
                // For pagination
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
            });
        },

        //END USER ACCOUNT
        init: function () {
            $scope.loginUser = appfissService.localStorageService.get("user_name")
            $scope.userAccountElement = {
                createLoader: false,
                editLoader: false,
                listLoader: false,
            };
            if (!angular.isUndefined($stateParams.id)) {
                $scope.userAccountObject.getUser($stateParams.id);
            }
            $scope.userAccountElement.userLists = [];
            $scope.userAccountObject.list(1);
            // $scope.userAccountObject.getUser(0);
        }
    }

    $scope.userAccountObject.init();

}

angular
        .module('appfiss')
        .controller('userAccountCtrl', userAccountCtrl);
