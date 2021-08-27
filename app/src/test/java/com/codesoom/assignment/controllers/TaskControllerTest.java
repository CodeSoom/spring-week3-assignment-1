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
import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskService taskService = new TaskService();
    private TaskController taskController = new TaskController(taskService);

    private final String[] TASK_TITLE = {"test1", "test2", "test3", "test4", "test5"};
    private final String TASK_UPDATE = "update";
    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 100L;

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("tasks 리스트에 값이 존재하면")
        class Context_exist_tasks {

            @BeforeEach
            @DisplayName("list에 값을 세팅해줍니다")
            void tasks_setUp() {

                for (int i = 0; i < TASK_TITLE.length; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE[i]);
                    taskController.create(task);
                }

            }

            @Test
            @DisplayName("task 객체들을 리턴한다")
            void It_return_tasks() throws Exception {

                Assertions.assertThat(taskController.list()).hasSize(5);

            }

        }

        @Nested
        @DisplayName("tasks 리스트에 값이 존재하지 않는다면")
        class Context_exist_not_tasks {

            @BeforeEach
            @DisplayName("리스트의 값을 비워줍니다")
            void tasks_empty_setUp() {

                taskController.list().clear();

            }

            @Test
            @DisplayName("비어있는 list를 리턴한다")
            void It_return_tasks() throws Exception {

                Assertions.assertThat(taskController.list()).isEmpty();

            }

        }

    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("list에 추가할 Task 객체가 있다면")
        class Context_add_task {

            Task task = new Task();

            @BeforeEach
            @DisplayName("Task 객체를 세팅합니다")
            void create_setUo() {

                task.setTitle(TASK_TITLE[0]);

            }

            @Test
            @DisplayName("list에 Task 객체를 추가한다")
            void It_add_list() {

                taskController.create(task);

                assertEquals(taskController.detail(VALID_ID).getId(),VALID_ID);
                assertEquals(taskController.detail(VALID_ID).getTitle(), TASK_TITLE[0]);

            }

        }

    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("list에 id가 존재하면")
        class Context_exist_id {

            Task foundTask = new Task();

            @BeforeEach
            @DisplayName("찾을 Task 객체를 세팅합니다")
            void detail_setUp() {

                Task task = new Task();
                task.setTitle(TASK_TITLE[0]);
                taskController.create(task);

            }

            @Test
            @DisplayName("Task 객체를 리턴한다")
            void It_return_task() throws Exception {

                foundTask = taskController.detail(VALID_ID);

                assertEquals(taskController.detail(VALID_ID).getId(),VALID_ID);
                assertEquals(taskController.detail(VALID_ID).getTitle(), TASK_TITLE[0]);

            }

        }

        @Nested
        @DisplayName("list에 id가 존재하지 않는다면")
        class Context_exist_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void It_exception_detail() throws Exception {

                It_return_taskNotFoundException();

            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정하고 싶은 id가 존재하면")
        class Context_exist_update_id {

            Task updateTask = new Task();

            @BeforeEach
            @DisplayName("수정할 Task 객체를 세팅합니다")
            void update_setUp() {

                Task task = new Task();
                task.setTitle(TASK_TITLE[0]);
                taskController.create(task);

            }

            @Test
            @DisplayName("list에서 id를 찾아 값을 수정한다")
            void It_update() {

                updateTask.setTitle(TASK_TITLE[0] + TASK_UPDATE);
                taskController.update(VALID_ID, updateTask);

                assertEquals(taskController.detail(VALID_ID).getTitle(), TASK_TITLE[0] + TASK_UPDATE);

            }

        }

        @Nested
        @DisplayName("수정하고 싶은 id가 존재하지 않는다면")
        class Context_exist_update_not_id {

            Task updateTask = new Task();

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void It_exception_update() {

                updateTask.setTitle(TASK_TITLE[0]+TASK_UPDATE);

                It_return_taskNotFoundException();

            }

        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제할 id가 존재하면")
        class Context_exist_delete_id {

            @BeforeEach
            @DisplayName("삭제할 값을 세팅합니다")
            void delete_setUp() {

                for (int i = 0; i < TASK_TITLE.length; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE[i]);
                    taskController.create(task);
                }

            }

            @Test
            @DisplayName("list에서 해당 id의 Task 객체를 삭제한다")
            void It_delete_id() {

                taskController.delete(VALID_ID);
                assertThat(taskController.list()).hasSize(4);

            }
        }

        @Nested
        @DisplayName("삭제할 id가 존재하지 않는다면")
        class Context_exist_delete_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void It_exception_delete() {

                It_return_taskNotFoundException();

            }

        }

    }

    private void It_return_taskNotFoundException() {
        assertThatThrownBy(() -> {
            taskService.getTask(INVALID_ID);
        }).isInstanceOf(TaskNotFoundException.class);
    }

}
