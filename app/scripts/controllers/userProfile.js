function userProfileCtrl($scope, slmlpService, $http, toastr) {
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
        .module('slmlp')
        .controller('userProfileCtrl', userProfileCtrl);
