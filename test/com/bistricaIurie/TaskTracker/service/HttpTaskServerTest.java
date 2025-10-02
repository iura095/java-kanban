package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;
import com.bistricaIurie.TaskTracker.service.handlers.BaseHttpHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    static TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();
    HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    static void beforeAll() {
        manager.clearTaskList();
        manager.clearSubTaskList();
        manager.clearEpicList();
        manager.clearHistory();
        manager.setTaskCount(0);

    }

    @BeforeEach
    void setUp() {
        manager.clearTaskList();
        manager.clearEpicList();
        manager.clearSubTaskList();
        manager.clearHistory();
        manager.setTaskCount(0);
        taskServer.startServer();
    }

    @AfterEach
    void tearDown() {
        taskServer.stopServer();
        client.close();
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task(1, "Test GETTasks", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertFalse(manager.getTaskList().isEmpty());
        assertEquals(gson.toJson(manager.getTaskList()), response.body());
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task(1, "Test GETTasks", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task1 = new Task(2, "Test", "Testing",
                TaskStatus.NEW, Duration.ofMinutes(50), LocalDateTime.now());
        manager.addTask(task);
        manager.addTask(task1);
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(task), response.body());
        assertNotEquals(gson.toJson(task1), response.body());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task(1, "Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task1 = new Task(1, "Test", "Testing",
                TaskStatus.NEW, Duration.ofMinutes(50), LocalDateTime.now());
        manager.addTask(task);
        assertEquals(1, manager.getTaskList().size());
        assertEquals("Test 2", manager.getTaskList().getFirst().getTaskName());
        String taskJson = gson.toJson(task1);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getTaskList().size());
        assertEquals("Test", manager.getTaskList().getFirst().getTaskName());

    }

    @Test
    public void testPostTasks() throws IOException, InterruptedException {
        Task task = new Task(1, "Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testPostAnotherTypeThruPostTask() throws IOException, InterruptedException {
        SubTask task = new SubTask(1, "Test 2", "Testing task 2",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        String subtaskJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task(1, "Test GETTasks", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);
        assertFalse(manager.getTaskList().isEmpty());
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(manager.getTaskList().isEmpty());
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        SubTask subTask = new SubTask(1, "Test", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertFalse(manager.getSubTaskList().isEmpty());
        assertEquals(gson.toJson(manager.getSubTaskList()), response.body());
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        SubTask subTask = new SubTask(2, "Test", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        assertEquals("Test", manager.getSubTaskList().getFirst().getTaskName());

        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.fromJson(response.body(), SubTask.class), subTask);
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        SubTask subTask = new SubTask(2, "Test", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        SubTask subTask1 = new SubTask(2, "new name", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        assertEquals("Test", manager.getSubTaskList().getFirst().getTaskName());
        assertEquals(1, manager.getSubTaskList().size());

        String taskJson = gson.toJson(subTask1);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getSubTaskList().size());
        assertEquals("new name", manager.getSubTaskList().getFirst().getTaskName());
    }

    @Test
    public void testPostSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        SubTask subTask = new SubTask(0, "Test", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addEpic(epic);

        assertTrue(manager.getSubTaskList().isEmpty());

        String taskJson = gson.toJson(subTask);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getSubTaskList().size());
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        SubTask subTask = new SubTask(2, "Test", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        assertEquals(1, manager.getSubTaskList().size());

        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(manager.getSubTaskList().isEmpty());
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        manager.addEpic(epic);

        assertFalse(manager.getEpicList().isEmpty());

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpicList()), response.body());
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        manager.addEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(gson.fromJson(response.body(), Epic.class), epic);
    }

    @Test
    public void testPostEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");

        assertTrue(manager.getEpicList().isEmpty());

        String taskJson = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getEpicList().size());
    }

    @Test
    public void testGetEpicSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        SubTask subTask = new SubTask(2, "Test", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        assertEquals(1, manager.getSubTaskList().size());

        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpicByID(1).getSubTaskList(),
                gson.fromJson(response.body(), new SubtaskListToken().getType()));
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("name", "desc");
        SubTask subTask = new SubTask(2, "Test", "Testing",
                TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        assertEquals(1, manager.getSubTaskList().size());
        assertEquals(1, manager.getEpicList().size());

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(manager.getEpicList().isEmpty());
        assertTrue(manager.getSubTaskList().isEmpty());
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task(1, "Test GETTasks", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);
        manager.getTaskByID(1);
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertFalse(manager.getHistory().isEmpty());
        assertEquals(manager.getHistory(), gson.fromJson(response.body(), new TaskListToken().getType()));
    }

    @Test
    public void testGetPrioritized() throws IOException, InterruptedException {
        Task task = new Task(1, "Test GETTasks", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task1 = new Task(1, "Test GETTasks", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(1), LocalDateTime.now().plusMinutes(10));
        manager.addTask(task);
        manager.addTask(task1);

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getPrioritizedTasks(), gson.fromJson(response.body(), new TaskListToken().getType()));
    }


}

class TaskListToken extends TypeToken<List<Task>> {

}

class SubtaskListToken extends TypeToken<List<SubTask>> {

}


