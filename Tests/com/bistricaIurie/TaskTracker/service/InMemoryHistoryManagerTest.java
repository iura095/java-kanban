package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Node;
import com.bistricaIurie.TaskTracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        assertTrue(historyManager.getHistory().isEmpty());
        historyManager.add(new Task("name", "desc"));
        assertFalse(historyManager.getHistory().isEmpty());
        for (int i = 0; i < 13; i++) {
            Task task = new Task("name", "desc");
            task.setTaskID(i);
            historyManager.add(task);
        }
        assertEquals(13, historyManager.getHistory().size());
        historyManager.add(new Task("name", "desc"));
    }

    @Test
    void addTasksWithSimilarID() {
        Task task1 = new Task("name", "desc");
        Task task2 = new Task("name2", "desc2");  //task1 & task2 are similar id
        Task task3 = new Task("name3", "desc3");
        task3.setTaskID(3);

        assertTrue(historyManager.getHistory().isEmpty());
        historyManager.add(task1);
        assertFalse(historyManager.getHistory().isEmpty());
        assertEquals(1, historyManager.getSize());
        historyManager.add(task2);
        assertEquals(1, historyManager.getSize());
        historyManager.add(task3);
        assertEquals(2, historyManager.getSize());
    }

    @Test
    void addNullException() {
        assertTrue(historyManager.getHistory().isEmpty());
        historyManager.add(new Task("name", "desc"));
        assertFalse(historyManager.getHistory().isEmpty());
        assertEquals(1, historyManager.getHistory().size());
        historyManager.add(null);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void remove() {
        for (int i = 0; i < 4; i++) {
            Task task = new Task("name", "desc");
            task.setTaskID(i);
            historyManager.add(task);
        }
        assertEquals(4, historyManager.getSize());
        historyManager.remove(2);
        assertEquals(3, historyManager.getSize());

    }

    @Test
    void getHistoryAndGetTasks() {
        List<Task> list =new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Task task = new Task("name", "desc");
            task.setTaskID(i);
            historyManager.add(task);
            list.add(task);
        }
        assertEquals(list, historyManager.getHistory());
        assertEquals(list, historyManager.getTasks());
    }

    @Test
    void linkLast() {
        for (int i = 0; i < 4; i++) {
            Task task = new Task("name", "desc");
            task.setTaskID(i);
            historyManager.linkLast(task);
            assertEquals(task, historyManager.getHistory().getLast());
        }
    }

    @Test
    void removeNode() {
        for (int i = 0; i < 4; i++) {
            Task task = new Task("name", "desc");
            task.setTaskID(i);
            historyManager.add(task);
        }

        List<Node> list = historyManager.getNodeList();
        assertEquals(4, historyManager.getHistory().size());
        assertEquals(4, historyManager.getNodeList().size());
        assertTrue(historyManager.getNodeList().contains(list.get(2)));
        historyManager.removeNode(list.get(2));
        assertEquals(3, historyManager.getHistory().size());
        assertEquals(3, historyManager.getNodeList().size());
        assertFalse(historyManager.getNodeList().contains(list.get(2)));
        historyManager.removeNode(null);
        assertEquals(3, historyManager.getHistory().size());
        assertEquals(3, historyManager.getNodeList().size());
        historyManager.removeNode(historyManager.getTail());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(2, historyManager.getNodeList().size());
        historyManager.removeNode(historyManager.getHead());
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(1, historyManager.getNodeList().size());
        assertFalse(historyManager.getNodeList().isEmpty());
        historyManager.removeNode(historyManager.getHead());
        assertTrue(historyManager.getNodeList().isEmpty());
    }
}