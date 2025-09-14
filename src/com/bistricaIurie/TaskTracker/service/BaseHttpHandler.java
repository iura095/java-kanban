package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.DurationAdapter;
import com.bistricaIurie.TaskTracker.model.Endpoint;
import com.bistricaIurie.TaskTracker.model.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    TaskManager taskManager = HttpTaskServer.getTaskManager();

    static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    protected void sendText(HttpExchange exchange, String text, int rCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(rCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    public static Endpoint getEndpoint(String[] path, String requestMethod) {
        switch (requestMethod) {
            case "GET": {
                switch (path[1]) {
                    case "tasks": {
                        if (path.length == 3) {
                            return Endpoint.GET_TASKBYID;
                        } else {
                            return Endpoint.GET_TASKS;
                        }
                    }
                    case "subtasks": {
                        if (path.length == 3) {
                            return Endpoint.GET_SUBTASKBYID;
                        } else {
                            return Endpoint.GET_SUBTASKS;
                        }
                    }
                    case "epics": {
                        if (path.length == 3) {
                            return Endpoint.GET_EPICBYID;
                        } else if (path.length == 4 && path[3].equals("subtasks")) {
                            return Endpoint.GET_EPICSUBTASKS;
                        } else {
                            return Endpoint.GET_EPICS;
                        }
                    }
                    case "history": {
                        return Endpoint.GET_HISTORY;
                    }
                    case "prioritized": {
                        return Endpoint.GET_PRIORITIZED_TASKS;
                    }
                }
            }
            case "POST": {
                switch (path[1]) {
                    case "tasks": {
                        return Endpoint.ADD_TASK;
                    }
                    case "subtasks": {
                        return Endpoint.ADD_SUBTASK;
                    }
                    case "epics": {
                        return Endpoint.ADD_EPIC;
                    }
                }
            }
            case "DELETE": {
                switch (path[1]) {
                    case "tasks": {
                        return Endpoint.DELETE_TASK;
                    }
                    case "subtasks": {
                        return Endpoint.DELETE_SUBTASK;
                    }
                    case "epics": {
                        return Endpoint.DELETE_EPIC;
                    }
                }
            }
            default:
                return Endpoint.UNKNOWN;
        }
    }

    public static Gson getGson() {
        return gson;
    }
}
