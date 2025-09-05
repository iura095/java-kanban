package com.bistricaIurie.TaskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
            List<TaskStatus> list = subTaskList.values().stream().peek(s -> {
                if (s.getStartTime() != null) {
                    if (this.getStartTime() == null) {
                        this.setStartTime(s.getStartTime());
                    } else {
                        if (s.getStartTime().isBefore(this.getStartTime())) {
                            this.setStartTime(s.getStartTime());
                        }
                    }
                }
                if (s.getEndTime() != null) {
                    if (this.getEndTime() == null) {
                        setEndTime(s.getEndTime());
                    } else {
                        if (s.getEndTime().isAfter(this.getEndTime())) {
                            setEndTime(s.getEndTime());
                        }
                    }
                }
                setDuration(this.getDuration().plus(s.getDuration()));
            }).map(Task::getStatus).toList();
            if (list.stream().allMatch( t -> t == TaskStatus.NEW)) {
                setStatus(TaskStatus.NEW);
            } else if (list.stream().allMatch(t -> t == TaskStatus.DONE)) {
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
