package com.codesoom.assignment.service;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TaskServiceTest {

    public static final String TASK_TITLE = "Test Title";
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = new TaskRepository();
        taskService = new TaskService(taskRepository);
        Task task = new Task(1L, TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTaskList()).hasSize(1);
    }

    @Test
    void getTaskWithValidId() {
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TaskServiceTest.TASK_TITLE);
    }

    @Test
    void getTaskWithInValidId() {
        assertThatThrownBy(() -> taskService.getTask(100000L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTaskList().size();

        Task second = new Task(2L, "second");
        taskService.createTask(second);

        int newSize = taskService.getTaskList().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void updateTask() {

        String newTitle = "New Title";
        Task source = new Task(100L, newTitle);

        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(newTitle);

    }

}