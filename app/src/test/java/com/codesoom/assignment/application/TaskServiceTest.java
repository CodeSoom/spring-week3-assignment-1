package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;
    private Long TASK_TEST_ID = 0L;
    private final static Long NOT_SAVED_TASK_ID = 100L;
    private final static String TASK_TITLE = "Task title";
    private final static String TASK_NOT_FOUND_MESSAGE = "Task not found: %d";

    @BeforeEach
    void setUp() {
        this.taskService = new TaskService();

        // given
        Task resource = new Task();
        resource.setTitle(TASK_TITLE);
        resource = taskService.createTask(resource);
        TASK_TEST_ID = resource.getId();
    }

    @Test
    @DisplayName("getTasks는 모든 할 일 목록을 반환한다.")
    void getTasks() {
        assertThat(taskService.getTasks()).hasSize(1);
        assertThat(taskService.getTasks()).isInstanceOf(List.class);
    }


    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        private Task task;

        @BeforeEach
        void prepare() {
            task = taskService.getTask(TASK_TEST_ID);
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재한다면")
        class Context_with_a_valid_id {

            @Test
            @DisplayName("할 일을 찾아서 반환한다.")
            void it_returns_a_task() {
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재하지 않는다면")
        class Context_with_a_invalid_id {

            @Test
            @DisplayName("예외를 발생한다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskService.getTask(NOT_SAVED_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);

                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.getTask(NOT_SAVED_TASK_ID))
                        .withMessage(String.format(TASK_NOT_FOUND_MESSAGE, NOT_SAVED_TASK_ID));
            }
        }
    }

    @Test
    @DisplayName("createTask는 새로운 할 일을 저장하여 반환한다.")
    void createTask() {
        // given
        String newTitle = "New " +TASK_TITLE;
        int oldSize = taskService.getTasks().size();
        Task newTask = new Task();
        newTask.setTitle(newTitle);

        // when
        Task task = taskService.createTask(newTask);

        // then
        Task findTask = taskService.getTask(task.getId());
        int newSize = taskService.getTasks().size();

        assertThat(findTask.getTitle()).isEqualTo(newTitle);
        assertThat(newSize).isEqualTo(oldSize + 1);
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        private Task resource;
        private final static String updatedTitle = "Updated " +TASK_TITLE;

        @BeforeEach
        void prepare() {
            resource = new Task();
            resource.setTitle(updatedTitle);
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재한다면")
        class Context_with_a_valid_id {
            @Test
            @DisplayName("할 일을 수정하여 반환한다.")
            void it_returns_a_updated_task() {
                Task task = taskService.updateTask(TASK_TEST_ID, resource);
                assertThat(task.getTitle()).isEqualTo(updatedTitle);
            }
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재하지 않는다면")
        class Context_with_a_invalid_id {
            @Test
            @DisplayName("예외를 발생한다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskService.updateTask(NOT_SAVED_TASK_ID, resource))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {

        private int oldSize = 0;

        @BeforeEach
        void prepare() {
            oldSize = taskService.getTasks().size();
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재한다면")
        class Context_with_a_valid_id {
            @Test
            @DisplayName("할 일을 삭제한다.")
            void it_returns_a_deleted_task() {
                taskService.deleteTask(TASK_TEST_ID);

                int newSize = taskService.getTasks().size();

                assertThatThrownBy(() -> taskService.getTask(TASK_TEST_ID))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThat(newSize).isEqualTo(oldSize - 1);
            }
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재하지 않는다면")
        class Context_with_a_invalid_id {
            @Test
            @DisplayName("예외를 발생한다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(NOT_SAVED_TASK_ID))
                    .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}