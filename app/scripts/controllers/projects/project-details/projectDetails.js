function projectSummaryCtrl($scope, slmlpService, $http) {
    /** Generic object for the controller. */
    $scope.projectSummaryObject = {};
    $scope.projectSummaryObject = {
        setActiveTab: function (index) {
            $scope.projectDetailsTab = index;
        },
        init: function () {
            $scope.projectSummaryElement = {
                checkInOpened: false,
                checkOutOpened: false,
                dateOptions: slmlpService.dateService.getDateOptions()
            };
        }
    }

    $scope.projectSummaryObject.init();

}

angular
        .module('slmlp')
        .controller('projectSummaryCtrl', projectSummaryCtrl);
