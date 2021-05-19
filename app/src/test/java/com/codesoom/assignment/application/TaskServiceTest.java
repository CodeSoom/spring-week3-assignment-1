package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private static final String TASK_TITLE = "test";
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    void createTestTask() {
        Task task = new Task();

        task.setTitle("Test1");
        taskService.createTask(task);

        task.setTitle("Test2");
        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("만약 Task가 아무것도 없다면")
         class Context_with_nothing {
            @Test
            @DisplayName("빈 list를 리턴한다.")
            void it_return_empty_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 Task가 1개 이상 존재한다면")
        class Context_with_anything {
            @Test
            @DisplayName("전체 Task의 list를 리턴한다.")
            void it_return_every_task() {
                createTestTask();

                assertThat(taskService.getTasks()).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {

        @Nested
        @DisplayName("올바르지 않은 ID값이 주어지면")
        class Context_with_wrong_ID {
            @Test
            @DisplayName("TaskNotFound에러를 발생시킨다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("올바른 ID값이 주어지면")
        class Context_with_right_ID {
            @Test
            @DisplayName("해당 ID의 Task를 리턴한다.")
            void it_return_task() {
                createTestTask();

                assertThat(taskService.getTask(1L).getTitle()).isEqualTo("Test1");
                assertThat(taskService.getTask(2L).getTitle()).isEqualTo("Test2");
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {

        @Nested
        @DisplayName("Task에 들어갈 Title을 받으면")
        class Context_with_some_title {
            @Test
            @DisplayName("Task를 생성하고, 해당 Task를 리턴한다.")
            void it_create_task() {
                Task task = new Task();

                task.setTitle("Test1");
                taskService.createTask(task);

                task.setTitle("Test2");
                taskService.createTask(task);

                assertThat(taskService.getTasks().get(0).getId()).isEqualTo(1L);
                assertThat(taskService.getTask(1L).getTitle()).isEqualTo("Test1");

                assertThat(taskService.getTasks().get(1).getId()).isEqualTo(2L);
                assertThat(taskService.getTask(2L).getTitle()).isEqualTo("Test2");
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("올바르지 않은 ID값이 주어지면")
        class Context_with_wrong_ID {
            @Test
            @DisplayName("TaskNotFound에러를 발생시킨다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(1L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("올바른 ID값과 변경할 title이 주어지면")
        class Context_with_right_ID {
            @Test
            @DisplayName("title을 변경하고, 변경된 Task를 리턴한다")
            void it_update_task() {
                createTestTask();

                Task task = new Task();
                task.setTitle("New Test1");
                taskService.updateTask(1L, task);

                task.setTitle("New Test2");
                taskService.updateTask(2L, task);

                assertThat(taskService.getTask(1L).getTitle()).isEqualTo("New Test1");
                assertThat(taskService.getTask(2L).getTitle()).isEqualTo("New Test2");
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("올바르지 ID값이 주어지면")
        class Context_with_wrong_ID {
            @Test
            @DisplayName("TaskNotFound에러를 발생시킨다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(1L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("올바른 ID값이 주어지면")
        class Context_with_right_ID {
            @Test
            @DisplayName("해당 Task를 삭제한다.")
            void it_delete_task() {
                createTestTask();

                assertThat(taskService.getTask(1L)).isNotNull();
                assertThat(taskService.getTask(2L)).isNotNull();

                taskService.deleteTask(1L);

                assertThatThrownBy(() -> taskService.getTask(1L)).isInstanceOf(TaskNotFoundException.class);
                assertThat(taskService.getTask(2L)).isNotNull();

                taskService.deleteTask(2L);

                assertThatThrownBy(() -> taskService.getTask(2L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
