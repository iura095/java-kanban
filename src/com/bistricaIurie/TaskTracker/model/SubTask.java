package com.bistricaIurie.TaskTracker.model;

public class SubTask extends Task {

    private int EpicId;

    public SubTask(String name, String description, int EpicId) {
        super(name, description);
        this.EpicId = EpicId;
    }

    public Integer getEpicId() {
        return EpicId;
    }

    @Override
    public String toString() {
        return this.getClass() +"{" +
                "taskID=" + this.getTaskID() +
                ", taskName='" + this.getTaskName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                ", whichEpicId='" + EpicId + '\'' +
                '}';
    }
}