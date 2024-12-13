package de.metaphoriker.progress.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskDao taskDao;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskDao = Mockito.mock(TaskDao.class);
        taskService = new TaskService(taskRepository, taskDao);
    }

    @Test
    void add_shouldAddTaskToRepositoryAndSave() {
        Task task = new Task(1, "Task 1", List.of(), TaskState.OPEN);
        taskService.add(task);
        verify(taskRepository).addTask(task);
        verify(taskDao).save(task);
    }

    @Test
    void delete_shouldRemoveTaskFromRepositoryAndDelete() {
        Task task = new Task(1, "Task 1", List.of(), TaskState.OPEN);
        taskService.delete(task);
        verify(taskRepository).removeTask(task);
        verify(taskDao).delete(task);
    }

    @Test
    void completeTask_shouldCompleteTaskAndUpdateRepositoryAndStorage() {
        Task task = new Task(1, "Task 1", List.of(), TaskState.OPEN);
        taskService.completeTask(task);

        verify(taskRepository).removeTask(task);
        verify(taskDao).delete(task);

        Task completedTask = new Task(1, "Task 1", List.of(), TaskState.DONE);
        verify(taskRepository).addTask(completedTask);
        verify(taskDao).save(completedTask);
    }

    @Test
    void loadAll_shouldLoadTasksAndAddToRepository() {
        Task task1 = new Task(1, "Task 1", List.of(), TaskState.OPEN);
        Task task2 = new Task(2, "Task 2", List.of(), TaskState.DONE);
        when(taskDao.load()).thenReturn(List.of(task1, task2));

        taskService.loadAll();

        verify(taskRepository).addTask(task1);
        verify(taskRepository).addTask(task2);
    }

    @Test
    void isTaskComplete_shouldReturnTrue_whenTaskAndSubtasksAreComplete() {
        Task subTask1 = new Task(2, "Subtask 1", List.of(), TaskState.DONE);
        Task subTask2 = new Task(3, "Subtask 2", List.of(), TaskState.DONE);
        Task task = new Task(1, "Task 1", List.of(2L, 3L), TaskState.DONE);

        when(taskRepository.getTask(2L)).thenReturn(subTask1);
        when(taskRepository.getTask(3L)).thenReturn(subTask2);

        assertTrue(taskService.isTaskComplete(task));
    }

    @Test
    void isTaskComplete_shouldReturnFalse_whenTaskIsDoneButSubtaskIsNot() {
        Task subTask1 = new Task(2, "Subtask 1", List.of(), TaskState.OPEN);
        Task task = new Task(1, "Task 1", List.of(2L), TaskState.DONE);

        when(taskRepository.getTask(2L)).thenReturn(subTask1);

        assertFalse(taskService.isTaskComplete(task));
    }

    @Test
    void isTaskComplete_shouldReturnFalse_whenTaskIsNotDone() {
        Task task = new Task(1, "Task 1", List.of(), TaskState.OPEN);
        assertFalse(taskService.isTaskComplete(task));
    }
}
