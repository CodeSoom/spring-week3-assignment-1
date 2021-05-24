package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Nested
@DisplayName("TaskService 클래스의")
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        this.taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        private Task task;

        @BeforeEach
        void setUp() {
            Task src = new Task();
            src.setTitle("test");
            task = taskService.createTask(src);
        }

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 있다면")
        class Context_with_existing_task {

            @Test
            @DisplayName("할 일을 반환한다")
            void it_returns_a_task() {
                assertThat(taskService.getTask(task.getId()).getTitle())
                        .isEqualTo(task.getTitle());
            }
        }

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 없다면")
        class Context_with_no_task {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(10L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("할 일이 있다면")
        class Context_with_tasks {

            private Task task;

            @BeforeEach
            void setUp() {
                Task src = new Task();
                src.setTitle("test");
                task = taskService.createTask(src);
            }

            @Test
            @DisplayName("할 일 목록을 가져온다")
            void it_returns_all_tasks() {
                assertThat(taskService.getTasks()).isNotEmpty();
                assertThat(taskService.getTasks().get(0).getTitle())
                        .isEqualTo(task.getTitle());
            }
        }

        @Nested
        @DisplayName("할 일이 없다면")
        class Context_without_task {

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returns_empty_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {

        @Nested
        @DisplayName("할 일을 넘기면")
        class Context_with_task {

            private Task task;

            @BeforeEach
            void setUp() {
                task = new Task();
                task.setTitle("test");
            }

            @Test
            @DisplayName("할 일을 추가하고 반환한다")
            void it_returns_task() {
                assertThat(taskService.createTask(task).getTitle())
                        .isEqualTo(task.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 있다면")
        class Context_with_existing_task {

            private Task task;
            private Long taskId;

            @BeforeEach
            void setUp() {
                task = new Task();
                task.setTitle("test");
                taskId = taskService.createTask(task).getId();
            }

            @Test
            @DisplayName("할 일을 수정하고 반환한다")
            void it_returns_task() {
                String updatedTitle = "updated title";
                Task src = new Task();
                src.setTitle(updatedTitle);
                assertThat(taskService.updateTask(taskId, src).getTitle())
                        .isEqualTo(src.getTitle());
            }
        }

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 없다면")
        class Context_without_task {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_throws_TaskNotFoundException() {
                String updatedTitle = "updated title";
                Task src = new Task();
                src.setTitle(updatedTitle);
                assertThatThrownBy(() -> taskService.updateTask(1L, src))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }


    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 있다면")
        class Context_with_task {

            private Task task;
            private Long taskId;

            @BeforeEach
            void setUp() {
                task = new Task();
                task.setTitle("test");
                taskId = taskService.createTask(task).getId();
            }

            @Test
            @DisplayName("할 일을 삭제하고 반환한다")
            void it_returns_task() {
                assertThat(taskService.deleteTask(taskId).getId())
                        .isEqualTo(taskId);
            }
        }

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 없다면")
        class Context_without_task {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.deleteTask(1L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
