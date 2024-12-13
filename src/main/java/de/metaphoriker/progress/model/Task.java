package de.metaphoriker.progress.model;

import java.util.List;

public record Task(long id, String description, List<Long> subTasks, TaskState state) {

    private static void validateName(String description) {
        if (description.length() > 250) {
            throw new IllegalArgumentException("Die Beschreibung darf maximal 250 Zeichen lang sein.");
        }
    }

    public Task {
        validateName(description);
    }

    public boolean isComplete() {
        return state == TaskState.DONE;
    }

    public Task complete() {
        return new Task(id, description, subTasks, TaskState.DONE);
    }
}
