package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;
    private final static Long TASK_TEST_ID1 = 1L;
    private final static Long TASK_TEST_ID2 = 100L;
    private final static String TASK_TITLE = "Task title";

    @BeforeEach
    void setUp() {
        this.taskService = new TaskService();
        this.taskController = new TaskController(taskService);

        // given
        Task resource = new Task();
        resource.setTitle(TASK_TITLE);
        taskController.create(resource);
    }

    @Test
    @DisplayName("list 메소드는 모든 할 일 목록을 반환한다.")
    void list() {
        List<Task> tasks = taskController.list();
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재한다면")
        class Context_with_a_valid_id {

            @Test
            @DisplayName("할 일을 찾아서 반환한다.")
            void it_returns_a_task() {
                Task task = taskController.detail(TASK_TEST_ID1);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재하지 않는다면")
        class Context_with_a_invalid_id {

            @Test
            @DisplayName("예외를 발생한다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskController.detail(TASK_TEST_ID2))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Test
    @DisplayName("create 메소드는 새로운 할 일을 저장하여 반환한다.")
    void create() {
        // given
        String newTitle = "New " +TASK_TITLE;
        int oldSize = taskController.list().size();
        Task newTask = new Task();
        newTask.setTitle(newTitle);

        // when
        Task task = taskController.create(newTask);

        // then
        Task findTask = taskController.detail(task.getId());
        int newSize = taskController.list().size();

        assertThat(findTask.getTitle()).isEqualTo(newTitle);
        assertThat(newSize).isEqualTo(oldSize + 1);
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

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
                Task task = taskController.update(TASK_TEST_ID1, resource);
                assertThat(task.getTitle()).isEqualTo(updatedTitle);
            }
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재하지 않는다면")
        class Context_with_a_invalid_id {
            @Test
            @DisplayName("예외를 발생한다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskController.update(TASK_TEST_ID2, resource))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {

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
                Task task = taskController.patch(TASK_TEST_ID1, resource);
                assertThat(task.getTitle()).isEqualTo(updatedTitle);
            }
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재하지 않는다면")
        class Context_with_a_invalid_id {
            @Test
            @DisplayName("예외를 발생한다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskController.patch(TASK_TEST_ID2, resource))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        private int oldSize = 0;

        @BeforeEach
        void prepare() {
            oldSize = taskController.list().size();
        }

        @Nested
        @DisplayName("아이디가 할 일 목록에 존재한다면")
        class Context_with_a_valid_id {
            @Test
            @DisplayName("할 일을 삭제한다.")
            void it_returns_a_deleted_task() {
                taskController.delete(TASK_TEST_ID1);

                int newSize = taskController.list().size();

                assertThatThrownBy(() -> taskController.detail(TASK_TEST_ID1))
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
                assertThatThrownBy(() -> taskController.delete(TASK_TEST_ID2))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}