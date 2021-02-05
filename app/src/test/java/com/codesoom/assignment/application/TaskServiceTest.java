package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    final String TASK_TITLE = "Get Sleep";
    final String UPDATE_TITLE = "Do Study";

    TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    Task createTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        return taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks()")
    class Describe_getTasks {
        @Nested
        @DisplayName("task가 존재한다면")
        class Context_task_exist {
            @BeforeEach
            void setUp() {
                createTask();
            }

            @Test
            @DisplayName("task 리스트를 반환한다")
            void it_return_task_id() {
                //when
                List<Task> tasks = taskService.getTasks();
                //then
                assertThat(tasks).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("task가 존재하지 않는다면")
        class Context_task_not_exist {
            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_return_task_id() {
                //when
                List<Task> tasks = taskService.getTasks();
                //then
                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask()")
    class Describe_getTask {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_task_exist {
            Task givenTask;
            @BeforeEach
            void setUp() {
                givenTask = createTask();
            }

            @Test
            @DisplayName("id와 일치하는 task를 반환한다")
            void it_return_task() {
                //when
                Task task = taskService.getTask(givenTask.getId());
                //then
                assertThat(task.getId()).isEqualTo(givenTask.getId());
                assertThat(task.getTitle()).isEqualTo(givenTask.getTitle());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_task_not_exist {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.getTask(100L));
            }
        }
    }

    @Nested
    @DisplayName("updateTask()")
    class Describe_updateTask {
        @Nested
        @DisplayName("존재하는 task id와 변경하려는 task가 주어진다면")
        class Context_task_exist {
            Task givenTask;
            @BeforeEach
            void setUp() {
                givenTask = createTask();
            }

            @Test
            @DisplayName("title이 변경된 task를 반환한다")
            void it_return_updated_task() {
                //when
                givenTask.setTitle(UPDATE_TITLE);
                Task updatedTask = taskService.updateTask(givenTask.getId(), givenTask);
                //then
                assertThat(updatedTask.getId()).isEqualTo(givenTask.getId());
                assertThat(updatedTask.getTitle()).isEqualTo(givenTask.getTitle());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_task_not_exist {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(100L, new Task()));
            }
        }
    }

    @Nested
    @DisplayName("deleteTask()")
    class Describe_deleteTask {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_task_exist {
            Task givenTask;
            @BeforeEach
            void setUp() {
                givenTask = createTask();
            }

            @Test
            @DisplayName("주어진 id와 일치하는 task를 삭제한다")
            void it_task() {
                //when
                taskService.deleteTask(givenTask.getId());
                //then
                assertThrows(TaskNotFoundException.class, () -> taskService.getTask(givenTask.getId()));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id가 주어진다면")
        class Context_task_not_exist {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(100L, new Task()));
            }
        }
    }
}
