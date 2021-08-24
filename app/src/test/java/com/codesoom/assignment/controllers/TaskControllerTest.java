package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;
    private Task newTask;

    private static final String TASK_TITLE = "my first task";
    private static final String NEW_TASK_TITLE = "my new task";

    @BeforeEach
    void setUpController() {
        Task task = new Task(1L, TASK_TITLE);

        taskService = new TaskService();
        taskService.createTask(task);

        controller = new TaskController(taskService);
    }

    @BeforeEach
    void setUpFixtures() {
        newTask = new Task(2L, NEW_TASK_TITLE);
    }

    @Test
    void list() {
        assertThat(controller.list()).hasSize(1);
    }

    @Test
    void detail() {
        Task result = controller.detail(1L);

        assertThat(result.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void create() {
        Task result = controller.create(newTask);

        assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
    }

    @Test
    void update() {
        Task taskInService = controller.list().get(0);

        Task result = controller.update(taskInService.getId(), newTask);

        assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
    }

    @Test
    void patch() {
        Task taskInService = controller.list().get(0);

        Task result = controller.patch(taskInService.getId(), newTask);

        assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
    }

    @Test
    void delete() {
        Task taskInService = controller.list().get(0);

        controller.delete(taskInService.getId());

        assertThat(controller.list()).isEmpty();
    }
}
