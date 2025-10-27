package com.moola.obd.analyzer.model;

import lombok.Data;

/**
 * Represents a structured message received from a WebSocket client.
 * 'type' defines the action "modeChange"
 * 'payload' contains the data for that action (e.g., "ACCELERATION").
 */
@Data
public class WebSocketMessage {
    private String type;
    private String payload;
}