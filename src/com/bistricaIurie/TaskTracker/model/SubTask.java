package com.bistricaIurie.TaskTracker.model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return this.getClass() +"{" +
                "taskID=" + this.getTaskID() +
                ", taskName='" + this.getTaskName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                ", whichEpicId='" + epicId + '\'' +
                '}';
    }
}