package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        // fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        controller.create(task);
    }

    @Test
    void list() {
        assertThat(controller.list()).hasSize(1);
    }

    @Test
    void detail() {
        Task found = controller.detail(EXISTING_ID);

        assertThat(found.getId()).isEqualTo(EXISTING_ID);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void detailWithNotExistingId() {
        assertThatThrownBy(() -> controller.detail(NOT_EXISTING_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void create() {
        int oldSize = controller.list().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void update() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        controller.update(EXISTING_ID, source);

        Task task = controller.detail(EXISTING_ID);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    void updateWithNotExistingId() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        assertThatThrownBy(() -> controller.update(NOT_EXISTING_ID, source))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void delete() {
        controller.delete(EXISTING_ID);

        assertThatThrownBy(() -> controller.delete(EXISTING_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteWithNotExistingId() {
        assertThatThrownBy(() -> controller.delete(NOT_EXISTING_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void patch() {
        update();
    }

    @Test
    void patchWithNotExistId() {
        updateWithNotExistingId();
    }

}
