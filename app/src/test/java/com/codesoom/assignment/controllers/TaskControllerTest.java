package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TaskController 클래스")
public class TaskControllerTest {
    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        private TaskController taskController = null;

        @Nested
        @DisplayName("선조건")
        class Context_precondition {
            @Nested
            @DisplayName("TaskController 클래스가 null인 환경에서")
            class Context_taskController_null {
                @Test
                @DisplayName("NullPointException을 발생시킨다.")
                void it_throw_a_nullPointException() {
                    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> taskController.list());
                }
            }

            @Nested
            @DisplayName("TaskController 클래스의 멤버변수 TaskService가 null인 환경에서")
            class Context_taskService_null {

                public Context_taskService_null() {
                    taskController = new TaskController(null);
                }

                @Test
                @DisplayName("NullPointException을 발생시킨다.")
                void it_throw_a_nullPointException() {
                    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> taskController.list());
                }
            }
        }

        @Nested
        @DisplayName("올바른 사용자 시나리오(happy path) 환경에서")
        class Context_happyPath {
            private final TaskService taskServiceMock = mock(TaskService.class);

            public Context_happyPath() {
                taskController = new TaskController(taskServiceMock);
            }

            @Nested
            @DisplayName("저장된 Task가 없다면")
            class Context_task_empty {
                @Test
                @DisplayName("비어있는 리스트를 리턴한다.")
                void it_returns_a_empty_list() {
                    when(taskServiceMock.getTasks()).thenReturn(new ArrayList<Task>());
                    assertThat(taskController.list()).isEmpty();
                }
            }

            @Nested
            @DisplayName("저장된 Task가 있다면")
            class Context_task_exist {
                Task generateTask(final Long id, final String title) {
                    Task task = new Task();
                    task.setId(id);
                    task.setTitle(title);
                    return task;
                }

                @Test
                @DisplayName("저장된 Task가 포함된 리스트를 리턴한다.")
                void it_returns_a_list_with_tasks() {
                    final List<Task> tasks = new ArrayList<Task>();
                    tasks.add(generateTask(1L, "title"));
                    when(taskServiceMock.getTasks()).thenReturn(tasks);
                    assertThat(taskController.list())
                        .hasSize(1)
                        .extracting(Task::getId, Task::getTitle)
                        .containsExactly(tuple(1L, "title"));
                }
            }

            @AfterEach
            void after() {
                verify(taskServiceMock, atLeastOnce()).getTasks();
            }
        }
    }
}
