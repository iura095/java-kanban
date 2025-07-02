package com.bistricaIurie.TaskTracker.model;

import java.util.Objects;

public class Task {
    private int taskID = 0;
    private String taskName;
    private String description;
    private TaskStatus status;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        status = TaskStatus.NEW;
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
        return this.getClass() +"{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
