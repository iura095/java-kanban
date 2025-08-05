package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private int taskCount = 0;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void addTask(Task task) {
        taskCount++;
        task.setTaskID(taskCount);
        tasks.put(task.getTaskID(), task);
    }

    @Override
    public void addSubTask(SubTask task) {
        taskCount++;
        task.setTaskID(taskCount);
        subTasks.put(task.getTaskID(), task);
        epics.get(task.getEpicId()).addSubtask(task);
        epics.get(task.getEpicId()).updateEpicStatus();
    }

    @Override
    public void addEpic(Epic task) {
        taskCount++;
        task.setTaskID(taskCount);
        epics.put(task.getTaskID(), task);
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearTaskList() {
        tasks.clear();
    }

    @Override
    public void clearSubTaskList() {
        subTasks.clear();
        for (Integer i : epics.keySet()) {
            epics.get(i).subTaskList.clear();
            epics.get(i).updateEpicStatus();
        }
    }

    @Override
    public void clearEpicList() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTaskByID(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTaskByID(Integer id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpicByID(Integer id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskID(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskID(), subTask);
        epics.get(subTask.getEpicId()).subTaskList.put(subTask.getTaskID(), subTask);
        epics.get(subTask.getEpicId()).updateEpicStatus();
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskID(), epic);
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(Integer id) {
        epics.get(subTasks.get(id).getEpicId()).subTaskList.remove(id);
        epics.get(subTasks.get(id).getEpicId()).updateEpicStatus();
        subTasks.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
        for (SubTask subTask : epics.get(id).getSubTaskList()) {
            subTasks.remove(subTask.getTaskID());
        }
        epics.remove(id);
    }

    @Override
    public void setNewTaskStatus(Integer id, TaskStatus newStatus) {
        Task tempTask = tasks.get(id);
        tempTask.setStatus(newStatus);
        updateTask(tempTask);
    }

    @Override
    public void setNewSubTaskStatus(Integer id, TaskStatus newStatus) {
        SubTask tempTask = subTasks.get(id);
        tempTask.setStatus(newStatus);
        updateSubTask(tempTask);
    }

    @Override
    public ArrayList <SubTask> getSubTaskListByEpicId(Integer id) {
        return epics.get(id).getSubTaskList();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return Objects.equals(tasks, that.tasks) && Objects.equals(subTasks, that.subTasks) && Objects.equals(epics, that.epics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subTasks, epics);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
