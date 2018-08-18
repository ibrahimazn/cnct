function webSocket($rootScope) {
	var webSocketObj = {
		isConnected : false,

		stompClient : null,

		connect : function(url) {
			console.log("CONNECTING...", url);
			var socket = new SockJS(url);
			webSocketObj.stompClient = Stomp.over(socket);
                        console.log("STOMP CLIENT",webSocketObj.stompClient);
			webSocketObj.stompClient.connect({}, function(frame) {
				console.log("CONNECTED>>>");
				webSocketObj.isConnected = true;
				webSocketObj.subscribe();
			});
		},
               
		subscribe : function() {
                     webSocketObj.stompClient.subscribe('/topic/deployment', function (message) {
                        var eventObject = JSON.parse(message.body);
                        console.log("eventObject", eventObject);
                        var eventKey = eventObject.involvedObject.kind + "_" + eventObject.reason;
                        $rootScope.$broadcast(eventKey, JSON.parse(message.body));
                    });
		},

		send : function() {
			if (webSocketObj.isConnected) {
				webSocketObj.stompClient.send(destination, headers, object);
			}
		},

		disconnect : function() {
			if (webSocketObj.stompClient !== null) {
				console.log("<<<DISCONNECTED");
				webSocketObj.stompClient.disconnect();
			}
		}
	}

	return webSocketObj;
};

angular.module('slmlp').factory('webSocket', webSocket);