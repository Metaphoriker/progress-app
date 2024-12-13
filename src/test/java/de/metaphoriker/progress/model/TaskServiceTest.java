package de.metaphoriker.progress.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Task task = taskService.createTask("Task 1");
        taskService.add(task);
        verify(taskRepository).addTask(task);
        verify(taskDao).save(task);
    }

    @Test
    void delete_shouldRemoveTaskFromRepositoryAndDelete() {
        Task task = taskService.createTask("Task 1");
        taskService.delete(task);
        verify(taskRepository).removeTask(task);
        verify(taskDao).delete(task);
    }

    @Test
    void completeTask_shouldCompleteTaskAndUpdateRepositoryAndStorage() {
        Task task = taskService.createTask("Task 1");
        taskService.completeTask(task);

        verify(taskRepository).removeTask(task);
        verify(taskDao).delete(task);

        Task completedTask = task.complete();
        verify(taskRepository).addTask(completedTask);
        verify(taskDao).save(completedTask);
    }

    @Test
    void loadAll_shouldLoadTasksAndAddToRepository() {
        Task task1 = taskService.createTask("Task 1");
        Task task2 = taskService.createTask("Task 2").complete();
        when(taskDao.load()).thenReturn(List.of(task1, task2));

        taskService.loadAll();

        verify(taskRepository).addTask(task1);
        verify(taskRepository).addTask(task2);
    }

    @Test
    void isTaskComplete_shouldReturnTrue_whenTaskAndSubtasksAreComplete() {
        Task subTask1 = taskService.createTask("Subtask 1").complete();
        Task subTask2 = taskService.createTask("Subtask 2").complete();
        Task task = taskService.createTask("Task 1").complete();

        when(taskRepository.getTask(2L)).thenReturn(subTask1);
        when(taskRepository.getTask(3L)).thenReturn(subTask2);

        assertTrue(taskService.isTaskComplete(task));
    }

    @Test
    void isTaskComplete_shouldReturnFalse_whenTaskIsDoneButSubtaskIsNot() {
        Task subTask1 = taskService.createTask("Subtask 1");
        Task task = taskService.createTask("Task 1").complete();
        task.addSubTask(subTask1);

        when(taskRepository.getTask(1L)).thenReturn(subTask1);

        assertFalse(taskService.isTaskComplete(task));
    }

    @Test
    void isTaskComplete_shouldReturnFalse_whenTaskIsNotDone() {
        Task task = taskService.createTask("Task 1");
        assertFalse(taskService.isTaskComplete(task));
    }
}
