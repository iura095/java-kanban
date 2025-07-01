import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> taskList = new HashMap<>();

    public void getTaskList() {
        for (Integer i : taskList.keySet()) {
            System.out.println(taskList.get(i));
        }
    }

    public void clearList() {
        taskList.clear();
    }

    public Task getTaskToID(Integer ID) {
        for (Integer i : taskList.keySet()) {
            if (i.equals(ID)) {
                return taskList.get(i);
            }
        }
        return null;
    }

    public <T extends Task> void addTask(T task) {
        taskList.put(task.getTaskID(), task);
    }

    public void updateTask(Task task) {
        taskList.put(task.getTaskID(), task);
    }

    public void deleteTask(Integer ID) {
        taskList.remove(ID);
    }

    public void setNewStatus(Integer id, TaskStatus newStatus) {
        Task tempTask = getTaskToID(id);
        tempTask.setStatus(newStatus);
        taskList.put(id, tempTask);
    }

}
