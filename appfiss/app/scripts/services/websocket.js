function webSocket($rootScope, toastr, appConfig, localStorageService) {
	var webSocketObj = {
		isConnected : false,

		stompClient : null,

		headers : {},

		connect : function(url) {
			console.log("CONNECTING...", url);
			var socket = new SockJS(url, null, {
        'protocols_whitelist': ['websocket']
    });
			webSocketObj.stompClient = Stomp.over(socket);
                        console.log("STOMP CLIENT",webSocketObj.stompClient);
			webSocketObj.headers['Authorization'] = appConfig.TOKEN_BEARER + localStorageService.get("access_token");
			webSocketObj.stompClient.connect(webSocketObj.headers, function(frame) {
				console.log("CONNECTED>>>");
				webSocketObj.isConnected = true;
				webSocketObj.subscribe();
			});
		},

		subscribe : function() {
                     webSocketObj.stompClient.subscribe('/topic/deployment.event', function (message) {
                        var eventObject = JSON.parse(message.body);
                        //console.log("eventObject", eventObject);
						// toastr.success(eventObject.content, {
	                    //     closeButton: true
	                    // });
                        $rootScope.$broadcast("launchers.create", JSON.parse(message.body));
                    });
		},

		send : function() {
			if (webSocketObj.isConnected) {
				webSocketObj.stompClient.send(destination, webSocketObj.headers, object);
			}
		},

		disconnect : function() {
			if (webSocketObj.stompClient !== null) {
				console.log("<<<DISCONNECTED");
				webSocketObj.stompClient.disconnect(webSocketObj.headers);
			}
		}
	}

	return webSocketObj;
};

angular.module('appfiss').factory('webSocket', webSocket);
