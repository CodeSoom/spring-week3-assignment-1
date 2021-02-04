package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
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
            @Test
            @DisplayName("task 리스트를 반환한다")
            void it_return_task_id() {
                createTask();
                assertThat(taskService.getTasks()).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("task가 존재하지 않는다면")
        class Context_task_not_exist {
            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_return_task_id() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask()")
    class Describe_getTask {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_task_exist {
            @Test
            @DisplayName("id와 일치하는 task를 반환한다")
            void it_return_task() {
                Task task = createTask();
                assertThat(taskService.getTask(task.getId()).getId()).isEqualTo(task.getId());
                assertThat(taskService.getTask(task.getId()).getTitle()).isEqualTo(task.getTitle());
            }
        }

        @Nested
        @DisplayName("존재하지 않 task id가 주어진다면")
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
            @Test
            @DisplayName("주어진 task의 title 값으로 업데이트 한후 task를 리턴한다")
            void it_return_updated_task() {
                Task task = createTask();
                task.setTitle(UPDATE_TITLE);
                Task updatedTask = taskService.updateTask(task.getId(), task);

                assertThat(updatedTask.getId()).isEqualTo(task.getId());
                assertThat(updatedTask.getTitle()).isEqualTo(task.getTitle());
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
            @Test
            @DisplayName("주어진 id와 일치하는 task가 삭제된다")
            void it_task() {
                Task task = createTask();
                taskService.deleteTask(task.getId());
                assertThrows(TaskNotFoundException.class, () -> taskService.getTask(task.getId()));
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
