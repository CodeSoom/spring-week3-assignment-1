package com.codesoom.assignment.controllers.controllers.controller;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private final String TASK_TITLE = "task1";
    private final int EXISTING_ID_COUNT = 1;
    private final Long EXSITING_ID = 1L;
    private final String NEW_TASK_TITLE = "changeTaskTitle";

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
    @DisplayName("할 일 목록에 저장된 데이터가 있으면")
    class Context_with_tasks {
        @Test
        @DisplayName("할 일이 저장된 개수만큼 숫자를 리턴한다.")
        void It_return_number_of_tasks() {
            assertThat(controller.list()).hasSize(EXISTING_ID_COUNT);
        }
    }

    @Nested
    @DisplayName("찾는 id가 목록에 있으면")
    class Context_contains_target_id {
        @Test
        @DisplayName("그 id에 해당하는 할 일 제목을 리턴한다.")
        void It_returns_task_title() {
            Task searchTask = controller.detail(EXSITING_ID);

            assertThat(searchTask.getId()).isEqualTo(EXSITING_ID);
            assertThat(searchTask.getTitle()).isEqualTo(TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("새로운 할 일 목록을 생성하면")
    class Context_create_task {
        @BeforeEach
        void create_Task() {
            Task task2 = new Task();
            task2.setTitle("Test2");
            controller.create(task2);
        }

        @Test
        @DisplayName("할 일이 저장된 목록 개수만큼 숫자를 리턴한다.")
        void It_return_number_of_tasks() {
            assertThat(controller.list()).hasSize(EXISTING_ID_COUNT + 1);
        }
    }

    @Nested
    @DisplayName("제목을 수정하면")
    class Context_edit_title_put {
        Task edit_task() {
            Task task = taskService.getTask(EXSITING_ID);
            task.setTitle(NEW_TASK_TITLE);
            controller.update(EXSITING_ID, task);
            return task;
        }

        @Test
        @DisplayName("기존 제목에서 수정된 제목으로 변경하여 리턴한다.")
        void It_returns_newTitle() {
            assertThat(edit_task().getTitle()).isEqualTo(NEW_TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("제목을 수정하면")
    class Context_edit_title_patch {
        Task edit_task() {
            Task task = taskService.getTask(EXSITING_ID);
            task.setTitle(NEW_TASK_TITLE);
            controller.patch(EXSITING_ID, task);
            return task;
        }

        @Test
        @DisplayName("기존 제목에서 수정된 제목으로 변경하여 리턴한다.")
        void It_returns_newTitle() {
            assertThat(edit_task().getTitle()).isEqualTo(NEW_TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("삭제하려는 id가 목록에 있으면")
    class Context_delete_target_id {
        void search_task_by_id() {
            Task task = taskService.getTask(EXSITING_ID);
        }

        @Test
        @DisplayName("그 id에 해당하는 할 일을 목록에서 삭제한다.")
        void It_delete_task() {
            controller.delete(EXSITING_ID);
        }
    }

}
