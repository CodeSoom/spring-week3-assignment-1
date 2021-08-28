package com.codesoom.assignment.controllers.taskController;

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

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TaskController 클래스")
public final class TaskControllerUnitTest extends TaskControllerTest {
    @Mock
    private TaskService taskServiceMock;

    @InjectMocks
    private TaskController taskController;

    @Nested
    @DisplayName("모든 메소드는")
    class Describe_all_method {
        @Nested
        @DisplayName("'멤버변수 TaskService는 null이 될 수 없다.'를 위반한 경우")
        class Context_taskService_null {
            public Context_taskService_null() {
                taskController = new TaskController(null);
            }
            @Test
            @DisplayName("NullPointerException을 던진다.")
            void it_throw_a_nullPointException() {
                assertThatThrownBy(() -> taskController.list())
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> taskController.detail(validId))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> taskController.create(task))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> taskController.update(validId, task))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> taskController.patch(validId, task))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> taskController.delete(validId))
                    .isInstanceOf(NullPointerException.class);
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
                when(taskServiceMock.getTasks()).thenReturn(tasks);
                assertThat(taskController.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("저장된 Task가 있다면")
        class Context_task_exist {

            @BeforeEach
            void setUp() {
                tasks.add(task);
                when(taskServiceMock.getTasks()).thenReturn(tasks);
            }

            @Test
            @DisplayName("저장된 Task가 포함된 리스트를 리턴한다.")
            void it_returns_a_list_with_tasks() {
                assertThat(taskController.list())
                    .hasSize(tasks.size())
                    .extracting(Task::getId, Task::getTitle)
                    .containsExactly(tuple(validId, taskTitle));
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
                    .getTask(argThat(input -> input == null));
            }
        }

        @Nested
        @DisplayName("저장된 Task가 없다면")
        class Context_task_empty {
            @BeforeEach
            void setUp() {
                when(taskServiceMock.getTask(anyLong()))
                    .thenThrow(TaskNotFoundException.class);
            }
            @Test
            @DisplayName("TaskNotFoundException을 던진다.")
            void it_throw_a_taskNotFoundException() {
                assertThatThrownBy(() -> taskController.detail(validId))
                    .isInstanceOf(TaskNotFoundException.class);
            }
            @AfterEach
            void tearDown() {
                verify(taskServiceMock, atLeastOnce())
                    .getTask(argThat(input -> input == validId));
            }
        }
        @Nested
        @DisplayName("Task가 저장되어 있고")
        class Context_task_exist {
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
                        .matches(output -> output.getId() == validId && taskTitle.equals(output.getTitle()));
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
    @ExtendWith(MockitoExtension.class)
    class Describe_create {
        @Nested
        @DisplayName("'인자로 null이 들어올 수 없다.'를 위반한 경우")
        class Context_argument_null {
            @BeforeEach
            void setUp() {
                when(taskServiceMock.createTask(isNull()))
                    .thenThrow(NullPointerException.class);
            }

            @Test
            @DisplayName("NullPointerException을 던진다.")
            void it_throw_a_nullPointException() {
                assertThatThrownBy(() -> taskController.create(null))
                    .isInstanceOf(NullPointerException.class);
            }

            @AfterEach
            void tearDown() {
                verify(taskServiceMock, atLeastOnce())
                    .createTask(argThat(input -> input == null));
            }
        }

        @Nested
        @DisplayName("인자로 null이 아닌 값이 들어온 경우")
        class Context_argument_not_null {
            @BeforeEach
            void setUp() {
                when(taskServiceMock.createTask(task)).thenReturn(task);
            }

            @Test
            @DisplayName("새로운 Task를 생성하고 리턴한다.")
            void it_returns_a_task() {
                assertThat(taskController.create(task))
                .matches(output -> output.getId() == validId && taskTitle.equals(output.getTitle()));
            }

            @AfterEach
            void testDown() {
                verify(taskServiceMock, atMostOnce())
                    .createTask(argThat(input -> input == task));
            }
        }
    }
}
