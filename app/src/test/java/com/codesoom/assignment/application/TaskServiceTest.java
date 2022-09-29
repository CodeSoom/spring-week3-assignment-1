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

class TaskServiceTest {
    private final long INVALID_TASK_ID = 0L;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks() 메서드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("task가 여러개 있을 때")
        class Context_with_tasks {
            @BeforeEach
            void setUp() {
                taskService.createTask(new Task(1L, "test1"));
                taskService.createTask(new Task(2L, "test2"));
            }

            @Test
            @DisplayName("모든 task를 리턴한다")
            void it_returns_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks.size()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("task가 없을 때")
        class Context_without_task {
            @Test
            @DisplayName("task가 없는 경우 빈 리스트를 반환한다")
            void it_returns_empty() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks.size()).isEqualTo(0);
            }
        }
    }


    @Nested
    @DisplayName("getTask() 메서드는")
    class Describe_getTask {
        @Nested
        @DisplayName("기존에 있는 task의 id로 요청할 때")
        class Context_with_existing_task_id {
            private Task testTask;

            @BeforeEach
            void setUp() {
                testTask = taskService.createTask(new Task(1L, "test1"));
            }

            @Test
            @DisplayName("요청에 맞는 task 객체를 리턴한다")
            void it_returns_task() {
                Long id = testTask.getId();
                Task task = taskService.getTask(id);

                assertThat(task.getId()).isEqualTo(id);
                assertThat(task.getTitle()).isEqualTo(testTask.getTitle());
            }
        }

        @Nested
        @DisplayName("기존에 없는 task의 id로 요청할 때")
        class Context_with_non_existent_task_id {
            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(
                        () -> taskService.getTask(INVALID_TASK_ID)
                ).isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask() 메서드는")
    class Describe_createTask {
        private Task requestTask;

        @BeforeEach
        void setUp() {
            requestTask = new Task(null, "task1");
        }

        @Nested
        @DisplayName("기존에 없던 task를 생성하는 경우")
        class Context_with_new_task {
            @Test
            @DisplayName("새로운 task를 생성한다")
            void it_returns_new_task() {
                Task createdTask = taskService.createTask(requestTask);

                assertThat(createdTask.getId()).isEqualTo(1L);
                assertThat(createdTask.getTitle()).isEqualTo(requestTask.getTitle());
            }
        }

        @Nested
        @DisplayName("기존에 있는 task와 title이 같은 task를 생성하는 경우")
        class Context_with_duplicated_title_task {
            @BeforeEach
            void setUp() {
                taskService.createTask(new Task(1L, "task1"));
            }

            @Test
            @DisplayName("새로운 task를 생성한다.")
            void it_returns_new_task() {
                Task createTask = taskService.createTask(requestTask);

                assertThat(createTask.getId()).isEqualTo(2L);
                assertThat(createTask.getTitle()).isEqualTo(requestTask.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("updateTask() 메서드는")
    class Describe_updateTask {
        private Task requestTask;

        @BeforeEach
        void setUp() {
            requestTask = new Task(null, "update Task");
        }

        @Nested
        @DisplayName("기존에 있는 task의 id로 요청할 때")
        class Context_with_existing_task_id {
            private Task testTask;

            @BeforeEach
            void setUp() {
                testTask = taskService.createTask(new Task(1L, "task1"));
            }

            @Test
            @DisplayName("요청에 맞는 task 객체를 수정 후 리턴한다")
            void it_returns_updated_task() {
                Long id = testTask.getId();
                Task updatedTask = taskService.updateTask(id, requestTask);

                assertThat(updatedTask.getId()).isEqualTo(id);
                assertThat(updatedTask.getTitle()).isEqualTo(requestTask.getTitle());
            }
        }

        @Nested
        @DisplayName("기존에 없는 task의 id로 요청할 때")
        class Context_with_non_existent_task_id {
            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(
                        () -> taskService.updateTask(INVALID_TASK_ID, requestTask)
                ).isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask() 메서드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("기존에 있는 task의 id로 요청할 때")
        class Context_with_existing_task_id {
            private Task testTask;

            @BeforeEach
            void setUp() {
                testTask = taskService.createTask(new Task(1L, "task1"));
            }

            @Test
            @DisplayName("요청에 맞는 task 객체를 삭제한다")
            void it_delete_task() {
                Long id = testTask.getId();

                taskService.deleteTask(id);

                assertThatThrownBy(
                        () -> taskService.getTask(id)
                ).isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("기존에 없는 task의 id로 요청할 때")
        class Context_with_non_existent_task_id {
            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(
                        () -> taskService.deleteTask(INVALID_TASK_ID)
                ).isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}