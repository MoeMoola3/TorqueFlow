package com.moola.obd.analyzer.service;

import lombok.Getter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Manages the WebSocket sessions and their corresponding scheduled tasks.
 * This service acts as a centralized point for starting and stopping the data generation
 * and sending tasks for each connected client. This design allows both the
 * WebSocket listener and the REST controller to manage sessions.
 */
@Service
public class WebSocketSessionManager {

    private final TaskScheduler taskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    /**
     * @param taskScheduler The scheduler responsible for executing the periodic tasks.
     */
    public WebSocketSessionManager(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * Starts a new scheduled task for a given session.
     * The task will generate and send data at a fixed rate of 5 seconds.
     *
     * @param sessionId The unique ID of the WebSocket session.
     */
    public void startSessionTask(String sessionId, Runnable task) {
        ScheduledFuture<?> scheduledTask = taskScheduler.scheduleAtFixedRate(
                task,
                Duration.ofSeconds(5)
        );
        tasks.put(sessionId, scheduledTask);
        System.out.println("WebSocket session " + sessionId + " connected. Task started.");
    }

    /**
     * Stops and removes the scheduled task for a given session.
     * This method is called both automatically by the WebSocket listener on disconnect
     * and manually by the REST controller for unregistering.
     *
     * @param sessionId The unique ID of the WebSocket session to disconnect.
     */
    public void stopSessionTask(String sessionId) {
        ScheduledFuture<?> task = tasks.remove(sessionId);
        if (task != null && !task.isCancelled()) {
            task.cancel(false);
            sessions.remove(sessionId);
            System.out.println("WebSocket session " + sessionId + " disconnected. Task cancelled.");
        }
    }


    public void storeSession(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
    }

}
