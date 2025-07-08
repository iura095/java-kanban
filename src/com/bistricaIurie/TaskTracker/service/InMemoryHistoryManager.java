package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task> historyList = new LinkedList<>();
    private static final int HISTORY_LIST_SIZE = 10;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        historyList.add(task);
        if (historyList.size() > HISTORY_LIST_SIZE) {
            historyList.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
