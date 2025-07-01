import java.util.HashMap;

public class Epic extends Task {

    public HashMap<Integer, SubTask> subTaskList;

    public Epic(String name, String description, HashMap<Integer, SubTask> subTaskList) {
        super(name, description);
        this.subTaskList = subTaskList;
    }

    public void getSubTaskList() {
        System.out.println("В " + this.taskName + " содержаться:");
        for (Integer i : subTaskList.keySet()) {
            System.out.println(subTaskList.get(i).taskName + " , Status = " + subTaskList.get(i).getStatus());
        }
    }

    public void checkEpicStatus() {
        int doneStatusCount = 0;
        int newStatusCount = 0;
        for (SubTask sb : subTaskList.values()) {
            if (sb.getStatus() == TaskStatus.IN_PROGRESS) {
                this.setStatus(TaskStatus.IN_PROGRESS);
                break;
            } else if (sb.getStatus() == TaskStatus.DONE) {
                doneStatusCount++;
            } else if (sb.getStatus() == TaskStatus.NEW) {
                newStatusCount++;
            }
        }

        if (doneStatusCount == subTaskList.size()) {
            this.setStatus(TaskStatus.DONE);
        }

        if (newStatusCount == subTaskList.size()) {
            this.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public String toString() {
        return this.getClass() +"{" +
                "taskID=" + this.getTaskID() +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + this.getStatus() + '\'' +
                '}';
    }
}
