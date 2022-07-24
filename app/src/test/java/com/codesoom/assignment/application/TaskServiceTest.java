package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.TaskRepository;
import com.codesoom.assignment.TaskRepositoryCleaner;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    abstract class Context_didCreateTwoTasks {
        final int numberOfTasksCreated = 2;

        @BeforeEach
        void context() {
            for (int i = 1; i <= numberOfTasksCreated; i++) {
                final Task task = new Task(FIXTURE_TITLE + i);
                service.createTask(task);
            }
        }
    }

    public static final String FIXTURE_TITLE = "title";
    private TaskRepositoryCleaner repositoryCleaner;
    private TaskService service;

    @BeforeEach
    void setup() {
        final TaskRepository repository = new TaskRepository();
        repositoryCleaner = new TaskRepositoryCleaner(repository);
        service = new TaskService(repository);
    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {
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
                List<Task> tasks = service.getTaskList();

                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("생성된 할 일들이 있다면")
        class Context_didCreateTasks extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("생성했던 할 일 목록을 반환한다")
            void it_returnsTasks() {
                List<Task> tasks = service.getTaskList();

                assertThat(tasks).hasSize(numberOfTasksCreated);
                final Task resultTask1 = tasks.get(0);
                final Task resultTask2 = tasks.get(1);
                assertThat(resultTask1.getId()).isEqualTo(1L);
                assertThat(resultTask1.getTitle()).isEqualTo(FIXTURE_TITLE + 1);
                assertThat(resultTask2.getId()).isEqualTo(2L);
                assertThat(resultTask2.getTitle()).isEqualTo(FIXTURE_TITLE + 2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("찾을 수 없는 id로 요청하면")
        class Context_withNotFindableTaskId {
            private final Long DELETED_TASK_ID = 1L;

            @BeforeEach
            void prepare() {
                service.createTask(new Task(FIXTURE_TITLE));
                service.deleteTask(DELETED_TASK_ID);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    service.getTask(DELETED_TASK_ID);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            private final Long CREATED_TASK_ID = 1L;

            @Test
            @DisplayName("요청한 id에 해당하는 할 일을 반환한다")
            void then_returnTask() {
                Task result = service.getTask(CREATED_TASK_ID);

                assertThat(result.getId()).isEqualTo(CREATED_TASK_ID);
                assertThat(result.getTitle()).isEqualTo(FIXTURE_TITLE + CREATED_TASK_ID);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("찾을 수 없는 id로 요청하면")
        class Context_withNotFindableTaskId {
            private final Long DELETED_TASK_ID = 1L;

            @BeforeEach
            void prepare() {
                service.createTask(new Task(FIXTURE_TITLE));
                service.deleteTask(DELETED_TASK_ID);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    Task task = new Task(FIXTURE_TITLE);
                    service.updateTask(DELETED_TASK_ID, task);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            private final Long CREATED_TASK_ID = 1L;

            @Test
            @DisplayName("요청한 id에 해당하는 할 일을 수정한다")
            void then_returnTask() {
                final String NEW_TITLE = "new title";
                Task task = new Task(NEW_TITLE);
                service.updateTask(CREATED_TASK_ID, task);

                Task updatedTask = service.getTask(CREATED_TASK_ID);
                assertThat(updatedTask.getId()).isEqualTo(CREATED_TASK_ID);
                assertThat(updatedTask.getTitle()).isEqualTo(NEW_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("찾을 수 없는 id로 요청하면")
        class Context_withNotFindableTaskId {
            private final Long DELETED_TASK_ID = 1L;

            @BeforeEach
            void prepare() {
                service.createTask(new Task(FIXTURE_TITLE));
                service.deleteTask(DELETED_TASK_ID);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    service.deleteTask(DELETED_TASK_ID);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            private final Long CREATED_TASK_ID = 1L;

            @Test
            @DisplayName("요청한 id에 해당하는 할 일이 삭제된다")
            void then_deletesTask() {
                service.deleteTask(CREATED_TASK_ID);

                assertThrows(TaskNotFoundException.class, () -> {
                    service.deleteTask(CREATED_TASK_ID);
                });
            }
        }
    }
}
