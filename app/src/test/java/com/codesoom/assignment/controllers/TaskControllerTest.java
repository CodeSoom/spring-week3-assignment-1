package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskController.create(task);
    }

    @Test
    void list() {
        assertThat(taskController.list()).hasSize(1);
    }

    @Test
    void detail() {
        Task task = taskController.detail(EXISTING_ID);

        assertThat(task.getId()).isEqualTo(EXISTING_ID);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void detailWithNotExistingId() {
        assertThatThrownBy(() -> taskController.detail(NOT_EXISTING_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void create() {
        int oldSize = taskController.list().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskController.create(task);

        int newSize = taskController.list().size();
        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void update() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        taskController.update(EXISTING_ID, source);

        Task task = taskController.detail(EXISTING_ID);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    void updateWithNotExistingId() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        assertThatThrownBy(() -> taskController.update(NOT_EXISTING_ID, source))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void delete() {
        taskController.delete(EXISTING_ID);

        assertThatThrownBy(() -> taskController.delete(EXISTING_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteWithNotExistingId() {
        assertThatThrownBy(() -> taskController.delete(NOT_EXISTING_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void patch() { update();}

    @Test
    void patchWithNotExistId() {
        updateWithNotExistingId();
    }
}