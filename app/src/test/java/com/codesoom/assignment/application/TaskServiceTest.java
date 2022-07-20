package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("TaskService 테스트")
class TaskServiceTest {
    public static final String EXAMPLE_TITLE = "title";
    private TaskService service;

    @BeforeEach
    void setup() {
        service = new TaskService();
    }

    @Nested
    @DisplayName("task가 생성되지 않았을 때")
    class Given_didNotCreateTask {
        private List<Task> tasks;

        @Nested
        @DisplayName("getTask() 요청하면")
        class When_getTasks {
            @BeforeEach
            public void when() {
                tasks = service.getTasks();
            }

            @Test
            @DisplayName("빈 배열을 반환한다")
            void then_returnEmptyList() {
                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("존재하지 않는 taskId로 getTask() 요청하면")
        class When_getTaskWithNotExistedTaskId {
            Throwable thrown;

            @BeforeEach
            void when() {
                thrown = catchThrowable(() -> { service.getTask(1L); });
            }

            @Test
            @DisplayName("할일을 찾을 수 없다는 예외가 던져짐")
            void then_throwTaskNotFoundException() {
                assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 taskId로 deleteTask() 요청하면")
        class When_DeleteTaskWithNotExistedTaskId {
            Throwable thrown;

            @BeforeEach
            void when() {
                thrown = catchThrowable(() -> { service.getTask(1L); });
            }

            @Test
            @DisplayName("할일을 찾을 수 없다는 예외가 던져짐")
            void then_throwTaskNotFoundException() {
                assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("task를 2개 생성했을 때")
    class Given_didCreateTasks {
        @BeforeEach
        void given() {
            final Task task1 = new Task();
            task1.setTitle(EXAMPLE_TITLE + 1);
            final Task task2 = new Task();
            task2.setTitle(EXAMPLE_TITLE + 2);
            service.createTask(task1);
            service.createTask(task2);
        }

        @Nested
        @DisplayName("getTasks() 요청하면")
        class When_getTasks {
            private List<Task> tasks;

            @BeforeEach
            void when() {
                tasks = service.getTasks();
            }

            @Test
            @DisplayName("생성했던 할일을 반환한다")
            void then_returnTasks() {
                assertThat(tasks).hasSize(2);
                final Task resultTask1 = tasks.get(0);
                final Task resultTask2 = tasks.get(1);
                assertThat(resultTask1.getId()).isEqualTo(1L);
                assertThat(resultTask1.getTitle()).isEqualTo(EXAMPLE_TITLE + 1);
                assertThat(resultTask2.getId()).isEqualTo(2L);
                assertThat(resultTask2.getTitle()).isEqualTo(EXAMPLE_TITLE + 2);
            }
        }

        @Nested
        @DisplayName("존재하는 taskId로 getTask() 요청하면")
        class When_getTaskWithExistedTaskId {
            private Task result;

            @BeforeEach
            void when() {
                result = service.getTask(1L);
            }

            @Test
            @DisplayName("요청한 id에 해당하는 task 반환")
            void then_returnTask() {
                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getTitle()).isEqualTo(EXAMPLE_TITLE + 1);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 taskId로 getTask() 요청하면")
        class When_getTaskWithNotExistedTaskId {
            Throwable thrown;

            @BeforeEach
            void when() {
                thrown = catchThrowable(() -> { service.getTask(3L); });
            }

            @Test
            @DisplayName("할일을 찾을 수 없다는 예외가 던져짐")
            void then_throwTaskNotFoundException() {
                assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 taskId로 updateTask() 요청하면")
        class When_updateTaskWithNotExistedTaskId {
            Throwable thrown;

            @BeforeEach
            void when() {
                thrown = catchThrowable(() -> { service.getTask(3L); });
            }

            @Test
            @DisplayName("할일을 찾을 수 없다는 예외가 던져짐")
            void then_throwTaskNotFoundException() {
                assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하는 task id로 updateTask() 요청하면")
        class When_updateTaskWithExistedId {
            @BeforeEach
            void when() {
                Task task = new Task();
                task.setTitle("new title");
                service.updateTask(1L, task);
            }

            @Test
            @DisplayName("할일이 업데이트 된다")
            void then_taskUpdated() {
                Task updatedTask = service.getTask(1L);
                assertThat(updatedTask.getId()).isEqualTo(1L);
                assertThat(updatedTask.getTitle()).isEqualTo("new title");
            }
        }

        @Nested
        @DisplayName("존재하는 task id로 updateTask() 요청하면")
        class When_deleteTaskWithExistedId {
            @BeforeEach
            void when() {
                service.deleteTask(1L);
            }

            @Test
            @DisplayName("할일이 삭제된다")
            void then_taskDeleted() {
                Throwable thrown = catchThrowable(() -> { service.getTask(1L); });
                assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
