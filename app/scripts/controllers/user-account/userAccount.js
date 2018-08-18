function userAccountCtrl($scope, slmlpService, $http, localStorageService, $window, $state, toastr) {
    /** Generic object for the controller. */
    $scope.userAccountObject = {};
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'userName';
    slmlpService.appConfig.sort.sortBy = 'userName';
    slmlpService.appConfig.sort.sortOrder = '+';
    var language = 'en';
    $scope.listPerPage = "5";
    $scope.confirmPassword = false;
    $scope.roles = [];
    $scope.sort = function (keyname) {
        $scope.sortKey = keyname;   //set the sortKey to the param passed
        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
    }
    $scope.userAccountObject = {

        //GET DEPARTMENT DETAILS
        getDepartments: function () {
            var hasResult = slmlpService.crudService.listAll("department/listbyisactive");
            hasResult.then(function (result) {
                $scope.departmentList = result;
            });
        },

        //GET ROLE DETAILS
        getRoles: function () {
            var hasResult = slmlpService.crudService.listAll("role/listbyisactive");
            hasResult.then(function (result) {
                $scope.roleList = result;
                angular.forEach(result, function (value, key) {
                    $scope.roles.push(value.role)
                });
            });
        },

        // ADD USER
        addUserAccount: function (size) {
            $scope.imageError = false;
            slmlpService.dialogService.openDialog("views/user-accounts/create.html", size, $scope,
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
                                    var saveMethod = slmlpService.appConfig.HTTP_POST;
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
                                                user.departmentId = user.department.id;
                                                user.userType = user.userType.role;
                                                angular.forEach(user, function (obj, key) {
                                                    if (!angular.isUndefined(obj)) {
                                                        formData.append(key, obj);
                                                    } else {
                                                        formData.append(key, "");
                                                    }
                                                });
                                                var hasResult = slmlpService.promiseAjax.httpFileuploadRequest(formData, saveUrl, user, localStorageService)
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
        addLdapUser: function (size) {
            slmlpService.dialogService.openDialog("views/user-accounts/create-ldap-user.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.userAccount = {
                                userType: 'User',
                                department: 'OPS'
                            };
                            $scope.userAccountObject.save = function (userAccount) {
                                $scope.userAccountForm.submitted = true;
                                if (true) {
                                    $scope.userAccountForm.submitted = false;
                                    $scope.userAccountElement.userLists.push(userAccount);

                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        // EDIT AND UPDATE USER
        edit: function (size, userAccountObj, index) {
            slmlpService.dialogService.openDialog("views/user-accounts/edit.html", size, $scope,
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
                                var saveMethod = slmlpService.appConfig.HTTP_POST;
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
                                    var hasResult = slmlpService.promiseAjax.httpFileuploadRequest(formData, saveUrl, user, localStorageService)
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
            var hasResult = slmlpService.crudService.listAll("user/launchfunction");
            hasResult.then(function (result) {
                $state.reload();
                toastr.success('Launched file function Successfully.', {
                    closeButton: true
                });
            })
        },
        // DELETE USER
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
                                        var hasResult = slmlpService.crudService.softDelete("user", item);
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
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var hasactionServer = slmlpService.crudService.listAllWithPagination("user/list", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit});
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasactionServer = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "user/findbysearchtext?=" + "lang=" + language + "&userType=" + $scope.role + encodeURI($scope.filter) + "&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
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
        },

        searchList: function (quicksearch, type) {
            $scope.quicksearch = quicksearch;
            $scope.role = type;
            $scope.userAccountObject.list(1);
        },

        changeSort: function (sortBy, pageNumber, sort) {
            var sort = slmlpService.utilService.getSortingOrderBySortBy(sortBy);
            $scope.sort = sort;
            slmlpService.appConfig.sort.sortBy = 'id';
            sort.sortOrder = (sort.sortOrder == "+") ? "-" : "+";
            $scope.sort = sort;
            slmlpService.appConfig.sort.sortOrder = sort.sortOrder;
            slmlpService.appConfig.sort.sortBy = sortBy;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            var hasResult = {};
            var hasResult = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "user/list?=" + "lang=" + language + "&sortBy=" + slmlpService.appConfig.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
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
            $scope.loginUser = slmlpService.localStorageService.get("user_name")
            $scope.userAccountElement = {
                createLoader: false,
                editLoader: false,
                listLoader: false,
            };
            $scope.userAccountObject.getDepartments();
            $scope.userAccountObject.getRoles();
            $scope.userAccountElement.userLists = [];
            $scope.userAccountObject.list(1);
        }
    }

    $scope.userAccountObject.init();

}

angular
        .module('slmlp')
        .controller('userAccountCtrl', userAccountCtrl);
