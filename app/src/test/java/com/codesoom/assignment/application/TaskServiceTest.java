package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TaskService 클래스의")
class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메서드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("만약 1개의 할 일이 등록되어 있다면")
        class Context_with_one_task {
            private final String validTaskTitle = "test";

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("등록되어 있는 할 일 1개를 반환한다")
            void It_returns_one_task() {
                List<Task> tasks = taskService.getTasks();
                assertAll(
                        () -> assertThat(tasks).hasSize(1),
                        () -> assertThat(tasks.get(0)
                                              .getTitle()).isEqualTo(validTaskTitle));
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일을 조회한다면")
        class Context_with_valid_task {
            private final Long validTaskId = 1L;
            private final String validTaskTitle = "test";

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("해당하는 할 일을 반환한다")
            void It_returns_the_task() {
                Task task = taskService.getTask(validTaskId);
                assertThat(task.getTitle())
                        .isEqualTo(validTaskTitle);
            }
        }

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 조회한다면")
        class Context_with_invalid_id {
            private final Long invalidTaskId = 100L;

            @Test
            @DisplayName("할 일을 찾을 수 없다는 내용의 예외를 던진다")
            void It_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(invalidTaskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {

        @Nested
        @DisplayName("만약 새로운 할 일이 주어진다면")
        class Context_with_one_task {
            private final String validTaskTitle = "test";
            private final Long validTaskId = 1L;
            private Task task;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle(validTaskTitle);
            }

            @Test
            @DisplayName("새로운 할 일을 생성한다")
            void It_creates_one_task() {
                final int oldSize = taskService.getTasks()
                                               .size();

                Task newTask = taskService.createTask(task);

                int newSize = taskService.getTasks()
                                         .size();

                assertThat(newSize - oldSize)
                        .isEqualTo(1);
                assertThat(newTask.getId())
                        .isEqualTo(validTaskId);
                assertThat(newTask.getTitle())
                        .isEqualTo(validTaskTitle);
            }
        }
    }


    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일의 ID와 새로운 제목이 주어진다면")
        class Context_with_one_task {
            private final Long validTaskId = 1L;
            private final String validTaskTitle = "test";
            private final String updatePostfix = "!!!";

            @BeforeEach
            void prepareTasks() {
                Task task = new Task();
                String validTaskTitle = "test";
                task.setTitle(validTaskTitle);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("등록되어 있는 할 일의 제목을 새로운 제목으로 갱신한다")
            void It_updates_one_task() {
                Task source = new Task();
                source.setTitle(validTaskTitle + updatePostfix);

                taskService.updateTask(validTaskId, source);

                Task task = taskService.getTask(validTaskId);
                assertThat(task.getTitle())
                        .isEqualTo(validTaskTitle + updatePostfix);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("만약 등록되어 있는 할 일의 ID가 주어진다면")
        class Context_with_valid_task {
            private final Long validTaskId = 1L;

            @BeforeEach
            void prepareTasks() {
                final String validTaskTitle = "test";
                Task task = new Task();
                task.setTitle(validTaskTitle);

                taskService.createTask(task);
            }

            @Test
            @DisplayName("주어진 ID의 할 일을 제거한다")
            void It_deletes_one_task() {
                int oldSize = taskService.getTasks()
                                         .size();

                taskService.deleteTask(validTaskId);

                int newSize = taskService.getTasks()
                                         .size();

                assertEquals(1,
                             oldSize - newSize,
                             "할 일을 제거한 이후 할 일 개수가 1개 줄어든다");
            }
        }

        @Nested
        @DisplayName("만약 등록되어 있지 않은 할 일의 ID가 주어진다면")
        class Context_with_invalid_task {
            private final Long invalidTaskId = 100L;

            @BeforeEach
            void prepareTasks() {
                final String validTaskTitle = "test";
                Task task = new Task();
                task.setTitle(validTaskTitle);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void It_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(invalidTaskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
