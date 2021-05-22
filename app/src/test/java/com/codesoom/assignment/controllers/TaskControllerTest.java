package com.codesoom.assignment.controllers;

import com.codesoom.assignment.EmptyTaskTitleException;
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

@DisplayName("TaskController class")
class TaskControllerTest {

    private final Long ID = 1L;
    private final String TITLE = "SAMPLE TITLE";
    private final String UPDATED = "[UPDATED]";

    private TaskController controller;
    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        task = new Task();
        task.setId(ID);
        task.setTitle(TITLE);

        controller.create(task);
    }

    @Nested
    @DisplayName("list method")
    class DescribeGetTasks {
        @Nested
        @DisplayName("when exist tasks")
        class ContextWithTasks {
            @Test
            @DisplayName("It returns the list contains tasks")
            void list() {
                List<Task> tasks = controller.list();
                assertThat(tasks).isNotEmpty();
                assertThat(tasks).hasSize(1);
            }
        }

        @Nested
        @DisplayName("when empty tasks")
        class ContextWithEmptyTasks {
            @BeforeEach
            void prepared() {
                controller.delete(ID);
            }

            @Test
            @DisplayName("It returns the empty list")
            void list() {
                List<Task> tasks = controller.list();
                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("detail method")
    class DescribeGetTask {
        @Nested
        @DisplayName("when exist matched task")
        class ContextWithMatchedTask {
            @Test
            @DisplayName("It returns a task that matches the ID")
            void detailTheTask() {
                Task task = controller.detail(ID);

                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(ID);
                assertThat(task.getTitle()).isEqualTo(TITLE);
            }
        }

        @Nested
        @DisplayName("when no matched task")
        class ContextWithNoMatchedTask {
            @Test
            @DisplayName("It throw the task not found exception")
            void detailNoTask() {
                assertThatThrownBy(() -> controller.detail(ID * 0L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("create method")
    class DescribeCreateTask {
        @Nested
        @DisplayName("when a valid task")
        class ContextWithValidTask {
            @Test
            @DisplayName("It adds to the list and returns created task")
            void create() {
                assertThat(controller.list()).hasSize(1);
                assertThat(controller.list().get(0).getId()).isEqualTo(ID);
                assertThat(controller.list().get(0).getTitle()).isEqualTo(TITLE);

                Task newTask = new Task();
                newTask.setTitle("NEW TASK");
                controller.create(newTask);

                assertThat(controller.list()).hasSize(2);
                assertThat(controller.list().get(1).getId()).isEqualTo(2L);
                assertThat(controller.list().get(1).getTitle()).isEqualTo("NEW TASK");
            }
        }

        @Nested
        @DisplayName("when a invalid task")
        class ContextWithInvalidTask {
            @Test
            @DisplayName("It throw the task not found exception")
            void create() {
                Task newTask = new Task();

                assertThatThrownBy(() -> controller.create(newTask))
                    .isInstanceOf(EmptyTaskTitleException.class);
            }
        }
    }

    @Nested
    @DisplayName("update method")
    class DescribeUpdateTask {
        @Nested
        @DisplayName("when exist matched task")
        class ContextWithMatchedTask {
            @Test
            @DisplayName("It updates and returns the task")
            void update() {
                Task task = controller.detail(ID);
                task.setTitle(TITLE + UPDATED);

                Task updatedTask = controller.update(ID, task);

                assertThat(updatedTask).isNotNull();
                assertThat(updatedTask.getId()).isEqualTo(ID);
                assertThat(updatedTask.getTitle()).isEqualTo(TITLE + UPDATED);
            }
        }

        @Nested
        @DisplayName("when no matched task")
        class ContextWithNoMatchedTask {
            @BeforeEach
            void prepared() {
                controller.delete(ID);
            }

            @Test
            @DisplayName("It throw a task not found exception")
            void update() {
                assertThatThrownBy(() -> controller.update(ID, new Task()))
                    .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("patch method")
    class DescribePatchTask {
        @Nested
        @DisplayName("when exist matched task")
        class ContextWithMatchedTask {
            @Test
            @DisplayName("It patches and returns the task")
            void patch() {
                Task task = controller.detail(ID);
                task.setTitle(TITLE + UPDATED);

                Task updatedTask = controller.patch(ID, task);

                assertThat(updatedTask).isNotNull();
                assertThat(updatedTask.getId()).isEqualTo(ID);
                assertThat(updatedTask.getTitle()).isEqualTo(TITLE + UPDATED);
            }
        }

        @Nested
        @DisplayName("when no matched task")
        class ContextWithNoMatchedTask {
            @BeforeEach
            void prepared() {
                controller.delete(ID);
            }

            @Test
            @DisplayName("It throw a task not found exception")
            void patch() {
                assertThatThrownBy(() -> controller.patch(ID, new Task()))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete method")
    class DescribeDeleteTask {
        @Nested
        @DisplayName("when exist matched task")
        class ContextWithMatchedTask {
            @Test
            @DisplayName("It deletes the task")
            void deleteTask() {
                List<Task> tasks = controller.list();
                assertThat(tasks).hasSize(1);

                controller.delete(ID);

                tasks = controller.list();
                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("when no matched task")
        class ContextWithNoMatchedTask {
            @BeforeEach
            void prepared() {
                controller.delete(ID);
            }

            @Test
            @DisplayName("It throw the task not found exception")
            void deleteTask() {
                assertThatThrownBy(() -> controller.delete(ID))
                    .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
