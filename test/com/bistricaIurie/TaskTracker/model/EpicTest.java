package com.bistricaIurie.TaskTracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic;
    SubTask subTask1, subTask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("name", "description");
        subTask1 = new SubTask("subtask1", "description1", 0);
        subTask1.setTaskID(1);
        subTask2 = new SubTask("subtask2", "description2", 0);
        subTask2.setTaskID(2);
    }

    @Test
    void addSubtask() {
        int size = epic.getSubTaskList().size();
        epic.addSubtask(subTask1);
        assertEquals(size + 1, epic.getSubTaskList().size());
    }

    @Test
    void deleteSubtask() {
        epic.addSubtask(subTask1);
        int size = epic.getSubTaskList().size();
        epic.deleteSubtask(subTask1.getTaskID());
        assertEquals(size - 1, epic.getSubTaskList().size());
    }

    @Test
    void getSubTaskList() {
        epic.addSubtask(subTask1);
        assertEquals(subTask1, epic.getSubTaskList().getFirst());
    }

    @Test
    void updateEpicStatus() {
        epic.addSubtask(subTask1);
        subTask1.setStatus(TaskStatus.DONE);
        epic.updateEpicStatus();
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void epicIdEquals() {
        Epic epic2 = new Epic("epic2", "eto Epic 2");
        assertEquals(epic, epic2);
    }

    @Test
    void updateStartTime() {
        assertNull(epic.getStartTime());
        epic.addSubtask(subTask1);
        subTask1.setStartTime(LocalDateTime.MIN.plusMinutes(50));
        epic.updateStartTime();
        assertEquals(LocalDateTime.MIN.plusMinutes(50), epic.getStartTime());
        subTask2.setStartTime(LocalDateTime.MIN.plusMinutes(15));
        epic.addSubtask(subTask2);
        assertEquals(LocalDateTime.MIN.plusMinutes(15), epic.getStartTime());

    }

    @Test
    void updateDuration() {
        assertEquals(0, epic.getDuration().toMinutes());
        subTask1.setDuration(Duration.ofMinutes(15));
        epic.addSubtask(subTask1);
        assertEquals(15, epic.getDuration().toMinutes());
        subTask2.setDuration(Duration.ofMinutes(14));
        epic.addSubtask(subTask2);
        assertEquals(29, epic.getDuration().toMinutes());
        epic.deleteSubtask(1);
        assertEquals(14, epic.getDuration().toMinutes());
    }

    @Test
    void updateEndTime() {
        assertNull(epic.getEndTime());
        subTask1.setStartTime(LocalDateTime.MIN);
        subTask1.setDuration(Duration.ofMinutes(60));
        epic.addSubtask(subTask1);
        assertEquals(LocalDateTime.MIN.plusMinutes(60), epic.getEndTime());
        subTask2.setStartTime(LocalDateTime.MIN);
        subTask2.setDuration(Duration.ofMinutes(75));
        epic.addSubtask(subTask2);
        assertEquals(LocalDateTime.MIN.plusMinutes(75), epic.getEndTime());
        subTask2.setDuration(Duration.ofMinutes(45));
        epic.addSubtask(subTask2);
        assertEquals(LocalDateTime.MIN.plusMinutes(60), epic.getEndTime());
    }

    @Test
    void clearSubtaskList() {
        assertTrue(epic.getSubTaskList().isEmpty());
        epic.addSubtask(subTask1);
        assertEquals(1, epic.getSubTaskList().size());
        epic.addSubtask(subTask2);
        assertEquals(2, epic.getSubTaskList().size());
        assertFalse(epic.getSubTaskList().isEmpty());
        epic.clearSubtaskList();
        assertTrue(epic.getSubTaskList().isEmpty());
    }

}