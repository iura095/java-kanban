package com.bistricaIurie.TaskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class Epic extends Task {

    private final TaskType type;
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private LocalDateTime endTime;
    final Comparator<LocalDateTime> comparatorLDT = (o1, o2) -> {
        if (o1.isBefore(o2)) {
            return -1;
        } else if (o1.isAfter(o2)) {
            return 1;
        }
        return 0;
    };

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
    }

    public Epic(int taskID, String taskName, String description) {
        super(taskID, taskName, description);
        this.type = TaskType.EPIC;
    }

    public void clearSubtaskList() {
        subTaskList.clear();
        updateEpicStatus();
        updateStartTime();
        updateDuration();
        updateEndTime();
    }

    public void addSubtask(SubTask subtask) {
        subTaskList.put(subtask.getTaskID(), subtask);
        updateEpicStatus();
        updateStartTime();
        updateDuration();
        updateEndTime();
    }

    public void deleteSubtask(Integer id) {
        subTaskList.remove(id);
        updateEpicStatus();
        updateStartTime();
        updateDuration();
        updateEndTime();
    }

    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList.values());
    }

    public void updateEpicStatus() {
        int doneStatusCount = 0;
        int newStatusCount = 0;
        if (subTaskList.isEmpty()) {
            this.setStatus(TaskStatus.NEW);
        } else {
            for (SubTask sb : subTaskList.values()) {
                if (sb.getStatus() == TaskStatus.IN_PROGRESS) {
                    this.setStatus(TaskStatus.IN_PROGRESS);
                    break;
                } else if (sb.getStatus() == TaskStatus.DONE) {
                    doneStatusCount++;
                } else if (sb.getStatus() == TaskStatus.NEW) {
                    newStatusCount++;
                }
            }

            if (doneStatusCount > 0) {
                this.setStatus(TaskStatus.IN_PROGRESS);
            }

            if (doneStatusCount == subTaskList.size()) {
                this.setStatus(TaskStatus.DONE);
            }

            if (newStatusCount == subTaskList.size()) {
                this.setStatus(TaskStatus.NEW);
            }
        }
    }

    public void updateStartTime() {
        if (subTaskList.isEmpty()) {
            setStartTime(null);
        } else {
            Optional<LocalDateTime> optional = subTaskList.values().stream()
                    .filter(s -> s.getStartTime() != null)
                    .map(Task::getStartTime)
                    .min(comparatorLDT);
            setStartTime(optional.orElse(null));

        }
    }

    public void updateDuration() {
        if (!subTaskList.isEmpty()) {
            setDuration(Duration.ZERO.plusMinutes(
                    subTaskList.values().stream()
                            .map(Task::getDuration)
                            .mapToLong(Duration::toMinutes).sum()
            ));
        } else {
            setDuration(Duration.ZERO);
        }
    }

    public void updateEndTime() {
        if (subTaskList.isEmpty()) {
            endTime = null;
        } else {
            Optional<LocalDateTime> optional = subTaskList.values().stream()
                    .filter(s -> s.getStartTime() != null)
                    .map(Task::getEndTime)
                    .max(comparatorLDT);
            endTime = optional.orElse(null);
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
