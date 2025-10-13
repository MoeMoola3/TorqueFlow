//package com.moola.obd.analyzer.handler;
//
//import com.moola.obd.analyzer.service.DataSenderService;
//import com.moola.obd.analyzer.service.WebSocketSessionManager;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
///**
// * Custom WebSocket handler to manage WebSocket sessions.
// * It uses the WebSocketSessionManager to start and stop data streams for each session.
// */
//@Component
//public class ObdWebSocketHandler extends TextWebSocketHandler {
//
//    private final WebSocketSessionManager sessionManager;
//    private final DataSenderService dataSenderService;
//
//    public ObdWebSocketHandler(WebSocketSessionManager sessionManager, DataSenderService dataSenderService) {
//        this.sessionManager = sessionManager;
//        this.dataSenderService = dataSenderService;
//    }
//
//    /**
//     * Called after a new WebSocket connection is established.
//     * This method starts the scheduled task for data generation and sending.
//     *
//     * @param session The newly opened WebSocket session.
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessionManager.storeSession(session.getId(), session);
//        sessionManager.startSessionTask(session.getId(), dataSenderService::sendDataToClients);
//        session.sendMessage(new TextMessage("Connection established. Waiting for OBD data..."));
//    }
//
//    /**
//     * Called when a WebSocket connection is closed.
//     * This method stops the scheduled task associated with the session.
//     *
//     * @param session The WebSocket session that was closed.
//     * @param status The status of the connection closure.
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessionManager.stopSessionTask(session.getId());
//    }
//
//    /**
//     * Handles incoming text messages from the client.
//     * This method can be used to process commands from the client, such as
//     * starting or stopping a stream.
//     *
//     * @param session The WebSocket session.
//     * @param message The text message received from the client.
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        System.out.println("Received message from client " + session.getId() + ": " + payload);
//
//        if ("close".equalsIgnoreCase(payload)) {
//            session.close(CloseStatus.NORMAL);
//            System.out.println("Connection with client " + session.getId() + " closed by client request.");
//        }
//    }
//}



package com.moola.obd.analyzer.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.obd.analyzer.model.EngineMode;
import com.moola.obd.analyzer.model.WebSocketMessage;
import com.moola.obd.analyzer.service.DataSenderService;
import com.moola.obd.analyzer.service.ObdDataService;
import com.moola.obd.analyzer.service.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ObdWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final DataSenderService dataSenderService;
    private final ObdDataService obdDataService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ObdWebSocketHandler(WebSocketSessionManager sessionManager, DataSenderService dataSenderService, ObdDataService obdDataService) {
        this.sessionManager = sessionManager;
        this.dataSenderService = dataSenderService;
        this.obdDataService = obdDataService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessionManager.storeSession(sessionId, session);
        // The task to be run is the sendDataToClients method
        sessionManager.startSessionTask(sessionId, dataSenderService::sendDataToClients);
        session.sendMessage(new TextMessage("Connection established. Waiting for OBD data..."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            WebSocketMessage webSocketMessage = objectMapper.readValue(message.getPayload(), WebSocketMessage.class);

            if ("modeChange".equalsIgnoreCase(webSocketMessage.getType())) {
                String modeString = webSocketMessage.getPayload();
                try {
                    EngineMode mode = EngineMode.valueOf(modeString.toUpperCase());
                    obdDataService.setEngineMode(mode);
                    System.out.println("Session " + session.getId() + " changed engine mode to: " + mode);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid engine mode received from " + session.getId() + ": " + modeString);
                }
            } else {
                System.err.println("Unknown message type received from " + session.getId() + ": " + webSocketMessage.getType());
            }

        } catch (Exception e) {
            System.err.println("Error processing message from " + session.getId() + ": " + message.getPayload());
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionManager.stopSessionTask(session.getId());
    }
}