package de.metaphoriker.progress.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class TaskRepository {

    private final Set<Task> tasks = new HashSet<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public Task getTask(long id) {
        return tasks.stream().filter(task -> task.id() == id).findFirst().orElseThrow();
    }
}
