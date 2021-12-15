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

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 0L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Test
        @DisplayName("등록된 Task 전체 리스트를 리턴한다.")
        void it_return_tasks() {
            List<Task> tasks = taskService.getTasks();
            Task foundTask = tasks.get(0);

            assertThat(tasks).hasSize(1);
            assertThat(foundTask.getTitle()).isEqualTo(TASK_TITLE);
        }

        @Nested
        @DisplayName("만약 등록된 Task의 id 값이 주어진다면")
        class Context_with_id {
            @Test
            @DisplayName("등록된 task 정보를 리턴한다.")
            void it_return_task() {
                Task foundTask = taskService.getTask(VALID_ID);

                assertThat(foundTask.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 등록되지 않은 Task의 id 값이 주어진다면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 을 리턴한다.")
            void it_return_taskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(INVALID_ID)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("만약 등록할 Task가 주어진다면")
        class Context_with_task {
            @Test
            @DisplayName("Task를 생성하고, 리턴한다.")
            void it_create_task_return_task() {
                int sizeBeforeCreation = getTasksSize();
                Task task = getTask();

                Task createdTask = taskService.createTask(task);
                int sizeAfterCreation = getTasksSize();

                assertThat(createdTask.getTitle()).isEqualTo(task.getTitle());
                assertThat(sizeAfterCreation - sizeBeforeCreation).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("만약 등록할 Task가 주어진다면")
        class Context_without_task {
            @Test
            @DisplayName("NullPointerException 을 리턴한다.")
            void it_return_exception() {
                assertThatThrownBy(() -> taskService.createTask(null)).isInstanceOf(NullPointerException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("만약 등록된 Task의 id 와 수정할 Task 가 주어진다면")
        class Context_with_id_and_task {
            @Test
            @DisplayName("해당 id의 Task를 수정하고, 리턴한다.")
            void it_update_task_return_task() {
                Task source = getTaskWithPostfix();

                Task updatedTask = taskService.updateTask(VALID_ID, source);

                assertThat(updatedTask.getTitle()).isEqualTo(source.getTitle());
            }
        }

        @Nested
        @DisplayName("만약 등록된 Task의 id 만 주어진다면")
        class Context_with_id {
            @Test
            @DisplayName("NullPointerException 이 리턴한다.")
            void it_update_task_return_task() {
                assertThatThrownBy(() -> taskService.updateTask(VALID_ID, null)).isInstanceOf(NullPointerException.class);
            }
        }

        @Nested
        @DisplayName("만약 등록된 Task 만 주어진다면")
        class Context_with_task {
            @Test
            @DisplayName("TaskNotFoundException 가 리턴한다.")
            void it_return_taskNotFoundException() {
                Task source = getTaskWithPostfix();

                assertThatThrownBy(() -> taskService.updateTask(null, source)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 등록되지 않은 Task의 id 와 Task가 있다면 ")
        class Context_with_invalid_id_and_task {
            @Test
            @DisplayName("TaskNotFoundException 가 리턴한다.")
            void it_return_taskNotFoundException() {
                Task source = getTask();

                assertThatThrownBy(() -> taskService.updateTask(INVALID_ID, source)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드")
    class Describe_deleteTask {
        @Nested
        @DisplayName("만약 등록된 Task의 id가 주어진다면")
        class Context_with_id {
            @Test
            @DisplayName("등록된 Task를 삭제하고, 빈값이 리턴한다.")
            void it_delete_task_return() {
                int sizeBeforeDeletion = getTasksSize();

                taskService.deleteTask(VALID_ID);
                int sizeAfterDeletion = getTasksSize();

                assertThat(sizeBeforeDeletion - sizeAfterDeletion).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("만약 등록되지 않은 Task의 id가 주어진다면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 을 리턴한다.")
            void it_return_taskNotFoundException() {
                assertThatThrownBy(() -> taskService.deleteTask(INVALID_ID)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    private int getTasksSize() {
        return taskService.getTasks().size();
    }

    private Task getTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        return task;
    }

    private Task getTaskWithPostfix() {
        Task task = new Task();
        task.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        return task;
    }
}
