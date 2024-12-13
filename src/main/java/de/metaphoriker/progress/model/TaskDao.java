package de.metaphoriker.progress.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.metaphoriker.progress.model.persistence.TaskAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TaskDao {

    private final static Path TASK_FOLDER = Paths.get(System.getenv("APPDATA"), "progress-app" + File.separator + "tasks");

    private final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter()).create();

    static {
        try {
            if (!Files.exists(TASK_FOLDER)) {
                Files.createDirectories(TASK_FOLDER);
            }
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Erstellen des Task-Verzeichnisses", e);
        }
    }

    public void save(Task task) {
        saveToFile(task);
    }

    public void delete(Task task) {
        deleteFile(task);
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try (Stream<Path> files = Files.list(TASK_FOLDER)) {
            files.filter(file -> file.toString().endsWith(".json"))
                    .forEach(file -> tasks.add(loadTaskFromFile(file)));
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden der Tasks", e);
        }
        return tasks;
    }

    public long getLatestId() {
        try (Stream<Path> files = Files.list(TASK_FOLDER)) {
            return files.filter(file -> file.toString().endsWith(".json"))
                    .mapToLong(file -> Long.parseLong(file.getFileName().toString().replace(".json", "")))
                    .max()
                    .orElse(0);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Ermitteln der höchsten ID", e);
        }
    }

    private Task loadTaskFromFile(Path file) {
        try {
            String json = Files.readString(file);
            return GSON.fromJson(json, Task.class);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden der Task aus Datei: " + file.getFileName(), e);
        }
    }

    private void saveToFile(Task task) {
        Path taskFile = createFile(task);
        try {
            Files.writeString(taskFile, GSON.toJson(task));
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der Task in Datei: " + taskFile.getFileName(), e);
        }
    }

    private Path createFile(Task task) {
        Path file = TASK_FOLDER.resolve(task.id() + ".json");
        try {
            return Files.createFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Erstellen der Datei: " + file.getFileName(), e);
        }
    }

    private void deleteFile(Task task) {
        Path taskFile = TASK_FOLDER.resolve(task.id() + ".json");
        try {
            Files.delete(taskFile);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Löschen der Datei: " + taskFile.getFileName(), e);
        }
    }
}