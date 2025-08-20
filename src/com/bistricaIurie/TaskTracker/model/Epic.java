package com.bistricaIurie.TaskTracker.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    private TaskType type;

    public HashMap<Integer, SubTask> subTaskList = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
    }

    public Epic(int taskID, String taskName, String description, TaskStatus status) {
        super(taskID,taskName, description, status);
        this.type = TaskType.EPIC;
    }

    public void addSubtask(SubTask subtask) {
        subTaskList.put(subtask.getTaskID(), subtask);
    }

    public void deleteSubtask(Integer id) {
        subTaskList.remove(id);
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
