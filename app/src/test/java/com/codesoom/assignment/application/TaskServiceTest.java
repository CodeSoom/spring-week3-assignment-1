package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService")
public class TaskServiceTest {
    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "Test";
    private static final String MODIFY_TASK_TITLE = "Modified";

    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        task = new Task();
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("getTasks")
    class Describe_getTasks {
        @Nested
        @DisplayName("without any task")
        class Context_without_any_task {
            @Test
            @DisplayName("return empty list")
            void it_return_empty() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("with a task")
        class Context_with_task {
            @BeforeEach
            void addNewTask() {
                taskService.createTask(task);
            }

            @Test
            @DisplayName("return list")
            void it_return_list() {
                assertThat(taskService.getTasks()).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("getTask")
    class Describe_getTask {
        @BeforeEach
        void addNewTask() {
            taskService.createTask(task);
        }

        @Nested
        @DisplayName("with valid id")
        class Context_with_valid_id {
            @Test
            @DisplayName("return task")
            void it_return_task() {
                assertThat(taskService.getTask(1L).getClass()).isEqualTo(Task.class);
                assertThat(taskService.getTask(1L).getId()).isEqualTo(TASK_ID);
                assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("with invalid id")
        class Context_with_invalid_id {
            @Test
            @DisplayName("throw exception")
            void it_throw_exception() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask")
    class Describe_createTask {
        private int size;
        private Task added;

        @BeforeEach
        void addNewTask() {
            size = taskService.getTasks().size();
            added = taskService.createTask(task);
        }

        @Test
        @DisplayName("return added task")
        void it_return_added_task() {
            assertThat(added.getClass()).isEqualTo(Task.class);
            assertThat(added.getId()).isEqualTo(TASK_ID);
            assertThat(added.getTitle()).isEqualTo(TASK_TITLE);
        }

        @Test
        @DisplayName("plus 1 at task list size")
        void it_count_up_task_list_size() {
            assertThat(taskService.getTasks().size()).isEqualTo(size + 1);
        }
    }

    @Nested
    @DisplayName("updateTask")
    class Describe_updateTask {
        private Task modifying;

        @BeforeEach
        void addTask() {
            taskService.createTask(task);

            modifying = new Task();
            modifying.setTitle(MODIFY_TASK_TITLE);
        }

        @Nested
        @DisplayName("with valid id")
        class Context_with_valid_id {
            @Test
            @DisplayName("return modified task and task title is modified")
            void it_return_modified_task() {
                Task modified = taskService.updateTask(1L, modifying);
                assertThat(taskService.getTask(1L).getClass()).isEqualTo(Task.class);
                assertThat(taskService.getTask(1L).getId()).isEqualTo(TASK_ID);
                assertThat(taskService.getTask(1L).getTitle()).isNotEqualTo(TASK_TITLE);
                assertThat(taskService.getTask(1L).getTitle()).isEqualTo(MODIFY_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("with invalid id")
        class Context_with_invalid_id {
            @Test
            @DisplayName("throw exception")
            void it_throw_exception() {
                assertThatThrownBy(() -> taskService.updateTask(100L, modifying))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask")
    class Describe_deleteTask {
        @BeforeEach
        void addTask() {
            taskService.createTask(task);
        }

        @Nested
        @DisplayName("with valid id")
        class Context_with_valid_id {
            private int size;
            private Task deleted;

            @BeforeEach
            void addTask() {
                size = taskService.getTasks().size();
                deleted = taskService.deleteTask(1L);
            }

            @Test
            @DisplayName("return deleted task")
            void it_return_deleted_task() {
                assertThat(deleted.getClass()).isEqualTo(Task.class);
                assertThat(deleted.getId()).isEqualTo(TASK_ID);
                assertThat(deleted.getTitle()).isEqualTo(TASK_TITLE);
            }

            @Test
            @DisplayName("count down 1 task list size")
            void it_count_down_1_task_list_size() {
                assertThat(taskService.getTasks().size()).isEqualTo(size - 1);
            }
        }

        @Nested
        @DisplayName("with invalid id")
        class Context_with_invalid_id {
            @Test
            @DisplayName("throw exception")
            void it_throw_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
