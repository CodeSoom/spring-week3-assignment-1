package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

    abstract class ContextTaskExists {
        TaskService existTasks() {
            taskService.createTask(task1);
            taskService.createTask(task2);
            return taskService;
        }
        TaskService notExistTasks() {
            return taskService;
        }
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
        class Context_with_tasks extends ContextTaskExists {
            @DisplayName("할 일 Task 값들을 리턴한다")
            @Test
            void it_returns_tasks() {
                TaskService taskService = existTasks();
                assertAll(
                        () -> assertThat(taskService.getTasks()).isNotEmpty(),
                        () -> assertThat(taskService.getTasks()).hasSize(2),
                        () -> assertThat(taskService.getTasks())
                                .extracting("title").contains(TASK_TITLE_1)
                );
            }
        }
        @Nested
        @DisplayName("할 일인 Task 값들이 없다면")
        class Context_without_tasks extends ContextTaskExists {
            @DisplayName("비어있는 값을 리턴한다")
            @Test
            void it_returns_empty_tasks() {
                TaskService taskService = notExistTasks();
                assertAll(
                        () -> assertThat(taskService.getTasks()).isEmpty(),
                        () -> assertThat(taskService.getTasks()).hasSize(0)
                );
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        @Nested
        @DisplayName("할 일인 Task 값 있으면서, 찾고자하는 Task id가 주어지진다면")
        class Context_with_task extends ContextTaskExists {
            final Long targetId = 1L;

            @DisplayName("할 일 Task 값을 리턴한다")
            @Test
            void it_returns_tasks() {
                TaskService taskService = existTasks();
                assertAll(
                        () -> assertThat(taskService.getTask(targetId)).isNotNull(),
                        () -> assertThat(taskService.getTask(targetId)).extracting("title").isEqualTo(TASK_TITLE_1)
                );
            }
        }
        @Nested
        @DisplayName("할 일인 Task 값들이 없으면서, 찾고자하는 Task id가 주어진다면")
        class Context_without_tasks extends ContextTaskExists {
            final Long targetId = 1L;
            @DisplayName("예외를 발생시킨다")
            @Test
            void it_throws_exception() {
                TaskService taskService = notExistTasks();
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.getTask(targetId));
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask extends ContextTaskExists {
        @Nested
        @DisplayName("Tasks에 포함할 Task가 주어지면")
        class Context_with_task {
            Task newTask = newTaskSubject();
            @DisplayName("입력한 Task 값을 리턴하고, Task 의 숫자가 증가한다")
            @Test
            void it_returns_task_and_size() {
                TaskService taskService = notExistTasks();

                assertThat(taskService.createTask(newTask)).extracting("title").isEqualTo(NEW_TITLE);
                assertThat(taskService.getTasks()).hasSize(1);
            }
        }
    }
}
