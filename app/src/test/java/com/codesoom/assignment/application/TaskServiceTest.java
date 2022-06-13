package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("TaskService 클래스")
public class TaskServiceTest {

    private TaskService taskService;
    private Task task;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        task = new Task();
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("getTasks 호출은")
    class Describe_get_tasks {

        @Nested
        @DisplayName("task가 저장돼 있다면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                taskService.createTask(task);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("task list를 리턴한다")
            void it_returns_task_list() {
                assertThat(taskService.getTasks()).hasSize(2);
            }
        }

        @Nested
        @DisplayName("저장된 task가 없다면")
        class Context_with_empty_tasks {

            @Test
            @DisplayName("빈 task list를 리턴한다")
            void it_returns_empty_task_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 호출은")
    class Describe_get_task {

        @Nested
        @DisplayName("저장된 task id를 호출한다면")
        class Context_with_task_id {

            @BeforeEach
            void setUp() {
                taskService.createTask(task);
            }

            @Test
            @DisplayName("task를 리턴한다")
            void it_returns_task() {
                Task task = taskService.getTask(EXISTING_ID);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 task id를 호출한다면")
        class Context_with_not_stored_task_id {

            @Test
            @DisplayName("TaskNotFoundException이 발생한다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(NOT_EXISTING_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 호출은")
    class Describe_create_task {

        @Nested
        @DisplayName("task가 주어지면")
        class Context_with_task {
            int oldSize;

            @BeforeEach
            void setUp() {
                oldSize = taskService.getTasks().size();
            }

            Task subject() {
                Task newTask = taskService.createTask(task);
                newTask.setTitle(TASK_TITLE);
                return newTask;
            }

            @Test
            @DisplayName("task를 리턴한다")
            void it_returns_task() {
                assertThat(subject().getTitle()).isEqualTo(TASK_TITLE);
                int newSize = taskService.getTasks().size();
                assertThat(newSize - oldSize).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 호출은")
    class Describe_update_task {

        @Nested
        @DisplayName("저장된 task id가 주어지면")
        class Context_with_stored_task_id {

            @Test
            @DisplayName("업데이트한 task를 리턴한다")
            void it_returns_updated_task() {
                task.setId(EXISTING_ID);
                task.setTitle(TASK_TITLE + UPDATE_POSTFIX);
                taskService.createTask(task);
                taskService.updateTask(EXISTING_ID, task);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 task id가 주어지면")
        class Context_with_not_stored_task_id {

            @Test
            @DisplayName("TaskNotFoundException이 발생한다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.updateTask(NOT_EXISTING_ID, task))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 호출은")
    class Describe_delete_task {

        @Nested
        @DisplayName("저장된 task id가 주어지면")
        class Context_with_stored_task_id {

            int oldSize;

            @BeforeEach
            void setUp() {
                taskService.createTask(task);
                oldSize = taskService.getTasks().size();
            }

            @Test
            @DisplayName("task를 삭제한다")
            void it_deletes_task() {
                taskService.deleteTask(EXISTING_ID);
                int newSize = taskService.getTasks().size();
                assertThat(oldSize - newSize).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("저장되지 않은 task id가 주어진다면")
        class Context_with_not_stored_task_id {

            @Test
            @DisplayName("TaskNotFoundException이 발생한다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(NOT_EXISTING_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
