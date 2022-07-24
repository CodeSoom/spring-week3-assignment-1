package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.TaskRepository;
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
    private TaskService service;

    @BeforeEach
    void setup() {
        service = new TaskService(new TaskRepository());
    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {
        @Nested
        @DisplayName("생성되어 있는 할 일이 없다면")
        class Context_didNotCreateTask {
            @BeforeEach
            void prepare() {
                service.createTask(new Task(FIXTURE_TITLE));
                service.deleteTask(1L);
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
            private final Long deletedTaskId = 1L;

            @BeforeEach
            void prepare() {
                service.createTask(new Task(FIXTURE_TITLE));
                service.deleteTask(deletedTaskId);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    service.getTask(deletedTaskId);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("요청한 id에 해당하는 할 일을 반환한다")
            void then_returnTask() {
                Task result = service.getTask(1L);

                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getTitle()).isEqualTo(FIXTURE_TITLE + 1);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("찾을 수 없는 id로 요청하면")
        class Context_withNotFindableTaskId {
            private final Long deletedTaskId = 1L;

            @BeforeEach
            void prepare() {
                service.createTask(new Task(FIXTURE_TITLE));
                service.deleteTask(deletedTaskId);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    Task task = new Task(FIXTURE_TITLE);
                    service.updateTask(deletedTaskId, task);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("요청한 id에 해당하는 할 일을 수정한다")
            void then_returnTask() {
                Task task = new Task("new title");
                service.updateTask(1L, task);

                Task updatedTask = service.getTask(1L);
                assertThat(updatedTask.getId()).isEqualTo(1L);
                assertThat(updatedTask.getTitle()).isEqualTo("new title");
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("찾을 수 없는 id로 요청하면")
        class Context_withNotFindableTaskId {
            private final Long deletedTaskId = 1L;

            @BeforeEach
            void prepare() {
                service.createTask(new Task(FIXTURE_TITLE));
                service.deleteTask(deletedTaskId);
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    service.deleteTask(deletedTaskId);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("요청한 id에 해당하는 할 일이 삭제된다")
            void then_deletesTask() {
                service.deleteTask(1L);

                assertThrows(TaskNotFoundException.class, () -> {
                    service.deleteTask(1L);
                });
            }
        }
    }
}
