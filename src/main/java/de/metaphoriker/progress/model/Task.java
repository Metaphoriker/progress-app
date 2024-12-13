package de.metaphoriker.progress.model;

import java.time.Instant;
import java.util.List;

public record Task(long id, String description, List<Long> subTasks, TaskState state, Instant instant) {

    private static void validateDescription(String description) {
        if (description.length() > 250) {
            throw new IllegalArgumentException("Die Beschreibung darf maximal 250 Zeichen lang sein.");
        }
    }

    public Task {
        validateDescription(description);
    }

    public void addSubTask(Task task) {
        subTasks.add(task.id());
    }

    public void removeSubTask(Task task) {
        subTasks.remove(task.id());
    }

    public boolean isComplete() {
        return state == TaskState.DONE;
    }

    public Task complete() {
        return new Task(id, description, subTasks, TaskState.DONE, instant);
    }
}
