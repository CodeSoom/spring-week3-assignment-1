package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("할 일에 대한 유닛 테스트")
class TaskControllerTest {

    private static final String TEST_TASK_TITLE = "테스트";
    private static final String TEST_TASK_UPDATE_TITLE_POSTFIX = "_수정";

    private static final Long NOT_FOUND_TASK_ID = 9999L;

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("할 일 목록 조회")
    void list() {
        //given
        int taskCount = 10;
        IntStream.rangeClosed(1, taskCount)
                .forEach((index) -> generateTask(String.format("%s_%s", TEST_TASK_TITLE, index)));

        //when
        List<Task> list = taskController.list();

        //then
        assertThat(list).hasSize(taskCount);
    }

    @Test
    @DisplayName("할 일 상세 조회")
    void detail() {
        //given
        final Task savedTask = generateTask(TEST_TASK_TITLE);

        //when
        Task foundTask = taskController.detail(savedTask.getId());

        //then
        assertThat(foundTask).isEqualTo(savedTask);
    }

    @Test
    @DisplayName("존재하지 않는 할 일 상세 조회 시 예외 발생")
    void detail_fail() {
        assertThatThrownBy(
                () -> taskController.detail(NOT_FOUND_TASK_ID)
        ).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 변경")
    void patch() {
        //given
        final Task givenTask = generateTask(TEST_TASK_TITLE);
        final Long taskId = givenTask.getId();

        String title = TEST_TASK_TITLE + TEST_TASK_UPDATE_TITLE_POSTFIX;
        Task source = new Task();
        source.setTitle(title);

        //when
        Task updatedTask = taskController.patch(taskId, source);

        //then
        assertAll(
                () -> assertThat(updatedTask).isEqualTo(givenTask),
                () -> assertThat(updatedTask.getTitle()).isEqualTo(title)
        );
    }

    @Test
    @DisplayName("존재하지 않는 할 일 변경 시 예외 발생")
    void patch_fail() {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        //when
        //then
        assertThatThrownBy(
                () -> taskController.patch(NOT_FOUND_TASK_ID, source)
        ).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 대체")
    void update() {
        //given
        final Task task = generateTask(TEST_TASK_TITLE);
        final Long taskId = task.getId();

        String updateTitle = "TODO!!@@";
        Task source = new Task();
        source.setTitle(updateTitle);

        //when
        Task updatedTask = taskController.update(taskId, source);

        //then
        assertThat(updatedTask.getTitle()).isEqualTo(updateTitle);
    }

    @Test
    @DisplayName("존재하지 않는 할 일 대체 시 예외 발생")
    void update_fail() {
        //given
        Task source = new Task();

        //when
        //then
        assertThatThrownBy(
                () -> taskController.update(NOT_FOUND_TASK_ID, source)
        ).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 등록")
    void save() {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        //when
        Task savedTask = taskController.create(source);

        //then
        assertAll(
                () -> assertThat(savedTask.getId()).isNotNull(),
                () -> assertThat(savedTask.getTitle()).isEqualTo(TEST_TASK_TITLE)
        );
    }

    @Test
    @DisplayName("할 일 삭제")
    void delete() {
        //given
        final Task task = generateTask(TEST_TASK_TITLE);
        final Long taskId = task.getId();

        //when
        //then
        assertDoesNotThrow(
                () -> taskController.delete(taskId)
        );
    }

    @Test
    @DisplayName("존재하지 않는 할 일 대체 시 예외 발생")
    void delete_fail() {
        assertThatThrownBy(() -> taskController.detail(NOT_FOUND_TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    private Task generateTask(String title) {
        Task source = new Task();
        source.setTitle(title);
        return taskController.create(source);
    }
}