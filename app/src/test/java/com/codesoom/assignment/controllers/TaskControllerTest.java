package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @DisplayName("할일 목록이 비었을 때 테스트")
    @Test
    void emptyListTest() {
        assertThat(taskController.list()).hasSize(0);
    }

    @DisplayName("할일 목록이 비어있지 않을 때 테스트")
    @Test
    void notEmptyListTest() {
        taskService.createTask(new Task());
        taskService.createTask(new Task());

        assertThat(taskController.list()).hasSize(2);
    }

    @DisplayName("유효한 `id`로 할일 가져오기 테스트")
    @Test
    void taskWithValidId() {
        Task task = new Task();
        task.setTitle("Task 1");

        taskService.createTask(task);

        assertThat(taskController.detail(1L).getTitle()).isEqualTo("Task 1");
    }

    @DisplayName("유효하지 않은 `id`로 할일 가져오기 테스트")
    @Test
    void taskWithInValidId() {
        Task task = new Task();
        task.setTitle("Task 1");

        taskService.createTask(task);

        assertThatThrownBy(() -> taskController.detail(100L).getTitle()).isInstanceOf(TaskNotFoundException.class);
    }
}
