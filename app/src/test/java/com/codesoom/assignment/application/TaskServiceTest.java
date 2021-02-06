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
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        // fixture
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("Task가 존재한다면")
        class Context_exist_task {

            @Test
            @DisplayName("Task 목록을 반환한다 ")
            void it_returns_list() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(1);

                Task task = tasks.get(0);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("유효한 ID가 주어진다면")
        class Context_with_valid_id {

            @Test
            @DisplayName("해당 ID를 갖는 Task를 반환한다")
            void it_returns_task() {
                Task task = taskService.getTask(1L);
                assertThat(task.getTitle()).isEqualTo("test");
            }
        }

        @Nested
        @DisplayName("유효하지 않은 ID가 주어진다면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("Task를 찾을 수 없다는 경고 메시지를 반환한다")
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
        @DisplayName("Task를 생성한다")
        void it_creates_task() {
            int oldSize = taskService.getTasks().size();

            Task task = new Task();
            Task newTask = taskService.createTask(task);

            int newSize = taskService.getTasks().size();

            assertThat(newSize - oldSize).isEqualTo(1);
            assertThat(taskService.getTasks()).contains(newTask);
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("Task를 삭제해야 하는 경우")
        class Context_delete_id {

            @Test
            @DisplayName("")
            void it_deletes_task() {
                int oldSize = taskService.getTasks().size();

                taskService.deleteTask(1L);

                int newSize = taskService.getTasks().size();

                assertThat(oldSize - newSize).isEqualTo(1);
            }
        }
    }

    @Test
    void updateTask() {
        Task source = new Task();
        source.setTitle(UPDATE_POSTFIX + "!!!");
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + "!!!");
    }
}
