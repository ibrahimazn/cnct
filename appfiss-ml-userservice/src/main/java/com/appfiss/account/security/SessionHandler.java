package com.appfiss.account.security;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SessionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHandler.class);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public SessionHandler() {
       /* scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sessionMap.keySet().forEach(k -> {
                    try {
                        sessionMap.get(k).close();
                        sessionMap.remove(k);
                    } catch (IOException e) {
                        LOGGER.error("Error while closing websocket session: {}", e);
                    }
                });
            }
        }, 10, 10, TimeUnit.SECONDS);*/
    }

    public void register(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
    }

    public void unregister(WebSocketSession session, String sessionId) throws IOException {
    	 sessionMap.get(sessionId).close();
         sessionMap.remove(sessionId);
    }

	public Boolean checkSessions(WebSocketSession session) throws IOException {
		return sessionMap.containsValue(session);
	}

	public Boolean checkKey(WebSocketSession session, String sessionId) throws IOException {
		return sessionMap.containsKey(sessionId);
	}

	public WebSocketSession singleSession(String sessionId) throws IOException {
		return sessionMap.get(sessionId);
	}

}
