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
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("TaskController 클래스의")
class TaskControllerTest {
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        final TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("listTasks 메서드는")
    class Describe_listTasks {

        @Nested
        @DisplayName("만약 등록되어 있는 전체 할 일 목록을 조회 요청한다면")
        class Context_with_one_task {
            private final String validTaskTitle = "test";
            private final Long validTaskId = 1L;

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskController.create(task);
            }

            @Test
            @DisplayName("등록되어 있는 할 일 1개를 응답한다")
            void It_returns_one_task() {
                assertAll(
                        () -> assertThat(taskController.list()).hasSize(1),
                        () -> assertThat(taskController.detail(validTaskId)
                                                       .getTitle()).isEqualTo(validTaskTitle));
            }
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일 1개를 조회 요청한다면")
        class Context_with_valid_task {
            private final String validTaskTitle = "test";
            private final Long validTaskId = 1L;

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskController.create(task);
            }

            @Test
            @DisplayName("해당하는 할 일을 응답한다")
            void It_returns_the_task() {
                assertThat(taskController.detail(validTaskId)
                                         .getTitle()).isEqualTo(validTaskTitle);
            }
        }

        @Nested
        @DisplayName("만약 등록되어 있지 않는 할 일 1개를 조회 요청한다면")
        class Context_with_invalid_task {
            private final int totalTaskCound = 2;
            private final Long invalidTaskId = totalTaskCound + 1L;

            @BeforeEach
            void prepareTasks() {
                for (Long id = 1L; id <= totalTaskCound; id++) {
                    System.out.println(id);
                    Task task = new Task();
                    task.setTitle("test" + id);
                    taskController.create(task);
                }
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 내용의 예외를 던진다")
            void It_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> taskController.detail(invalidTaskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("만약 새로운 할 일을 등록 요청한다면")
        class Context_with_valid_task {
            private final String validTaskTitle = "test1";
            private final Long additionalValidTaskId = 2L;
            private final String additionalValidTaskTitle = "test2";
            private final Integer expectedSize = 2;
            private Task newTask = new Task();

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskController.create(task);

                newTask.setTitle(additionalValidTaskTitle);
            }

            @Test
            @DisplayName("새로운 할 일을 생성 후 생성한 할 일을 응답한다")
            void It_creates_one_task() {
                taskController.create(newTask);

                assertAll(
                        () -> assertThat(taskController.list()).hasSize(expectedSize),
                        () -> assertThat(taskController.detail(additionalValidTaskId)
                                                       .getTitle()).isEqualTo(additionalValidTaskTitle));
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일의 ID와 새로운 제목이 주어진다면")
        class Context_with_valid_task {
            private final String validTaskTitle = "test1";
            private final String additionalValidTaskTitle = "test2";
            private final Long validTaskId = 1L;
            private Task newTask = new Task();

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskController.create(task);

                newTask.setTitle(additionalValidTaskTitle);
            }

            @Test
            @DisplayName("지정한 할 일의 제목을 새로운 제목으로 갱신 후 응답한다")
            void It_updates_one_task() {
                taskController.update(validTaskId, newTask);

                assertThat(taskController.detail(validTaskId)
                                         .getTitle()).isEqualTo(additionalValidTaskTitle);
            }
        }
    }

    @Nested
    @DisplayName("patch 메서드는")
    class Describe_patch {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일의 ID와 새로운 제목이 주어진다면")
        class Context_with_one_task {
            private final String validTaskTitle = "test1";
            private final String additionalValidTaskTitle = "test2";
            private final Long validTaskId = 1L;
            private Task newTask = new Task();

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskController.create(task);

                newTask.setTitle(additionalValidTaskTitle);
            }

            @Test
            @DisplayName("지정한 할 일의 제목을 새로운 제목으로 갱신 후 응답한다")
            void It_updates_one_task() {
                taskController.patch(validTaskId, newTask);

                assertThat(taskController.detail(validTaskId)
                                         .getTitle()).isEqualTo(additionalValidTaskTitle);
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일의 ID가 주어진다면")
        class Context_with_task_id {
            private final Long validTaskId = 1L;

            @BeforeEach
            void prepareTasks() {
                final String validTaskTitle = "test";
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskController.create(task);
            }

            @Test
            @DisplayName("주어진 ID의 할 일을 삭제한다")
            void It_deletes_one_task() {
                taskController.delete(validTaskId);

                assertThat(taskController.list()).isEmpty();
            }
        }
    }
}
