package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;
    private Task task = new Task(1L, "title");


    @Nested
    @DisplayName("list method")
    class Describe_list {

        @Nested
        @DisplayName("when nothing is given")
        class Context_nothing {
            @BeforeEach
            void setUp() {
                taskService = new TaskService();
                taskController = new TaskController(taskService);
            }

            @Test
            @DisplayName("returns an empty list")
            void it_returns_empty_list() {

                assertThat(taskController.list()).isEmpty();
            }
        }
    }

    @DisplayName("create method")
    @Nested
    class Describe_create {
        @DisplayName("if one id is created")
        @Nested
        class Context_one_id {
            @BeforeEach
            void setUp() {
                taskService = new TaskService();
                taskController = new TaskController(taskService);
            }
            @DisplayName("returns a created task")
            @Test
            void it_returns_created_task() {
                Task createdTask = taskController.create(new Task(1L, "title"));
                assertThat(createdTask.getId()).isEqualTo(1L);
                assertThat(createdTask.getTitle()).isEqualTo("title");
            }

        }
    }

    @Nested
    @DisplayName("detail method")
    class Describe_detail {

        @Nested
        @DisplayName("if a task is seached ")
        class Context_created_detailed {
            private Task detail;

            @BeforeEach
            void setUp() {
                taskService = new TaskService();
                taskController = new TaskController(taskService);
                taskService.createTask(task);
                detail = taskController.detail(1L);
            }

            @Test
            @DisplayName("returns task with a valid id")
            void it_returns_task_with_validId() {

                assertThat(detail.getId()).isEqualTo(task.getId());
            }

            @Test
            @DisplayName("throws exceptions")
            void it_returns_task_with_invalidId() {

                Throwable throwable = catchThrowable(() -> taskController.detail(2L));
                assertThat(throwable).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("detail method")
    class Describe_update {

        @Nested
        @DisplayName("if a task is updated ")
        class Context_created_task {

            @BeforeEach
            void setUp() {
                taskService = new TaskService();
                taskController = new TaskController(taskService);
                taskService.createTask(task);

            }

            @Test
            @DisplayName("returns an updated task")
            void it_returns_updated_task() {

                Task updatedTask = taskController.update(1L, task);
                //then
                assertThat(updatedTask.getTitle()).isEqualTo(updatedTask.getTitle());
                assertThat(updatedTask.getId()).isEqualTo(updatedTask.getId());
            }

            @DisplayName("throws exceptions if invalid id is given")
            @Test
            void it_throws_exception() {
                Throwable throwable = catchThrowable(() -> taskController.update(2L, task));
                assertThat(throwable).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @DisplayName("")
    @Nested
    class Describe_delete{

        @DisplayName("if a task is deleted")
        @Nested
        class Context_id_deleted{
            private Task createdTask;
            @BeforeEach
            void setup() {
                taskService = new TaskService();
                taskController = new TaskController(taskService);
                createdTask = taskService.createTask(task);
            }

            @DisplayName("returns a deleted task")
            @Test
            void it_returns_deleted_task() {
                Task deletedTask = taskController.delete(1L);
                assertThat(createdTask.getTitle()).isEqualTo(deletedTask.getTitle());
                assertThat(createdTask.getId()).isEqualTo(deletedTask.getId());
            }

            @DisplayName("throws an exception if an invalid id is given")
            @Test
            void it_throws_exception() {
                Throwable throwable = catchThrowable(() -> taskController.delete(2L));
                assertThat(throwable).isInstanceOf(TaskNotFoundException.class);
            }
        }

    }



//    @Test
//    @DisplayName("add two tasks and checks the size")
//    void createTwoTasks() {
//        Task task1 = new Task(1L, "title1");
//        taskController.create(task1);
//        Task task2 = new Task(2L, "title2");
//        taskController.create(task2);
//
//        assertThat(taskController.list()).hasSize(2);
//    }
//
//
//

}


