package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Task;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() throws FileNotFoundException {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("name", "asdf");
        task.setTaskID(1);
        taskManager.addTask(new Task("name", "asdf"));
        assertEquals(task, taskManager.getTaskByID(1));
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("name", "asdf");
        ArrayList<Task> list = new ArrayList<>();
        list.add(task);
        historyManager.add(task);
        assertEquals(list, historyManager.getHistory());
    }
}