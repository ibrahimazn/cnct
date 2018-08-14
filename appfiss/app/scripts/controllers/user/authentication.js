var authCtrl = function($scope, $http, localStorageService, appConfig) {
    
    
    var urlParams;
    (window.onpopstate = function () {
        var match,
            pl     = /\+/g,  // Regex for replacing addition symbol with a space
            search = /([^&=]+)=?([^&]*)/g,
            decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
            query  = window.location.search.substring(1);

        urlParams = {};
        while (match = search.exec(query))
           urlParams[decode(match[1])] = decode(match[2]);
    })();
    
    
    $scope.auth = {
        
            parseJwt: function(token) {
                var base64Url = token.split('.')[1];
                var base64 = base64Url.replace('-', '+').replace('_', '/');
                return JSON.parse(window.atob(base64));
            },
            progressValue: 70,
            httpRequest:  function (method, data, url, headers) {
            var config = {
                "method": method,
                "data": data,
                "url": url,
                "headers": {'Authorization': "Basic Y2xpZW50MTpzZWNyZXQ="}
            };
            return $http(config).then(function (result) {
                if(!angular.isUndefined(result.data)) {
                    $scope.auth.progressValue = 100;
                    var data = result.data;
                    var accessToken = $scope.auth.parseJwt(data.access_token);
                    localStorageService.set("user_name", accessToken.user_name);
                    localStorageService.set("access_token", data.access_token);
                    window.location.href = appConfig.UI_APP_URL  + "#/index/dashboard";
                }
                return result.data;
            }).catch(function (result) {
                if (!angular.isUndefined(result) && (result.status === -1 || result.status === 401)) {
                }
                throw result;
            });
        },
    }
    
    var accessCode = urlParams.code;
    var accessTokenUrl = appConfig.AUTH_APP_URL + "auth/oauth/token?grant_type=authorization_code&code=" + accessCode
    + "&redirect_uri=" + appConfig.UI_APP_URL + "authentication.html";
    $scope.auth.httpRequest("POST", {}, accessTokenUrl);
    
}

angular
    .module('appfiss')
    .controller('authCtrl', authCtrl);