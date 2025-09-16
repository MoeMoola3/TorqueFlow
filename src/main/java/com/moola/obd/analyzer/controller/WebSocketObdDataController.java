package com.moola.obd.analyzer.controller;

import com.moola.obd.analyzer.service.WebSocketSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WebSocketObdDataController {

    private final WebSocketSessionManager sessionManager;

    public WebSocketObdDataController(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    // API endpoint for manual unregister
    @PostMapping("/unregister")
    public ResponseEntity<String> unregisterSession(@RequestBody String sessionId) {
        sessionManager.stopSessionTask(sessionId);
        return ResponseEntity.ok("Session " + sessionId + " unregistered successfully.");
    }
}