package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    private final String TASK_TITLE = "test";
    private final String UPDATED_TASK_TITLE = "updated-test";
    private final int DEFAULT_TASK_COUNT = 1;
    private final Long DEFAULT_TASK_ID = 1L;


    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Nested
    @DisplayName("할 일 목록에 데이터가 있으면")
    class Context_have_tasks {
        @Test
        @DisplayName("할 일 목록의 데이터 갯수를 리턴한다")
       void it_returns_number_of_tasks() {

            assertThat(taskController.list().size()).isEqualTo(DEFAULT_TASK_COUNT);
        }
    }

    @Nested
    @DisplayName("할 일 목록에 해당 하는 ID가 있으면")
    class Context_has_task_by_id {

        Task detail() {
            return taskService.getTask(DEFAULT_TASK_ID);
        }

        @Test
        @DisplayName("할 일의 타이틀을 리턴한다")
        void it_returns_task() {
            assertThat(detail().getTitle()).isEqualTo(TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("할 일 목록에 해당 하는 ID가 없으면")
    class Context_has_no_task_by_id {

        Task detail() {
            return taskService.getTask(-1L);
        }

        @Test
        @DisplayName("에러를 리턴한다")
        void it_returns_error() {
            assertThatThrownBy(() -> detail())
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("새로운 할 일을 생성하면")
    class Context_create_task {
        @BeforeEach
        void create() {
            Task task = new Task();
            task.setTitle("test2");
            taskController.create(task);
        }

        @Test
        @DisplayName("할 일 목록의 갯수를 리턴한다")
        void it_returns_number_of_tasks() {
            assertThat(taskController.list()).hasSize(DEFAULT_TASK_COUNT + 1);
        }
    }

    @Nested
    @DisplayName("할 일을 수정하면")
    class Context_update_task {

        Task update() {
            Task task = taskService.getTask(DEFAULT_TASK_ID);
            task.setTitle(UPDATED_TASK_TITLE);
            return taskController.update(DEFAULT_TASK_ID, task);
        }

        @Test
        @DisplayName("변경된 할 일의 타이틀을 리턴한다")
        void it_returns_updated_task_title() {
            assertThat(update().getTitle()).isEqualTo(UPDATED_TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("할 일을 수정하면")
    class Context_patch_task {

        Task patch() {
            Task task = taskService.getTask(DEFAULT_TASK_ID);
            task.setTitle(UPDATED_TASK_TITLE);
            return taskController.update(DEFAULT_TASK_ID, task);
        }

        @Test
        @DisplayName("변경된 할 일의 타이틀을 리턴한다")
        void it_returns_patched_task_title() {
            assertThat(patch().getTitle()).isEqualTo(UPDATED_TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("특정 할 일을 삭제하면")
    class Context_delete_task {

        void delete() {
            taskController.delete(DEFAULT_TASK_ID);
        }

        @Test
        @DisplayName("할 일 목록의 갯수가 한 개 줄어든다")
        void it_reduces_one_from_task_list() {
            delete();
            assertThat(taskService.getTasks().size()).isEqualTo(DEFAULT_TASK_COUNT - 1);
        }
    }
}
