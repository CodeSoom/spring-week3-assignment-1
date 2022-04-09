package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.EmptyTitleException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    TaskService taskService = new TaskService();

    @Nested
    @DisplayName("getTasks 메서드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("Task 가 있다면")
        class Context_with_tasks {
            private final int addSize = 2;

            @BeforeEach
            void add_tasks() {
                addTasks(addSize);
            }

            @Test
            @DisplayName("Task 개수 만큼의 사이즈를 가진 리스트를 리턴한다.")
            void it_returns_list_containing_tasks() {
                assertThat(taskService.getTasks()).hasSize(addSize);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        @Nested
        @DisplayName("Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("TaskNotFoundException 을 던진다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(1L))
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
                    assertThat(taskService.getTask(correctId)).isNotNull();
                }
            }

            @Nested
            @DisplayName("잘못된 id 를 입력했을 때")
            class Context_wrong_id {
                private final long wrongId = 0L;

                @Test
                @DisplayName("TaskNotFoundException 을 던진다.")
                void it_throws_task_not_found_exception() {
                    assertThatThrownBy(() -> taskService.getTask(wrongId))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {
        @Nested
        @DisplayName("title 이 입력된 task 를 인자로 받았다면")
        class Context_with_task_argument_with_title {
            private final Task taskArgumentWithTitle;

            Context_with_task_argument_with_title() {
                taskArgumentWithTitle = new Task();
                taskArgumentWithTitle.setTitle("task1");
            }

            @Test
            @DisplayName("인자로 받은 task 를 추가한다.")
            void it_adds_the_task() {
                int previousSize = taskService.getTasks().size();
                taskService.createTask(taskArgumentWithTitle);
                int currentSize = taskService.getTasks().size();
                assertThat(currentSize).isEqualTo(previousSize + 1);

                Task addedTask = taskService.getTasks().get(0);
                assertThat(addedTask.getTitle()).isEqualTo(taskArgumentWithTitle.getTitle());
            }
        }

        @Nested
        @DisplayName("title 이 입력되지 않은 task 를 인자로 받았다면")
        class Context_with_task_argument_without_title {
            private final Task taskArgumentWithoutTitle;

            Context_with_task_argument_without_title() {
                taskArgumentWithoutTitle = new Task();
            }

            @Test
            @DisplayName("EmptyTitleException 을 던진다.")
            void it_throws_empty_task_exception() {
                assertThatThrownBy(() -> taskService.createTask(taskArgumentWithoutTitle))
                        .isInstanceOf(EmptyTitleException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("TaskNotFoundException 을 던진다.")
            void it_throws_task_not_found_exception() {
                Task source = new Task();
                source.setTitle("task");

                assertThatThrownBy(() -> {taskService.updateTask(1L, source);})
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
            class Context_with_correct_id {
                long correctId = 1L;

                @Test
                @DisplayName("Task 의 title 을 업데이트한다.")
                void it_updates_task() {
                    Task task = taskService.getTask(correctId);
                    String title = task.getTitle();

                    Task source = new Task();
                    source.setTitle("updated" + title);
                    taskService.updateTask(correctId, source);

                    assertThat(task.getTitle()).isEqualTo("updated" + title);
                }
            }

            @Nested
            @DisplayName("잘못된 id 를 입력했을 때")
            class Context_with_wrong_id {
                long wrongId = 0L;

                @Test
                @DisplayName("TaskNotFoundException 을 던진다.")
                void it_throws_task_not_found_exception() {
                    Task source = new Task();
                    source.setTitle("task");

                    assertThatThrownBy(() -> {taskService.updateTask(wrongId, source);})
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("Task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("TaskNotFoundException 을 던진다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> {taskService.deleteTask(1L);})
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
            class Context_with_correct_id {
                long correctId = 1L;

                @Test
                @DisplayName("Task 를 삭제한다.")
                void it_updates_task() {
                    int previousSize = taskService.getTasks().size();
                    taskService.deleteTask(correctId);
                    int currentSize = taskService.getTasks().size();

                    assertThat(currentSize).isEqualTo(previousSize - 1);

                    assertThatThrownBy(() -> taskService.getTask(correctId))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }

            @Nested
            @DisplayName("잘못된 id 를 입력했을 때")
            class Context_with_wrong_id {
                long wrongId = 0L;

                @Test
                @DisplayName("TaskNotFoundException 을 던진다.")
                void it_throws_task_not_found_exception() {
                    Task source = new Task();
                    source.setTitle("task");

                    assertThatThrownBy(() -> {taskService.deleteTask(wrongId);})
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
            taskService.createTask(task);
        }
    }
}