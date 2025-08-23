package com.bistricaIurie.TaskTracker.model;

public class SubTask extends Task {

    private int epicId;
    private TaskType type;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description);
        this.type = TaskType.SUBTASK;
        this.epicId = epicId;
    }

    public SubTask(int taskID, String taskName, String description, TaskStatus status, int epicId) {
        super(taskID,taskName, description, status);
        this.type = TaskType.SUBTASK;
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return this.type;
    }

    @Override
    public String toString() { //id,type,name,status,description,epic
        return getTaskID() +
                "," + getType() +
                "," + getTaskName() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getEpicId();
    }
}