package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager = Managers.getDefaultHistory();
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int taskCount = 1;

    @Override
    public void addTask(Task task) {
        task.setTaskID(taskCount);
        tasks.put(task.getTaskID(), task);
        taskCount++;
    }

    @Override
    public void addSubTask(SubTask task) {
        task.setTaskID(taskCount);
        subTasks.put(task.getTaskID(), task);
        taskCount++;
        epics.get(task.getEpicId()).addSubtask(task);
        epics.get(task.getEpicId()).updateEpicStatus();
    }

    @Override
    public void addEpic(Epic task) {
        task.setTaskID(taskCount);
        epics.put(task.getTaskID(), task);
        taskCount++;
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
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTaskByID(Integer id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpicByID(Integer id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
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
        Task tempTask = getTaskByID(id);
        tempTask.setStatus(newStatus);
        updateTask(tempTask);
    }

    @Override
    public void setNewSubTaskStatus(Integer id, TaskStatus newStatus) {
        SubTask tempTask = getSubTaskByID(id);
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
