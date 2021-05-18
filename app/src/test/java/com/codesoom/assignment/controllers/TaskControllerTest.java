package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private static final String TASK_TITLE = "test";
    private static final String TASK_CREATE_PREFIX = "new";
    private static final String TASK_UPDATE_PREFIX = "fix";
    private static final Long EXIST_ID = 1L;
    private static final Long WRONG_ID = -1L;

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    @DisplayName("모든 Task 목록 요청")
    void getTaskList() {
        assertThat(taskController.list()).isNotNull();
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 상세정보 요청")
    void getDetailTaskWithValidId() {
        assertThat(taskController.detail(EXIST_ID).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 상세정보 요청")
    void getDetailTaskWithInvalidId() {
        assertThatThrownBy(() ->taskController.detail(WRONG_ID)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("새 Task 만들기 요청")
    void createNewTask() {
        Task task = new Task();
        task.setTitle(TASK_CREATE_PREFIX + TASK_TITLE);

        Task newTask = taskController.create(task);

        assertThat(taskController.list()).isNotEmpty();
        assertThat(taskController.list()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(newTask.getTitle()).isEqualTo(TASK_CREATE_PREFIX + TASK_TITLE);
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 수정 요청")
    void updateTaskWithValidId() {
        Task task = taskController.detail(EXIST_ID);
        task.setTitle(TASK_UPDATE_PREFIX + TASK_TITLE);
        Task updatedTask = taskController.update(EXIST_ID, task);

        assertThat(updatedTask.getTitle()).isEqualTo(TASK_UPDATE_PREFIX + TASK_TITLE);

        updatedTask.setTitle(TASK_TITLE);
        Task patchedTask = taskController.patch(EXIST_ID, updatedTask);

        assertThat(patchedTask.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 수정 요청")
    void updateTaskWithInvalidId() {
        assertThatThrownBy(() -> taskController.update(WRONG_ID, new Task())).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Valid Id 값으로 Task 삭제 요청")
    void deleteTaskWithValidId() {
        taskController.delete(EXIST_ID);
        assertThatThrownBy(() -> taskController.delete(EXIST_ID)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Invalid Id 값으로 Task 삭제 요청")
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(() -> taskController.delete(WRONG_ID)).isInstanceOf(TaskNotFoundException.class);
    }
}
