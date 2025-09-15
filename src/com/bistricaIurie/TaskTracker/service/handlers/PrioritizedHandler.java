package com.bistricaIurie.TaskTracker.service.handlers;

import com.bistricaIurie.TaskTracker.model.Endpoint;
import com.bistricaIurie.TaskTracker.model.error.ManagerSaveException;
import com.bistricaIurie.TaskTracker.service.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager taskManager;
    Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String answer;
            String[] path = exchange.getRequestURI().getPath().split("/");
            Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());

            switch (endpoint) {
                case GET_PRIORITIZED_TASKS -> {
                    answer = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(exchange, answer, 200);
                }
                case UNKNOWN -> sendText(exchange, "Неизвестная команда.", 405);
            }
        } catch (ManagerSaveException e) {
            sendText(exchange, e.getMessage(), 500);
        }

    }
}
