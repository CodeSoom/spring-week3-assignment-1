package com.codesoom.assignment.services;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("TaskService의 단위 테스트")
class TaskServiceTest {

    private TaskService taskService;

    private final Long NOT_FOUND_TASK_ID = 100L; // 목록에 없는 할 일 ID
    private final Long NEW_TASK_ID = 1L; // 새로 생성할 할 일 ID

    private final String NEW_TASK_TITLE = "Test Title"; // 새로 생성할 할 일 제목
    private final String UPDATE_TASK_TITLE = "Test Title Updated"; // 수정된 할 일 제목

    @BeforeEach
    void setUp(){

        taskService = new TaskService();

    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {

        @Nested
        @DisplayName("만약 목록이 비어있다면")
        class Context_empty {

            @Test
            @DisplayName("빈 할 일 목록을 반환합니다.")
            void it_return_emptyList() {
                List<Task> taskList = taskService.getTaskList();

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
                List<Task> taskList = taskService.getTaskList();
                Assertions.assertThat(taskList).isNotEmpty().hasSize(2);
            }

        }

    }

    @Nested
    @DisplayName("findTaskOne 메소드는")
    class Describe_findTaskOne {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 조회한다면")
        class Context_invalid_task_id {

            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                taskService.saveTask(newTask);
            }

            @Test
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_task_not_found_exception() {
                Assertions.assertThatThrownBy( () -> taskService.findTaskOne(NOT_FOUND_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
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
                Task foundTask = taskService.findTaskOne(foundTaskId);

                Assertions.assertThat(foundTask).isNotNull();
                Assertions.assertThat(foundTask.getId()).isEqualTo(foundTaskId);
                Assertions.assertThat(foundTask.getTitle()).isEqualTo(NEW_TASK_TITLE);
            }

        }

    }

    @Nested
    @DisplayName("saveTask 메소드는")
    class Describe_saveTask {

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
                Task createdTask = taskService.saveTask(paramTask);

                Assertions.assertThat(createdTask.getId()).isEqualTo(newTaskId);
                Assertions.assertThat(createdTask.getTitle()).isEqualTo(NEW_TASK_TITLE);
            }

        }

    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

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
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_task_not_found_exception() {
                Assertions.assertThatThrownBy( () -> taskService.updateTask(updateTaskId, paramTask.getTitle()))
                        .isInstanceOf(TaskNotFoundException.class);
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

                Task targetTask = taskService.saveTask(newTask);
                updateTaskId = targetTask.getId();

                paramTask.setTitle(UPDATE_TASK_TITLE);
            }

            @Test
            @DisplayName("수정된 할 일을 반환합니다.")
            void it_return_updated_task() {
                Task updatedTask = taskService.updateTask(updateTaskId, paramTask.getTitle());

                Assertions.assertThat(updatedTask).isNotNull();
                Assertions.assertThat(updatedTask.getId()).isEqualTo(updateTaskId);
                Assertions.assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TASK_TITLE);
            }

        }

    }

    @Nested
    @DisplayName("removeTask 메소드는")
    class Describe_removeTask {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 삭제한다면")
        class Context_invalid_task_id {

            @Test
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_task_not_found_exception() {
                Assertions.assertThatThrownBy( () -> taskService.removeTask(NOT_FOUND_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 삭제한다면")
        class Context_valid_task_id {

            Long removedTaskId;

            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                newTask.setTitle(NEW_TASK_TITLE);
                Task targetTask = taskService.saveTask(newTask);

                removedTaskId = targetTask.getId(); // 삭제할 할 일 ID
            }

            @Test
            @DisplayName("제거한 할 일을 반환합니다.")
            void it_return_and_204_status() {
                Task deletedTask = taskService.removeTask(removedTaskId);
                Assertions.assertThat(deletedTask.getId()).isEqualTo(removedTaskId);
            }

        }

    }

}