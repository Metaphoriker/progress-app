package de.metaphoriker.progress.model;

import com.google.inject.Inject;

import java.time.Instant;
import java.util.ArrayList;

public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskDao taskDao;

    @Inject
    public TaskService(TaskRepository taskRepository, TaskDao taskDao) {
        this.taskRepository = taskRepository;
        this.taskDao = taskDao;
    }

    public void add(Task task) {
        taskRepository.addTask(task);
        taskDao.save(task);
    }

    public void delete(Task task) {
        taskRepository.removeTask(task);
        taskDao.delete(task);
    }

    public void completeTask(Task task) {
        Task completedTask = task.complete();
        delete(task);
        add(completedTask);
    }

    public Task createTask(String description) {
        return new Task(taskDao.getLatestId() + 1, description, new ArrayList<>(), TaskState.OPEN, Instant.now());
    }

    public void loadAll() {
        taskDao.load().forEach(taskRepository::addTask);
    }

    public boolean isTaskComplete(Task task) {
        return task.isComplete() && areSubTasksComplete(task);
    }

    private boolean areSubTasksComplete(Task task) {
        return task.subTasks().stream().map(taskRepository::getTask).allMatch(this::isTaskComplete);
    }
}
