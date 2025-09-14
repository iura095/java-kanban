package com.bistricaIurie.TaskTracker.service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static TaskManager taskManager;
    static HttpServer server;

    public HttpTaskServer(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
    }

    public static void main(String[] args) {

    }

    public static TaskManager getTaskManager() {
        return taskManager;
    }

    public void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/subtasks", new SubtasksHandler());
        server.createContext("/epics", new EpicsHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());
        server.start();
    }


    public void stopServer() {
        server.stop(0);
    }
}