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

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        // fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메서드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("저장된 task가 있다면")
        class Context_with_tasks {
            @Test
            @DisplayName("task list를 리턴한다")
            void it_returns_task_list() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(1);
            }
        }

        @Nested
        @DisplayName("저장된 task가 없다면")
        class Context_with_empty_tasks {
            @BeforeEach
            void setUp() {
                taskService = new TaskService();
            }

            @Test
            @DisplayName("빈 task list를 리턴한다")
            void it_returns_empty_task_list() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_an_existing_task_id {
            @Test
            @DisplayName("task를 리턴한다")
            void it_returns_a_task() {
                Task task = taskService.getTask(EXISTING_ID);

                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_with_not_an_existing_task_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(NOT_EXISTING_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {
        @Nested
        @DisplayName("task가 주어진다면")
        class Context_with_a_task {
            int oldSize;
            Task newTask;

            @BeforeEach
            void prepareTask() {
                oldSize = taskService.getTasks().size();
                newTask = new Task();
                newTask.setTitle(TASK_TITLE);
            }

            @Test
            @DisplayName("생성된 task를 리턴한다")
            void it_returns_a_created_task() {
                Task createdTask = taskService.createTask(newTask);

                int newSize = taskService.getTasks().size();

                assertThat(createdTask.getTitle()).isEqualTo(TASK_TITLE);
                assertThat(newSize - oldSize).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {
        Task source;

        @BeforeEach
        void prepareTask() {
            source = new Task();
            source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        }

        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_an_existing_task_id {
            @Test
            @DisplayName("수정된 task를 리턴한다")
            void it_returns_a_updated_task() {
                Task updatedTask = taskService.updateTask(EXISTING_ID, source);

                assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_with_not_an_existing_task_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.updateTask(NOT_EXISTING_ID, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_an_existing_task_id {
            int oldSize;

            @BeforeEach
            void setUp() {
                oldSize = taskService.getTasks().size();
            }

            @Test
            @DisplayName("task를 삭제한다")
            void it_deletes_a_task() {
                taskService.deleteTask(EXISTING_ID);

                int newSize = taskService.getTasks().size();

                assertThat(oldSize - newSize).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_with_not_an_existing_task_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(NOT_EXISTING_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
