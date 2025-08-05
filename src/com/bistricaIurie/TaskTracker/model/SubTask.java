package com.bistricaIurie.TaskTracker.model;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask" +"{" +
                "taskID=" + this.getTaskID() +
                ", taskName='" + this.getTaskName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                ", EpicId='" + epicId + '\'' +
                '}';
    }
}