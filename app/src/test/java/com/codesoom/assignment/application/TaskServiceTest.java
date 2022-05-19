package com.codesoom.assignment.application;


import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        @Nested
        @DisplayName("만약 기본 생성된 Task가 1개만 존재한다면")
        class Context_with_default_task {
            @Test
            @DisplayName("사이즈가 1인 콜렉션을 반환한다")
            void it_returns_1_size_of_Collection() {
                final int actual = service.getTasks().size();
                assertThat(actual).isEqualTo(TASKS_SIZE);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 조회한다면")
        class Context_with_default_task {
            @Test
            @DisplayName("Task를 반환하는데, 반환한 Task의 id와 기본 생성된 Task의 id는 동일하다")
            void it_returns_task_having_id_equal_to_default_task_id() {
                final Long actual = service.getTask(TASK_ID).getId();
                assertThat(actual).isEqualTo(TASK_ID);
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
        @DisplayName("만약 존재하지 않는 Task를 조회한다면")
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
    

}
