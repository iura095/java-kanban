package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    void addSubTask(SubTask task);

    void addEpic(Epic task);

    ArrayList<Task> getTaskList();

    ArrayList<SubTask> getSubTaskList();

    ArrayList<Epic> getEpicList();

    void clearTaskList();

    void clearSubTaskList();

    void clearEpicList();

    Task getTaskByID(Integer id);

    SubTask getSubTaskByID(Integer id);

    Epic getEpicByID(Integer id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteTask(Integer id);

    void deleteSubTask(Integer id);

    void deleteEpic(Integer id);

    void setNewTaskStatus(Integer id, TaskStatus newStatus);

    void setNewSubTaskStatus(Integer id, TaskStatus newStatus);

    ArrayList<SubTask> getSubTaskListByEpicId(Integer id);
}
