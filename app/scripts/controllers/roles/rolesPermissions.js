function rolesPermissionsCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.listPerPage = "5";
    $scope.showPrivilege = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };
    $scope.rolesPermissionsObject = {};

    $scope.rolesPermissionsObject = {
        sort: function (tablename) {
            $scope.sortKey = tablename;
            $scope.reverse = !$scope.reverse;
        },
        checkAllGroup: function (isCheckAll) {
            angular.forEach($scope.rolesPermissionsElement.role.groups, function (rolesGroup) {
                $scope.isAllChecked['group' + rolesGroup.id] = isCheckAll;
                $scope.rolesPermissionsObject.checkGroup(rolesGroup, 0);
            });
        },

        checkAll: function (isCheckAll) {
            $scope.allPermissions = $scope.rolesPermissionsElement.role.groups.every(function (group) {
                return $scope.isAllChecked['group' + group.id] == true;
            });
        },
        checkGroup: function (group, tabIndex) {
            $scope.showPrivilege(tabIndex);
            if (angular.isUndefined($scope.isAllChecked['group' + group.id])) {
                $scope.isAllChecked['group' + group.id] = false;
            }
            angular.forEach(group.permissions, function (permission) {
                permission.checked = $scope.isAllChecked['group' + group.id];
            });
            $scope.rolesPermissionsObject.checkAll();
        },
        optionToggled: function (permissions, group) {
            $scope.isAllChecked['group' + group.id] = permissions.every(function (itm) {
                return itm.checked == true;
            })
            $scope.rolesPermissionsObject.checkAll();
        },
        // GET Roles 
        getRoles: function () {
            $http.get("views/json/roles.json").success(function (result) {
                $scope.rolesElement.rolesLists = result;
            });
        },
        save: function (roleObj) {
            $scope.rolesElement.rolesLists.push(roleObj);
        },
        edit: function () {
//            $scope.rolesElement.rolesLists.push(roleObj);
        },
        // DELETE Roles
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
                                        $scope.rolesElement.rolesLists.splice($scope.rolesElement.rolesLists.indexOf(item), 1);
                                        $uibModalInstance.dismiss('cancel');
                                    })
                                }, 1000);
                            };

                        }]);
        },

        init: function () {
            $scope.isAllChecked = {};
            $scope.rolesElement = {};
            $scope.rolesElement.rolesLists = []
            $scope.showPrivilege(0);
            $scope.rolesPermissionsElement = {
                rolesList: [
                    {
                        name: 'roles 1'

                    },
                    {
                        name: 'roles 2'
                    }
                ],
                role: {
                    id: 1,
                    name: "ADMIN",
                    groups: [
                        {
                            id: 1,
                            name: "Project",
                            permissions: [
                                {
                                    id: 1,
                                    name: "Create Project",
                                    checked: false
                                },
                                {
                                    id: 2,
                                    name: "Update Project Status",
                                    checked: false
                                },
                                {
                                    id: 3,
                                    name: "Delete Project",
                                    checked: false
                                },
                                {
                                    id: 4,
                                    name: "Assign Users to Projects",
                                    checked: false
                                },
                                {
                                    id: 5,
                                    name: "Project Resource Quota Modification",
                                    checked: false
                                },
                                {
                                    id: 6,
                                    name: "Release Users to Projects",
                                    checked: false
                                },
                                {
                                    id: 7,
                                    name: "Modeling",
                                    checked: false
                                },
                                {
                                    id: 8,
                                    name: "Muning",
                                    checked: false
                                },
                                {
                                    id: 9,
                                    name: "Scoring",
                                    checked: false
                                }
                            ]
                        },
                        {
                            id: 2,
                            name: "Department",
                            permissions: [
                                {
                                    id: 10,
                                    name: "Create Department",
                                    checked: false
                                },
                                {
                                    id: 11,
                                    name: "Update Department Status",
                                    checked: false
                                },
                                {
                                    id: 12,
                                    name: "Delete Department",
                                    checked: false
                                },
                                {
                                    id: 13,
                                    name: "Department Resource Quota Modification",
                                    checked: false
                                }
                            ]
                        },
                        {
                            id: 3,
                            name: "User Account",
                            permissions: [
                                {
                                    id: 14,
                                    name: "Create User",
                                    checked: false
                                },
                                {
                                    id: 15,
                                    name: "Update User",
                                    checked: false
                                },
                                {
                                    id: 16,
                                    name: "Delete User",
                                    checked: false
                                },
                                {
                                    id: 17,
                                    name: "Enable User",
                                    checked: false
                                },
                                {
                                    id: 19,
                                    name: "Disable User",
                                    checked: false
                                },
                                {
                                    id: 18,
                                    name: "Reset User Password",
                                    checked: false
                                }
                            ]
                        },
                        {
                            id: 4,
                            name: "Roles",
                            permissions: [
                                {
                                    id: 21,
                                    name: "Create Roles",
                                    checked: false
                                },
                                {
                                    id: 22,
                                    name: "Update Roles",
                                    checked: false
                                },
                                {
                                    id: 23,
                                    name: "Delete Roles",
                                    checked: false
                                },
                                {
                                    id: 24,
                                    name: "Enable Roles",
                                    checked: false
                                },
                                {
                                    id: 25,
                                    name: "Disable Roles",
                                    checked: false
                                },
                                {
                                    id: 26,
                                    name: "Assign Roles",
                                    checked: false
                                }
                            ]
                        }
                    ],

                }
            };
            $scope.rolesPermissionsObject.getRoles();
            $scope.rolesPermissions = {};
        }
    }

    $scope.rolesPermissionsObject.init();

}

angular
        .module('slmlp')
        .controller('rolesPermissionsCtrl', rolesPermissionsCtrl);
