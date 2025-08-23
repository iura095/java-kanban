package com.bistricaIurie.TaskTracker.model;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {
      super();
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}
