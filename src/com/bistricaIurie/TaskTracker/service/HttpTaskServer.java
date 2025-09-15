package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.service.handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static void main(String[] args) {
    }

    public void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.start();
    }

    public void stopServer() {
        server.stop(0);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}