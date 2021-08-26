package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskService taskService = new TaskService();
    private TaskController taskController = new TaskController(taskService);

    private final String[] TASK_TITLE = {"","test1", "test2", "test3", "test4", "test5"};
    private final String TASK_UPDATE = "update";

    @BeforeEach
    @DisplayName("Task 객체 초기화")
    void setUp() {

        Task task1 = new Task();
        task1.setTitle(TASK_TITLE[1]);

        Task task2 = new Task();
        task2.setTitle(TASK_TITLE[2]);

        Task task3 = new Task();
        task2.setTitle(TASK_TITLE[3]);

        taskController.create(task1);
        taskController.create(task2);
        taskController.create(task3);

    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("tasks 리스트에 값이 존재하면")
        class Context_exist_tasks {

            @Test
            @DisplayName("task 객체들을 리턴한다")
            void It_return_tasks() throws Exception {

                assertThat(taskController.list()).hasSize(3);

            }

        }

        @Nested
        @DisplayName("tasks 리스트에 값이 존재하지 않는다면")
        class Context_exist_not_tasks {

            @Test
            @DisplayName("비어있는 list를 리턴한다")
            void It_return_tasks() throws Exception {

                taskController.list().clear();
                assertThat(taskController.list()).isEmpty();

            }

        }

    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("list에 추가할 Task 객체가 있다면")
        class Context_add_task {

            @Test
            @DisplayName("list에 Task 객체를 추가한다")
            void It_add_list() {

                Task task4 = new Task();
                task4.setTitle(TASK_TITLE[4]);

                taskController.create(task4);

                assertThat(taskController.list()).hasSize(4);
                assertThat(taskController.detail(4L).getTitle()).isEqualTo(TASK_TITLE[4]);

            }

        }

    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("list에 id가 존재하면")
        class Context_exist_id {

            @Test
            @DisplayName("Task 객체를 리턴한다")
            void It_return_task() throws Exception {

                Task foundTask = taskController.detail(1L);
                assertThat(foundTask.getTitle()).isEqualTo(TASK_TITLE[1]);

            }

        }

        @Nested
        @DisplayName("list에 id가 존재하지 않는다면")
        class Context_exist_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void It_exception_detail() throws Exception {

                assertThatThrownBy(() -> {
                    taskService.getTask(100L);
                }).isInstanceOf(TaskNotFoundException.class);
            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정하고 싶은 id가 존재하면")
        class Context_exist_update_id {

            @Test
            @DisplayName("list에서 id를 찾아 값을 수정한다")
            void It_update() {

                Task updateTask = new Task();
                updateTask.setTitle(TASK_TITLE[2]+TASK_UPDATE);

                assertThat(taskController.update(2L, updateTask).getTitle()).isEqualTo(updateTask.getTitle());

            }

        }

        @Nested
        @DisplayName("수정하고 싶은 id가 존재하지 않는다면")
        class Context_exist_update_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void It_exception_update() {

                Task updateTask = new Task();
                updateTask.setTitle(TASK_TITLE[2]+TASK_UPDATE);

                assertThatThrownBy(() -> {
                    taskController.update(200L, updateTask);
                }).isInstanceOf(TaskNotFoundException.class);

            }

        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제할 id가 존재하면")
        class Context_exist_delete_id {

            @Test
            @DisplayName("list에서 해당 id의 Task 객체를 삭제한다")
            void It_delete_id() {

                taskController.delete(3L);
                assertThat(taskController.list()).hasSize(2);

            }
        }

        @Nested
        @DisplayName("삭제할 id가 존재하지 않는다면")
        class Context_exist_delete_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void It_exception_delete() {
                assertThatThrownBy(() -> {
                    taskController.delete(100L);
                }).isInstanceOf(TaskNotFoundException.class);
            }

        }

    }

}
