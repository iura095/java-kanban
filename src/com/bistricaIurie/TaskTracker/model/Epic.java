package com.bistricaIurie.TaskTracker.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    public HashMap<Integer, SubTask> subTaskList = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(SubTask subtask) {
        subTaskList.put(subtask.getTaskID(), subtask);
    }

    public void deleteSubtask(Integer id) {
        subTaskList.remove(id);
    }

    public ArrayList <SubTask> getSubTaskList() {
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

            if (doneStatusCount == subTaskList.size()) {
                this.setStatus(TaskStatus.DONE);
            }

            if (newStatusCount == subTaskList.size()) {
                this.setStatus(TaskStatus.NEW);
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass() +"{" +
                "taskID=" + this.getTaskID() +
                ", taskName='" + this.getTaskName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                '}';
    }
}
