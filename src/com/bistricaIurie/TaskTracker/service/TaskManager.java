package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    Task getTaskByID(Integer id) throws FileNotFoundException;

    SubTask getSubTaskByID(Integer id) throws FileNotFoundException;

    Epic getEpicByID(Integer id) throws FileNotFoundException;

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteTask(Integer id);

    void deleteSubTask(Integer id);

    void deleteEpic(Integer id);

    void setNewTaskStatus(Integer id, TaskStatus newStatus);

    void setNewSubTaskStatus(Integer id, TaskStatus newStatus);

    ArrayList<SubTask> getSubTaskListByEpicId(Integer id);

    void setTaskCount(int taskCount);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    void clearHistory();
}
