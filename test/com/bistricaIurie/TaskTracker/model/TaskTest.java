package com.bistricaIurie.TaskTracker.model;

import com.bistricaIurie.TaskTracker.model.error.TaskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task;

    @BeforeEach
    void setUp() {
        task = new Task("task1", "testtask");
    }

    @Test
    void getTaskID() {
        assertEquals(0, task.getTaskID());
    }

    @Test
    void setTaskID() {
        task.setTaskID(1);
        assertEquals(1, task.getTaskID());
    }

    @Test
    void getStatus() {
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    void setStatus() {
        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void getTaskName() {
        assertEquals("task1", task.getTaskName());
    }

    @Test
    void setTaskName() {
        task.setTaskName("name");
        assertEquals("name", task.getTaskName());
    }

    @Test
    void getDescription() {
        assertEquals("testtask", task.getDescription());
    }

    @Test
    void setDescription() {
        task.setDescription("changed");
        assertEquals("changed", task.getDescription());
    }

    @Test
    void taskIdEquals() {
        Task task2 = new Task("task2", "descriptiontask2");
        assertEquals(task, task2);
    }

    @Test
    void getAndSetDuration() {
        assertEquals(0, task.getDuration().toMinutes());
        task.setDuration(Duration.ofMinutes(5));
        assertEquals(5, task.getDuration().toMinutes());
    }

    @Test
    void getAndSetStartTime() {
        assertNull(task.getStartTime());
        task.setStartTime(LocalDateTime.MIN);
        assertEquals(LocalDateTime.MIN, task.getStartTime());
    }

    @Test
    void getEndTime() {
        task.setStartTime(LocalDateTime.MIN);
        task.setDuration(Duration.ofMinutes(15));
        assertEquals(LocalDateTime.MIN.plusMinutes(15), task.getEndTime());
    }

    @Test
    void getTipe() {
        assertEquals(TaskType.TASK, task.getType());
    }
}