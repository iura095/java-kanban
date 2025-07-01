import java.util.Objects;

public class Task {
    private static int idcount = 1;
    private int taskID = 0;
    public String taskName;
    public String description;
    private TaskStatus status;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        status = TaskStatus.NEW;
        setTaskID(idcount);
        idcount++;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int newID) {
        taskID = newID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskID == task.taskID;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(taskID);
    }

    @Override
    public String toString() {
        return this.getClass() +"{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
