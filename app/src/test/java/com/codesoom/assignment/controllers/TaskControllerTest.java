package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private static final String FIRST_TASK_TITLE = "test1";
    private static final String SECOND_TASK_TITLE = "test2";
    private static final String UPDATE_POSTFIX = "!!!";

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        // Fixtures
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE);
        taskController.create(task);
    }

    @Test
    void list() {
        List<Task> tasks = new ArrayList<>();

        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo(FIRST_TASK_TITLE);
    }

    @Test
    void createNewTask() {
        int oldSize = taskController.list().size();

        // Create a new task
        Task task = new Task();
        task.setTitle(SECOND_TASK_TITLE);
        Task createdTask = taskController.create(task);

        int newSize = taskController.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
        assertThat(taskController.list().get(1).getId()).isEqualTo(2);
        assertThat(taskController.list().get(1).getTitle()).isEqualTo(SECOND_TASK_TITLE);
        assertThat(createdTask.getId()).isEqualTo(2L);
        assertThat(createdTask.getTitle()).isEqualTo(SECOND_TASK_TITLE);
    }

    @Test
    void detailWithInvalidId() {
        assertThatThrownBy(() -> taskController.detail(0L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void detailWithValidId() {
        assertThat(taskController.detail(1L).getId()).isEqualTo(1L);
        assertThat(taskController.detail(1L).getTitle()).isEqualTo(FIRST_TASK_TITLE);
    }

    @Test
    void updateWithValidId() {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        Task updatedTask = taskController.update(1L, task);

        assertThat(updatedTask.getId()).isEqualTo(1L);
        assertThat(updatedTask.getTitle()).isEqualTo(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo(FIRST_TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    void updateWithInvalidId() {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);

        assertThatThrownBy(() -> taskController.update(0L, task)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void patchWithValidId() {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        Task patchedTask = taskController.patch(1L, task);

        assertThat(patchedTask.getId()).isEqualTo(1L);
        assertThat(patchedTask.getTitle()).isEqualTo(FIRST_TASK_TITLE + UPDATE_POSTFIX);
        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo(FIRST_TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    void patchWithInvalidId() {
        Task task = new Task();
        task.setTitle(FIRST_TASK_TITLE + UPDATE_POSTFIX);

        assertThatThrownBy(() -> taskController.patch(0L, task)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteWithValidId() {
        taskController.delete(1L);

        assertThat(taskController.list()).hasSize(0);
    }

    @Test
    void deleteWithInvalidId() {
        assertThatThrownBy(() -> taskController.delete(0L)).isInstanceOf(TaskNotFoundException.class);
    }

}
