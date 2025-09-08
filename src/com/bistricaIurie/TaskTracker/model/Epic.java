package com.bistricaIurie.TaskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {

    private final TaskType type = TaskType.EPIC;
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int taskID, String taskName, String description) {
        super(taskID, taskName, description);
    }

    public void clearSubtaskList() {
        subTaskList.clear();
        updateEpicFields();
    }

    public void addSubtask(SubTask subtask) {
        subTaskList.put(subtask.getTaskID(), subtask);
        updateEpicFields();
    }

    public void deleteSubtask(Integer id) {
        subTaskList.remove(id);
        updateEpicFields();
    }

    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList.values());
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateEpicFields() {
        setStatus(TaskStatus.NEW);
        setStartTime(null);
        setDuration(Duration.ZERO);
        setEndTime(null);
        if (!subTaskList.isEmpty()) {
            List<TaskStatus> statusList = subTaskList.values().stream().peek(subTask -> {
                if (subTask.getStartTime() != null) {
                    if (this.getStartTime() == null) {
                        this.setStartTime(subTask.getStartTime());
                    } else {
                        if (subTask.getStartTime().isBefore(this.getStartTime())) {
                            this.setStartTime(subTask.getStartTime());
                        }
                    }
                }
                if (subTask.getEndTime() != null) {
                    if (this.getEndTime() == null) {
                        setEndTime(subTask.getEndTime());
                    } else {
                        if (subTask.getEndTime().isAfter(this.getEndTime())) {
                            setEndTime(subTask.getEndTime());
                        }
                    }
                }
                setDuration(this.getDuration().plus(subTask.getDuration()));
            }).map(Task::getStatus).toList();
            if (statusList.stream().allMatch(taskStatus -> taskStatus == TaskStatus.NEW)) {
                setStatus(TaskStatus.NEW);
            } else if (statusList.stream().allMatch(taskStatus -> taskStatus == TaskStatus.DONE)) {
                this.setStatus(TaskStatus.DONE);
            } else {
                this.setStatus(TaskStatus.IN_PROGRESS);
            }

        }
    }

    @Override
    public TaskType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.getTaskID() +
                "," + this.getType() +
                "," + this.getTaskName() +
                "," + this.getStatus() +
                "," + this.getDescription();
    }
}
