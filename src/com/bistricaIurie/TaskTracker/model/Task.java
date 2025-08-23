package com.bistricaIurie.TaskTracker.model;

import java.util.Objects;

public class Task {
    private int taskID = 0;
    private String taskName;
    private String description;
    private TaskStatus status;
    private final TaskType type = TaskType.TASK;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(int taskID, String taskName, String description, TaskStatus status) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int newID) {
        taskID = newID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskID == task.taskID;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(taskID);
    }

    @Override
    public String toString() {
        return this.taskID +
                "," + this.type +
                "," + this.taskName +
                "," + this.status +
                "," + this.description;
    }
}
