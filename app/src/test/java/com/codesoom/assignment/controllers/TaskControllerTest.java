package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class TaskControllerTest {

    private static final Long GIVEN_ID = 1L;
    private static final Long NOT_FOUND_ID = 0L;

    private static final String GIVEN_TITLE = "test";
    private static final String UPDATE_TITLE = "test-update";

    private TaskController taskController;

    private TaskService taskService;

    private Task task;

    private Task updateTask;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        task = new Task();
        task.setTitle(GIVEN_TITLE);

        updateTask = new Task();
        updateTask.setTitle(UPDATE_TITLE);

        taskController.create(task);
    }

    @DisplayName("할일 목록을 조회한다")
    @Test
    void list() {
        assertThat(taskController.list()).hasSize(1);
    }

    @DisplayName("할일을 조회한다")
    @Test
    void detail() {
        Task task = taskController.detail(GIVEN_ID);

        assertThat(task.getId()).isEqualTo(GIVEN_ID);
        assertThat(task.getTitle()).isEqualTo(GIVEN_TITLE);
    }

    @DisplayName("존재하지 않는 할일을 조회할시 예외가 발생한다")
    @Test
    void detail_with_not_exist_id() {
        assertThatExceptionOfType(TaskNotFoundException.class)
                .isThrownBy(() -> taskController.detail(NOT_FOUND_ID));
    }

    @DisplayName("할 일 생성하기")
    @Test
    void create() {
        int beforeTaskSize = taskController.list().size();

        taskController.create(task);

        int afterTaskSize = taskService.getTasks().size();
        assertThat(afterTaskSize - beforeTaskSize).isEqualTo(1);
    }

    @DisplayName("할 일 갱신하기")
    @Test
    void update() {
        Task expect = updateTask;

        taskController.update(GIVEN_ID, expect);

        Task actual = taskController.detail(GIVEN_ID);
        assertThat(actual.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @DisplayName("할 일 갱신하기")
    @Test
    void patch() {
        Task expect = updateTask;

        taskController.patch(GIVEN_ID, expect);

        Task actual = taskController.detail(GIVEN_ID);
        assertThat(actual.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @DisplayName("존재하지 않는 할 일 갱신하기")
    @Test
    void update_with_not_exist_id() {
        Task expect = updateTask;

        assertThatExceptionOfType(TaskNotFoundException.class)
                .isThrownBy(() -> taskController.update(NOT_FOUND_ID, expect));
    }

    @DisplayName("존재하지 않는 할 일 갱신하기")
    @Test
    void patch_with_not_exist_id() {
        Task expect = updateTask;

        assertThatExceptionOfType(TaskNotFoundException.class)
                .isThrownBy(() -> taskController.patch(NOT_FOUND_ID, expect));
    }

    @DisplayName("할 일 삭제하기")
    @Test
    void delete() {
        taskController.delete(GIVEN_ID);

        assertThat(taskController.list()).isEmpty();
    }

    @DisplayName("존재하지 않는 할 일 삭제하기")
    @Test
    void delete_with_not_exist_id() {
        assertThatExceptionOfType(TaskNotFoundException.class)
                .isThrownBy(() -> taskController.delete(NOT_FOUND_ID));
    }
}
