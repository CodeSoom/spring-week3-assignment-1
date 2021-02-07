package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Service에서")
public class NestedTaskServiceTest {

    @Nested
    @DisplayName("모든 테스크를 얻으려고 한다면")
    class Describe_getTasks {

        @Nested
        @DisplayName("기존에 생성된 task가 없는 경우")
        class Context_noCreatedTask {
            private TaskService taskService;

            @BeforeEach
            void init_application_service() {
                taskService = new TaskService();
            }

            @Test
            @DisplayName("empty list를 반환한다.")
            void get_tasks_return_empty_list() {
                List<Task> tasks = taskService.getTasks();
                assertTrue(tasks.isEmpty());
            }
        }

        @Nested
        @DisplayName("이미 task 하나를 생성한경우")
        class Context_alreadyCreatedTask {
            private TaskService taskService;

            @BeforeEach
            void init_application_service() {
                taskService = new TaskService();
                Task task = new Task();
                taskService.createTask(task);
            }

            @Test
            @DisplayName("태스크 하나가 포함된 list를 반환한다.")
            void get_tasks_return_empty_list() {
                List<Task> tasks = taskService.getTasks();
                assertEquals(1, tasks.size());
            }
        }
    }

    @Nested
    @DisplayName("테스크를 생성하려고 한다면")
    class Describe_createTask {

        @Test
        @DisplayName("task가 생성된다.")
        void get_tasks_return_empty_list() {
            TaskService taskService = new TaskService();
            Task task = new Task();

            taskService.createTask(task);
        }
    }

    @Nested
    @DisplayName("테스크를 얻으려고 한다면")
    class Describe_getTask {

        @Nested
        @DisplayName("기존에 생성된 task가 없는 경우")
        class Context_noCreatedTask {
            private TaskService taskService;

            @BeforeEach
            void init_application_service() {
                taskService = new TaskService();
            }

            @Test
            @DisplayName("에러가 발생한다.")
            void get_tasks_return_empty_list() {
                assertThrows(
                        TaskNotFoundException.class,
                        () -> taskService.getTask(1L)
                );
            }
        }

        @Nested
        @DisplayName("이미 태스크가 생성된 경우")
        class Context_alreadyCreatedTask {
            private TaskService taskService;
            private Task createdTask;

            @BeforeEach
            void init_application_service() {
                taskService = new TaskService();
                Task task = new Task();

                createdTask = taskService.createTask(task);
            }

            @Test
            @DisplayName("생성된 태스크를 가져온다.")
            void get_tasks_return_empty_list() {
                Task task = taskService.getTask(createdTask.getId());

                assertEquals(createdTask.getId(), task.getId());
            }
        }
    }
}
