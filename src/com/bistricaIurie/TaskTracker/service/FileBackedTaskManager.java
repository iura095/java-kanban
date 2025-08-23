package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static String savePath = Paths.get("").toAbsolutePath() + "\\saves\\";
    private static String fileName = "Tasks.csv";
    private static final Path dir = Paths.get(savePath);
    private static final Path saveFile = Paths.get(savePath, fileName);

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

    public void save() {
        List<String> tasks = new ArrayList<>();

        for (Task task : getTaskList()) {
            tasks.add(task.toString());
        }

        for (Epic task : getEpicList()) {
            tasks.add(task.toString());
        }

        for (SubTask task : getSubTaskList()) {
            tasks.add(task.toString());
        }

        try {
            checkFileState();
        } catch (FileCeckException e) {
            throw new ManagerSaveException(e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + fileName))) {
            for (String task : tasks) {
                writer.write(task + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных.");
        }
    }

    public static FileBackedTaskManager loadFromFile() {
        FileBackedTaskManager newManager = new FileBackedTaskManager();

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
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    newManager.loadTask(new Task(Integer.parseInt(sLine[0]), sLine[2], sLine[4], status));
                } else if (sLine[1].equals(TaskType.SUBTASK.toString())) {
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    newManager.loadSubTask(new SubTask(Integer.parseInt(sLine[0]), sLine[2], sLine[4], status,
                            Integer.parseInt(sLine[5])));
                } else if (sLine[1].equals(TaskType.EPIC.toString())) {
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    newManager.loadEpic(new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[4], status));
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException(e.getMessage());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла сохранения.");
        }
        return newManager;
    }

    public void setSavePath(String savePath) {
        FileBackedTaskManager.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        FileBackedTaskManager.fileName = fileName;
    }

    public void loadTask(Task task) {
        super.addTask(task);
    }

    public void loadSubTask(SubTask task) {
        super.addSubTask(task);
    }

    public void loadEpic(Epic task) {
        super.addEpic(task);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask task) {
        super.addSubTask(task);
        save();
    }

    @Override
    public void addEpic(Epic task) {
        super.addEpic(task);
        save();
    }

    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void clearSubTaskList() {
        super.clearSubTaskList();
        save();
    }

    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
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
