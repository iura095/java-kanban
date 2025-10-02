package com.bistricaIurie.TaskTracker.service.handlers;

import com.bistricaIurie.TaskTracker.model.Endpoint;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.error.ManagerSaveException;
import com.bistricaIurie.TaskTracker.model.error.NotFoundException;
import com.bistricaIurie.TaskTracker.model.error.TaskException;
import com.bistricaIurie.TaskTracker.service.TaskManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager taskManager;
    Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String answer;
            int id;
            String[] path = exchange.getRequestURI().getPath().split("/");
            Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());

            switch (endpoint) {
                case GET_TASKS -> {
                    answer = gson.toJson(taskManager.getTaskList());
                    sendText(exchange, answer, 200);
                }
                case GET_TASKBYID -> {
                    try {
                        answer = gson.toJson(taskManager.getTaskByID(Integer.parseInt(path[2])));
                        sendText(exchange, answer, 200);
                    } catch (NotFoundException e) {
                        sendText(exchange, e.getMessage(), 404);
                    }
                }
                case ADD_TASK -> {
                    String requestBody = getRequestBody(exchange);
                    JsonObject jsonObject = getJsonObject(requestBody);

                    if (!jsonObject.isJsonObject()) {
                        sendText(exchange, "Неправильный формат данных.", 406);
                        return;
                    }

                    if (!(jsonObject.has("epicId") || jsonObject.has("subTaskList"))) {

                        try {
                            Task task = gson.fromJson(requestBody, Task.class);
                            id = task.getTaskID();
                            if (id == 0) {
                                taskManager.addTask(task);
                                sendText(exchange, "Задача успешно добавлена.", 201);
                            } else {
                                taskManager.updateTask(task);
                                sendText(exchange, "Задача успешно обновлена.", 201);
                            }
                        } catch (TaskException | NotFoundException e) {
                            sendText(exchange, e.getMessage(), 406);
                        }
                    } else {
                        sendText(exchange, "Неправильный формат данных.", 406);
                    }

                }
                case DELETE_TASK -> {
                    taskManager.deleteTask(Integer.parseInt(path[2]));
                    sendText(exchange, "Задача успешно удалена.", 201);
                }
                case UNKNOWN -> sendText(exchange, "неизвестная команда.", 405);
            }
        } catch (ManagerSaveException e) {
            sendText(exchange, e.getMessage(), 500);
        }
    }
}
