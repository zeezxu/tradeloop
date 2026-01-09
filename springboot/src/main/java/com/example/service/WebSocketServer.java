package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//WebSocket server for realtime chat

@ServerEndpoint(value = "/chatServer/{userId}")
@Component
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    // Map to track current online connections
    public static final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();

    // Called when a new connection is established
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        sessionMap.put(userId, session);
        log.info("New user joined, userId={}, current online users: {}", userId, sessionMap.size());
    }

    // Called when a connection is closed
    @OnClose
    public void onClose(Session session, @PathParam("userId") Integer userId) {
        sessionMap.remove(userId);
        log.info("Connection closed, removed userId={}'s session, current online users: {}", userId, sessionMap.size());
    }

    /**
     * Called when a message is received from client
     * Acts as a message relay station
     * Receives JSON data sent from browser via socket.send
     */
    @OnMessage
    public void onMessage(String message) {
        for (Session session : sessionMap.values()) {
            log.info("Server sending message to client [{}]: {}", session.getId(), message);
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("Failed to send WebSocket message", e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Socket error:", error);
    }
}

