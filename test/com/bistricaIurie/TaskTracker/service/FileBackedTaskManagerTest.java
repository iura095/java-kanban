package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FileBackedTaskManagerTest {
    public FileBackedTaskManager tm;
    public Task task1, task2;
    public Epic epic1;
    public SubTask subTask1, subTask2;

    @BeforeEach
    void setUp() {
        tm = new FileBackedTaskManager();
        task1 = new Task(1, "name", "desc", TaskStatus.NEW, Duration.ZERO
                , LocalDateTime.now());
        task2 = new Task(2, "name", "desc", TaskStatus.NEW, Duration.ZERO
                , LocalDateTime.now());
        epic1 = new Epic(3, "name", "desc");
        subTask1 = new SubTask(4, "name", "desc", TaskStatus.NEW, epic1.getTaskID()
                , Duration.ZERO
                , LocalDateTime.now());
        subTask2 = new SubTask(5, "name", "desc", TaskStatus.NEW, 3, Duration.ZERO
                , LocalDateTime.now());
    }

    @AfterEach
    void afterEach() {
        tm.setTaskCount(0);
        String newPath = Paths.get("").toAbsolutePath() + "\\saves\\";
        tm.setSavePath(newPath);
        tm.setFileName("Tasks.csv");

    }

    @Test
    void saveAndLoadFromFile() {
        tm.setFileName("TestTasks.csv");
        tm.addTask(task1);
        tm.addEpic(epic1);
        subTask1.setEpicId(2);
        tm.addSubTask(subTask1);
        FileBackedTaskManager tm1 = FileBackedTaskManager.loadFromFile();
        assertEquals(1, tm1.getTaskList().size());
        Task nTask = tm1.getTaskList().getFirst();
        assertEquals(task1.getTaskID(), nTask.getTaskID());
        assertEquals(task1.getTaskName(), nTask.getTaskName());
        assertEquals(task1.getDescription(), nTask.getDescription());
        assertEquals(task1.getStatus(), nTask.getStatus());
        assertEquals(task1.getType(), nTask.getType());
        assertEquals(task1.getDuration(), nTask.getDuration());
        assertEquals(task1.getStartTime(), nTask.getStartTime());
        assertEquals(1, tm1.getSubTaskList().size());
        SubTask nSubTask = tm1.getSubTaskList().getFirst();
        assertEquals(subTask1.getTaskID(), nSubTask.getTaskID());
        assertEquals(subTask1.getTaskName(), nSubTask.getTaskName());
        assertEquals(subTask1.getDescription(), nSubTask.getDescription());
        assertEquals(subTask1.getStatus(), nSubTask.getStatus());
        assertEquals(subTask1.getType(), nSubTask.getType());
        assertEquals(subTask1.getDuration(), nSubTask.getDuration());
        assertEquals(subTask1.getStartTime(), nSubTask.getStartTime());
        assertEquals(1, tm1.getEpicList().size());
        Epic nEpic = tm1.getEpicList().getFirst();
        assertEquals(epic1.getTaskID(), nEpic.getTaskID());
        assertEquals(epic1.getTaskName(), nEpic.getTaskName());
        assertEquals(epic1.getDescription(), nEpic.getDescription());
        assertEquals(epic1.getStatus(), nEpic.getStatus());
        assertEquals(epic1.getType(), nEpic.getType());
        assertEquals(epic1.getDuration(), nEpic.getDuration());
        assertEquals(epic1.getStartTime(), nEpic.getStartTime());
        try {
            Files.deleteIfExists(Paths.get(tm.getSavePath(), tm.getFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void setAndGetSavePath() {
        String newPath = Paths.get("").toAbsolutePath() + "\\TestSaves\\";
        assertNotEquals(newPath, tm.getSavePath());
        tm.setSavePath(newPath);
        assertEquals(newPath, tm.getSavePath());
    }

    @Test
    void getAndSetFileName() {
        String newFileName = "TestTasks.csv";
        assertNotEquals(newFileName, tm.getFileName());
        tm.setFileName(newFileName);
        assertEquals(newFileName, tm.getFileName());
    }

}