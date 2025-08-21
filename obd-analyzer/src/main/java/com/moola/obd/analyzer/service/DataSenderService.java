package com.moola.obd.analyzer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.obd.analyzer.model.ObdData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Service
public class DataSenderService {

    private WebSocketSessionManager sessionManager;
    private final ObdDataService obdDataService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataSenderService(ObdDataService obdDataService) {
        this.obdDataService = obdDataService;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Autowired
    public void setSessionManager(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Generates new OBD data and sends it to all active WebSocket clients.
     */
    public void sendDataToClients() {
        ObdData data = obdDataService.generateAndSaveData();
        String jsonData;

        try {
            // Convert the ObdData object to a JSON string for client-side parsing
            jsonData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing OBD data to JSON: " + e.getMessage());
            return;
        }

        // Iterate through all active sessions and send the JSON message
        for (WebSocketSession session : sessionManager.getSessions().values()) {
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonData));
                } catch (IOException e) {
                    System.err.println("Failed to send message to session " + session.getId());
                }
            }
        }
    }
}