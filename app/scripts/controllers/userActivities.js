function userActivitiesCtrl($scope, slmlpService, $http, toastr) {
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
        .module('slmlp')
        .controller('userActivitiesCtrl', userActivitiesCtrl);
