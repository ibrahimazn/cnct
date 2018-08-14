package az.ldap;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SubProtocolHandler;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

import az.ldap.sync.constants.GenericConstants;

public class CustomSubProtocolWebSocketHandler extends SubProtocolWebSocketHandler {

    /**
     * Logger websocket connect, message, disconnect, etc.,
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSubProtocolWebSocketHandler.class);

    @Autowired
    private SessionHandler sessionHandler;

    private MessageChannel clientInboundChannel;

    public CustomSubProtocolWebSocketHandler(MessageChannel clientInboundChannel, SubscribableChannel clientOutboundChannel) {
        super(clientInboundChannel, clientOutboundChannel);
        this.clientInboundChannel = clientInboundChannel;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("New websocket connection was established");
        LOGGER.debug("new connection" + session.getId());
		sessionHandler.register(session);
        super.afterConnectionEstablished(session);
    }

    /**
	 * Handle an inbound message from a WebSocket client.
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		SubProtocolHandler protocolHandler = findProtocolHandler(session);
		LOGGER.debug("new messsage from client session id" + session.getId());
		LOGGER.debug("new messsage from client" + message.getPayload());
		protocolHandler.handleMessageFromClient(session, message, this.clientInboundChannel);
	}
	/**
	 * Handle an outbound Spring Message to a WebSocket client.
	 */
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		LOGGER.debug("Message to client session id " + (String) message.getHeaders().get(GenericConstants.SIMP_SESSION_ID));
		LOGGER.debug("Message to client " + message.getPayload());
		super.handleMessage(message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		LOGGER.debug("disconnected session" + session.getId());
		super.afterConnectionClosed(session, closeStatus);
	}
}
