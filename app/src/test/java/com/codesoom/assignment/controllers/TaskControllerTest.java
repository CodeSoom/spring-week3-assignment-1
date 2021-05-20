package com.codesoom.assignment.controllers;

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

    private Task generateTask(long id, String title) {
        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTitle(title);
        return newTask;
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
        @DisplayName("만약 tasks가 비어있다면")
        class Context_of_empty_tasks {

            @Test
            @DisplayName("비어있는 배열을 반환한다")
            void it_returns_empty_array() {
                List<Task> tasks = taskController.list();
                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 tasks에 비어있지 않다면")
        class Context_of_not_empty_tasks {

            private Task task1;
            private Task task2;

            @BeforeEach
            void setTasksNotEmpty() {
                this.task1 = generateTask(1L, "task1");
                this.task2 = generateTask(2L, "task2");

                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @Test
            @DisplayName("두 Task를 배열로 반환한다")
            void it_returns_task_array() {
                List<Task> tasks = taskController.list();
                assertThat(tasks)
                        .hasSize(2)
                        .contains(task1, task2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_of_getTask {

        private Task source;

        @BeforeEach
        void appendSourceToTasks() {
            this.source = generateTask(1L, "task1");
            taskService.createTask(source);
        }

        @Nested
        @DisplayName("만약 유효한 id가 인자로 주어지면")
        class Context_of_valid_id {

            @Test
            @DisplayName("id에 해당하는 Task 객체를 반환한다")
            void it_returns_task() {
                Task task = taskController.detail(source.getId());
                assertThat(task)
                        .isEqualTo(source);
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 id가 인자로 주어지면")
        class Context_of_invalid_id {

            @Test
            @DisplayName("'Task not found' 메시지를 담은 예외를 던진다")
            void it_throws_exception() {
                long non_existent_id = source.getId() + 42;
                Throwable thrown = catchThrowable(() -> { taskController.detail(non_existent_id); });
                assertThat(thrown)
                        .hasMessageContaining("Task not found");
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_of_create {

        @Nested
        @DisplayName("만약 Task 객체가 인자로 주어지면")
        class Context_of_valid_task_object {

            private Task source;

            @BeforeEach
            void setSource() {
                this.source = generateTask(1L, "task1");
            }

            @Test
            @DisplayName("Task를 추가하고 추가한 Task를 반환한다")
            void it_returns_task_appending_task_to_tasks() {
                Task createdTask = taskController.create(source);
                assertThat(createdTask)
                        .isEqualTo(source);

                createdTask = taskController.detail(source.getId());
                assertThat(createdTask)
                        .isEqualTo(source);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_of_update {

        @Nested
        @DisplayName("만약 유효한 id와 Task 객체가 인자로 주어지면")
        class Context_of_valid_id_and_task {

            private Task source;
            private Task dest;

            @BeforeEach
            void createTaskAnd() {
                this.source = generateTask(1L, "task1");
                this.dest = generateTask(1L, "updatedTask");

                taskService.createTask(source);
            }

            @Test
            @DisplayName("주어진 인자에 따라 Task를 갱신하다")
            void it_returns_task_updating_it() {
                Task updatedTask = taskController.update(source.getId(), dest);
                assertThat(updatedTask)
                        .isEqualTo(dest);

                updatedTask = taskController.detail(source.getId());
                assertThat(updatedTask)
                        .isEqualTo(dest);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_of_patch {

        @Nested
        @DisplayName("만약 유효한 id와 Task 객체가 인자로 주어지면")
        class Context_of_valid_id_and_task {

            private Task source;
            private Task dest;

            @BeforeEach
            void createTaskAnd() {
                this.source = generateTask(1L, "task1");
                this.dest = generateTask(1L, "updatedTask");

                taskService.createTask(source);
            }

            @Test
            @DisplayName("주어진 인자에 따라 Task를 갱신하다")
            void it_returns_task_updating_it() {
                Task updatedTask = taskController.update(source.getId(), dest);
                assertThat(updatedTask)
                        .isEqualTo(dest);

                updatedTask = taskController.detail(source.getId());
                assertThat(updatedTask)
                        .isEqualTo(dest);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_of_delete {

        @Nested
        @DisplayName("만약 유효한 id가 인자로 주어지면")
        class Context_of_valid_id {

            private Task source1;
            private Task source2;

            @BeforeEach
            void setSources() {
                source1 = generateTask(1L, "task1");
                source2 = generateTask(2L, "task2");

                taskService.createTask(source1);
                taskService.createTask(source2);
            }

            @Test
            @DisplayName("해당 id task를 tasks에서 삭제하고, 아무 값도 반환하지 않는다")
            void it_returns_noting() {
                taskController.delete(source1.getId());

                assertThat(taskController.list())
                        .hasSize(1)
                        .doesNotContain(source1);
            }
        }
    }

}