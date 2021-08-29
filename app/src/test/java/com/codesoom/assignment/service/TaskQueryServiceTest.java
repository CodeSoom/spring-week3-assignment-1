package com.codesoom.assignment.service;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskQueryService의")
class TaskQueryServiceTest {

    public static final String TASK_TITLE = "Test Title";
    private TaskQueryService taskQueryService;
    private TaskCommandService taskCommandService;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = new TaskRepository();
        taskQueryService = new TaskQueryService(taskRepository);
        taskCommandService = new TaskCommandService(taskRepository, taskQueryService);
        Task task = new Task(1L, TASK_TITLE);
        taskCommandService.createTask(task);
    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {
        @Test
        @DisplayName("할 일 리스트를 조회한다.")
        void getTasks() {
            assertThat(taskQueryService.getTaskList()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("유효한 Id를 조회하면")
        class Context_with_task {
            @Test
            @DisplayName("할 일을 찾는다.")
            void getTaskWithValidId() {
                assertThat(taskQueryService.getTask(1L).getTitle()).isEqualTo(TaskServiceTest.TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 Id를 조회하면")
        class Context_with_task_not_valid_id {
            @Test
            @DisplayName("TaskNotFound 예외를 던진다.")
            void getTaskWithInValidId() {
                assertThatThrownBy(() -> taskQueryService.getTask(100000L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

}