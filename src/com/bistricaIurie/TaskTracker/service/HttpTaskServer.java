package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Endpoint;
import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.error.ManagerSaveException;
import com.bistricaIurie.TaskTracker.model.error.NotFoundException;
import com.bistricaIurie.TaskTracker.model.error.TaskException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

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

class TasksHandler extends BaseHttpHandler implements HttpHandler {

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
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(requestBody);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
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

class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String answer;
            int id;
            String[] path = exchange.getRequestURI().getPath().split("/");
            Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());

            switch (endpoint) {
                case GET_SUBTASKS -> {
                    try {
                        answer = gson.toJson(taskManager.getSubTaskList());
                        sendText(exchange, answer, 200);
                    } catch (NotFoundException e) {
                        sendText(exchange, e.getMessage(), 404);
                    }
                }
                case GET_SUBTASKBYID -> {
                    try {
                        answer = gson.toJson(taskManager.getSubTaskByID(Integer.parseInt(path[2])));
                        sendText(exchange, answer, 200);
                    } catch (NotFoundException e) {
                        sendText(exchange, e.getMessage(), 404);
                    }
                }
                case ADD_SUBTASK -> {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(requestBody);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (!jsonObject.isJsonObject()) {
                        sendText(exchange, "Неправильный формат данных.", 406);
                        return;
                    }

                    if (jsonObject.has("epicId")) {
                        try {
                            SubTask task = gson.fromJson(requestBody, SubTask.class);
                            id = jsonObject.get("taskID").getAsInt();
                            if (id == 0) {
                                taskManager.addSubTask(task);
                                sendText(exchange, "Задача успешно добавлена.", 201);
                            } else {
                                taskManager.updateSubTask(task);
                                sendText(exchange, "Задача успешно обновлена.", 201);
                            }
                        } catch (TaskException e) {
                            sendText(exchange, e.getMessage(), 406);
                        }
                    } else {
                        sendText(exchange, "Неправильный формат данных.", 406);
                    }
                }
                case DELETE_SUBTASK -> {
                    taskManager.deleteSubTask(Integer.parseInt(path[2]));
                    sendText(exchange, "Задача успешно удалена.", 201);
                }
                case UNKNOWN -> sendText(exchange, "неизвестная команда.", 405);
            }

        } catch (ManagerSaveException e) {
            sendText(exchange, e.getMessage(), 500);
        }
    }
}

class EpicsHandler extends BaseHttpHandler implements HttpHandler {

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
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(requestBody);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

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

class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String answer;
            String[] path = exchange.getRequestURI().getPath().split("/");
            Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());

            switch (endpoint) {
                case GET_HISTORY -> {
                    answer = gson.toJson(taskManager.getHistory());
                    sendText(exchange, answer, 200);
                }
                case UNKNOWN -> sendText(exchange, "Неизвестная команда.", 405);
            }
        } catch (ManagerSaveException e) {
            sendText(exchange, e.getMessage(), 500);
        }
    }
}

class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

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