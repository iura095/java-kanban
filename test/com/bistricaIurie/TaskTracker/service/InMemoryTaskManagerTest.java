package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addTask() {
        task = new Task("name", "desc");
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskByID(1));
    }

    @Test
    void addSubTask() {
        subTask = new SubTask("name", "desc", 1);
        taskManager.addEpic(new Epic("name", "desc"));
        taskManager.addSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTaskByID(2));
    }

    @Test
    void addEpic() {
        epic = new Epic("name", "desc");
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpicByID(1));
    }

    @Test
    void getTaskList() {
        task = new Task("name", "desc");
        taskManager.addTask(task);
        ArrayList<Task> list = new ArrayList<>();
        list.add(task);
        assertEquals(list, taskManager.getTaskList());
    }

    @Test
    void getSubTaskList() {
        taskManager.addEpic(new Epic("name", "desc"));
        subTask = new SubTask("name", "desc", 1);
        taskManager.addSubTask(subTask);
        ArrayList<SubTask> list = new ArrayList<>();
        list.add(subTask);
        assertEquals(list, taskManager.getSubTaskList());
    }

    @Test
    void getEpicList() {
        epic = new Epic("name", "desc");
        taskManager.addEpic(epic);
        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic);
        assertEquals(list, taskManager.getEpicList());
    }

    @Test
    void clearTaskList() {
        taskManager.addTask(new Task("name", "desc"));
        assertFalse(taskManager.getTaskList().isEmpty());
        taskManager.clearTaskList();
        assertTrue(taskManager.getTaskList().isEmpty());
    }

    @Test
    void clearSubTaskList() {
        taskManager.addEpic(new Epic("name", "desc"));
        taskManager.addSubTask(new SubTask("name", "desc", 1));
        assertFalse(taskManager.getSubTaskList().isEmpty());
        assertFalse(taskManager.getEpicByID(1).getSubTaskList().isEmpty());
        taskManager.clearSubTaskList();
        assertTrue(taskManager.getSubTaskList().isEmpty());
        assertTrue(taskManager.getEpicByID(1).getSubTaskList().isEmpty());
    }

    @Test
    void clearEpicList() {
        taskManager.addEpic(new Epic("name", "desc"));
        taskManager.addSubTask(new SubTask("name", "desc", 1));
        assertFalse(taskManager.getEpicList().isEmpty());
        assertFalse(taskManager.getSubTaskList().isEmpty());
        taskManager.clearEpicList();
        assertTrue(taskManager.getEpicList().isEmpty());
        assertTrue(taskManager.getSubTaskList().isEmpty());
    }

    @Test
    void getTaskByID() {
        task = new Task("name", "desc");
        taskManager.addTask(task);
        Task task2 = taskManager.getTaskByID(1);
        assertEquals(task, task2);
    }

    @Test
    void getSubTaskByID() {
        taskManager.addEpic(new Epic("name", "desc"));
        subTask = new SubTask("name", "desc", 1);
        taskManager.addSubTask(subTask);
        SubTask subTask2 = taskManager.getSubTaskByID(2);
        assertEquals(subTask, subTask2);
    }

    @Test
    void getEpicByID() {
        epic = new Epic("name", "desc");
        taskManager.addEpic(epic);
        Epic epic1 = taskManager.getEpicByID(1);
        assertEquals(epic, epic1);
    }

    @Test
    void updateTask() {
        taskManager.addTask(new Task("name", "desc"));
        Task newTask = new Task("name1", "desc1");
        newTask.setTaskID(1);
        taskManager.updateTask(newTask);
        assertEquals(1, taskManager.getTaskList().size());
        assertEquals(newTask, taskManager.getTaskByID(1));

    }

    @Test
    void updateSubTask() {
        taskManager.addEpic(new Epic("epic", "desc"));
        taskManager.addSubTask(new SubTask("name", "desc", 1));
        SubTask subTask1 = new SubTask("name", "desc", 1);
        subTask1.setTaskID(2);
        taskManager.updateSubTask(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskList().getFirst());
        assertEquals(subTask1, taskManager.getEpicByID(1).getSubTaskList().getFirst());
    }

    @Test
    void updateEpic() {
        taskManager.addEpic(new Epic("name", "desc"));
        Epic epic1 =  new Epic("EpicName", "epicDesc");
        epic1.setTaskID(1);
        taskManager.updateEpic(epic1);
        assertEquals(epic1, taskManager.getEpicList().getFirst());
    }

    @Test
    void deleteTask() {
        taskManager.addTask(new Task("name", "desc"));
        assertEquals(1, taskManager.getTaskList().size());
        taskManager.deleteTask(1);
        assertTrue(taskManager.getTaskList().isEmpty());
    }

    @Test
    void deleteSubTask() {
        taskManager.addEpic(new Epic("name", "desc"));
        taskManager.addSubTask(new SubTask("name", "desc", 1));
        assertFalse(taskManager.getSubTaskList().isEmpty());
        assertFalse(taskManager.getEpicByID(1).getSubTaskList().isEmpty());
        taskManager.deleteSubTask(2);
        assertTrue(taskManager.getSubTaskList().isEmpty());
        assertTrue(taskManager.getEpicByID(1).getSubTaskList().isEmpty());
    }

    @Test
    void deleteEpic() {
        taskManager.addEpic(new Epic("name", "desc"));
        taskManager.addSubTask(new SubTask("name", "desc", 1));
        assertFalse(taskManager.getSubTaskList().isEmpty());
        assertFalse(taskManager.getEpicList().isEmpty());
        taskManager.deleteEpic(1);
        assertTrue(taskManager.getEpicList().isEmpty());
        assertTrue(taskManager.getSubTaskList().isEmpty());
    }

    @Test
    void setNewTaskStatus() {
        taskManager.addTask(new Task("name", "desc"));
        assertEquals(TaskStatus.NEW, taskManager.getTaskByID(1).getStatus());
        taskManager.setNewTaskStatus(1, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, taskManager.getTaskByID(1).getStatus());

    }

    @Test
    void setNewSubTaskStatus() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addEpic(new Epic("name", "desc"));
        taskManager.addSubTask(new SubTask("name", "desc", 1));
        taskManager.addSubTask(new SubTask("name1", "desc2", 1));
        assertEquals(TaskStatus.NEW, taskManager.getEpicByID(1).getStatus());
        assertEquals(TaskStatus.NEW, taskManager.getSubTaskByID(2).getStatus());
        assertEquals(TaskStatus.NEW, taskManager.getSubTaskByID(3).getStatus());
        taskManager.setNewSubTaskStatus(2, TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicByID(1).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getSubTaskByID(2).getStatus());
        taskManager.setNewSubTaskStatus(2, TaskStatus.DONE);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicByID(1).getStatus());
        assertEquals(TaskStatus.DONE, taskManager.getSubTaskByID(2).getStatus());
        taskManager.setNewSubTaskStatus(3, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, taskManager.getEpicByID(1).getStatus());
        assertEquals(TaskStatus.DONE, taskManager.getSubTaskByID(2).getStatus());
        assertEquals(TaskStatus.DONE, taskManager.getSubTaskByID(3).getStatus());
    }

    @Test
    void getSubTaskListByEpicId() {
        taskManager.addEpic(new Epic("name", "desc"));
        assertTrue(taskManager.getSubTaskListByEpicId(1).isEmpty());
        taskManager.addSubTask(new SubTask("name", "desc", 1));
        taskManager.addSubTask(new SubTask("name1", "desc2", 1));
        assertFalse(taskManager.getSubTaskListByEpicId(1).isEmpty());
        assertEquals(2, taskManager.getSubTaskListByEpicId(1).size());
    }

    @Test
    void getHistory() {
        InMemoryTaskManager taskManager1 = new InMemoryTaskManager();
        taskManager1.addEpic(new Epic("name", "desc"));
        taskManager1.addSubTask(new SubTask("name", "desc", 1));
        taskManager1.addSubTask(new SubTask("name1", "desc2", 1));
        assertTrue(taskManager1.getHistory().isEmpty());
        taskManager1.getSubTaskByID(2);
        assertFalse(taskManager1.getHistory().isEmpty());
        taskManager1.getSubTaskByID(3);
        assertEquals(2, taskManager1.getHistory().size());
    }
}