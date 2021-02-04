package com.codesoom.assignment.controller;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.controllers.TaskErrorAdvice;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;
    private TaskErrorAdvice taskErrorAdvice;
    private ErrorResponse errorResponse;

    private final String TASK_TITLE = "task1";
    private final long TASK_ID = 1L;
    private final long NOT_EXISTING_TASK_ID = 100L;
    private final String NOT_FOUND_ID_ERROR_MESSAGE = "할 일 목록에서 해당하는 id를 찾을 수 없습니다.";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("할 일이 있으면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("할 일 목록을 리턴한다.")
            void It_return_number_of_tasks() {
                assertThat(controller.list()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("할 일이 없으면")
        class Context_with_no_task {

            @Test
            @DisplayName("비어있는 배열을 리턴한다.")
            void It_return_empty_array() {
                assertThat(controller.list()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("detail은")
    class Describe_detail {

        @Nested
        @DisplayName("찾는 id가 목록에 있으면")
        class Context_contains_target_id {

            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setId(TASK_ID);
                task.setTitle(TASK_TITLE);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("그 id에 해당하는 할 일을 리턴한다.")
            void It_returns_task() {
                Task task = controller.detail(TASK_ID);

                assertThat(task).isEqualTo(taskService.getTask(TASK_ID));
            }
        }

        @Nested
        @DisplayName("찾는 id가 목록에 없으면")
        class Context_not_contains_target_id {
            void not_found_id() {
                assertThatThrownBy(() -> taskService.getTask(NOT_EXISTING_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void It_retruns_exception() {
                taskErrorAdvice = new TaskErrorAdvice();
                taskErrorAdvice.handleNotFound();
            }
        }
    }

    @Nested
    @DisplayName("create는")
    class Describe_create {
        Task newTask = new Task();

        @BeforeEach
        void setUp() {
            newTask.setTitle("newTaskTitle");
        }

        @Test
        @DisplayName("새롭게 생성한 할 일을 리턴한다.")
        void It_return_new_task() {
            Task task = controller.create(newTask);
            assertThat(task.getTitle()).isEqualTo("newTaskTitle");

        }
    }

    @Nested
    @DisplayName("update는")
    class Describe_update {
        Task originTask = new Task();

        @BeforeEach
        void setUp() {
            originTask.setId(TASK_ID);
            controller.create(originTask);
        }

        @Test
        @DisplayName("수정된 할 일을 리턴한다.")
        void It_returns_updated_task() {
            originTask.setTitle("Other title");
            Task task = controller.update(TASK_ID, originTask);

            assertThat(task.getTitle()).isEqualTo("Other title");
        }
    }

    @Nested
    @DisplayName("patch는")
    class Describe_patch {
        Task originTask = new Task();

        @BeforeEach
        void setUp() {
            originTask.setId(TASK_ID);
            controller.create(originTask);
        }

        @Test
        @DisplayName("수정된 할 일을 리턴한다.")
        void It_returns_updated_task() {
            originTask.setTitle("Other title");
            Task task = controller.patch(TASK_ID, originTask);

            assertThat(task.getTitle()).isEqualTo("Other title");
        }
    }

    @Nested
    @DisplayName("delete는")
    class Describe_delete {
        Task originTask = new Task();

        @Nested
        @DisplayName("삭제하려는 id가 목록에 있으면")
        class Context_contains_target_id {
            @BeforeEach
            void setUp() {
                originTask.setId(TASK_ID);
                originTask.setTitle(TASK_TITLE);
                taskService.createTask(originTask);
            }

            @Test
            @DisplayName("그 id에 해당하는 할 일을 목록에서 삭제한다.")
            void It_delete_task() {
                controller.delete(TASK_ID);

                assertThatThrownBy(() -> taskService.getTask(TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);

            }
        }

        @Nested
        @DisplayName("삭제하려는 id가 목록에 없으면")
        class Context_not_contains_target_id {

            @Test
            @DisplayName("예외를 던진다.")
            void It_retruns_exception() {
                taskErrorAdvice = new TaskErrorAdvice();
                taskErrorAdvice.handleNotFound();
            }
        }
    }

    @Nested
    @DisplayName("handleNotFound")
    class Describe_handleNotFound {

        @Nested
        @DisplayName("존재하지 않는 id가 주어진다면")
        class Context_not_found_id_in_tasks {

            @Test
            @DisplayName("예외를 던진다.")
            void It_retruns_exception() {
                assertThatThrownBy(() -> taskService.getTask(NOT_EXISTING_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);

                taskErrorAdvice = new TaskErrorAdvice();
                taskErrorAdvice.handleNotFound();
            }
        }
    }

    @Nested
    @DisplayName("ErrorResponse 메소드는")
    class Describe_ErrorResponse {
        @Nested
        @DisplayName("찾는 id가 목록에 없으면")
        class Context_not_found_id {
            @BeforeEach
            void setUp() {
                assertThatThrownBy(() -> taskService.getTask(NOT_EXISTING_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("에러메세지를 보낸다.")
            void It_retruns_error_message() {
                errorResponse = new ErrorResponse(NOT_FOUND_ID_ERROR_MESSAGE);
                assertThat(errorResponse.getMessage()).isEqualTo(NOT_FOUND_ID_ERROR_MESSAGE);
            }
        }
    }
}
