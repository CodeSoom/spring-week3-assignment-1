package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService service;
    private Task newTask;

    private static final Long INVALID_ID = 100L;
    private static final String TASK_TITLE = "my first task";
    private static final String NEW_TASK_TITLE = "my new task";

    @BeforeEach
    void setUpService() {
        service = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        service.createTask(task);
    }

    @BeforeEach
    void setUpFixtures() {
        newTask = new Task();
        newTask.setTitle(NEW_TASK_TITLE);
    }

    @Test
    void getTasks() {
        List<Task> result = service.getTasks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithValidId() {
        Task taskInService = service.getTasks().get(0);

        Task result = service.getTask(taskInService.getId());

        assertThat(result.getTitle())
                .isEqualTo(taskInService.getTitle());
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> service.getTask(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTaskWithValidId() {
        Task taskInService = service.getTasks().get(0);

        Task result = service.updateTask(taskInService.getId(), newTask);

        assertThat(result.getTitle())
                .isEqualTo(NEW_TASK_TITLE);
    }

    @Test
    void updateTaskWithInvalidId() {
        assertThatThrownBy(() -> service.updateTask(INVALID_ID, newTask))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithValidId() {
        Task taskInService = service.getTasks().get(0);

        service.deleteTask(taskInService.getId());

        assertThat(service.getTasks()).isEmpty();
    }
}