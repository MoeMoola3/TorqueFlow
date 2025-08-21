package com.moola.obd.analyzer.handler;

import com.moola.obd.analyzer.service.DataSenderService;
import com.moola.obd.analyzer.service.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Custom WebSocket handler to manage WebSocket sessions.
 * This handler extends TextWebSocketHandler to handle text messages.
 * It uses the WebSocketSessionManager to start and stop data streams for each session.
 */
@Component
public class ObdWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final DataSenderService dataSenderService;

    public ObdWebSocketHandler(WebSocketSessionManager sessionManager, DataSenderService dataSenderService) {
        this.sessionManager = sessionManager;
        this.dataSenderService = dataSenderService;
    }

    /**
     * Called after a new WebSocket connection is established.
     * This method starts the scheduled task for data generation and sending.
     *
     * @param session The newly opened WebSocket session.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionManager.getSessions().put(session.getId(), session);
        sessionManager.startSessionTask(session.getId(), dataSenderService::sendDataToClients);
        session.sendMessage(new TextMessage("Connection established. Waiting for OBD data..."));
    }

    /**
     * Called when a WebSocket connection is closed.
     * This method stops the scheduled task associated with the session.
     *
     * @param session The WebSocket session that was closed.
     * @param status The status of the connection closure.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionManager.stopSessionTask(session.getId());
    }

    /**
     * Handles incoming text messages from the client.
     * This method can be used to process commands from the client, such as
     * starting or stopping a stream.
     *
     * @param session The WebSocket session.
     * @param message The text message received from the client.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message from client " + session.getId() + ": " + payload);

        if ("close".equalsIgnoreCase(payload)) {
            session.close(CloseStatus.NORMAL);
            System.out.println("Connection with client " + session.getId() + " closed by client request.");
        }
    }
}
