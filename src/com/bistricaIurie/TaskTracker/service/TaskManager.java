package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int taskCount = 1;

    public void addTask(Task task) {
        task.setTaskID(taskCount);
        tasks.put(task.getTaskID(), task);
        taskCount++;
    }

    public void addSubTask(SubTask task) {
        task.setTaskID(taskCount);
        subTasks.put(task.getTaskID(), task);
        taskCount++;
        epics.get(task.getEpicId()).addSubtask(task);
        epics.get(task.getEpicId()).updateEpicStatus();
    }

    public void addEpic(Epic task) {
        task.setTaskID(taskCount);
        epics.put(task.getTaskID(), task);
        taskCount++;
    }

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    public void clearTaskList() {
        tasks.clear();
    }

    public void clearSubTaskList() {
        subTasks.clear();
        for (Integer i : epics.keySet()) {
            epics.get(i).subTaskList.clear();
            epics.get(i).updateEpicStatus();
        }
    }

    public void clearEpicList() {
        epics.clear();
        subTasks.clear();
    }

    public Task getTaskByID(Integer id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskByID(Integer id) {
        return subTasks.get(id);
    }

    public Epic getEpicByID(Integer id) {
        return epics.get(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskID(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskID(), subTask);
        epics.get(subTask.getEpicId()).subTaskList.put(subTask.getTaskID(), subTask);
        epics.get(subTask.getEpicId()).updateEpicStatus();
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskID(), epic);
    }

    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    public void deleteSubTask(Integer id) {
        subTasks.remove(id);
        epics.get(subTasks.get(id).getEpicId()).subTaskList.remove(id);
        epics.get(subTasks.get(id).getEpicId()).updateEpicStatus();
    }

    public void deleteEpic(Integer id) {
        for (SubTask subTask : epics.get(id).getSubTaskList()) {
            subTasks.remove(subTask.getTaskID());
        }
        epics.remove(id);
    }

    public void setNewTaskStatus(Integer id, TaskStatus newStatus) {
        Task tempTask = getTaskByID(id);
        tempTask.setStatus(newStatus);
        tasks.put(id, tempTask);
    }

    public void setNewSubTaskStatus(Integer id, TaskStatus newStatus) {
        SubTask tempTask = getSubTaskByID(id);
        tempTask.setStatus(newStatus);
        subTasks.put(id, tempTask);
    }

    public ArrayList <SubTask> getSubTaskListByEpicId(Integer id) {
        return epics.get(id).getSubTaskList();
    }

}
