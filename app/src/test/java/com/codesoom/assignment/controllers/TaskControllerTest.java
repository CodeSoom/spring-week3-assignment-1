package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("TaskController 유닛 테스트")
class TaskControllerTest {
    private static final Long TEST_ID = 1L;
    private static final Long TEST_NOT_EXSIT_ID = 100L;
    private static final String TEST_TITLE = "테스트는 재밌군요!";
    private TaskController taskController;
    private TaskService taskService;
    private Task taskSource;

    @BeforeEach
    void setUp() {
        taskSource = Task.builder()
                .title(TEST_TITLE)
                .build();

        taskService = spy(new TaskService());
        taskController = new TaskController(taskService);

        taskService.createTask(taskSource);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class list_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 할_일이_없을_때 {
            @Test
            @DisplayName("빈 리스트를 리턴한다")
            void it_returns_empty_array() {
                given(taskController.list())
                        .willReturn(new ArrayList<>());

                assertThat(taskController.list())
                        .isEmpty();

                verify(taskService).getTasks();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 할_일이_있을_때 {
            @Test
            @DisplayName("비어있지 않은 리스트를 리턴한다")
            void it_returns_tasks() {
                assertThat(taskController.list())
                        .isNotEmpty();

                verify(taskService).getTasks();
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("해당 id의 할 일을 리턴한다")
            void it_returns_task() {
                assertThat(taskController.detail(TEST_ID))
                        .isNotNull();

                verify(taskService).getTask(TEST_ID);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskController.detail(TEST_NOT_EXSIT_ID))
                        .isInstanceOf(TaskNotFoundException.class);

                verify(taskService).getTask(TEST_NOT_EXSIT_ID);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class createTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 새로운_할_일이_주어지면 {
            @Test
            @DisplayName("할 일을 저장하고 리턴한다")
            void it_return_created_task() {
                assertThat(taskController.create(new Task()))
                        .isNotNull();

                verify(taskService, times(2)).createTask(any(Task.class));
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class null이_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_NullPointerException() {
                assertThatThrownBy(() -> taskController.create(null))
                        .isInstanceOf(NullPointerException.class);

                verify(taskService).createTask(null);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class updateTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("할 일을 수정하고 리턴한다")
            void it_returns_updated_task() {
                assertThat(taskController.update(TEST_ID, new Task()))
                        .isNotNull();

                verify(taskService).updateTask(eq(TEST_ID), any(Task.class));
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskController.update(TEST_NOT_EXSIT_ID, new Task()))
                        .isInstanceOf(TaskNotFoundException.class);

                verify(taskService).updateTask(eq(TEST_NOT_EXSIT_ID), any(Task.class));
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class deleteTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("할 일을 삭제하고 리턴한다")
            void it_returns_deleted_task() {
                assertThat(taskController.delete(TEST_ID))
                        .isNotNull();

                verify(taskService).deleteTask(TEST_ID);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskController.delete(TEST_NOT_EXSIT_ID))
                        .isInstanceOf(TaskNotFoundException.class);

                verify(taskService).deleteTask(TEST_NOT_EXSIT_ID);
            }
        }
    }
}
