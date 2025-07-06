package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
