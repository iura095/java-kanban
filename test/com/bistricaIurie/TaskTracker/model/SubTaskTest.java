package com.bistricaIurie.TaskTracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {

    SubTask task;

    @BeforeEach
    void setUp() {
        task = new SubTask("task1", "description", 1);
    }

    @Test
    void setEpicId() {
        task.setEpicId(1);
        assertEquals(1, task.getEpicId());
    }

    @Test
    void getEpicId() {
        assertEquals(1, task.getEpicId());
    }

    @Test
    void subTaskIdEquals() {
        SubTask task2 = new SubTask("subtask2", "description2", 1);
        assertEquals(task, task2);
    }

}