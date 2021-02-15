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

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "new";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        Task task;

        @Nested
        @DisplayName("할 일이 존재한다면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                task = new Task();

                taskService.createTask(task);
            }

            @Test
            @DisplayName("할 일 목록을 반환한다 ")
            void it_returns_list() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isNotEmpty();
                assertThat(tasks).hasSize(1);
            }
        }

        @Nested
        @DisplayName("할 일이 존재하지 않는다면")
        class Context_without_task {

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returns_list() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        Task task;

        @Nested
        @DisplayName("저장된 할 일의 ID가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                task = new Task();
                task.setTitle(TASK_TITLE);

                taskService.createTask(task);
            }

            @Test
            @DisplayName("해당 ID를 갖는 할 일을 반환한다")
            void it_returns_task() {
                Task task = taskService.getTask(1L);
                assertThat(task.getTitle()).isEqualTo("test");
            }
        }

        @Nested
        @DisplayName("저장되지 않은 할 일의 ID가 주어진다면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("해당 할 일을 찾을 수 없다는 경고 메시지를 반환한다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {

        @Test
        @DisplayName("새로운 할 일을 생성한다")
        void it_returns_task() {
            int oldSize = taskService.getTasks().size();

            Task task = new Task();
            Task newTask = taskService.createTask(task);

            int newSize = taskService.getTasks().size();

            assertThat(newSize - oldSize).isEqualTo(1);
            assertThat(taskService.getTasks()).contains(newTask);
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        Task task;

        @Nested
        @DisplayName("저장된 할 일의 ID가 주어진다면")
        class Context_with_valid_id_and_task {

            @BeforeEach
            void setUp() {
                task = new Task();
                task.setTitle(UPDATE_POSTFIX + "!!!");

                taskService.createTask(task);
            }

            @Test
            @DisplayName("해당 ID를 갖는 할 일의 Title을 변경하고 반환한다")
            void it_returns_updated_task() {
                taskService.updateTask(1L, task);

                Task task = taskService.getTask(1L);

                assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + "!!!");
            }
        }

        @Nested
        @DisplayName("저장되지 않은 할 일의 ID가 주어진다면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("수정할 할 일을 찾을 수 없다는 경고 메시지를 반환한다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> taskService.updateTask(100L, task))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        Task task;

        @Nested
        @DisplayName("저장된 할 일의 ID가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                task = new Task();

                taskService.createTask(task);
            }

            @Test
            @DisplayName("해당 ID를 갖는 할 일을 삭제하고 반환한다")
            void it_returns_deleted_task() {
                int oldSize = taskService.getTasks().size();

                taskService.deleteTask(1L);

                int newSize = taskService.getTasks().size();

                assertThat(oldSize - newSize).isEqualTo(1);
                assertThat(taskService.getTasks()).isNotIn(1L);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 할 일의 ID가 주어진다면")
        class Context_without_invalid_id {

            @Test
            @DisplayName("삭제할 할 일을 찾을 수 없다는 경고 메시지를 반환한다")
            void it_returns_warning_message() {
                assertThatThrownBy(() -> taskService.deleteTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
