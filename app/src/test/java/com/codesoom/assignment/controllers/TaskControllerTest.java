package com.codesoom.assignment.controllers;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("TaskController의 단위 테스트")
class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private final Long NOT_FOUND_TASK_ID = 100L; // 목록에 없는 할 일 ID
    private final Long NEW_TASK_ID = 1L; // 새로 생성할 할 일 ID

    private final String NEW_TASK_TITLE = "Test Title"; // 새로 생성할 할 일 제목
    private final String UPDATE_TASK_TITLE = "Test Title Updated"; // 수정된 할 일 제목
    private final String TASK_NOT_FOUND_ERROR_MESSAGE = "해당하는 Task가 존재하지 않습니다.";

    @BeforeEach
    void setUp(){
        taskService = new TaskService();
        controller = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("만약 목록이 비어있다면")
        class Context_empty {

            @Test
            @DisplayName("빈 할 일 목록을 반환합니다.")
            void it_return_emptyList() {

                List<Task> taskList = controller.list();

                Assertions.assertThat(taskList).isEmpty();
                Assertions.assertThat(taskList).hasSize(0);
            }

        }

        @Nested
        @DisplayName("만약 목록이 비어있지 않다면")
        class Context_not_empty {

            @BeforeEach
            void setUp() {
                Task task1 = new Task();
                Task task2 = new Task();
                taskService.saveTask(task1);
                taskService.saveTask(task2);
            }

            @Test
            @DisplayName("비어있지 않은 할 일 목록을 반환합니다.")
            void it_return_list() {
                List<Task> taskList = controller.list();
                Assertions.assertThat(taskList).isNotEmpty().hasSize(2);
            }

        }

    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 조회한다면")
        class Context_invalid_task_id {

            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                taskService.saveTask(newTask);
            }

            @Test
            @DisplayName("404 상태코드와 에러 메세지를 반환합니다.")
            void it_return_404_status_and_err_message(){
                Assertions.assertThatThrownBy( () -> controller.detail(NOT_FOUND_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(TASK_NOT_FOUND_ERROR_MESSAGE);
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 조회한다면")
        class Context_valid_task_id {

            private Long foundTaskId = NEW_TASK_ID; // 조회할 할 일 ID

            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                newTask.setTitle(NEW_TASK_TITLE);
                taskService.saveTask(newTask);
            }

            @Test
            @DisplayName("조회된 할 일을 반환합니다.")
            void it_return_found_task() {
                Task foundTask = controller.detail(foundTaskId);

                Assertions.assertThat(foundTask).isNotNull();
                Assertions.assertThat(foundTask.getId()).isEqualTo(foundTaskId);
                Assertions.assertThat(foundTask.getTitle()).isEqualTo(NEW_TASK_TITLE);
            }

        }

    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("만약 할 일 생성 요청을 보낸다면")
        class Context_valid_task_id {

            private Long newTaskId = NEW_TASK_ID; // 생성할 할 일 ID
            private Task paramTask = new Task();

            @BeforeEach
            void setUp() {
                paramTask.setTitle(NEW_TASK_TITLE);
            }

            @Test
            @DisplayName("생성된 할 일을 반환합니다.")
            void it_return_created_task() {
                Task createdTask = controller.create(paramTask);

                Assertions.assertThat(createdTask.getId()).isEqualTo(newTaskId);
                Assertions.assertThat(createdTask.getTitle()).isEqualTo(NEW_TASK_TITLE);
            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 수정한다면")
        class Context_invalid_task_id {

            private Long updateTaskId = NEW_TASK_ID; // 수정할 할 일 ID
            private Task paramTask = new Task(); // 파라미터로 사용될 할 일 객체

            @BeforeEach
            void setUp() {
                paramTask.setTitle(UPDATE_TASK_TITLE);
            }

            @Test
            @DisplayName("404 상태코드와 에러 메세지를 반환합니다.")
            void it_return_404_status_and_err_message() {
                Assertions.assertThatThrownBy( () -> controller.update(updateTaskId, paramTask))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(TASK_NOT_FOUND_ERROR_MESSAGE);
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 수정한다면")
        class Context_valid_task_id {

            private Long updateTaskId; // 수정할 할 일 ID
            private Task paramTask = new Task(); // 파라미터로 사용될 할 일 객체

            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                newTask.setTitle(NEW_TASK_TITLE);

                Task targetTask = controller.create(newTask);
                updateTaskId = targetTask.getId();

                paramTask.setTitle(UPDATE_TASK_TITLE);
            }

            @Test
            @DisplayName("수정된 할 일을 반환합니다.")
            void it_return_updated_task()  {
                Task updatedTask = controller.update(updateTaskId, paramTask);

                Assertions.assertThat(updatedTask).isNotNull();
                Assertions.assertThat(updatedTask.getId()).isEqualTo(updateTaskId);
                Assertions.assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TASK_TITLE);
            }

        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 삭제한다면")
        class Context_invalid_task_id {

            @Test
            @DisplayName("404 상태코드와 에러 메세지를 반환합니다.")
            void it_return_404_status_and_err_message() {
                Assertions.assertThatThrownBy( () -> controller.delete(NOT_FOUND_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(TASK_NOT_FOUND_ERROR_MESSAGE);
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 삭제한다면")
        class Context_valid_task_id {

            Long deleteTaskId;

            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                newTask.setTitle(NEW_TASK_TITLE);
                Task targetTask = controller.create(newTask);

                deleteTaskId = targetTask.getId(); // 삭제할 할 일 ID
            }

            @Test
            @DisplayName("204 상태코드를 반환합니다.")
            void it_return_and_204_status() {
                controller.delete(deleteTaskId);
                Assertions.assertThat(controller.list()).isEmpty();
            }

        }

    }

}