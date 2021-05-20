package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    private Task generateTask(long id, String title) {
        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTitle(title);
        return newTask;
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_of_getTasks {

        @Nested
        @DisplayName("만약 tasks가 비어있다면")
        class Context_of_empty_tasks {

            @Test
            @DisplayName("비어있는 배열을 반환한다")
            void it_returns_empty_array() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 tasks가 비어있지 않다면")
        class Context_of_tasks_not_empty {

            private Task task1;
            private Task task2;
            private Long currentTaskId = 0L;

            @BeforeEach
            void append_two_task_to_tasks() {
                currentTaskId += 1;
                task1 = generateTask(currentTaskId, "task" + currentTaskId);
                currentTaskId += 1;
                task2 = generateTask(currentTaskId, "task" + currentTaskId);

                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @Test
            @DisplayName("tasks에 포함된 모든 객체를 반환한다")
            void it_returns_all_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).containsExactly(task1, task2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_of_getTask {

        private Task task1;
        private Task task2;
        private Long currentTaskId = 0L;

        @BeforeEach
        void append_two_task_to_tasks() {
            currentTaskId += 1;
            task1 = generateTask(currentTaskId, "task" + currentTaskId);
            currentTaskId += 1;
            task2 = generateTask(currentTaskId, "task" + currentTaskId);

            taskService.createTask(task1);
            taskService.createTask(task2);
        }

        @Nested
        @DisplayName("만약 tasks에 존재하지 않는 task id를 인자로 입력하면")
        class Context_of_invalid_id {

            private long invalidId;

            @BeforeEach
            void setInvalidId() {
                invalidId = currentTaskId + 42L;
            }

            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_task_not_found_exception() {
                assertThrows(
                        TaskNotFoundException.class,
                        () -> taskService.getTask(invalidId));
            }
        }

        @Nested
        @DisplayName("만약 tasks에 포함된 task의 id를 인자로 전달 받으면")
        class Context_of_valid_id {

            @Test
            @DisplayName("id에 해당하는 task를 반환한다")
            void getTask() {
                Task task = taskService.getTask(task1.getId());

                assertThat(task).isEqualTo(task1);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_of_createTask {

        @Nested
        @DisplayName("만약 Task 객체를 인자로 전달받으면")
        class Context_of_given_task {

            private Task source;
            private Long currentTaskId = 0L;

            @BeforeEach
            void append_two_task_to_tasks() {
                currentTaskId += 1;
                source = generateTask(currentTaskId, "task" + currentTaskId);
            }

            @Test
            @DisplayName("새 task를 생성 및 tasks에 추가하고, 추가한 객체 정보를 반환한다")
            void it_appends_new_task_and_returns_it() {
                Task createdTask = taskService.createTask(source);
                assertThat(createdTask).isEqualTo(source);

                createdTask = taskService.getTask(source.getId());
                assertThat(createdTask).isEqualTo(source);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_of_updateTask {

        @Nested
        @DisplayName("만약 id와 Task 객체가 인자로 주어지면")
        class Context_of_input_task {

            private Task source;
            private Long currentTaskId = 0L;

            @BeforeEach
            void append_two_task_to_tasks() {
                currentTaskId += 1;
                source = generateTask(currentTaskId, "task" + currentTaskId);
            }

            @Test
            @DisplayName("인자에 따라 task를 갱신한 후, 갱신한 task를 반환한다 ")
            void it_updates_task_and_returns_it() {
                taskService.createTask(source);

                source.setTitle("modifed_title");
                Task updatedTask = taskService.updateTask(source.getId(), source);

                assertThat(updatedTask).isEqualTo(source);

                updatedTask = taskService.getTask(source.getId());
                assertThat(updatedTask).isEqualTo(source);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_of_deleteTask {

        private Task source;
        private Long currentTaskId = 0L;

        @BeforeEach
        void append_a_task_to_tasks() {
            currentTaskId += 1;
            source = generateTask(currentTaskId, "task" + currentTaskId);

            taskService.createTask(source);
        }

        @Nested
        @DisplayName("만약 tasks에 존재하지 않는 task의 id를 인자로 전달하면")
        class Context_of_non_existent_id {

            private Long nonExistentId = 0L;

            @BeforeEach
            void setInvalidId() {
                nonExistentId = currentTaskId + 42L;
            }

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다")
            void it_throws_TaskNotFoundException() {
                assertThrows(
                        TaskNotFoundException.class,
                        () -> taskService.deleteTask(nonExistentId));
            }
        }

        @Nested
        @DisplayName("만약 tasks에 존재하는 task의 id를 인자로 전달하면")
        class Context_of_exist_id {

            private Long existId = 0L;

            @BeforeEach
            void setExistId() {
                existId = currentTaskId;
            }

            @Test
            @DisplayName("해당 id task를 tasks에서 제거하고, 제거한 task를 반환한다")
            void it_removes_task_from_tasks_and_returns_it() {
                Task deletedTask = taskService.deleteTask(existId);

                assertThat(deletedTask).isEqualTo(source);
                assertThrows(
                        TaskNotFoundException.class,
                        () -> taskService.getTask(existId));
            }
        }
    }

}