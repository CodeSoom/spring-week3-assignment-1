package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TaskController 클래스")
public class TaskControllerTest {
    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        private TaskController taskController;

        @Nested
        @DisplayName("선조건")
        class Context_precondition {
            @Nested
            @DisplayName("'멤버변수 TaskService는 null이 될 수 없다.'를 위반한 경우")
            class Context_taskService_null {
                public Context_taskService_null() {
                    taskController = new TaskController(null);
                }

                @Test
                @DisplayName("NullPointException을 던진다.")
                void it_throw_a_nullPointException() {
                    assertThatThrownBy(() -> taskController.list())
                                .isInstanceOf(NullPointerException.class);
                }
            }
        }

        // TODO @RunWith으로 taskServiceMock처리 가능한지 확인
        @Nested
        @DisplayName("올바른 사용자 시나리오(happy path)에서")
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

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        private TaskController taskController;

        @Nested
        @DisplayName("선조건")
        class Context_precondition {
            @Nested
            @DisplayName("'멤버변수 TaskService는 null이 될 수 없다.'를 위반한 경우")
            class Context_taskService_null {
                public Context_taskService_null() {
                    taskController = new TaskController(null);
                }

                @Test
                @DisplayName("NullPointException을 던진다.")
                void it_throw_a_nullPointException() {
                    assertThatThrownBy(() -> taskController.detail(1L))
                                .isInstanceOf(NullPointerException.class);
                }
            }

            @Nested
            @DisplayName("'인자로 null이 들어올 수 없다.'를 위반한 경우")
            class Context_argument_null {
                private final TaskService taskServiceMock = mock(TaskService.class);

                public Context_argument_null() {
                    taskController = new TaskController(taskServiceMock);
                }

                @BeforeEach
                void setUp() {
                    when(taskServiceMock.getTask(isNull()))
                                .thenThrow(TaskNotFoundException.class);
                }

                @Test
                @DisplayName("TaskNotFoundException을 던진다.")
                void it_throw_a_taskNotFoundException() {
                    assertThatThrownBy(() -> taskController.detail(null))
                                .isInstanceOf(TaskNotFoundException.class);
                }

                @AfterEach
                void tearDown() {
                    verify(taskServiceMock, atLeastOnce())
                        .getTask(argThat((final Long id) -> id == null));
                }
            }
        }

        @Nested
        @DisplayName("올바른 사용자 시나리오(happy path)에서")
        class Context_happyPath {
            private final TaskService taskServiceMock = mock(TaskService.class);

            public Context_happyPath() {
                taskController = new TaskController(taskServiceMock);
            }

            @Nested
            @DisplayName("저장된 Task가 없다면")
            class Context_task_empty {
                private final Long id;

                public Context_task_empty() {
                    id = 1L;
                }

                @BeforeEach
                void setUp() {
                    when(taskServiceMock.getTask(anyLong()))
                        .thenThrow(TaskNotFoundException.class);
                }

                @Test
                @DisplayName("TaskNotFoundException을 던진다.")
                void it_throw_a_taskNotFoundException() {
                    assertThatThrownBy(() -> taskController.detail(id))
                        .isInstanceOf(TaskNotFoundException.class);
                }

                @AfterEach
                void tearDown() {
                    verify(taskServiceMock, atLeastOnce())
                        .getTask(argThat((final Long argument) -> argument == id));
                }
            }

            @Nested
            @DisplayName("Task가 저장되어 있고")
            class Context_task_exist {
                private final Long validId;
                private final Long invalidId;
                private final Task task;
                private final String title;

                public Context_task_exist() {
                    validId = 1L;
                    invalidId = 2L;
                    title = "title";
                    task = new Task();
                    task.setId(validId);
                    task.setTitle(title);
                }

                @BeforeEach
                void setUp() {
                    when(taskServiceMock.getTask(validId))
                        .thenReturn(task);
                    when(taskServiceMock.getTask(invalidId))
                        .thenThrow(TaskNotFoundException.class);
                }

                @Nested
                @DisplayName("id를 통해 Task를 찾을 수 있다면")
                class Context_find_task {
                    @Test
                    @DisplayName("찾은 Task를 리턴한다.")
                    void it_returns_a_task() {
                        assertThat(taskController.detail(validId))
                            .isEqualTo(task);
                    }
                }

                @Nested
                @DisplayName("id를 통해 Task를 찾을 수 없다면")
                class Context_can_not_find_task {
                    @Test
                    @DisplayName("TaskNotFoundException을 던진다.")
                    void it_throw_a_taskNotFoundException() {
                        assertThatThrownBy(() -> taskController.detail(invalidId))
                            .isInstanceOf(TaskNotFoundException.class);
                    }
                }

                @AfterEach
                void tearDown() {
                    verify(taskServiceMock, atMostOnce()).getTask(anyLong());
                }
            }
        }
    }
}
