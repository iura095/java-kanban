import com.bistricaIurie.TaskTracker.model.*;
import com.bistricaIurie.TaskTracker.service.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(new Task("task1", "eto 1ii task"));
        taskManager.addTask(new Task("task2", "eto 2oi task"));
        taskManager.addTask(new Task("task3", "eto 3ii task"));
        taskManager.addEpic(new Epic("epic1", "eto 1ii epic"));
        taskManager.addEpic(new Epic("epic2", "eto 2oi epic"));
        taskManager.addSubTask(new SubTask("subtask1", "etub subtask1 from epic1", 4));
        taskManager.addSubTask(new SubTask("subtask2", "etub subtask2 from epic1", 4));
        taskManager.addSubTask(new SubTask("subtask3", "etub subtask3 from epic1", 4));
        taskManager.addSubTask(new SubTask("subtask4", "etub subtask1 from epic2", 4));
        taskManager.addSubTask(new SubTask("subtask5", "etub subtask2 from epic2", 4));
        taskManager.addSubTask(new SubTask("subtask6", "etub subtask3 from epic2", 4));
        taskManager.getSubTaskByID(6);
        taskManager.getTaskByID(1);
        taskManager.getEpicByID(4);

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        //printAllTasks(taskManager);

    }

    private static void printAllTasks(InMemoryTaskManager manager) {
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
