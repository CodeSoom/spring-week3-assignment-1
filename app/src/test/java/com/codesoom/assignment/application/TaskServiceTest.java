package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("TaskService 클래스")
public class TaskServiceTest {
    private static final String TASK_TITLE_1 = "task1";
    private static final String TASK_TITLE_2 = "task2";
    private static final String NEW_TITLE = "new title";

    private TaskService taskService;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        task1 = new Task();
        task1.setTitle(TASK_TITLE_1);
        task2 = new Task();
        task2.setTitle(TASK_TITLE_2);
    }

    TaskService existTasksSubject() {
        taskService.createTask(task1);
        taskService.createTask(task2);
        return taskService;
    }

    Task newTaskSubject() {
        Task task = new Task();
        task.setTitle(NEW_TITLE);
        return task;
    }

    @Nested
    @DisplayName("getTasks 메서드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("할 일인 Task 값들이 있다면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @DisplayName("할 일 Task 값들을 리턴한다")
            @Test
            void it_returns_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(2);
                assertThat(tasks.get(0).getTitle()).isEqualTo(TASK_TITLE_1);
            }
        }

        @Nested
        @DisplayName("할 일인 Task 값들이 없다면")
        class Context_without_tasks {
            @DisplayName("비어있는 값을 리턴한다")
            @Test
            void it_returns_empty_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        final Long id = 1L;

        @Nested
        @DisplayName("할 일인 Task 값 있으면서, 찾고자하는 Task id가 주어지진다면")
        class Context_with_task {
            @BeforeEach
            void setUp() {
                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @DisplayName("할 일 Task 값을 리턴한다")
            @Test
            void it_returns_tasks() {
                assertAll(
                        () -> assertThat(taskService.getTask(id)).isNotNull(),
                        () -> assertThat(taskService.getTask(id).getTitle()).isEqualTo(TASK_TITLE_1)
                );
            }
        }

        @Nested
        @DisplayName("할 일인 Task 값들이 없으면서, 찾고자하는 Task id가 주어진다면")
        class Context_without_tasks {

            @DisplayName("예외를 발생시킨다")
            @Test
            void it_throws_exception() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.getTask(id));
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {
        @Nested
        @DisplayName("Tasks에 포함할 Task가 주어지면")
        class Context_with_task {
            Task newTask = newTaskSubject();

            @DisplayName("입력한 Task 값을 리턴하고, Task 의 숫자가 증가한다")
            @Test
            void it_returns_task_and_size() {

                assertThat(taskService.createTask(newTask).getTitle()).isEqualTo(NEW_TITLE);
                assertThat(taskService.getTasks()).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {
        final Long id = 1L;
        final Task newTask = newTaskSubject();

        @Nested
        @DisplayName("할 일인 Task 값 있으면서, 변경하고자는 Task id가 주어지진다면")
        class Context_with_task {

            @DisplayName("변경된 일 Task 값을 리턴한다")
            @Test
            void it_returns_update_task() {
                TaskService taskService = existTasksSubject();
                assertAll(
                        () -> assertThat(taskService.updateTask(id, newTask)).isNotNull(),
                        () -> assertThat(taskService.getTask(id).getTitle()).isEqualTo(NEW_TITLE)
                );
            }
        }

        @Nested
        @DisplayName("할 일인 Task 값들이 없으면서, 찾고자하는 Task id가 주어진다면")
        class Context_without_tasks {

            @DisplayName("예외를 발생시킨다")
            @Test
            void it_throws_exception() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.updateTask(id, newTask));
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {
        final Long id = 1L;

        @Nested
        @DisplayName("할 일인 Task 값 있으면서, 삭제하고자 하는 Task id가 주어지진다면")
        class Context_with_task {
            @BeforeEach
            void setUp() {
                taskService.createTask(task1);
                taskService.createTask(task2);
            }


            @DisplayName("삭제된 일 Task 값을 리턴한다")
            @Test
            void it_returns_delete_task() {
                assertAll(
                        () -> assertThat(taskService.deleteTask(id).getTitle()).isEqualTo(TASK_TITLE_1),
                        () -> assertThat(taskService.getTasks()).hasSize(1)
                );
            }
        }

        @Nested
        @DisplayName("할 일인 Task 값들이 없으면서, 찾고자하는 Task id가 주어진다면")
        class Context_without_tasks {

            @DisplayName("예외를 발생시킨다")
            @Test
            void it_throws_exception() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.deleteTask(id));
            }
        }
    }
}
