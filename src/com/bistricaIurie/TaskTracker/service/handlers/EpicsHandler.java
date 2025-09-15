package com.bistricaIurie.TaskTracker.service.handlers;

import com.bistricaIurie.TaskTracker.model.Endpoint;
import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.error.ManagerSaveException;
import com.bistricaIurie.TaskTracker.model.error.NotFoundException;
import com.bistricaIurie.TaskTracker.model.error.TaskException;
import com.bistricaIurie.TaskTracker.service.TaskManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public EpicsHandler(TaskManager taskManager) {
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
                case GET_EPICS -> {
                    try {
                        answer = gson.toJson(taskManager.getEpicList());
                        sendText(exchange, answer, 200);
                    } catch (NotFoundException e) {
                        sendText(exchange, e.getMessage(), 404);
                    }
                }
                case GET_EPICBYID -> {
                    try {
                        answer = gson.toJson(taskManager.getEpicByID(Integer.parseInt(path[2])));
                        sendText(exchange, answer, 200);
                    } catch (NotFoundException e) {
                        sendText(exchange, e.getMessage(), 404);
                    }
                }
                case GET_EPICSUBTASKS -> {
                    answer = gson.toJson(taskManager.getSubTaskListByEpicId(Integer.parseInt(path[2])));
                    sendText(exchange, answer, 200);
                }
                case ADD_EPIC -> {
                    String requestBody = getRequestBody(exchange);
                    JsonObject jsonObject = getJsonObject(requestBody);

                    if (!jsonObject.isJsonObject()) {
                        sendText(exchange, "Неправильный формат данных.", 406);
                        return;
                    }

                    if (jsonObject.has("subTaskList")) {
                        try {
                            Epic task = gson.fromJson(requestBody, Epic.class);
                            taskManager.addEpic(task);
                            sendText(exchange, "Задача успешно добавлена.", 201);
                        } catch (TaskException e) {
                            sendText(exchange, e.getMessage(), 406);
                        }
                    } else {
                        sendText(exchange, "Неправильный формат данных.", 406);
                    }
                }
                case DELETE_EPIC -> {
                    taskManager.deleteEpic(Integer.parseInt(path[2]));
                    sendText(exchange, "Задача успешно удалена.", 201);
                }
                case UNKNOWN -> sendText(exchange, "Неизвестная команда.", 405);
            }
        } catch (ManagerSaveException e) {
            sendText(exchange, e.getMessage(), 500);
        }
    }
}
