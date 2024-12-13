package de.metaphoriker.progress.model;

import java.util.List;

public record Task(long id, String name, List<Long> subTasks, TaskState state) {

    public boolean isComplete() {
        return state == TaskState.DONE;
    }

    public Task complete() {
        return new Task(id, name, subTasks, TaskState.DONE);
    }
}
