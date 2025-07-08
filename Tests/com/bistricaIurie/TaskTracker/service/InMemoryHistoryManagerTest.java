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
        assertTrue(historyManager.getHistory().isEmpty());
        historyManager.add(new Task("name", "desc"));
        assertFalse(historyManager.getHistory().isEmpty());
        for (int i = 0; i < 13; i++) {
            historyManager.add(new Task("name", "desc"));
        }
        assertEquals(10, historyManager.getHistory().size());
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
    void getHistory() {
        historyManager.add(new Task("name", "desc"));
        assertFalse(historyManager.getHistory().isEmpty());
        assertEquals(historyManager.getHistory(), historyManager.getHistory());
    }
}