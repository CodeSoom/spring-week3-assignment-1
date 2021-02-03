package com.codesoom.assignment.controllers.controllers.controller;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.controllers.TaskErrorAdvice;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;
    private TaskErrorAdvice taskErrorAdvice;
    private ErrorResponse errorResponse;

    private final String TASK_TITLE = "task1";
    private final int EXISTING_TASK_COUNT = 1;
    private final Long EXISTING_ID = 1L;
    private final String NEW_TASK_TITLE = "changeTaskTitle";
    private final String NOT_FOUND_ID_ERROR_MESSAGE = "할 일 목록에서 해당하는 id를 찾을 수 없습니다.";

    private Task task1;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        task1 = new Task();
        task1.setTitle(TASK_TITLE);
        taskService.createTask(task1);
    }

    @AfterEach
    public void afterEach() {
        taskService.cleartasks();
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("할 일 목록에 저장된 데이터가 있으면")
        class Context_with_tasks {
            @Test
            @DisplayName("할 일이 저장된 개수만큼 숫자를 리턴한다.")
            void It_return_number_of_tasks() {
                assertThat(controller.list()).hasSize(EXISTING_TASK_COUNT);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("찾는 id가 목록에 있으면")
        class Context_contains_target_id {
            @Test
            @DisplayName("그 id에 해당하는 할 일 제목을 리턴한다.")
            void It_returns_task_title() {
                Task task = controller.detail(EXISTING_ID);

                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("새로운 할 일을 생성하면")
        class Context_create_new_task {
            @BeforeEach
            void create_Task() {
                Task task2 = new Task();
                task2.setTitle("Test2");
                controller.create(task2);
            }

            @Test
            @DisplayName("그 할 일을 리턴한다.")
            void It_return_new_task() {
                assertThat(taskService.getTask(EXISTING_ID + 1).getTitle()).isEqualTo("Test2");
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("제목을 수정하면")
        class Context_edit_title_put {
            Task edit_task() {
                Task task = taskService.getTask(EXISTING_ID);
                task.setTitle(NEW_TASK_TITLE);
                controller.update(EXISTING_ID, task);
                return task;
            }

            @Test
            @DisplayName("기존 제목에서 수정된 제목으로 변경하여 리턴한다.")
            void It_returns_newTitle() {

                assertThat(edit_task().getTitle()).isEqualTo(NEW_TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        @Nested
        @DisplayName("제목을 수정하면")
        class Context_edit_title_patch {
            Task edit_task() {
                Task task = taskService.getTask(EXISTING_ID);
                task.setTitle(NEW_TASK_TITLE);
                controller.patch(EXISTING_ID, task);
                return task;
            }

            @Test
            @DisplayName("기존 제목에서 수정된 제목으로 변경하여 리턴한다.")
            void It_returns_newTitle() {

                assertThat(edit_task().getTitle()).isEqualTo(NEW_TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("삭제하려는 id가 목록에 있으면")
        class Context_delete_target_id {
            void search_task_by_id() {
                Task task = taskService.getTask(EXISTING_ID);
            }

            @Test
            @DisplayName("그 id에 해당하는 할 일을 목록에서 삭제한다.")
            void It_delete_task() {
                controller.delete(EXISTING_ID);
            }
        }
    }

    @Nested
    @DisplayName("handleNotFound 메소드는")
    class Describe_handleNotFound {
        @Nested
        @DisplayName("찾는 id가 목록에 없으면")
        class Context_not_found_id_in_tasks {
            void not_found_id() {
                assertThatThrownBy(() -> taskService.getTask(100L))
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
    @DisplayName("ErrorResponse 메소드는")
    class Describe_ErrorResponse {
        @Nested
        @DisplayName("찾는 id가 목록에 없으면")
        class Context_not_found_id {
            void not_found_id() {
                assertThatThrownBy(() -> taskService.getTask(100L))
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
