package com.codesoom.assignment.controllers.taskController.unitTests;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codesoom.assignment.TaskNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

public final class DeleteTest extends TaskControllerUnitTest {
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
