package com.bistricaIurie.TaskTracker.service;

import com.bistricaIurie.TaskTracker.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String savePath = "C:\\Users\\Iura\\AppData\\Local\\Temp\\java-kanban";
    private String fileName = "Tasks.csv";
    private final Path dir = Paths.get(savePath);
    private final Path saveFile = Paths.get(savePath, fileName);

    private void checkFileState() throws FileCeckException {
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
            Files.deleteIfExists(saveFile);
            Files.createFile(saveFile);
            Files.write(saveFile, tasks);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения данных.");
        } catch (FileCeckException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadFromFile() {
        try {
            checkFileState();
            List<String> lines = Files.readAllLines(saveFile);
            clearEpicList();
            clearTaskList();
            clearSubTaskList();
            setTaskCount(0);
            for (String line : lines) {
                String[] sLine = line.split(",");
                if (sLine[1].equals(TaskType.TASK.toString())) {
                    TaskStatus status = TaskStatus.NEW;
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    Task task = new Task(Integer.parseInt(sLine[0]), sLine[2], sLine[4], status);
                    addTask(task);
                } else if (sLine[1].equals(TaskType.SUBTASK.toString())) {
                    TaskStatus status = TaskStatus.NEW;
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    SubTask task = new SubTask(Integer.parseInt(sLine[0]), sLine[2], sLine[4], status,
                            Integer.parseInt(sLine[5]));
                    addSubTask(task);
                } else if (sLine[1].equals(TaskType.EPIC.toString())) {
                    TaskStatus status = TaskStatus.NEW;
                    status = switch (sLine[3]) {
                        case "NEW" -> TaskStatus.NEW;
                        case "DONE" -> TaskStatus.DONE;
                        case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
                        default -> status;
                    };
                    Epic task = new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[4], status);
                    addEpic(task);
                }
            }
        } catch (FileCeckException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла сохранения.");
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
