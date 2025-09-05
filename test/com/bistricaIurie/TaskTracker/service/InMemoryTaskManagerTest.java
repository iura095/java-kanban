package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.model.TaskStatus;
import com.bistricaIurie.TaskTracker.model.error.TaskException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager tm;
    public Task task1, task2;
    public Epic epic1;
    public SubTask subTask1, subTask2;

    @BeforeEach
    void setUp() {
        tm = new InMemoryTaskManager();
        task1 = new Task(1, "name", "desc", TaskStatus.NEW, Duration.ZERO
                , LocalDateTime.now());
        task2 = new Task(2, "name", "desc", TaskStatus.NEW, Duration.ZERO
                , LocalDateTime.now());
        epic1 = new Epic(3, "name", "desc");
        subTask1 = new SubTask(4, "name", "desc", TaskStatus.NEW, epic1.getTaskID()
                , Duration.ZERO
                , LocalDateTime.now());
        subTask2 = new SubTask(5, "name", "desc", TaskStatus.NEW, 3, Duration.ZERO
                , LocalDateTime.now());
    }

    @AfterEach
    void setEnd() {
        tm.setTaskCount(0);
    }

    @Test
    void checkTaskIntersection() {
        task1.setDuration(Duration.ofMinutes(10));
        task2.setDuration(Duration.ofMinutes(5));
        tm.addTask(task1);
        assertFalse(tm.checkTaskIntersection(task2));
        task2.setStartTime(task2.getStartTime().plusMinutes(25));
        assertTrue(tm.checkTaskIntersection(task2));
        subTask1.setStartTime(subTask1.getStartTime().plusMinutes(13));
        subTask1.setDuration(Duration.ofMinutes(5));
        assertTrue(tm.checkTaskIntersection(subTask1));
    }

    @Test
    void prioritizeTaskAndGetPrioritizeList() {
        task1.setStartTime(task1.getStartTime().plusMinutes(5));
        tm.prioritizeTask(task1);
        tm.prioritizeTask(task2);
        assertEquals(List.of(task2, task1), tm.getPrioritizedTasks());
    }

    @Test
    void addTask() {
        tm.addTask(task1);
        assertEquals(task1, tm.getTaskByID(1));
    }

    @Test
    void addSubTask() {
        assertThrows(TaskException.class, () -> tm.addSubTask(subTask1));
        tm.addEpic(epic1);
        subTask1.setEpicId(epic1.getTaskID());
        tm.addSubTask(subTask1);
        assertEquals(subTask1, tm.getSubTaskList().getFirst());
        assertEquals(subTask1, epic1.getSubTaskList().getFirst());
    }

    @Test
    void addEpic() {
        tm.addEpic(epic1);
        assertEquals(epic1, tm.getEpicByID(1));
    }

    @Test
    void getTaskList() {
        tm.addTask(task1);
        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        assertEquals(list, tm.getTaskList());
    }

    @Test
    void getSubTaskList() {
        tm.addEpic(epic1);
        subTask1.setEpicId(1);
        tm.addSubTask(subTask1);
        ArrayList<SubTask> list = new ArrayList<>();
        list.add(subTask1);
        assertEquals(list, tm.getSubTaskList());
    }

    @Test
    void getEpicList() {
        tm.addEpic(epic1);
        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic1);
        assertEquals(list, tm.getEpicList());
    }

    @Test
    void clearTaskList() {
        tm.addTask(task1);
        assertFalse(tm.getTaskList().isEmpty());
        tm.clearTaskList();
        assertTrue(tm.getTaskList().isEmpty());
    }

    @Test
    void clearSubTaskList() {
        tm.addEpic(epic1);
        subTask1.setEpicId(1);
        tm.addSubTask(subTask1);
        assertFalse(tm.getSubTaskList().isEmpty());
        assertFalse(tm.getEpicByID(1).getSubTaskList().isEmpty());
        tm.clearSubTaskList();
        assertTrue(tm.getSubTaskList().isEmpty());
        assertTrue(tm.getEpicByID(1).getSubTaskList().isEmpty());
    }

    @Test
    void clearEpicList() {
        tm.addEpic(epic1);
        subTask1.setEpicId(1);
        tm.addSubTask(subTask1);
        assertFalse(tm.getEpicList().isEmpty());
        assertFalse(tm.getSubTaskList().isEmpty());
        tm.clearEpicList();
        assertTrue(tm.getEpicList().isEmpty());
        assertTrue(tm.getSubTaskList().isEmpty());
    }

    @Test
    void getTaskByID() {
        tm.addTask(task1);
        Task ntask = tm.getTaskByID(1);
        assertEquals(task1, ntask);
    }

    @Test
    void getSubTaskByID() {
        tm.addEpic(epic1);
        subTask1.setEpicId(1);
        tm.addSubTask(subTask1);
        SubTask ntask = tm.getSubTaskByID(2);
        assertEquals(subTask1, ntask);
    }

    @Test
    void getEpicByID() {
        tm.addEpic(epic1);
        Epic epic = tm.getEpicByID(1);
        assertEquals(epic, epic1);
    }

    @Test
    void updateTask() {
        tm.addTask(task1);
        Task newTask = new Task("name1", "desc1");
        newTask.setTaskID(1);
        tm.updateTask(newTask);
        assertEquals(1, tm.getTaskList().size());
        assertEquals(newTask, tm.getTaskByID(1));

    }

    @Test
    void updateSubTask() {
        tm.addEpic(epic1);
        subTask1.setEpicId(1);
        tm.addSubTask(subTask1);
        SubTask subTask = new SubTask("name", "desc", 1);
        subTask.setTaskID(2);
        tm.updateSubTask(subTask);
        assertEquals(subTask, tm.getSubTaskList().getFirst());
        assertEquals(subTask, tm.getEpicByID(1).getSubTaskList().getFirst());
    }

    @Test
    void updateEpic() {
        tm.addEpic(epic1);
        Epic epic = new Epic("EpicName", "epicDesc");
        epic.setTaskID(1);
        tm.updateEpic(epic);
        assertEquals(epic, tm.getEpicList().getFirst());
    }

    @Test
    void deleteTask() {
        tm.addTask(task1);
        assertEquals(1, tm.getTaskList().size());
        tm.deleteTask(1);
        assertTrue(tm.getTaskList().isEmpty());
    }

    @Test
    void deleteSubTaskAndEpic() {
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addSubTask(subTask1);
        tm.addSubTask(subTask2);
        assertEquals(2, tm.getSubTaskList().size());
        tm.deleteSubTask(subTask1.getTaskID());
        assertEquals(1, tm.getSubTaskList().size());
        assertFalse(tm.getSubTaskList().isEmpty());
        assertFalse(tm.getEpicList().isEmpty());
        tm.deleteEpic(epic1.getTaskID());
        assertTrue(tm.getSubTaskList().isEmpty());
        assertTrue(tm.getEpicList().isEmpty());
    }

    @Test
    void setNewTaskStatus() {
        tm.addTask(task1);
        assertEquals(TaskStatus.NEW, tm.getTaskByID(1).getStatus());
        tm.setNewTaskStatus(1, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, tm.getTaskByID(1).getStatus());

    }

    @Test
    void setNewSubTaskStatus() {
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addSubTask(subTask1);
        tm.addSubTask(subTask2);
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        assertEquals(TaskStatus.NEW, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask1.getTaskID(), TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask1.getTaskID(), TaskStatus.DONE);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(TaskStatus.DONE, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask2.getTaskID(), TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(TaskStatus.DONE, subTask1.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask2.getTaskID(), TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, epic1.getStatus());
        assertEquals(TaskStatus.DONE, subTask1.getStatus());
        assertEquals(TaskStatus.DONE, subTask2.getStatus());

        tm.setNewSubTaskStatus(subTask1.getTaskID(), TaskStatus.NEW);
        tm.setNewSubTaskStatus(subTask2.getTaskID(), TaskStatus.NEW);
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        assertEquals(TaskStatus.NEW, subTask1.getStatus());
        assertEquals(TaskStatus.NEW, subTask2.getStatus());
    }

    @Test
    void getSubTaskListByEpicId() {
        tm.addEpic(new Epic("name", "desc"));
        assertTrue(tm.getSubTaskListByEpicId(1).isEmpty());
        tm.addSubTask(new SubTask("name", "desc", 1));
        tm.addSubTask(new SubTask("name1", "desc2", 1));
        assertFalse(tm.getSubTaskListByEpicId(1).isEmpty());
        assertEquals(2, tm.getSubTaskListByEpicId(1).size());
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