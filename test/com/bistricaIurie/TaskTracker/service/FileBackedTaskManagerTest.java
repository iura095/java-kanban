package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;
import com.bistricaIurie.TaskTracker.model.error.TaskException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void checkTaskIntersection() {
        task1.setDuration(Duration.ofMinutes(10));
        task2.setDuration(Duration.ofMinutes(5));
        tm.addTask(task1);
        assertFalse(tm.checkTaskIntersection(task2));
        task2.setStartTime(task2.getStartTime().plusMinutes(25));
        assertTrue(tm.checkTaskIntersection(task2));
        subTask1.setStartTime(subTask1.getStartTime().plusMinutes(13));
        subTask1.setDuration(Duration.ofMinutes(5));
        assertTrue(tm.checkTaskIntersection(subTask1));
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
    void prioritizeTaskAndGetPrioritizeList() {
        task1.setStartTime(task1.getStartTime().plusMinutes(5));
        tm.prioritizeTask(task1);
        tm.prioritizeTask(task2);
        assertEquals(List.of(task2, task1), tm.getPrioritizedTasks());
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

    @Test
    void addTask() {
        tm.addTask(task1);
        assertEquals(task1, tm.getTaskList().getFirst());
    }

    @Test
    void addEpicAndSubTaskAndAddSubTaskWithWrongEpic() {
        assertThrows(TaskException.class, () -> tm.addSubTask(subTask1));
        tm.addEpic(epic1);
        subTask1.setEpicId(epic1.getTaskID());
        tm.addSubTask(subTask1);
        assertEquals(subTask1, tm.getSubTaskList().getFirst());
        assertEquals(subTask1, epic1.getSubTaskList().getFirst());
    }

    @Test
    void clearTaskAndSubtaskAndEpicLists() {
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addSubTask(subTask1);
        tm.addSubTask(subTask2);
        assertEquals(2, tm.getTaskList().size());
        assertEquals(1, tm.getEpicList().size());
        assertEquals(2, tm.getSubTaskList().size());
    }

    @Test
    void deleteTask() {
        tm.addTask(task1);
        tm.addTask(task2);
        assertEquals(2, tm.getTaskList().size());
        tm.deleteTask(task1.getTaskID());
        assertEquals(1, tm.getTaskList().size());
    }

    @Test
    void deleteSubTaskAndEpic() {
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addSubTask(subTask1);
        tm.addSubTask(subTask2);
        assertEquals(2, tm.getSubTaskList().size());
        tm.deleteSubTask(subTask1.getTaskID());
        assertEquals(1, tm.getSubTaskList().size());
        assertFalse(tm.getSubTaskList().isEmpty());
        assertFalse(tm.getEpicList().isEmpty());
        tm.deleteEpic(epic1.getTaskID());
        assertTrue(tm.getSubTaskList().isEmpty());
        assertTrue(tm.getEpicList().isEmpty());
    }

    @Test
    void setNewTaskStatus() {
        tm.addTask(task1);
        assertEquals(TaskStatus.NEW, task1.getStatus());
        tm.setNewTaskStatus(1, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task1.getStatus());
    }

    @Test
    void setNewSubTaskStatus() {
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addSubTask(subTask1);
        tm.addSubTask(subTask2);
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        assertEquals(TaskStatus.NEW, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask1.getTaskID(), TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask1.getTaskID(), TaskStatus.DONE);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(TaskStatus.DONE, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask2.getTaskID(), TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(TaskStatus.DONE, subTask1.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask2.getTaskID(), TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, epic1.getStatus());
        assertEquals(TaskStatus.DONE, subTask1.getStatus());
        assertEquals(TaskStatus.DONE, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask1.getTaskID(), TaskStatus.NEW);
        tm.setNewSubTaskStatus(subTask2.getTaskID(), TaskStatus.NEW);
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        assertEquals(TaskStatus.NEW, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());
    }
}