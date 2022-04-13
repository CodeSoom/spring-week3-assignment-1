package com.codesoom.assignment.controllers;

import com.codesoom.assignment.exceptions.TaskNotFoundException;
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
    TaskController taskController;

    @BeforeEach
    void task_dependency_injection() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {
        @Nested
        @DisplayName("Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_returns_empty_list() {
                assertThat(taskController.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("Task 가 있다면")
        class Context_with_tasks {
            private final int addSize = 2;

            @BeforeEach
            void add_task() {
                addTasks(addSize);
            }

            @Test
            @DisplayName("Task 개수 만큼의 사이즈를 가진 리스트를 리턴한다.")
            void it_returns_list_containing_tasks() {
                assertThat(taskController.list()).hasSize(addSize);
            }
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {
        @Nested
        @DisplayName("Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("TaskNotFoundException 을 던진다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskController.detail(1L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("Task 가 있다면")
        class Context_with_tasks {
            private final int addSize = 2;

            @BeforeEach
            void add_task() {
                addTasks(addSize);
            }

            @Nested
            @DisplayName("올바른 id 를 입력했을 때")
            class Context_correct_id {
                private final long correctId = 1L;

                @Test
                @DisplayName("task 객체를 리턴한다.")
                void it_returns_task() {
                    assertThat(taskController.detail(correctId)).isNotNull();
                }
            }

            @Nested
            @DisplayName("잘못된 id 를 입력했을 때")
            class Context_wrong_id {
                private final long wrongId = 0L;

                @Test
                @DisplayName("TaskNotFoundException 을 던진다.")
                void it_throws_task_not_found_exception() {
                    assertThatThrownBy(() -> taskController.detail(wrongId))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Nested
        @DisplayName("Task 를 인자로 받으면")
        class Context_with_task {
            Task task;

            Context_with_task() {
                task = new Task();
                task.setTitle("hi");
            }

            @Test
            @DisplayName("Task 를 생성한다.")
            void it_creates_task() {
                List<Task> tasks = taskController.list();

                int previousSize = tasks.size();
                taskController.create(task);
                int currentSize = tasks.size();

                assertThat(currentSize).isEqualTo(previousSize + 1);
                assertThat(tasks.get(0).getTitle()).isEqualTo(task.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("Task 가 있다면")
        class Context_with_tasks {
            private final int addSize = 2;

            @BeforeEach
            void add_task() {
                addTasks(addSize);
            }

            @Nested
            @DisplayName("올바른 id 를 입력받았을 때")
            class Context_with_correct_id {
                private final long correctId = 1L;

                @Test
                @DisplayName("Task 를 수정한다.")
                void it_updates_task() {
                    Task task = taskController.detail(correctId);
                    String previousTitle = task.getTitle();

                    Task taskArgument = new Task();
                    taskArgument.setTitle("updated" + previousTitle);

                    taskController.update(correctId, taskArgument);

                    assertThat(task.getTitle()).isEqualTo(taskArgument.getTitle());
                    assertThat(task.getTitle()).isNotEqualTo(previousTitle);
                }
            }

            @Nested
            @DisplayName("잘못된 id 를 입력받았을 때")
            class Context_with_wrong_id {
                private final long wrongId = 0L;

                @Test
                @DisplayName("TaskNotFoundException 을 던진다.")
                void it_throws_task_not_found_exception() {
                    Task taskArgument = new Task();
                    taskArgument.setTitle("updated");

                    assertThatThrownBy(() -> taskController.update(wrongId, taskArgument))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }
        }
    }

    /**
     * number 만큼 Task 를 추가한다.
     * @param number
     */
    private void addTasks(int number) {
        for (int i = 1; i <= number; i++) {
            Task task = new Task();
            task.setTitle("task" + i);
            taskController.create(task);
        }
    }
}