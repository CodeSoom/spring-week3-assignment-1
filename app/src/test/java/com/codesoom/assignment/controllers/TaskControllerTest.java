package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private static final String TEST_TASK_TITLE = "test";
    private static final Long INVALID_ID = 0L;
    private static final Long VALID_ID = 1L;

    TaskController taskController;
    TaskService taskService;

    void prepareTaskController() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    void prepareTestTask() {
        Task task = new Task();
        task.setTitle(TEST_TASK_TITLE);
        taskService.createTask(task);
    }

    @DisplayName("detail 메서드는")
    @Nested
    class Describe_detail {
        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
            }

            Task subject() {
                return taskController.detail(INVALID_ID);
            }

            @DisplayName("'Task를 찾을수 없다'는 예외를 던진다.")
            @Test
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> subject()).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
            }

            Task subject() {
                return taskController.detail(VALID_ID);
            }

            @DisplayName("해당 id의 Task를 리턴한다.")
            @Test
            void it_returns_task_of_that_id() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(TEST_TASK_TITLE);
                assertThat(result.getId()).isEqualTo(VALID_ID);
            }
        }
    }

    @DisplayName("update 메서드는")
    @Nested
    class Describe_update {

        private static final String UPDATE_TASK_TITLE = "updated_title";
        private Task task;

        void prepareTask() {
            task = new Task();
            task.setTitle(UPDATE_TASK_TITLE);
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
                prepareTask();
            }

            Task subject() {
                return taskController.update(INVALID_ID, task);
            }

            @DisplayName("'Task를 찾을수 없다'는 예외를 던진다.")
            @Test
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> subject()).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
                prepareTask();
            }

            Task subject() {
                return taskController.update(VALID_ID, task);
            }

            @DisplayName("해당 id의 Task를 업데이트하고 업데이트한 Task를 리턴한다.")
            @Test
            void it_returns_updated_task() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(UPDATE_TASK_TITLE);
                assertThat(result.getId()).isEqualTo(VALID_ID);
                assertThat(taskController.list().get(0).getTitle()).isEqualTo(UPDATE_TASK_TITLE);
                assertThat(taskController.list().get(0).getId()).isEqualTo(VALID_ID);
            }
        }
    }

    @DisplayName("patch 메서드는")
    @Nested
    class Describe_patch {

        private static final String UPDATE_TASK_TITLE = "updated_title";
        private Task task;

        void prepareTask() {
            task = new Task();
            task.setTitle(UPDATE_TASK_TITLE);
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
                prepareTask();
            }

            Task subject() {
                return taskController.patch(INVALID_ID, task);
            }

            @DisplayName("'Task를 찾을수 없다'는 예외를 던진다.")
            @Test
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> subject()).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
                prepareTask();
            }

            Task subject() {
                return taskController.patch(VALID_ID, task);
            }

            @DisplayName("해당 id의 Task를 업데이트하고 업데이트한 Task를 리턴한다.")
            @Test
            void it_returns_task_of_that_id() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(UPDATE_TASK_TITLE);
                assertThat(result.getId()).isEqualTo(VALID_ID);
                assertThat(taskController.list().get(0).getTitle()).isEqualTo(UPDATE_TASK_TITLE);
                assertThat(taskController.list().get(0).getId()).isEqualTo(VALID_ID);
            }
        }
    }

    @DisplayName("delete 메서드는")
    @Nested
    class Describe_delete {

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_invalid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
            }

            void subject() {
                taskController.delete(INVALID_ID);
            }

            @DisplayName("'Task를 찾을수 없다'는 예외를 던진다.")
            @Test
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> subject()).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_valid_id {

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
            }

            void subject() {
                taskController.delete(VALID_ID);
            }

            @DisplayName("해당 id의 Task를 삭제하고 아무것도 반환하지 않는다.")
            @Test
            void it_returns_nothing() {
                subject();
                assertThat(taskController.list()).hasSize(0);
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {
        @DisplayName("등록된 Task가 있다면")
        @Nested
        class Context_with_task {
            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareTestTask();
            }

            List<Task> subject() {
                return taskController.list();
            }

            @DisplayName("등록된 모든 Task의 List를 리턴한다.")
            @Test
            void it_returns_list_with_task() {
                List<Task> result = subject();
                assertThat(result).hasSize(1);
                assertThat(result.get(0).getTitle()).isEqualTo(TEST_TASK_TITLE);
            }
        }

        @DisplayName("등록된 Task가 하나도 없다면")
        @Nested
        class Context_without_task {
            @BeforeEach
            void prepare() {
                prepareTaskController();
            }

            List<Task> subject() {
                return taskController.list();
            }

            @DisplayName("비어있는 List를 리턴한다.")
            @Test
            void it_returns_empty_list() {
                List<Task> result = subject();
                assertThat(result).isEmpty();
            }
        }
    }

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {
        @DisplayName("'new' Task가 주어진다면")
        @Nested
        class Context_with_new_task {
            private static final String NEW_TASK_TITLE = "new";

            private Task task;

            void prepareNewTask() {
                task = new Task();
                task.setTitle(NEW_TASK_TITLE);
            }

            @BeforeEach
            void prepare() {
                prepareTaskController();
                prepareNewTask();
            }

            Task subject() {
                return taskController.create(task);
            }

            @DisplayName("'new' Task를 저장하고 반환한다.")
            @Test
            void it_returns_new_task() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
                assertThat(result.getId()).isEqualTo(1L);
                assertThat(taskController.list()).hasSize(1);
            }
        }
    }
}
