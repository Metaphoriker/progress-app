package de.metaphoriker.progress.model;

import java.util.List;

public record TaskDto(long id, String name, List<Long> subTasks, TaskState state) {
}
