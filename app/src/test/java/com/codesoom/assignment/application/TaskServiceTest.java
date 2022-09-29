package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.dto.TaskRequestDto;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private final String TITLE = "test";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    void setUpTask() {
        taskService.createTask(new TaskRequestDto(TITLE));
    }

    @Test
    @DisplayName("create 메소드는 생성된 할 일을 반환한다")
    void createTask_returns_newly_added_task() {
        // when
        Task task = taskService.createTask(new TaskRequestDto(TITLE));

        // then
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("만약 할 일이 등록되지 않았다면")
        class Context_with_added_nothing {

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returns_empty() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 할 일이 등록되어 있으면")
        class Context_with_added_tasks {
            private final int taskSize = 5;

            void setUp() {
                for (int i = 0; i < taskSize; i++) {
                    setUpTask();
                }
            }

            @Test
            @DisplayName("등록된 모든 할 일을 조회한다")
            void it_returns_tasks() {
                setUp();

                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(taskSize);
                assertThat(tasks.get(0).getId()).isEqualTo(1L);
                assertThat(tasks.get(1).getId()).isEqualTo(2L);
                assertThat(tasks.get(2).getId()).isEqualTo(3L);
                assertThat(tasks.get(3).getId()).isEqualTo(4L);
                assertThat(tasks.get(4).getId()).isEqualTo(5L);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("만약 할 일을 찾을 수 없는 id가 주어지면")
        class Context_with_valid_id {
            private final Long id = 1L;

            @Test
            @DisplayName("저장된 할 일을 반환한다")
            void it_returns_task() {
                setUpTask();

                Task task = taskService.getTask(id);

                assertThat(task.getId()).isEqualTo(id);
                assertThat(task.getTitle()).isEqualTo(TITLE);
            }
        }

        @Nested
        @DisplayName("만약 할 일을 찾을 수 없는 id가 주어지면")
        class Context_with_invalid_id {
            private final Long invalidId = 1000L;

            @Test
            @DisplayName("에러를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskService.getTask(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("만약 할 일을 찾을 수 있는 id와 변경할 title이 주어지면")
        class Context_with_valid_data {
            private final Long id = 1L;
            private final String UPDATE_TITLE = "update test";

            @Test
            @DisplayName("title이 수정된 할 일을 반환한다")
            void it_returns_updated_task() {
                setUpTask();

                Task updatedTask = taskService.updateTask(id, new TaskRequestDto(UPDATE_TITLE));

                assertThat(updatedTask.getId()).isEqualTo(id);
                assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 찾을 수 없는 id가 주어지면")
        class Context_with_invalid_id {
            private final Long invalidId = 1000L;
            private final String UPDATE_TITLE = "update test";

            @Test
            @DisplayName("에러를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskService.updateTask(invalidId, new TaskRequestDto(UPDATE_TITLE)))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("만약 할 일을 찾을 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private final Long id = 1L;

            @Test
            @DisplayName("삭제된 할 일을 반환한다")
            void it_returns_deleted_task() {
                setUpTask();

                Task deletedTask = taskService.deleteTask(id);

                assertThat(deletedTask.getId()).isEqualTo(id);
                assertThat(deletedTask.getTitle()).isEqualTo(TITLE);
            }
        }

        @Nested
        @DisplayName("만약 할 일을 찾을 수 없는 id가 주어지면")
        class Context_with_invalid_id {
            private final Long invalidId = 1000L;

            @Test
            @DisplayName("에러를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
