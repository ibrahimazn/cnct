function departmentCtrl($scope, slmlpService, $http, $state, toastr) {
    /** Generic object for the controller. */
    $scope.departmentObject = {};
    $scope.global = slmlpService.appConfig;
    $scope.paginationObject = {};
    $scope.quickSearch = null;
    $scope.sort = slmlpService.appConfig.sort;
    $scope.paginationObject.sortOrder = '+';
    $scope.paginationObject.sortBy = 'name';
    slmlpService.appConfig.sort.sortBy = 'name';
    slmlpService.appConfig.sort.sortOrder = '+';
    var language = 'en';
    $scope.listPerPage = "5";
    $scope.departmentForm = {};

    $scope.departmentObject = {
        // BIGINS DEPARTMENT CRUD
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },

        // ADD DEPARTMENT
        addDepartment: function (size) {
            slmlpService.dialogService.openDialog("views/departments/create.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.departmentObject.save = function (form, department) {
                                $scope.departmentForm.submitted = true;
                                if ($scope.departmentForm.$valid) {
                                    $scope.departmentElement.createLoader = true;
                                    var hasResult = slmlpService.crudService.add("department", department);
                                    hasResult.then(function (result) {  // this is only run after $http completes
                                        $scope.departmentElement.createLoader = false;
                                        $scope.departmentForm.submitted = false;
                                        $state.reload();
                                        $scope.departmentObject.list(1);
                                        
                                        $uibModalInstance.dismiss('cancel');
                                        toastr.success('Created Successfully.', {
                                            closeButton: true
                                        });
                                        
                                    }).catch(function (result) {
                                        $scope.departmentElement.createLoader = false;
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
                            $scope.department = {};
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },

        // EDIT AND UPDATE DEPARTMENT
        edit: function (size, departmentObj, index) {
            slmlpService.dialogService.openDialog("views/departments/edit.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.department = angular.copy(departmentObj);
                            $scope.departmentElement.updateButton = true;
                            $scope.departmentElement.editedIndex = index;
                            $scope.departmentObject.update = function (department) {
                                $scope.departmentForm.submitted = true;
                                if ($scope.departmentForm.$valid) {
                                    $scope.departmentForm.submitted = false;
                                    $scope.departmentElement.editLoader = true;
                                    var hasResult = slmlpService.crudService.update("department/" + department.id, department);
                                    hasResult.then(function (result) {  
                                        $scope.departmentElement.editLoader = false;
                                        $state.reload();
                                        $scope.departmentObject.list(1);
                                        toastr.success('Updated Successfully.', {
                                            closeButton: true
                                        });
                                    }).catch(function (result) {
                                        $scope.departmentElement.editLoader = false;
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
                                    $scope.departmentElement.editedIndex = "";
                                    $scope.departmentElement.updateButton = false;
                                    $uibModalInstance.dismiss('cancel');
                                }
                            }
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                                $scope.departmentElement.updateButton = false;
                            };
                        }]);
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
                                        var hasResult = slmlpService.crudService.softDelete("department", item);
                                        hasResult.then(function (result) {
                                            $state.reload();
                                            $scope.departmentObject.list(1);
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
            $scope.departmentElement.listLoader = true;
            var limit = (angular.isUndefined($scope.paginationObject.limit)) ? slmlpService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
            if ($scope.quicksearch == null || angular.isUndefined($scope.quicksearch)) {
                var hasactionServer = slmlpService.crudService.listAllWithPagination("department/list", slmlpService.appConfig.paginationHeaders(pageNumber, limit), {
                    "limit": limit});
            } else {
                $scope.filter = "&searchText=" + $scope.quicksearch;
                hasactionServer = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "department/findbysearchtext?=" + "lang=" + language + encodeURI($scope.filter) + "&projectId=&sortBy=" + $scope.global.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {
                    "limit": limit
                });
            }
            hasactionServer.then(function (result) {  // this is only run after $http completes
                $scope.departmentElement.listLoader = false;
                $scope.departmentElement.departmentLists = result;
                // For pagination
                $scope.paginationObject.limit = limit;
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
                $scope.paginationObject.pagingMethod = $scope.departmentObject.list;
            }).catch(function (result) {
                    $scope.departmentElement.listLoader = false;
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
                });;
        },

        searchList: function (quicksearch) {
            $scope.quicksearch = quicksearch;
            $scope.departmentObject.list(1);
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
            var hasResult = slmlpService.promiseAjax.httpTokenRequest(slmlpService.appConfig.HTTP_GET, slmlpService.appConfig.APP_URL + "department/list?=" + "lang=" + language + "&sortBy=" + slmlpService.appConfig.sort.sortOrder + $scope.paginationObject.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
            hasResult.then(function (result) { // this is only run after $http
                // completes0
                $scope.departmentElement.departmentLists = result;
                // For pagination
                $scope.paginationObject.currentPage = pageNumber;
                $scope.paginationObject.totalItems = result.totalItems;
            });
        },

        init: function () {
            $scope.departmentElement = {
                createLoader: false,
                editLoader: false,
                listLoader: false
            };
            $scope.department = {};
            $scope.departmentElement.departmentLists = [];
            $scope.departmentObject.list(1);
        }
    }
    $scope.departmentObject.init();

}

angular
        .module('slmlp')
        .controller('departmentCtrl', departmentCtrl);

