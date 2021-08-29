package com.codesoom.assignment.controllers.taskController.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

@DisplayName("TaskController 클래스")
public final class UpdateTest extends TaskControllerUnitTest {
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
    
}
