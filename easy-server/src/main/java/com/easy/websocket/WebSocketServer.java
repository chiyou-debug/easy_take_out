package com.easy.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    private static Map<String, Session> sessionMap = new HashMap<>(); // Session objects that have established a connection with the server

    @OnOpen // Triggered when a connection is established
    public void open(Session session, @PathParam("sid") String sid) {
        log.info("Connection established, {}", sid);
        sessionMap.put(sid, session);
    }

    @OnMessage // Triggered when a message is received from the client
    public void onMessage(Session session, String message, @PathParam("sid") String sid) {
        log.info("Message received, {}", message);
    }

    @OnClose // Triggered when the connection is closed
    public void onClose(Session session, @PathParam("sid") String sid) {
        log.info("Connection closed");
        sessionMap.remove(sid);
    }

    @OnError // Triggered on communication errors
    public void onError(Session session, @PathParam("sid") String sid, Throwable throwable) {
        log.info("An error occurred");
        throwable.printStackTrace();
    }


    /**
     * Broadcast message
     */
    public void sendMessageToBackendSystem(String message) throws Exception {
        Collection<Session> sessions = sessionMap.values();
        if (!CollectionUtils.isEmpty(sessions)) {
            for (Session session : sessions) {
                // Send message
                session.getBasicRemote().sendText(message);
            }
        }
    }
}
