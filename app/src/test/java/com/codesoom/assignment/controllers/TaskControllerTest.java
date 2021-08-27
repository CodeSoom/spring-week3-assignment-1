package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TaskController 클래스")
public class TaskControllerTest {
    @Mock
    private TaskService taskServiceMock;

    @InjectMocks
    private TaskController taskController;

    @Nested
    @DisplayName("모든 메소드는")
    class Describe_all_method {
        @Nested
        @DisplayName("선조건")
        class Context_precondition {
            @Nested
            @DisplayName("'멤버변수 TaskService는 null이 될 수 없다.'를 위반한 경우")
            class Context_taskService_null {
                private final Long id;
                private final Task task;

                public Context_taskService_null() {
                    id = 1L;
                    task = new Task();
                }

                @Test
                @DisplayName("NullPointerException을 던진다.")
                void it_throw_a_nullPointException() {
                    assertThatThrownBy(() -> taskController.list())
                        .isInstanceOf(NullPointerException.class);
                    assertThatThrownBy(() -> taskController.detail(id))
                        .isInstanceOf(NullPointerException.class);
                    assertThatThrownBy(() -> taskController.create(task))
                        .isInstanceOf(NullPointerException.class);
                    assertThatThrownBy(() -> taskController.update(id, task))
                        .isInstanceOf(NullPointerException.class);
                    assertThatThrownBy(() -> taskController.patch(id, task))
                        .isInstanceOf(NullPointerException.class);
                    assertThatThrownBy(() -> taskController.delete(id))
                        .isInstanceOf(NullPointerException.class);
                }
            }
        }
    }

    @Nested
    @DisplayName("list 메소드는")
    @ExtendWith(MockitoExtension.class)
    class Describe_list {
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

    @Nested
    @DisplayName("detail 메소드는")
    @ExtendWith(MockitoExtension.class)
    class Describe_detail {
        @Nested
        @DisplayName("선조건")
        class Context_precondition {
            @Nested
            @DisplayName("'인자로 null이 들어올 수 없다.'를 위반한 경우")
            class Context_argument_null {
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
            
            @Nested
            @DisplayName("id를 통해 Task를 찾을 수 있다면")
            class Context_find_task {
                @BeforeEach
                void setUp() {
                    when(taskServiceMock.getTask(validId))
                        .thenReturn(task);
                }
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
                @BeforeEach
                void setUp() {
                    when(taskServiceMock.getTask(invalidId))
                        .thenThrow(TaskNotFoundException.class);
                }

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

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("선조건")
        class Context_precondition {
        }


    }
}
