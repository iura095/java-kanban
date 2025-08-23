package com.bistricaIurie.TaskTracker.service;

public class Managers {

    private Managers() {

    }

    public static FileBackedTaskManager getDefault() {
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
