package com.moola.obd.analyzer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.obd.analyzer.model.ObdData;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Service
public class DataSenderService {

    private final WebSocketSessionManager sessionManager;
    private final ObdDataService obdDataService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataSenderService(ObdDataService obdDataService, WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.obdDataService = obdDataService;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void sendDataToClients() {
        ObdData data = obdDataService.generateAndSaveData();
        String jsonData;

        try {
            jsonData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing OBD data to JSON: " + e.getMessage());
            return;
        }

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