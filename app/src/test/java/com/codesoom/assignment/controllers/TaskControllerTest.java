package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Nested
@DisplayName("TaskControllerTest 클래스")
class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private List<Task> tasks;

    final private Long VALID_ID = 1L;
    final private Long INVALID_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);

        task1 = new Task(1L, "title1");
        task2 = new Task(2L, "title2");
        tasks = Arrays.asList(task1, task2);
    }

    @Nested
    @DisplayName("list 메소드")
    class Describe_list {

        @BeforeEach
        void setUp() {
            given(taskService.getTasks()).willReturn(tasks);
        }

        @Test
        @DisplayName("tasks을 반환합니다.")
        void it_return_tasks() {
            assertThat(taskController.list()).isEqualTo(tasks);
        }
    }

    @Nested
    @DisplayName("detail 메소드")
    class Describe_detail {

        @BeforeEach
        void setUp() {
            given(taskService.getTask(VALID_ID)).willReturn(task1);
            given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하면")
        class Context_with_valid_id {

            @Test
            @DisplayName("Task를 반환한다.")
            void it_return_task() {
                assertThat(taskController.detail(VALID_ID)).isEqualTo(task1);
            }
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하지 않으면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("TastNotFoundException을 던진다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskController.detail(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드")
    class Describe_create {

        @BeforeEach
        void setUp() {
            given(taskService.createTask(task1)).willReturn(task1);
        }

        @Test
        @DisplayName("Task를 반환합니다.")
        void it_return_task() {
            assertThat(taskController.create(task1)).isEqualTo(task1);
        }
    }

    @Nested
    @DisplayName("update 메소드")
    class Describe_update {

        @BeforeEach
        void setUp() {
            given(taskService.updateTask(VALID_ID, task1)).willReturn(task1);
            given(taskService.updateTask(INVALID_ID, task1)).willThrow(new TaskNotFoundException(INVALID_ID));
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하면")
        class Context_with_valid_id {

            @Test
            @DisplayName("Task를 반환한다.")
            void it_return_task() {
                assertThat(taskController.update(VALID_ID, task1)).isEqualTo(task1);
            }
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하지 않으면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("TastNotFoundException을 던진다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskController.update(INVALID_ID, task1))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드")
    class Describe_patch {

        @BeforeEach
        void setUp() {
            given(taskService.updateTask(VALID_ID, task1)).willReturn(task1);
            given(taskService.updateTask(INVALID_ID, task1)).willThrow(new TaskNotFoundException(INVALID_ID));
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하면")
        class Context_with_valid_id {

            @Test
            @DisplayName("Task를 반환한다.")
            void it_return_task() {
                assertThat(taskController.patch(VALID_ID, task1)).isEqualTo(task1);
            }
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하지 않으면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("TastNotFoundException을 던진다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskController.patch(INVALID_ID, task1))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드")
    class Describe_delete {

        @BeforeEach
        void setUp() {
            given(taskService.deleteTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하지 않으면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("TastNotFoundException을 던진다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskController.delete(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
