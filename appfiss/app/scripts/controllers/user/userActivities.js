function userActivitiesCtrl($scope, appfissService, $http, toastr) {
    /** Generic object for the controller. */
    $scope.userActivitiesObject = {};
    $scope.userActivitiesObject = {
        init: function() {
            $scope.userActivitiesElement = {};
        }
    };
    
    $scope.userActivitiesObject.init();

}

angular
        .module('appfiss')
        .controller('userActivitiesCtrl', userActivitiesCtrl);
