function userProfileCtrl($scope, appfissService, $http, toastr) {
    /** Generic object for the controller. */
    $scope.userProfileObject = {};
    $scope.userProfileObject = {
        init: function() {
            $scope.userProfileElement = {};
        }
    };
    
    $scope.userProfileObject.init();

}

angular
        .module('appfiss')
        .controller('userProfileCtrl', userProfileCtrl);
