package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        assertTrue(historyManager.historyList.isEmpty());
        historyManager.add(new Task("name", "desc"));
        assertFalse(historyManager.historyList.isEmpty());
        for (int i = 0; i < 13; i++) {
            historyManager.add(new Task("name", "desc"));
        }
        assertEquals(10, historyManager.historyList.size());
    }

    @Test
    void getHistory() {
        historyManager.add(new Task("name", "desc"));
        assertFalse(historyManager.historyList.isEmpty());
        assertEquals(historyManager.historyList, historyManager.getHistory());
    }
}