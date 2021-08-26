package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskServiceTest 클래스")
class TaskServiceTest {

    private TaskService taskService;
    private Task task1;
    private Task task2;
    final private Long VALID_ID = 1L;
    final private Long INVALID_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        task1 = new Task(1L, "title1");
        task2 = new Task(2L, "title2");
    }

    @Nested
    @DisplayName("getTasks 메소드")
    class Describe_getTasks {

        @Nested
        @DisplayName("tasks가 비어 있다면")
        class Context_with_no_tasks {
            @Test
            @DisplayName("빈 tasks을 반환한다.")
            void it_return_empty_tasks() {
                final List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("tasks에 task가 있다면")
        class Context_with_tasks {

            @BeforeEach
            void prepareTask() {
                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @Test
            @DisplayName("tasks을 반환한다.")
            void it_return_tasks() {
                final List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(2);
                assertThat(tasks.get(0).getId()).isEqualTo(task1.getId());
                assertThat(tasks.get(0).getTitle()).isEqualTo(task1.getTitle());
                assertThat(tasks.get(1).getId()).isEqualTo(task2.getId());
                assertThat(tasks.get(1).getTitle()).isEqualTo(task2.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드")
    class Describe_getTask {

        @BeforeEach
        void prepareTask() {
            taskService.createTask(task1);
            taskService.createTask(task2);
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하면")
        class Context_with_valid_id {
            @Test
            @DisplayName("id에 해당되는 Task을 반환한다.")
            void it_return_task() {
                final Task task = taskService.getTask(VALID_ID);

                assertThat(task.getId()).isEqualTo(VALID_ID);
                assertThat(task.getTitle()).isEqualTo(task1.getTitle());
            }
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하지 않으면")
        class Context_with_inValid_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드")
    class Describe_createTask {

        @Test
        @DisplayName("새로 생성된 Task를 반환한다.")
        void it_return_new_task() {
            Task newTask = taskService.createTask(task1);

            assertThat(newTask.getTitle()).isEqualTo(task1.getTitle());
        }

        @Test
        @DisplayName("Tasks에 추가된다.")
        void it_add_task() {
            assertThat(taskService.getTasks()).hasSize(0);

            Task newTask = taskService.createTask(task1);

            assertThat(taskService.getTasks()).hasSize(1);
        }

        @Test
        @DisplayName("내부에서 생성된 Id를 Task에 부여한다.")
        void it_assign_generated_id_to_task() {
            Task newTask = taskService.createTask(task2);

            assertThat(newTask.getId()).isEqualTo(1L);
            assertThat(newTask.getId()).isNotEqualTo(task2.getId());
        }
    }


    @Nested
    @DisplayName("updateTask 메소드")
    class Describe_updateTask {

        @BeforeEach
        void prepareTask() {
            taskService.createTask(task1);
            taskService.createTask(task2);
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하면")
        class Context_with_valid_id {
            @Test
            @DisplayName("업데이트된 Task을 반환한다.")
            void it_return_task() {
                final Task updatedTask = taskService.updateTask(VALID_ID, task2);

                assertThat(updatedTask.getTitle()).isEqualTo(task2.getTitle());
            }
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하지 않으면")
        class Context_with_inValid_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.updateTask(INVALID_ID, task2))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }


    @Nested
    @DisplayName("deleteTask 메소드")
    class Describe_deleteTask {

        @BeforeEach
        void prepareTask() {
            taskService.createTask(task1);
            taskService.createTask(task2);
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하면")
        class Context_with_valid_id {
            @Test
            @DisplayName("삭제되는 Task을 반환한다.")
            void it_return_task() {
                final Task deletedTask = taskService.deleteTask(VALID_ID);

                assertThat(deletedTask.getId()).isEqualTo(VALID_ID);
            }

            @Test
            @DisplayName("Tasks에서 삭제된다.")
            void it_delete_task() {
                assertThat(taskService.getTasks()).hasSize(2);

                taskService.deleteTask(VALID_ID);

                assertThat(taskService.getTasks()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("요청한 id에 해당되는 Task가 Tasks에 존재하지 않으면")
        class Context_with_inValid_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다.")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.deleteTask(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
