package com.bistricaIurie.TaskTracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic;
    SubTask subTask1, subTask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("name", "description");
        subTask1 = new SubTask("subtask1", "description1", 1);
    }

    @Test
    void addSubtask() {
        int size = epic.subTaskList.size();
        epic.addSubtask(subTask1);
        assertEquals(size + 1, epic.subTaskList.size());
    }

    @Test
    void deleteSubtask() {
        epic.addSubtask(subTask1);
        int size = epic.subTaskList.size();
        epic.deleteSubtask(subTask1.getTaskID());
        assertEquals(size - 1, epic.subTaskList.size());
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
        assertTrue(epic.equals(epic2));
    }

//    @Test
//    void cantAddEpicInEpic() {
//        Epic epic2 = new Epic("epic2", "description");
//        epic.addSubtask(epic);
//    }

}