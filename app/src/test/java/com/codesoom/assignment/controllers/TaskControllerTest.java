package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskRequestDto;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private final String TITLE = "test";
    private final String UPDATE_TITLE = "update test";

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("create 메소드는 생성된 할 일을 반환한다")
    void create_returns_created_task() {
        // when
        Task task = taskController.create(new TaskRequestDto(TITLE));

        // then
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("만약 할 일이 등록되어 있지 않으면")
        class Context_with_no_tasks {

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returns_empty_list() {
                assertThat(taskController.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 할 일이 등록되어 있으면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                taskController.create(new TaskRequestDto(TITLE));
                taskController.create(new TaskRequestDto(TITLE));
            }

            @Test
            @DisplayName("모든 할 일을 조회한다")
            void it_returns_all_tasks() {
                List<Task> tasks = taskController.list();

                assertThat(tasks).hasSize(2);
                assertThat(tasks.get(0).getId()).isEqualTo(1L);
                assertThat(tasks.get(1).getId()).isEqualTo(2L);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("만약 할 일을 찾을 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private final Long id = 1L;

            @BeforeEach
            void setUp() {
                taskController.create(new TaskRequestDto(TITLE));
            }

            @Test
            @DisplayName("찾은 할 일을 반환한다")
            void it_returns_found_task() {
                Task task = taskController.detail(id);

                assertThat(task.getId()).isEqualTo(id);
            }
        }

        @Nested
        @DisplayName("만약 할 일을 찾을 수 없는 id가 주어지면")
        class Context_with_invalid_id {
            private final Long invalidId = 1000L;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskController.detail(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("만약 할 일을 찾을 수 있는 id와 title이 주어지면")
        class Context_with_valid_id {
            private final Long id = 1L;

            @BeforeEach
            void setUp() {
                taskController.create(new TaskRequestDto(TITLE));
            }

            @Test
            @DisplayName("수정된 할 일을 반환한다")
            void it_returns_updated_task() {
                Task updatedTask = taskController.update(id, new TaskRequestDto(UPDATE_TITLE));

                assertThat(updatedTask.getId()).isEqualTo(id);
                assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 할 일을 찾을 수 없는 id가 주어지면")
        class Context_with_invalid_id {
            private final Long invalidId = 1000L;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskController.update(invalidId, new TaskRequestDto(UPDATE_TITLE)))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("만약 할 일을 찾을 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private final Long id = 1L;

            @BeforeEach
            void setUp() {
                taskController.create(new TaskRequestDto(TITLE));
            }

            @Test
            @DisplayName("할 일을 삭제한다")
            void it_removes_task() {
                int beforeSize = taskController.list().size();

                taskController.delete(id);

                int afterSize = taskController.list().size();
                assertThat(beforeSize - afterSize).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("만약 할 일을 찾을 수 없는 id가 주어지면")
        class Context_with_invalid_id {
            private final Long invalidId = 1000L;

            @Test
            @DisplayName("에러를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskController.delete(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
