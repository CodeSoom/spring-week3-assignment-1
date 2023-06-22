package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;
    private static final String TASK_TITLE = "title";

    @BeforeEach
    void setup() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("Task가 주어지면")
        class Context_with_a_task {

            private Task source;

            @BeforeEach
            void setup() {
                source = new Task();
                source.setTitle(TASK_TITLE);
            }

            @Test
            @DisplayName("Task List에 Task를 추가하고, 추가된 Task를 리턴한다")
            void it_creates_task_and_returns_created_task() {
                Task task = taskService.createTask(source);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
                assertThat(task.getId()).isNotNull();
            }

        }

    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {

        private Long taskId;

        @Nested
        @DisplayName("Task List에 존재하는 Task id가 주어지면")
        class Context_with_a_existing_task_id {

            @BeforeEach
            void setup() {
                Task source = new Task();
                source.setTitle(TASK_TITLE);
                taskId = taskService.createTask(source).getId();
            }

            @Test
            @DisplayName("id로 찾은 Task를 리턴한다")
            void it_returns_a_task_found_by_id() {
                Task task = taskService.getTask(taskId);
                assertThat(task.getId()).isEqualTo(taskId);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }

        }

        @Nested
        @DisplayName("Task List에 존재하지 않는 Task id가 주어지면")
        class Context_with_a_not_existing_task_id {

            @BeforeEach
            void setup() {
                taskId = 100L;
            }

            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_a_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(taskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("Task List에 추가된 Task가 있으면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("Task List를 리턴한다")
            void It_return_tasks() {
                assertThat(taskController.list()).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("Task List에 추가된 Task가 없으면")
        class Context_with_no_task {

            @BeforeEach
            void setUp() {
                taskService.deleteAllTasks();
            }

            @Test
            @DisplayName("비어있는 배열을 리턴한다")
            void It_return_empty_array() {
                assertThat(taskController.list()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {

        private Long taskId;
        private Task source;

        @Nested
        @DisplayName("Task List에 존재하는 Task id와 수정할 소스가 주어지면")
        class Context_with_existing_task_id_and_source_task {

            @BeforeEach
            void setup() {
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                taskId = taskService.createTask(task).getId();

                source = new Task();
                source.setTitle("new title");
            }

            @Test
            @DisplayName("id로 찾은 Task를 수정하고, 수정된 Task를 리턴한다")
            void It_returns_updated_task() {
                Task updatedTask = taskController.update(taskId, source);
                assertThat(updatedTask.getTitle()).isEqualTo("new title");
                assertThat(updatedTask.getId()).isEqualTo(taskId);
            }

        }

    }

    @Nested
    @DisplayName("patch 메서드는")
    class Describe_patch {

        private Long taskId;
        private Task source;

        @Nested
        @DisplayName("Task List에 존재하는 Task id와 수정할 소스가 주어지면")
        class Context_with_existing_task_id_and_source_task {

            @BeforeEach
            void setup() {
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                taskId = taskService.createTask(task).getId();

                source = new Task();
                source.setTitle("new title");
            }

            @Test
            @DisplayName("id로 찾은 Task를 수정하고, 수정된 Task를 리턴한다")
            void It_returns_updated_task() {
                Task updatedTask = taskController.patch(taskId, source);
                assertThat(updatedTask.getTitle()).isEqualTo("new title");
                assertThat(updatedTask.getId()).isEqualTo(taskId);
            }

        }

    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {

        private Long taskId;

        @Nested
        @DisplayName("Task List에 존재하는 Task id가 주어지면")
        class Context_with_a_existing_task_id {

            @BeforeEach
            void setup() {
                Task source = new Task();
                source.setTitle(TASK_TITLE);
                taskId = taskService.createTask(source).getId();
            }

            @Test
            @DisplayName("id로 찾은 Task를 삭제한다")
            void it_returns_a_task_found_by_id() {
                taskController.delete(taskId);

                assertThatThrownBy(() -> taskService.getTask(taskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

        @Nested
        @DisplayName("Task List에 존재하지 않는 Task id가 주어지면")
        class Context_with_a_not_existing_task_id {

            @BeforeEach
            void setup() {
                taskId = 100L;
            }

            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_a_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(taskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }

    }

}

