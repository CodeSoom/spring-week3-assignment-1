package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController taskController;
    private Task source;

    private static final String TASK_TITLE = "babo";
    private static final Long TASK_INVALID_ID = 123L;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);

        source = new Task();
        source.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Test
        @DisplayName("tasks를 반환한다.")
        void it_returns_tasks() {
            List<Task> tasks = taskController.list();

            assertThat(tasks).isEmpty();
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Test
        @DisplayName("생성된 task를 반환한다")
        void it_returns_task() {
            Task task = taskController.create(source);

            assertThat(task).isNotNull();
            assertThat(task.getId()).isNotNull();
            assertThat(task.getTitle()).isEqualTo(source.getTitle());
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("존재하는 id가 주어지면")
        class Context_with_exist_id {

            private Long id;

            @BeforeEach
            void setUp() {
                Task task = taskController.create(source);
                id = task.getId();
            }

            @Test
            @DisplayName("task를 반환한다.")
            void it_returns_task() {
                Task task = taskController.detail(id);

                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(id);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_non_exist_id {

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskController.detail(TASK_INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("존재하는 id가 주어지면")
        class Context_with_exist_id {

            private Long id;

            @BeforeEach
            void setUp() {
                Task task = taskController.create(source);
                id = task.getId();
            }

            @Test
            @DisplayName("task를 삭제한다.")
            void it_deletes_task() {
                taskController.delete(id);

                assertThatThrownBy(() -> taskController.detail(id))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_not_exist_id {

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskController.delete(TASK_INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("존재하는 id가 주어지면")
        class Context_with_exist_id {

            private Long id;
            private static final String TASK_UPDATE_TITLE = "babo1";

            @BeforeEach
            void setUp() {
                Task task = taskController.create(source);
                source.setTitle(TASK_UPDATE_TITLE);

                id = task.getId();
            }

            @Test
            @DisplayName("변경된 task를 반환한다.")
            void it_returns_updated_task() {
                Task task = taskController.update(id, source);

                assertThat(task.getTitle()).isEqualTo(TASK_UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_non_exist_id {

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskController.update(TASK_INVALID_ID, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}