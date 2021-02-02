package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {
    private static final String TASK_TITLE = "Test";
    private static final String ANOTHER_TASK_TITLE = "Test2";
    private static final String MODIFY_TASK_TITLE = "Modified";

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    /**
     * Test about "GET /tasks" request.
     * Check if task list is empty before add new task
     */
    @Test
    void listBeforeAdding() {
        assertThat(taskController.list()).isEmpty();
    }

    /**
     * Test about "GET /tasks/:id" request with valid id.
     * Check if the selected task has correct id and title.
     */
    @Test
    void detailWithValidId() {
        addTask(TASK_TITLE);

        assertThat(taskController.detail(1L).getId()).isEqualTo(1L);
        assertThat(taskController.detail(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    /**
     * Test about "GET /tasks/:id" request with invalid id.
     * Check retuning expected exception.
     */
    @Test
    void detailWithInvalidId() {
        assertThatThrownBy(() -> taskController.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    /**
     * Test about "POST /tasks" request.
     * After Add new tasks, check size of task list and last task fields
     */
    @Test
    void createNewTask() {
        Task task = addTask(TASK_TITLE);

        assertThat(taskController.list()).isNotEmpty();
        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo(TASK_TITLE);

        task.setTitle(ANOTHER_TASK_TITLE);
        taskController.create(task);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.list().get(1).getId()).isEqualTo(2L);
        assertThat(taskController.list().get(1).getTitle()).isEqualTo(ANOTHER_TASK_TITLE);
    }

    /**
     * Test about "PUT /tasks/:id" request.
     */
    @Test
    void updateTask() {
        Task task = addTask(TASK_TITLE);

        task.setTitle(MODIFY_TASK_TITLE);
        taskController.update(1L, task);

        assertThat(taskController.detail(1L).getTitle()).isNotEqualTo(TASK_TITLE);
        assertThat(taskController.detail(1L).getTitle()).isEqualTo(MODIFY_TASK_TITLE);
    }

    /**
     * Test about "PATCH /tasks/:id" request.
     */
    @Test
    void patchTask() {
        Task task = addTask(TASK_TITLE);

        task.setTitle(MODIFY_TASK_TITLE);
        taskController.patch(1L, task);

        assertThat(taskController.detail(1L).getTitle()).isNotEqualTo(TASK_TITLE);
        assertThat(taskController.detail(1L).getTitle()).isEqualTo(MODIFY_TASK_TITLE);
    }

    /**
     * Test about "DELETE /tasks/:id" request.
     * Compare size of task list before and after deletion.
     */
    @Test
    void deleteTask() {
        addTask(TASK_TITLE);
        assertThat(taskController.list()).hasSize(1);

        taskController.delete(1L);

        assertThat(taskController.list()).hasSize(0);
    }

    /**
     * Fixture method, to add new task at taskController and then return task.
     */
    private Task addTask(String taskTitle) {
        Task task = new Task();
        task.setTitle(taskTitle);
        taskController.create(task);

        return task;
    }
}
