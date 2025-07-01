import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("delo1", "nado delat delo 1");
        Task task2 = new Task("delo2", "nado delat delo 2");
        SubTask subTask1 = new SubTask("subdelo 1", "nado delat subdelo 1", "epic1");
        SubTask subTask2 = new SubTask("subdelo 2", "nado delat subdelo 2", "epic1");
        SubTask subTask3 = new SubTask("subdelo 3", "nado delat subdelo 3", "epic1");
        HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
        subTaskHashMap.put(subTask1.getTaskID(), subTask1);
        subTaskHashMap.put(subTask2.getTaskID(), subTask2);
        subTaskHashMap.put(subTask3.getTaskID(), subTask3);
        Epic epic1 = new Epic("epicdelo 1", "nado delat epicdelo 1", subTaskHashMap);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(epic1);

        taskManager.getTaskList();
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        epic1.checkEpicStatus();
        taskManager.getTaskList();
        epic1.getSubTaskList();
        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        subTask3.setStatus(TaskStatus.DONE);
        epic1.checkEpicStatus();
        epic1.getSubTaskList();
        taskManager.getTaskList();
        taskManager.deleteTask(1);
        taskManager.getTaskList();
        task2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task2);
        taskManager.getTaskList();


    }
}
