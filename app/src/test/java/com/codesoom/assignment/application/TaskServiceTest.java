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
        @DisplayName("만약 기본 생성된 Task 1개만 존재한다면")
        class Context_with_default_task {
            @Test
            @DisplayName("반환값의 사이즈는 1이다")
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
            @DisplayName("기본 생성된 Task의 id값을 반환한다")
            void it_returns_default_task_id() {
                final Long actual = service.getTask(TASK_ID).getId();
                assertThat(actual).isEqualTo(TASK_ID);
            }

            @Test
            @DisplayName("기본 생성된 Task의 title값을 반환한다")
            void it_returns_default_task_title() {
                final String actual = service.getTask(TASK_ID).getTitle();
                assertThat(actual).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 조회한다면")
        class Context_with_not_existing_task {
            @Test
            @DisplayName("TaskNotFoundException이 발생한다")
            void it_throws_exception() {
                assertThatThrownBy(() -> service.getTask(TASK_ID_NOT_EXISTING))
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }
    }

}
