package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {

    private TaskController taskController;

    private static final String TITLE = "TEST_TITLE";

    private static final String UPDATE_TITLE = "UPDATE_TITLE";

    private static final Long DEFAULT_ID = 1L;

    private static final Long CREATE_ID = 2L;

    private static final Long INVALID_ID = 100L;

    @BeforeEach
    void setUp(){
        TaskService taskService = new TaskService();
        this.taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TITLE);
        taskController.create(task);
    }

    @Test
    void list() {
        assertThat(taskController.list()).hasSize(1);
    }

    @Test
    void detailWithValidId() {
        assertThat(taskController.detail(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(taskController.detail(DEFAULT_ID).getTitle()).isEqualTo(TITLE);
    }

    @Test
    void detailWithInvalidId() {
        assertThatThrownBy(() -> taskController.delete(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void create() {
        Task createTask = new Task();
        createTask.setTitle(TITLE);
        taskController.create(createTask);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.detail(CREATE_ID).getTitle()).isEqualTo(TITLE);
    }

    @Test
    void updateWithValidId() {
        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);
        taskController.update(DEFAULT_ID,updateTask);

        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.detail(DEFAULT_ID).getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    void updateWithInvalidId() {
        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);
        assertThatThrownBy(() -> taskController.update(INVALID_ID,updateTask))
                .isInstanceOf(TaskNotFoundException.class);

    }

    @Test
    void patchWithValidId() {
        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);
        taskController.update(DEFAULT_ID,updateTask);

        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.detail(DEFAULT_ID).getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    void patchWithInvalidId() {
        Task updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);
        assertThatThrownBy(() -> taskController.update(INVALID_ID,updateTask))
                .isInstanceOf(TaskNotFoundException.class);

    }

    @Test
    void deleteWithValidId() {
        Task createTask = new Task();
        createTask.setTitle(TITLE);
        taskController.create(createTask);

        assertThat(taskController.list()).hasSize(2);
        taskController.delete(CREATE_ID);
        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.detail(DEFAULT_ID)).isNotNull();
    }

    @Test
    void deleteWithInvalidId() {
        assertThatThrownBy(() -> taskController.delete(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }
}