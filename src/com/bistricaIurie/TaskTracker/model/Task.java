package com.bistricaIurie.TaskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
    private int taskID = 0;
    private String taskName;
    private String description;
    private TaskStatus status;
    private final TaskType type = TaskType.TASK;
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(int taskID, String taskName, String description) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
    }

    public Task(int taskID, String taskName, String description, TaskStatus status,
                Duration duration) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.duration = duration;
    }

    public Task(int taskID, String taskName, String description, TaskStatus status,
                Duration duration, LocalDateTime startTime) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.duration = duration;
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = startTime.truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = startTime.truncatedTo(ChronoUnit.SECONDS);
        }
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

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration.toMinutes());
        }
        return null;
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
        String sTime = startTime == null ? "null"
                : this.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        return this.taskID +
                "," + this.type +
                "," + this.taskName +
                "," + this.status +
                "," + this.description +
                "," + this.duration.toMinutes() +
                "," + sTime;
    }
}
