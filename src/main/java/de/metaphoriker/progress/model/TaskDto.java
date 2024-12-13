package de.metaphoriker.progress.model;

import java.util.List;

public record TaskDto(long id, String description, List<Long> subTasks, TaskState state) {
}
