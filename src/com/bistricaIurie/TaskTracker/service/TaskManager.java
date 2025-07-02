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
    }

    public void addEpic(Epic task) {
        task.setTaskID(taskCount);
        epics.put(task.getTaskID(), task);
        taskCount++;
    }

    public ArrayList<Task> getTaskList() {
        ArrayList<Task> list = new ArrayList<>();
        for (Integer i : tasks.keySet()) {
            list.add(tasks.get(i));
        }
        return list;
    }

    public ArrayList<SubTask> getSubTaskList() {
        ArrayList<SubTask> list = new ArrayList<>();
        for (Integer i : subTasks.keySet()) {
            list.add(subTasks.get(i));
        }
        return list;
    }

    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> list = new ArrayList<>();
        for (Integer i : epics.keySet()) {
            list.add(epics.get(i));
        }
        return list;
    }

    public void clearTaskList() {
        tasks.clear();
    }

    public void clearSubTaskList() {
        subTasks.clear();
    }

    public void clearEpicList() {
        epics.clear();
    }

    public Task getTaskByID(Integer ID) {
        if (tasks.containsKey(ID)) {
            return tasks.get(ID);
        } else return null;
    }

    public SubTask getSubTaskByID(Integer ID) {
        if (subTasks.containsKey(ID)) {
            return subTasks.get(ID);
        } else return null;
    }

    public Epic getEpicByID(Integer ID) {
        if (epics.containsKey(ID)) {
            return epics.get(ID);
        } else return null;
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskID(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskID(), subTask);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskID(), epic);
    }

    public void deleteTask(Integer ID) {
        tasks.remove(ID);
    }

    public void deleteSubTask(Integer ID) {
        subTasks.remove(ID);
    }

    public void deleteEpic(Integer ID) {
        epics.remove(ID);
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

}
