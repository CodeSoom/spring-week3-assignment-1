package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    private Task generateTask(long id, String title) {
        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTitle(title);
        return newTask;
    }

    private TaskService generateTaskService(long taskCount) {
        TaskService newTaskService = new TaskService();
        for (long i = 1L; i <= taskCount; i++) {
            Task newTask = generateTask(i, String.format("task%d", i));
            newTaskService.createTask(newTask);
        }
        return newTaskService;
    }

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_of_list {

        @Nested
        @DisplayName("만약 '할 일 목록'이 비어있다면")
        class Context_of_empty_tasks {

            @Test
            @DisplayName("비어있는 리스트를 반환한다")
            void it_returns_empty_array() {
                List<Task> tasks = taskController.list();
                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 '할 일 목록'이 비어있지 않다면")
        class Context_of_not_empty_tasks {

            private TaskService size1;
            private TaskService size2;
            private TaskService size100;
            private TaskController controller1;
            private TaskController controller2;
            private TaskController controller100;

            @BeforeEach
            void setTasksNotEmpty() {
                size1 = generateTaskService(1);
                size2 = generateTaskService(2);
                size100 = generateTaskService(100);

                controller1 = new TaskController(size1);
                controller2 = new TaskController(size2);
                controller100 = new TaskController(size100);
            }

            @Test
            @DisplayName("'할 일 목록'에 등록된 모든 '할 일'을 리스트로 반환한다")
            void it_returns_task_array() {
                List<Task> tasks = controller1.list();
                assertThat(tasks)
                        .hasSize(1);

                tasks = controller2.list();
                assertThat(tasks)
                        .hasSize(2);

                tasks = controller100.list();
                assertThat(tasks)
                        .hasSize(100);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_of_getTask {

        private Task givenTask;

        @Nested
        @DisplayName("등록된 '할 일'들이 있을 때")
        class Context_of_appended_task {

            @BeforeEach
            void appendSourceToTasks() {
                givenTask = generateTask(1L, "task1");
                givenTask = taskService.createTask(givenTask);
            }

            @Nested
            @DisplayName("만약 존재하는 '할 일'의 id가 인자로 주어지면")
            class Context_of_exist_id {

                private long existId;

                @BeforeEach
                void setValidId() {
                    existId = givenTask.getId();
                }

                @Test
                @DisplayName("'할 일'을 반환한다")
                void it_returns_task() {
                    Task task = taskController.detail(existId);
                    assertThat(task)
                            .isEqualTo(givenTask);
                }
            }

            @Nested
            @DisplayName("만약 존재하지 않는 '할 일'의 id가 인자로 주어지면")
            class Context_of_non_existent_id {

                private long nonExistentId;

                @BeforeEach
                void setInvalidId() {
                    taskController.delete(givenTask.getId());
                    nonExistentId = givenTask.getId();
                }

                @Test
                @DisplayName("'할 일'을 찾을 수 없다는 예외를 던진다")
                void it_throws_exception() {
                    Throwable thrown = catchThrowable(() -> { taskController.detail(nonExistentId); });
                    assertThat(thrown)
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessageContaining("Task not found");
                }
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_of_create {

        @Nested
        @DisplayName("만약 Task 객체가 인자로 주어지면")
        class Context_of_valid_task_object {

            private Task givenTask;

            @BeforeEach
            void setSource() {
                givenTask = generateTask(1L, "task1");
            }

            @Test
            @DisplayName("'할 일'을 추가하고 추가한 '할 일'을 반환한다")
            void it_returns_task_appending_task_to_tasks() {
                Task createdTask = taskController.create(givenTask);
                assertThat(createdTask)
                        .isEqualTo(givenTask)
                        .withFailMessage("추가한 '할 일'를 반환하지 않았다");

                createdTask = taskController.detail(givenTask.getId());
                assertThat(createdTask)
                        .isEqualTo(givenTask)
                        .withFailMessage("'할 일'이 추가되지 않았다");
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_of_update {

        @Nested
        @DisplayName("만약 유효한 id와 Task 객체가 인자로 주어지면")
        class Context_of_valid_id_and_task {

            private Task givenTask;
            private Task destTask;

            @BeforeEach
            void createTaskAnd() {
                givenTask = generateTask(1L, "givenTask");
                destTask = generateTask(1L, "destTask");

                taskService.createTask(givenTask);
            }

            @Test
            @DisplayName("'할 일'을 갱신하고, 갱신한 '할 일'을 반환한다")
            void it_returns_task_updating_it() {
                Task updatedTask = taskController.update(givenTask.getId(), destTask);
                assertThat(updatedTask)
                        .isEqualTo(destTask)
                        .withFailMessage("갱신한 '할 일'이 반환되지 않았다");

                updatedTask = taskController.detail(givenTask.getId());
                assertThat(updatedTask)
                        .isEqualTo(destTask)
                        .withFailMessage("'할 일'이 갱신되지 않았다");
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_of_patch {

        @Nested
        @DisplayName("만약 유효한 id와 Task 객체가 인자로 주어지면")
        class Context_of_valid_id_and_task {

            private Task givenTask;
            private Task destTask;

            @BeforeEach
            void createTaskAnd() {
                givenTask = generateTask(1L, "givenTask");
                destTask = generateTask(1L, "destTask");

                taskService.createTask(givenTask);
            }

            @Test
            @DisplayName("'할 일'을 갱신하고, 갱신한 '할 일'을 반환한다")
            void it_returns_task_updating_it() {
                Task updatedTask = taskController.update(givenTask.getId(), destTask);
                assertThat(updatedTask)
                        .isEqualTo(destTask)
                        .withFailMessage("갱신한 '할 일'이 반환되지 않았다");

                updatedTask = taskController.detail(givenTask.getId());
                assertThat(updatedTask)
                        .isEqualTo(destTask)
                        .withFailMessage("'할 일'이 갱신되지 않았다");
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_of_delete {

        @Nested
        @DisplayName("만약 유효한 id가 인자로 주어지면")
        class Context_of_valid_id {

            private Task deleteTargetTask;
            private long validId;
            private long beforeCount;
            private long afterCount;

            @BeforeEach
            void setUp() {
                deleteTargetTask = generateTask(1L, "task1");
                Task task = generateTask(2L, "task2");

                taskService.createTask(deleteTargetTask);
                taskService.createTask(task);

                validId = deleteTargetTask.getId();
                beforeCount = taskController.list().size();
            }

            @Test
            @DisplayName("해당되는 '할 일'을 '할 일 목록'에서 삭제한다")
            void it_returns_noting() {
                taskController.delete(validId);

                afterCount = taskController.list().size();

                assertThat(beforeCount - 1)
                        .isEqualTo(afterCount);
            }
        }
    }
}
