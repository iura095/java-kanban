package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.*;
import com.bistricaIurie.TaskTracker.model.error.FileCeckException;
import com.bistricaIurie.TaskTracker.model.error.ManagerSaveException;
import com.bistricaIurie.TaskTracker.model.error.TaskException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static String savePath = Paths.get("").toAbsolutePath() + "\\saves\\";
    private static String fileName = "Tasks.csv";
    private static final Path dir = Paths.get(savePath);
    private static final Path saveFile = Paths.get(savePath, fileName);
    private final Comparator<Task> comparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            }
            return 0;
        }
    };
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(comparator);

    private static void checkFileState() throws FileCeckException {
        try {
            if (Files.isDirectory(dir)) {
                if (!Files.isRegularFile(saveFile)) {
                    Files.createFile(saveFile);
                }
            } else {
                Files.createDirectory(dir);
            }
        } catch (Exception e) {
            throw new FileCeckException("Ошибка при проверке файла сохранения.");
        }
    }

    public boolean checkTaskIntersection(Task task) throws TaskException {
        try {
            return prioritizedTasks.stream()
                    .noneMatch(t -> task.getStartTime().isAfter(t.getStartTime()) &&
                            task.getStartTime().isBefore(t.getEndTime()) ||
                            task.getEndTime().isAfter(t.getStartTime()) &&
                                    task.getEndTime().isBefore(t.getEndTime()));
        } catch (NullPointerException e) {
            throw new TaskException("Не установленно время начало задачи.");
        }
    }

    public void save() {
        List<String> tasks = new ArrayList<>();

        getTaskList().forEach(
                t -> tasks.add(t.toString())
        );

        getEpicList().forEach(
                e -> {
                    tasks.add(e.toString());
                    e.getSubTaskList().forEach(
                            s -> tasks.add(s.toString())
                    );
                }
        );

        try {
            checkFileState();
        } catch (FileCeckException e) {
            throw new ManagerSaveException(e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + fileName))) {
            tasks.forEach(
                    t -> {
                        try {
                            writer.write(t + "\n");
                        } catch (IOException e) {
                            throw new ManagerSaveException("Ошибка сохранения данных.");
                        }
                    }
            );
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных.");
        }
    }

    public static FileBackedTaskManager loadFromFile() {
        FileBackedTaskManager newManager = new FileBackedTaskManager();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        try {
            checkFileState();
        } catch (FileCeckException e) {
            throw new ManagerSaveException(e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(savePath + fileName))) {
            String line;
            TaskStatus status = TaskStatus.NEW;
            while ((line = reader.readLine()) != null) {
                String[] sLine = line.split(",");
                if (sLine[1].equals(TaskType.TASK.toString())) {
                    newManager.setTaskCount(Integer.parseInt(sLine[0]) - 1);
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    if (sLine[6].equals("null")) {
                        newManager.loadTask(
                                new Task(Integer.parseInt(sLine[0]),
                                        sLine[2],
                                        sLine[4],
                                        status,
                                        Duration.ofMinutes(Long.parseLong(sLine[5]))
                                ));
                    } else {
                        newManager.loadTask(
                                new Task(Integer.parseInt(sLine[0]),
                                        sLine[2],
                                        sLine[4],
                                        status,
                                        Duration.ofMinutes(Long.parseLong(sLine[5])),
                                        LocalDateTime.parse(sLine[6], formatter)
                                ));
                    }
                } else if (sLine[1].equals(TaskType.SUBTASK.toString())) {
                    newManager.setTaskCount(Integer.parseInt(sLine[0]) - 1);
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    if (sLine[6].equals("null")) {
                        newManager.loadSubTask(
                                new SubTask(Integer.parseInt(sLine[0]),
                                        sLine[2],
                                        sLine[4],
                                        status,
                                        Integer.parseInt(sLine[7]),
                                        Duration.ofMinutes(Long.parseLong(sLine[5]))
                                ));
                    } else {
                        newManager.loadSubTask(
                                new SubTask(Integer.parseInt(sLine[0]),
                                        sLine[2],
                                        sLine[4],
                                        status,
                                        Integer.parseInt(sLine[7]),
                                        Duration.ofMinutes(Long.parseLong(sLine[5])),
                                        LocalDateTime.parse(sLine[6], formatter)
                                ));
                    }
                } else if (sLine[1].equals(TaskType.EPIC.toString())) {
                    newManager.setTaskCount(Integer.parseInt(sLine[0]) - 1);
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    newManager.loadEpic(
                            new Epic(Integer.parseInt(sLine[0]),
                                    sLine[2],
                                    sLine[4]
                            ));
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException(e.getMessage());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла сохранения.");
        }
        return newManager;
    }

    public void prioritizeTask(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void setSavePath(String savePath) {
        FileBackedTaskManager.savePath = savePath;
    }

    public String getSavePath() {
        return savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        FileBackedTaskManager.fileName = fileName;
    }

    public void loadTask(Task task) {
        super.setTaskCount(task.getTaskID() - 1);
        super.addTask(task);
        prioritizeTask(task);
    }

    public void loadSubTask(SubTask task) {
        super.setTaskCount(task.getTaskID() - 1);
        super.addSubTask(task);
        prioritizeTask(task);
    }

    public void loadEpic(Epic task) {
        super.setTaskCount(task.getTaskID() - 1);
        super.addEpic(task);
    }

    @Override
    public void addTask(Task task) {
        if (checkTaskIntersection(task)) {
            super.addTask(task);
            save();
            prioritizeTask(task);
        }
    }

    @Override
    public void addSubTask(SubTask task) throws TaskException {
        if (checkTaskIntersection(task)) {
            super.addSubTask(task);
            save();
            prioritizeTask(task);
        }
    }

    @Override
    public void addEpic(Epic task) {
        super.addEpic(task);
        save();
    }

    @Override
    public void clearTaskList() {
        getTaskList().forEach(
                t -> prioritizedTasks.remove(t)
        );
        super.clearTaskList();
        save();
    }

    @Override
    public void clearSubTaskList() {
        getSubTaskList().forEach(
                s -> prioritizedTasks.remove(s)
        );
        super.clearSubTaskList();
        save();
    }

    @Override
    public void clearEpicList() {
        getEpicList().forEach(
                e -> {
                    e.getSubTaskList().forEach(
                            s -> prioritizedTasks.remove(s)
                    );
                }
        );
        super.clearEpicList();
        save();
    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(task);
        if (checkTaskIntersection(task)) {
            super.updateTask(task);
            save();
            prioritizeTask(task);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        prioritizedTasks.remove(subTask);
        if (checkTaskIntersection(subTask)) {
            super.updateSubTask(subTask);
            save();
            prioritizeTask(subTask);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(Integer id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void setNewTaskStatus(Integer id, TaskStatus newStatus) {
        super.setNewTaskStatus(id, newStatus);
        save();
    }

    @Override
    public void setNewSubTaskStatus(Integer id, TaskStatus newStatus) {
        super.setNewSubTaskStatus(id, newStatus);
        save();
    }

}
