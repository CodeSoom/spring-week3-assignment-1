package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.TaskRepository;
import com.codesoom.assignment.TaskRepositoryCleaner;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskControllerTest {
    abstract class Context_didCreateTwoTasks {
        final int numberOfTasksCreated = 2;

        @BeforeEach
        void context() {
            for (int i = 1; i <= numberOfTasksCreated; i++) {
                final Task task = new Task(FIXTURE_TITLE + i);
                controller.create(task);
            }
        }
    }

    public static final String FIXTURE_TITLE = "title";
    private TaskRepositoryCleaner repositoryCleaner;
    private TaskController controller;

    @BeforeEach
    void setup() {
        final TaskRepository repository = new TaskRepository();
        repositoryCleaner = new TaskRepositoryCleaner(repository);
        controller = new TaskController(new TaskService(repository));
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("생성되어 있는 할 일이 없다면")
        class Context_didNotCreateTask {
            @BeforeEach
            void prepare() {
                repositoryCleaner.deleteAllTasks();
            }

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returnsEmptyList() {
                List<Task> result = controller.list();

                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("할 일을 생성했었을 때")
        class Context_didCreateContext extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("생성된 할 일 목록을 반환한다")
            void it_returnsTasks() {
                List<Task> result = controller.list();

                assertThat(result).hasSize(numberOfTasksCreated);
                assertThat(result.get(0).getTitle()).isEqualTo(FIXTURE_TITLE + 1);
                assertThat(result.get(0).getId()).isEqualTo(1L);
                assertThat(result.get(1).getTitle()).isEqualTo(FIXTURE_TITLE + 2);
                assertThat(result.get(1).getId()).isEqualTo(2L);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("찾을 수 없는 id로 조회했을 때")
        class Context_withNotFindableTaskId {
            final Long DELETED_TASK_ID = 1L;

            @BeforeEach
            void context() {
                controller.create(new Task(FIXTURE_TITLE));
                controller.delete(DELETED_TASK_ID);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFound() {
                assertThrows(TaskNotFoundException.class, () -> {
                    controller.detail(DELETED_TASK_ID);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id로 조회했을 떄")
        class Context_didCreateContext extends Context_didCreateTwoTasks {
            private final Long CREATED_TASK_ID = 1L;

            @Test
            @DisplayName("조회한 할 일을 반환한다")
            void it_returnsTasks() {
                Task result = controller.detail(CREATED_TASK_ID);

                assertThat(result.getId()).isEqualTo(CREATED_TASK_ID);
                assertThat(result.getTitle()).isEqualTo(FIXTURE_TITLE + CREATED_TASK_ID);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("찾을 수 없는 id로 요청했을 때")
        class Context_withNotFindableTaskId {
            final Long DELETED_TASK_ID = 1L;

            @BeforeEach
            void context() {
                controller.create(new Task(FIXTURE_TITLE));
                controller.delete(DELETED_TASK_ID);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                Task newTask = new Task(FIXTURE_TITLE);
                assertThrows(TaskNotFoundException.class, () -> {
                    controller.update(DELETED_TASK_ID, newTask);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id로 요청했을 때")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            private final Long CREATED_TASK_ID = 1L;

            @Test
            @DisplayName("수정된 할 일을 반환한다")
            void it_returnsUpdatedTask() {
                Task newTask = new Task(FIXTURE_TITLE + CREATED_TASK_ID);
                Task updatedTask = controller.update(CREATED_TASK_ID, newTask);

                assertThat(updatedTask.getId()).isEqualTo(CREATED_TASK_ID);
                assertThat(updatedTask.getTitle()).isEqualTo(FIXTURE_TITLE + CREATED_TASK_ID);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("찾을 수 없는 할 일 id로 요청했을 때")
        class Context_withNotFindableTaskId {
            final Long DELETED_TASK_ID = 1L;

            @BeforeEach
            void context() {
                controller.create(new Task(FIXTURE_TITLE));
                controller.delete(DELETED_TASK_ID);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    controller.delete(DELETED_TASK_ID);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id로 요청했을 때")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            private final Long CREATED_TASK_ID = 1L;

            @Test
            @DisplayName("요청된 할 일을 삭제한다")
            void it_deletesTask() {
                controller.delete(CREATED_TASK_ID);

                assertThrows(TaskNotFoundException.class, () -> {
                    controller.detail(CREATED_TASK_ID);
                });
            }
        }
    }
}
