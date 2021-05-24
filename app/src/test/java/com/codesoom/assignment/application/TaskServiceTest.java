package com.codesoom.assignment.application;

import com.codesoom.assignment.EmptyTaskTitleException;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService class")
class TaskServiceTest {

    private final String TASK_TITLE = "test";
    private final String TASK_UPDATE_POSTFIX = "UPDATED";

    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks method")
    class DescribeGetTasks {
        @Nested
        @DisplayName("when empty tasks")
        class ContextWithEmptyTasks {
            @Test
            @DisplayName("It returns empty list")
            void getTasks() {
                List<Task> tasks = (new TaskService()).getTasks();
                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("when some tasks")
        class ContextWithSomeTasks {
            @Test
            @DisplayName("It returns task list")
            void getTasks() {
                List<Task> tasks = taskService.getTasks();
                assertThat(tasks).hasSize(1);

                Task task = tasks.get(0);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("getTask method")
    class DescribeGetTask {
        @Nested
        @DisplayName("when exist a task with a matching ID")
        class ContextWithoutTask {
            @Test
            @DisplayName("It returns a task")
            void getTaskWithValidId() {
                Task task = taskService.getTask(1L);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("when no task with a matching ID")
        class ContextWithTask {
            @Test
            @DisplayName("It throw the not found exception")
            void getTaskWithInvalidId() {
                assertThatThrownBy(() -> taskService.getTask(99L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask method")
    class DescribeCreateTask {
        int lastSize;
        Long lastId;
        Task newTask;

        @BeforeEach
        void prepare() {
            List<Task> tasks = taskService.getTasks();
            lastSize = tasks.size();
            lastId = taskService.currentId();

            newTask = new Task();
        }

        @Nested
        @DisplayName("when task title is not empty")
        class ContextWithTaskTitle {
            @Test
            @DisplayName("It creates and returns a new task ")
            void createTask() {
                newTask.setTitle(TASK_TITLE);

                Task createdTask = taskService.createTask(newTask);

                assertThat(taskService.getTasks()).hasSize(lastSize + 1);
                assertThat(createdTask.getId()).isEqualTo(lastId + 1);
                assertThat(createdTask.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("when task title is empty")
        class ContextWithoutTaskTitle {
            @Test
            @DisplayName("It throw the bad request exception")
            void createTask() {
                assertThatThrownBy(() -> taskService.createTask(null))
                        .isInstanceOf(EmptyTaskTitleException.class);

                assertThatThrownBy(() -> taskService.createTask(newTask))
                        .isInstanceOf(EmptyTaskTitleException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask method")
    class DescribeDeleteTask {
        @Nested
        @DisplayName("when exist a task with a matching ID")
        class ContextWithMatchedTask {
            @Test
            @DisplayName("It returns deleted and returns the task")
            void deleteTask() {
                Task deletedTask = taskService.deleteTask(1L);

                assertThat(deletedTask.getId()).isEqualTo(1L);
                assertThat(deletedTask.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("when no task with a matching ID")
        class ContextWithoutMatchedTask {
            @Test
            @DisplayName("It throw the not found exception")
            void deleteTask() {
                assertThatThrownBy(() -> taskService.deleteTask(99L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask method")
    class DescribeUpdateTask {
        Task source;

        @BeforeEach
        void prepare() {
            source = new Task();
            source.setTitle(TASK_TITLE + TASK_UPDATE_POSTFIX);
        }

        @Nested
        @DisplayName("when exist a task with a matching ID")
        class ContextWithMatchedTask {
            @Test
            @DisplayName("It updates and returns the task")
            void updateTask() {
                Task task = taskService.updateTask(1L, source);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE + TASK_UPDATE_POSTFIX);
            }
        }

        @Nested
        @DisplayName("when no task with a matching ID")
        class ContextWithoutMatchedTask {
            @Test
            @DisplayName("It throw the not found exception")
            void deleteTask() {
                assertThatThrownBy(() -> taskService.updateTask(99L, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
