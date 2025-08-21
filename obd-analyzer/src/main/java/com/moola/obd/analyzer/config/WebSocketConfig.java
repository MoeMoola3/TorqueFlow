package com.moola.obd.analyzer.config;

import com.moola.obd.analyzer.handler.ObdWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * This class configures the WebSocket server for the application.
 * It implements WebSocketConfigurer to register a simple WebSocket handler.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ObdWebSocketHandler obdWebSocketHandler;

    /**
     * @param obdWebSocketHandler The handler for WebSocket connections.
     */
    public WebSocketConfig(ObdWebSocketHandler obdWebSocketHandler) {
        this.obdWebSocketHandler = obdWebSocketHandler;
    }

    /**
     * @param registry The registry to add the WebSocket handler to.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(obdWebSocketHandler, "/register")
                .setAllowedOrigins("*");
    }
}
