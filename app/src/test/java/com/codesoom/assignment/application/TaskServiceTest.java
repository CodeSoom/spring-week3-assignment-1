package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
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
        @BeforeEach
        void context() {
            final Task task1 = new Task();
            task1.setTitle(FIXTURE_TITLE + 1);
            final Task task2 = new Task();
            task2.setTitle(FIXTURE_TITLE + 2);
            service.createTask(task1);
            service.createTask(task2);
        }
    }

    public static final String FIXTURE_TITLE = "title";
    private TaskService service;

    @BeforeEach
    void setup() {
        service = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("할일이 생성되지 않았을 때")
        class Context_didNotCreateTask {
            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returnsEmptyList() {
                List<Task> tasks = service.getTasks();

                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("할일을 생성했었다면")
        class Context_didCreateTasks extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("생성했던 할일 목록을 반환한다")
            void it_returnsTasks() {
                List<Task> tasks = service.getTasks();

                assertThat(tasks).hasSize(2);
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
            @Test
            @DisplayName("할일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    service.getTask(1L);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("요청한 id에 해당하는 할일을 반환한다")
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
            @Test
            @DisplayName("할일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    Task task = new Task();
                    task.setTitle("new title");
                    service.updateTask(1L, task);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("요청한 id에 해당하는 할일을 수정한다")
            void then_returnTask() {
                Task task = new Task();
                task.setTitle("new title");
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
            @Test
            @DisplayName("할일을 찾을 수 없다는 예외를 던진다")
            void it_throwsTaskNotFoundException() {
                assertThrows(TaskNotFoundException.class, () -> {
                    service.deleteTask(1L);
                });
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 Id로 요청하면")
        class Context_withFindableTaskId extends Context_didCreateTwoTasks {
            @Test
            @DisplayName("요청한 id에 해당하는 할일이 삭제된다")
            void then_deletesTask() {
                service.deleteTask(1L);

                assertThrows(TaskNotFoundException.class, () -> {
                    service.deleteTask(1L);
                });
            }
        }
    }
}
