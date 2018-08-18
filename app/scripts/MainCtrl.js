/**
 * SLMLP - Project Management App
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl($scope, appConfig, slmlpService, $http) {
    $scope.resetModelTabActive = function(){
        slmlpService.localStorageService.set("automaticTab", 0);
    } 
    
    var vm = this;
    vm.bodyClasses = 'default';

    // this'll be called on every state change in the app
    $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
        if (angular.isDefined(toState.data.bodyClasses)) {
            vm.bodyClasses = toState.data.bodyClasses;
            return;
        }

        vm.bodyClasses = 'default';
    });
    
    
    $scope.mainObject = {
        userName: slmlpService.localStorageService.get("user_name"),
        logout: function() {
            
            var config = {
                "method": "POST",
                "data": {
                },
                "url": appConfig.AUTH_APP_URL + "auth/oauth/token/revoke?token=" + slmlpService.localStorageService.get("access_token"),
                "headers": {}
            };
            return $http(config).then(function (result) {
                alert()
                    slmlpService.localStorageService.set("access_token", null);
                    window.location.href = appConfig.AUTH_APP_URL  + "auth/oauth/authorize?&response_type=code&client_id=client1&redirect_uri=" + appConfig.UI_APP_URL + "authentication.html";

                return result.data;
            }).catch(function (result) {
                if (!angular.isUndefined(result) && (result.status === -1 || result.status === 401)) {
                }
                throw result;
            });
            
        },

    }
    
 
    
};


angular
    .module('slmlp')
    .controller('MainCtrl', MainCtrl)