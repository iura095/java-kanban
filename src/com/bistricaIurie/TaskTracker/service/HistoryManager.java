package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    public void add(Task task);

    public List<Task> getHistory();
}
