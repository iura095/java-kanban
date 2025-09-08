package com.bistricaIurie.TaskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {

    private int epicId;
    private final TaskType type = TaskType.SUBTASK;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(int taskID, String taskName, String description, TaskStatus status, int epicId,
                   Duration duration, LocalDateTime startTime) {
        super(taskID, taskName, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(int taskID, String taskName, String description, TaskStatus status, int epicId,
                   Duration duration) {
        super(taskID, taskName, description, status, duration);
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
    public String toString() {
        String sTime = getStartTime() == null ? "null"
                : getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        return getTaskID() +
                "," + this.getType() +
                "," + this.getTaskName() +
                "," + this.getStatus() +
                "," + this.getDescription() +
                "," + this.getDuration().toMinutes() +
                "," + sTime +
                "," + this.getEpicId();
    }
}