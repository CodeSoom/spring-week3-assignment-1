package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private TaskServiceImpl taskService;

    @BeforeEach
    void before() {
        taskService = new TaskServiceImpl();

        Task task = new Task();
        task.setTitle("test");
        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("Task가 존재한다면")
        class Context_with_Task {
            @Test
            @DisplayName("List의 모든 Task들을 리턴한다")
            void it_return_tasks() {
                assertThat(taskService.getTasks()).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("Task가 존재하지 않는다면")
        class Context_with_NoTask {
            @BeforeEach
            void setUp() {
                taskService.deleteTask(1L);
            }

            @Test
            @DisplayName("빈 배열을 리턴한다")
            void it_return_empty_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("존재하는 id로 찾을 경우")
        class Context_with_validId {
            @Test
            @DisplayName("해당하는 Task를 리턴한다")
            void it_return_task() {
                assertThat(taskService.getTask(1L)).isNotNull();
                assertThat(taskService.getTasks().contains(taskService.getTask(1L)))
                        .isTrue();
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id로 찾을 경우")
        class Context_with_invalidId {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

   @Nested
   @DisplayName("createTask 메소드는")
   class Describe_createTask {
        @Test
        @DisplayName("넘어온 title로 Task 객체를 생성한다")
        void it_return_task() {
            Task source = new Task();
            source.setTitle("task");
            Task task = taskService.createTask(source);

            assertThat(task.getTitle()).isEqualTo("task");
        }
   }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        private Task getTaskWithNewTitle() {
            Task task = new Task();
            task.setTitle("update title");
            return task;
        }

        @Nested
        @DisplayName("id와 title이 넘어올 경우")
        class Context_with_id_title {
            @Test
            @DisplayName("Id에 해당하는 Task의 title을 수정하고 리턴한다")
            void it_return_updated_task() {
                Task task = getTaskWithNewTitle();

                assertThat(taskService.updateTask(1L, task).getTitle())
                        .isNotEqualTo("test");
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id일 경우")
        class Context_with_invalidId {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throw_TaskNotFoundException() {
                Task task = getTaskWithNewTitle();

                assertThatThrownBy(() -> taskService.updateTask(100L, task))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("존재하는 id일 경우")
        class Context_with_validId {
            @Test
            @DisplayName("Task를 List에서 삭제한다")
            void it_remove_task() {
                Task deletedTask = taskService.getTask(1L);
                taskService.deleteTask(1L);

                assertThat(taskService.getTasks()).doesNotContain(deletedTask);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id일 경우")
        class Context_with_invalidId {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throw_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.deleteTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
