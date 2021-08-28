package com.codesoom.assignment.controllers.taskController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
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
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TaskController 클래스")
@ExtendWith(MockitoExtension.class)
public final class TaskControllerUnitTest extends TaskControllerTest {
    @Mock
    private TaskService taskServiceMock;

    @InjectMocks
    private TaskController taskController;

    @Nested
    @DisplayName("list 메소드는")
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
    class Describe_detail {
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
    class Describe_create {
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
        void tearDown() {
            verify(taskServiceMock, atMostOnce())
                .createTask(argThat(input -> input == task));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        private ArgumentMatcher<Long> idMatcher = inputId -> inputId == validId;
        private ArgumentMatcher<Task> taskMatcher = inputTask -> inputTask == task;

        @Nested
        @DisplayName("저장된 Task가 없는 경우")
        class Context_task_empty {
            @BeforeEach
            void setUp() {
                when(taskServiceMock.updateTask(validId, task))
                    .thenThrow(TaskNotFoundException.class);
            }
    
            @Test
            @DisplayName("TaskNotFoundException을 던진다.")
            void it_throw_a_taskNotFoundException() {
                assertThatThrownBy(() -> taskController.update(validId, task))
                    .isInstanceOf(TaskNotFoundException.class);
            }

            @AfterEach
            void tearDown() {
                verify(taskServiceMock, atMostOnce())
                    .updateTask(argThat(idMatcher), argThat(taskMatcher));
            }
        }

        @Nested
        @DisplayName("저장된 Task가 있고")
        class Context_task_exist {
            @Nested
            @DisplayName("id에 해당하는 Task를 찾을 수 있는 경우")
            class Context_find_task {
                @BeforeEach
                void setUp() {
                    when(taskServiceMock.updateTask(validId, task))
                        .thenReturn(task);
                }

                @Test
                @DisplayName("업데이트한 Task를 리턴한다.")
                void it_returns_a_updated_task() {
                    assertThat(taskController.update(validId, task))
                        .matches((output) -> output.getId() == validId && taskTitle.equals(output.getTitle()));
                }

                @AfterEach
                void tearDown() {
                    verify(taskServiceMock, atMostOnce())
                        .updateTask(argThat(idMatcher), argThat(taskMatcher));
                }
            }

            @Nested
            @DisplayName("id에 해당하는 Task를 찾을 수 없는 경우")
            class Context_can_not_find_task {
                @BeforeEach
                void setUp() {
                    when(taskServiceMock.updateTask(invalidId, task))
                        .thenThrow(TaskNotFoundException.class);
                }

                @Test
                @DisplayName("TaskNotFoundException을 던진다.")
                void it_throw_a_taskNotFoundException() {
                    assertThatThrownBy(() -> taskController.update(invalidId, task))
                        .isInstanceOf(TaskNotFoundException.class);
                }

                @AfterEach
                void tearDown() {
                    ArgumentMatcher<Long> invalidIdMatcher = inputId -> inputId == invalidId;
                    verify(taskServiceMock, atMostOnce())
                        .updateTask(argThat(invalidIdMatcher), argThat(taskMatcher));
                }
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        private ArgumentMatcher<Long> idMatcher = inputId -> inputId == validId;

        @Nested
        @DisplayName("저장된 Task가 없는 경우")
        class Context_task_empty {
            @BeforeEach
            void setUp() {
                when(taskServiceMock.deleteTask(validId))
                    .thenThrow(TaskNotFoundException.class);
            }
    
            @Test
            @DisplayName("TaskNotFoundException을 던진다.")
            void it_throw_a_taskNotFoundException() {
                assertThatThrownBy(() -> taskController.delete(validId))
                    .isInstanceOf(TaskNotFoundException.class);
            }

            @AfterEach
            void tearDown() {
                verify(taskServiceMock, atMostOnce())
                    .deleteTask(argThat(idMatcher));
            }
        }

        @Nested
        @DisplayName("저장된 Task가 있고")
        class Context_task_exist {
            @Nested
            @DisplayName("id에 해당하는 Task를 찾을 수 있는 경우")
            class Context_find_task {
                @Test
                @DisplayName("Task를 삭제한다.")
                void it_remove_a_task() {
                    taskController.delete(validId);
                }

                @AfterEach
                void tearDown() {
                    verify(taskServiceMock, atMostOnce())
                        .deleteTask(argThat(idMatcher));
                }
            }

            @Nested
            @DisplayName("id에 해당하는 Task를 찾을 수 없는 경우")
            class Context_can_not_find_task {
                @BeforeEach
                void setUp() {
                    when(taskServiceMock.deleteTask(invalidId))
                        .thenThrow(TaskNotFoundException.class);
                }

                @Test
                @DisplayName("TaskNotFoundException을 던진다.")
                void it_throw_a_taskNotFoundException() {
                    assertThatThrownBy(() -> taskController.delete(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
                }

                @AfterEach
                void tearDown() {
                    ArgumentMatcher<Long> invalidIdMatcher = inputId -> inputId == invalidId;
                    verify(taskServiceMock, atMostOnce())
                        .deleteTask(argThat(invalidIdMatcher));
                }
            }
        }
    }



}
