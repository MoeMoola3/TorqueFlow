package com.moola.obd.analyzer.controller;

import com.moola.obd.analyzer.service.WebSocketSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WebSocketObdDataController {

    private final WebSocketSessionManager sessionManager;

    public WebSocketObdDataController(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @MessageMapping("/register")
    @SendTo("/topic/register")
    public String handleDataRequest() {
        return "Acknowledged: You are now subscribed to the OBD data stream.";
    }

    // New API endpoint for manual unregister
    @PostMapping("/unregister")
    public ResponseEntity<String> unregisterSession(@RequestBody String sessionId) {
        sessionManager.stopSessionTask(sessionId);
        return ResponseEntity.ok("Session " + sessionId + " unregistered successfully.");
    }
}