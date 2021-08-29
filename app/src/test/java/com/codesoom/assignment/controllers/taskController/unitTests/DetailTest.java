package com.codesoom.assignment.controllers.taskController.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codesoom.assignment.TaskNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TaskController 클래스")
public class DetailTest extends TaskControllerUnitTest {
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
    
}
