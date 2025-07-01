public class SubTask extends Task {

    public String whichEpic;

    public SubTask(String name, String description, String whichEpic) {
        super(name, description);
        this.whichEpic = whichEpic;
    }

    public String getWhichEpic() {
        return whichEpic;
    }

    @Override
    public String toString() {
        return this.getClass() +"{" +
                "taskID=" + this.getTaskID() +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + this.getStatus() + '\'' +
                ", wichEpic='" + whichEpic + '\'' +
                '}';
    }
}