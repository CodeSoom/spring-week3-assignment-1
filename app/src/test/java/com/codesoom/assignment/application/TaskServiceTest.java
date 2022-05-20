package com.codesoom.assignment.application;


import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private TaskService service;
    private final String TASK_TITLE = "Test Task";
    private final String TASK_TITLE_UPDATED = "Updated Task";
    private final Long TASK_ID = 1L;
    private final Long TASK_ID_NOT_EXISTING = 0L;
    private final int TASKS_SIZE = 1;

    @BeforeEach
    void setUp() {
        final Task task = new Task();
        task.setTitle(TASK_TITLE);

        service = new TaskService();
        service.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Test
        @DisplayName("Collection 타입의 형태로 값을 반환한다")
        void it_returns_list() {
            final List<Task> actual = service.getTasks();
            assertThat(actual).isInstanceOf(Collection.class);
        }
    }

    @Nested
    @DisplayName("createTasks 메소드는")
    class Describe_createTasks {
        @Test
        @DisplayName("Task 타입의 형태로 값을 반환한다")
        void it_returns_id_not_null() {
            final Task task = new Task();

            final Task actual = service.createTask(task);
            assertThat(actual).isInstanceOf(Task.class);
        }

        @Test
        @DisplayName("매개변수로 전달한 값이 모두 반영되어 생성된 Task를 반환한다")
        void it_returns_task_reflecting_params() {
            final Task task = new Task(TASK_TITLE_UPDATED);

            final Task actual = service.createTask(task);
            assertThat(actual.getTitle()).isEqualTo(TASK_TITLE_UPDATED);
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("만약 존재하는 Task를 상세조회한다면")
        class Context_with_existing_task {
            @Test
            @DisplayName("매개변수로 전달한 id와 동일한 id를 가지고 있는 Task를 반환한다")
            void it_returns_task_having_id_equal_to_param() {
                final Task actual = service.getTask(TASK_ID);
                assertThat(actual.getId()).isEqualTo(TASK_ID);
            }

            @Test
            @DisplayName("Task를 반환하는데, 반환한 Task의 title과 기본 생성된 Task의 title은 동일하다")
            void it_returns_task_having_title_equal_to_default_task_title() {
                final String actual = service.getTask(TASK_ID).getTitle();
                assertThat(actual).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 조회한다면")
        class Context_with_not_existing_task {
            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다")
            void it_throws_exception() {
                assertThatThrownBy(() -> service.getTask(TASK_ID_NOT_EXISTING))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 수정한다면")
        class Context_with_default_task {
            @Test
            @DisplayName("Task를 반환하는데, 반환한 Task의 id와 기본 생성된 Task의 id는 동일하다")
            void it_returns_task_having_id_equal_to_default_task_id() {
                final Task task = new Task();
                task.setTitle(TASK_TITLE_UPDATED);

                final Long actual = service.updateTask(TASK_ID, task).getId();
                assertThat(actual).isEqualTo(TASK_ID);
            }

            @Test
            @DisplayName("Task를 반환하는데, 반환한 Task의 title과 요청 시 전달한 Task의 title과 동일한다")
            void it_returns_task_having_title_equal_to_task_title_delivered() {
                final Task task = new Task();
                task.setTitle(TASK_TITLE_UPDATED);

                final String actual = service.updateTask(TASK_ID, task).getTitle();
                assertThat(actual).isEqualTo(TASK_TITLE_UPDATED);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 수정한다면")
        class Context_with_not_existing_task {
            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다")
            void it_throws_exception() {
                final Task task = new Task();
                task.setTitle(TASK_TITLE_UPDATED);

                assertThatThrownBy(() -> service.updateTask(TASK_ID_NOT_EXISTING, task))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 삭제한다면")
        class Context_with_default_task {
            @Test
            @DisplayName("삭제된 기본생성 Task를 조회할 때 Not Found Exception이 발생한다")
            void it_throws_exception() {
                service.deleteTask(TASK_ID);
                assertThatThrownBy(() -> service.getTask(TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 조회한다면")
        class Context_with_not_existing_task {
            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다")
            void it_throws_exception() {
                assertThatThrownBy(() -> service.deleteTask(TASK_ID_NOT_EXISTING))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }
    }

}
