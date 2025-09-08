import com.bistricaIurie.TaskTracker.model.Epic;
import com.bistricaIurie.TaskTracker.model.SubTask;
import com.bistricaIurie.TaskTracker.model.Task;
import com.bistricaIurie.TaskTracker.service.FileBackedTaskManager;
import com.bistricaIurie.TaskTracker.service.TaskManager;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        taskManager.addTask(new Task("task1", "eto 1ii task"));
        taskManager.addTask(new Task("task2", "eto 2oi task"));
        taskManager.addTask(new Task("task3", "eto 3ii task"));
        taskManager.addEpic(new Epic("epic1", "eto 1ii epic"));
        taskManager.addSubTask(new SubTask("subtask1", "etub subtask1 from epic1", 4));
        taskManager.addSubTask(new SubTask("subtask2", "etub subtask2 from epic1", 4));
        taskManager.addSubTask(new SubTask("subtask3", "etub subtask3 from epic1", 4));
        taskManager.addTask(new Task("task4", "eto 4ii task"));
        taskManager.addEpic(new Epic("epic2", "eto 2oi epic"));
        taskManager.addSubTask(new SubTask("subtask4", "etub subtask1 from epic2", 9));
        taskManager.addSubTask(new SubTask("subtask5", "etub subtask2 from epic2", 9));
        taskManager.addSubTask(new SubTask("subtask6", "etub subtask3 from epic2", 9));
        taskManager.getTaskByID(1).setStartTime(LocalDateTime.now());
        taskManager.getEpicByID(4);
        taskManager.getTaskByID(3).setStartTime(LocalDateTime.now().plusMinutes(5));
        taskManager.getSubTaskByID(6).setStartTime(LocalDateTime.now().plusMinutes(4));
        taskManager.getSubTaskByID(7).setStartTime(LocalDateTime.now().plusMinutes(55));
        taskManager.deleteTask(2);
        taskManager.getSubTaskByID(10).setStartTime(LocalDateTime.now().plusMinutes(2));
        taskManager.getSubTaskByID(6);


//        printAllTasks(taskManager);
        FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile();
        printAllTasks(tm);
        for (Task task : tm.getPrioritizedTasks()) {
            System.out.println(task);
        }


    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicList()) {
            System.out.println(epic);

            for (Task task : manager.getSubTaskListByEpicId(epic.getTaskID())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTaskList()) {
            System.out.println(subtask);
        }


        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}
