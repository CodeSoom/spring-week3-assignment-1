package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService taskService;

    private static final String TASK_TITLE = "babo";
    private static final Long TASK_INVALID_ID = 123L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    private Task givenSource(String title) {
        Task source = new Task();
        source.setTitle(title);
        return source;
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("저장된 task가 있으면")
        class Context_when_there_is_saved_task {
            Task task;

            @BeforeEach
            void setUp() {
                task = taskService.createTask(givenSource(TASK_TITLE));
            }

            @Test
            @DisplayName("tasks를 반환한다.")
            void it_returns_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).contains(task);
            }
        }

        @Nested
        @DisplayName("저장된 task가 없으면")
        class Context_when_there_is_not_saved_task {

            @Test
            @DisplayName("빈 tasks를 반환한다.")
            void it_returns_empty_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        Task source;

        @BeforeEach
        void setUp() {
            source = givenSource(TASK_TITLE);
        }

        @Test
        @DisplayName("생성된 task를 반환한다.")
        void it_returns_created_task() {
            Task task = taskService.createTask(source);

            assertThat(task.getId()).isNotNull();
            assertThat(task.getTitle()).isEqualTo(source.getTitle());
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        private Long id;

        @Nested
        @DisplayName("존재하는 id가 주어지면")
        class Context_with_exist_id {

            @BeforeEach
            void setUp() {
                Task task = taskService.createTask(givenSource(TASK_TITLE));
                id = task.getId();
            }

            @Test
            @DisplayName("task를 반환한다.")
            void it_returns_task() {
                Task task = taskService.getTask(id);

                assertThat(task.getId()).isEqualTo(id);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_non_exist_id {

            @BeforeEach
            void setUp() {
                id = TASK_INVALID_ID;
            }

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(id))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        private Long id;
        private Task source;

        private static final String TASK_UPDATE_TITLE = "babo1";

        @BeforeEach
        void setUp() {
            source = givenSource(TASK_UPDATE_TITLE);
        }

        @Nested
        @DisplayName("존재하는 id가 주어지면")
        class Context_with_exist_id_and_source {

            @BeforeEach
            void setUp() {
                Task task = taskService.createTask(source);
                id = task.getId();
            }

            @Test
            @DisplayName("변경된 task를 반환한다.")
            void it_returns_updated_task() {
                Task task = taskService.updateTask(id, source);

                assertThat(task.getTitle()).isEqualTo(TASK_UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_non_exist_id {

            @BeforeEach
            void setUp() {
                id = TASK_INVALID_ID;
            }

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.updateTask(id, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        private Long id;

        @Nested
        @DisplayName("존재하는 id가 주어지면")
        class Context_with_exist_id {

            @BeforeEach
            void setUp() {
                Task task = taskService.createTask(givenSource(TASK_TITLE));
                id = task.getId();
            }

            @Test
            @DisplayName("task를 삭제한다.")
            void it_deletes_task() {
                taskService.deleteTask(id);

                assertThatThrownBy(() -> taskService.getTask(id))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_non_exist_id {

            @BeforeEach
            void setUp() {
                id = TASK_INVALID_ID;
            }

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(id))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
