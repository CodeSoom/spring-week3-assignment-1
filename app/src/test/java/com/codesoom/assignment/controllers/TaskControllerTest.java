package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    private static final Long VALID_ID = 1L;
    private static final Long NEW_VALID_ID = 2L;
    private static final Long INVALID_ID = 100L;
    private static final String TASK_TITLE = "my first task";
    private static final String NEW_TASK_TITLE = "my new task";

    private final Task originalTaskFixture = new Task(VALID_ID, TASK_TITLE);
    private final Task newTaskFixture = new Task(NEW_VALID_ID, NEW_TASK_TITLE);

    void setUpEmptyTaskService() {
        taskService = new TaskService();
        controller = new TaskController(taskService);
    }

    void setUpNotEmptyTaskService() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(originalTaskFixture);

        taskService = new TaskService(VALID_ID + 1, tasks);
        controller = new TaskController(taskService);
    }

    @Test
    void listWithEmptyTasks() {
        setUpEmptyTaskService();

        assertThat(controller.list()).hasSize(0);
    }

    @Test
    void listWithNotEmptyTasks() {
        setUpNotEmptyTaskService();

        assertThat(controller.list()).hasSize(1);
    }


    @Test
    void detailWithValidId() {
        setUpNotEmptyTaskService();

        Task result = controller.detail(VALID_ID);

        assertThat(result.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void detailWithInvalidId() {
        setUpNotEmptyTaskService();

        assertThatThrownBy(() -> controller.detail(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void create() {
        setUpEmptyTaskService();

        Task result = controller.create(newTaskFixture);

        assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
    }

    @Test
    void updateWithValidId() {
        setUpNotEmptyTaskService();

        Task result = controller.update(VALID_ID, newTaskFixture);

        assertThat(result.getId()).isEqualTo(VALID_ID);
        assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
    }

    @Test
    void updateWithInvalidId() {
        setUpNotEmptyTaskService();

        assertThatThrownBy(() -> controller.update(INVALID_ID, newTaskFixture))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void patchWithValidId() {
        setUpNotEmptyTaskService();

        Task result = controller.patch(VALID_ID, newTaskFixture);

        assertThat(result.getId()).isEqualTo(VALID_ID);
        assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
    }

    @Test
    void patchWithInvalidId() {
        setUpNotEmptyTaskService();

        assertThatThrownBy(() -> controller.patch(INVALID_ID, newTaskFixture))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteWithValidId() {
        setUpNotEmptyTaskService();

        controller.delete(VALID_ID);

        assertThat(controller.list()).isEmpty();
    }

    @Test
    void deleteWithInvalidId() {
        setUpNotEmptyTaskService();

        assertThatThrownBy(() -> controller.delete(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
