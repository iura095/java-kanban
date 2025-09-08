package com.bistricaIurie.TaskTracker.model.error;

public class TaskException extends RuntimeException {
    public TaskException(String message) {
        super(message);
    }
}
