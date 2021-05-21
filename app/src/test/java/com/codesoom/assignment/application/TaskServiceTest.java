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

//    private static final String TASK_TITLE = "test";
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    void createTestTask(String source) {
        Task task = new Task();

        task.setTitle(source);
        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("등록된 객체가 없다면")
         class Context_with_nothing {
            @Test
            @DisplayName("빈 list를 리턴한다.")
            void it_return_empty_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록된 객체가 1개 이상 존재한다면")
        class Context_with_anything {
            @BeforeEach
            void registerTask() {
                createTestTask("test");
            }

            @Test
            @DisplayName("전체 객체의 list를 리턴한다.")
            void it_return_every_task() {
                assertThat(taskService.getTasks()).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {

        @Nested
        @DisplayName("등록되지 않은 ID값이 주어지면")
        class Context_with_not_registered_ID {

            Long notRegisteredId = 100L;

            @Test
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(notRegisteredId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("등록된 ID값이 주어지면")
        class Context_with_registered_ID {
            Long registeredId1 = 1L;

            @BeforeEach
            void registerTask() {
                createTestTask("Test1");
            }

            @Test
            @DisplayName("해당 ID의 객체를 리턴한다.")
            void it_return_task() {
                assertThat(taskService.getTask(registeredId1).getTitle()).isEqualTo("Test1");
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {

        @Nested
        @DisplayName("객체에 들어갈 Title을 받으면")
        class Context_with_some_title {
            @Test
            @DisplayName("객체를 생성하고, 생성한 객체를 리턴한다.")
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
        Task task = new Task();

        @BeforeEach
        void ready_task() {
            createTestTask("test");
            task.setTitle("New test");
        }

        @Nested
        @DisplayName("등록되지 않은 ID값이 주어지면")
        class Context_with_not_registered_ID {
            Long notRegisteredId = 100L;



            @Test
            @DisplayName("TaskNotFound 예외를 던집니다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.updateTask(notRegisteredId, task))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("등록된 ID값과 변경할 title이 주어지면")
        class Context_with_registered_ID {

            String text1 = "New Test1";

            @Test
            @DisplayName("title을 변경하고, 변경된 객체를 리턴한다")
            void it_update_task() {
                task.setTitle(text1);
                taskService.updateTask(1L,task);

                assertThat(taskService.getTask(1L).getTitle()).isEqualTo("New Test1");
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {

        @BeforeEach
        void id_register() {
            createTestTask("test");
        }

        @Nested
        @DisplayName("등록되지 ID값이 주어지면")
        class Context_with_not_registered_ID {
            Long notRegisteredId = 100L;

            @Test
            @DisplayName("TaskNotFound 예외를 던집다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(notRegisteredId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("등록된 ID값이 주어지면")
        class Context_with_registered_ID {
            Long registeredId = 1L;

            @Test
            @DisplayName("해당 객체를 삭제한다.")
            void it_delete_task() {
                assertThat(taskService.getTask(registeredId))
                        .isNotNull();

                taskService.deleteTask(registeredId);

                assertThat(taskService.getTasks())
                        .isEmpty();
                assertThatThrownBy(() -> taskService.getTask(registeredId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
